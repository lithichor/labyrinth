package com.web.api.map;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.models.Game;
import com.models.Map;
import com.models.User;
import com.models.api.APIErrorMessage;
import com.models.api.APIMap;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;

public class MapServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = -4940820141991252197L;
	private ArrayList<Map> maps;
	
	public MapServlet(ArrayList<Map> map)
	{
		this.maps = map;
	}

	/**
	 * api/maps
	 * api/maps/:gameId
	 * 
	 * If a valid Game ID is included, the servlet will parse it and use
	 * that to load the Maps. If no Game ID is provided, it loads the
	 * Maps for the most recent Game
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
		User user;
		int gameId = 0;
		ArrayList<APIMap> apiMaps = new ArrayList<APIMap>();
		
		String idStr = splitUrl(request.getRequestURI(), EndpointsWithIds.MAPS);
		
		// if there is a string after the endpoint
		if(idStr.length() > 0)
		{
			gameId = parseIdFromString(idStr);
		}

		try
		{
			user = this.authenticateUser(request, response);
			
			// if no gameId is provided, we need to get
			// the most recent gameId for the user
			if(gameId == 0)
			{
				ArrayList<Game> games = new Game().load(user.getId(), 0);
				gameId = games.get(games.size() - 1).getId();
			}

			if(user != null)
			{

				maps = new Map().load(gameId, 0);
				for(Map m: maps)
				{
					APIMap am = new APIMap(m);
					apiMaps.add(am);
				}
			}
		}
		catch(LabyrinthException le)
		{
			errors.add(le.getMessage());
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
			return;
		}
		
		// if we're only returning one map, don't return an array
		if(apiMaps.size() == 1)
		{
			apiOut(gson.toJson(apiMaps.get(0)), response);
		}
		else
		{
			apiOut(gson.toJson(apiMaps), response);
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();

		User user = null;
		Map map = null;
		JsonObject data = null;
		MapServletActions actions = new MapServletActions();
		MapValidationHelper validation = new MapValidationHelper();
		
		try
		{
			user = authenticateUser(request, response);
		}
		catch(LabyrinthException le)
		{
			errors.add(le.getMessage());
		}
		
		// if authentication fails, return immediately
		if(user == null || errors.size() > 0)
		{
			if(user == null)
			{
				errors.add(messages.getMessage("user.no_authorization"));
			}
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
			return;
		}
		
		try
		{
			data = actions.getData(request);
		}
		catch(LabyrinthException le)
		{
			errors.add(le.getMessage());
		}
		
		// we require data to create a map to reinforce the fact that
		// a game is required (unlike getting the most recent map)
		if(data == null)
		{
			errors.add(messages.getMessage("map.map_has_no_data"));
		}
		else
		{
			map = validation.validateApi(data);
			// if the map is null, then something went wrong during
			// validation. Add the errors to the output and return
			if(map == null)
			{
				errors.addAll(validation.getErrors());
				apiOut(gson.toJson(new APIErrorMessage(errors)), response);
				return;
			}
			try
			{
				ArrayList<Game> games = new Game().load(user.getId(), map.getGameId());
				if(games.size() == 0)
				{
					errors.add(messages.getMessage("map.game_does_not_match_user"));
				}
				else
				{
					// replace this with map.generateMap();
//					map.save();
					map.generateMap();
				}
			}
			catch(LabyrinthException le)
			{
				errors.add(le.getMessage());
			}
		}
		
		if(errors.size() > 0)
		{
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
		}
		else
		{
			apiOut(gson.toJson(new APIMap(map)), response);
		}
	}

	/** 
	 * Not sure I need this; I'll leave it here for now, but it may get deleted
	 */
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
		MapServletActions actions = new MapServletActions();
		User user = null;
		JsonObject data = null;
		int gameId = 0;
		int mapId = 0;
		
		// get mapId from url
		String idStr = splitUrl(request.getRequestURI(), EndpointsWithIds.MAPS);
		// if there is a string after the endpoint
		if(idStr.length() > 0)
		{
			mapId = parseIdFromString(idStr);
		}

		// load the maps array
		try
		{
			user = this.authenticateUser(request, response);
			data = actions.getData(request);
			
			gameId = actions.getGameId(user, data);

			maps = new Map().load(gameId, mapId);
		}
		catch(LabyrinthException le)
		{
			errors.add(le.getMessage());
		}
		
		if(data == null)
		{
			if(errors.isEmpty())
			{
				errors.add(messages.getMessage("map.map_has_no_data"));
			}
		}
		else
		{
			for(Map m: maps)
			{
				new APIMap(m);
				// finish me
			}
		}
	}
}
