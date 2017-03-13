package com.web.api.maps;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.models.api.APIErrorMessage;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;
import com.web.api.game.Game;
import com.web.api.user.User;

public class MapsServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = -4940820141991252197L;
	private ArrayList<Map> maps;
	
	public MapsServlet(ArrayList<Map> map)
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
		maps = new ArrayList<>();
		
		User user;
		int mapId = 0;
		String idStr = "";
		int gameId = 0;
		ArrayList<APIMap> apiMaps = new ArrayList<APIMap>();
		MapsServletActions actions = new MapsServletActions();
		
		idStr = actions.splitUrl(request.getRequestURI(), EndpointsWithIds.MAPS);
		mapId = actions.getIdFromUrl(request, EndpointsWithIds.MAPS);

		try
		{
			user = actions.authenticateUser(request);

			if(user != null)
			{
				// if no id is provided, we need to get the most recent
				// gameId for the user and load with that
				if("".equals(idStr) && mapId == 0)
				{
					ArrayList<Game> games = new Game().load(user.getId(), 0);
					gameId = games.get(games.size() - 1).getId();
					maps = new Map().load(gameId, mapId);
				}
				else if(mapId > 0)
				{
					Map map = new Map().loadOneMapByUser(user.getId(), mapId);
					if(map != null)
					{
						maps.add(map);
					}
				}
				
				// if the id is invalid, maps will be empty here
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
		
		// if no maps were found
		if(apiMaps.size() == 0)
		{
			errors.add(messages.getMessage("map.no_match_for_id_user"));
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
		}
		// if we're only returning one map, don't return an array
		else if(apiMaps.size() == 1)
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
		MapsServletActions actions = new MapsServletActions();
		MapValidationHelper validation = new MapValidationHelper();
		
		try
		{
			user = actions.authenticateUser(request);
		}
		catch(LabyrinthException le)
		{
			errors.add(le.getMessage());
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
			return;
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
		
		// do not allow this endpoint for now
		boolean invalid = true;
		if(invalid)
		{
			apiOut(gson.toJson(new APIErrorMessage(messages.getMessage("method.no_put"))), response);
			return;
		}
		
		MapsServletActions actions = new MapsServletActions();
		User user = null;
		JsonObject data = null;
		int gameId = 0;
		int mapId = 0;
		
		mapId = actions.getIdFromUrl(request, EndpointsWithIds.MAPS);

		// load the maps array
		try
		{
			user = actions.authenticateUser(request);
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
	
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		errors.clear();
		MapsServletActions actions = new MapsServletActions();
		int mapId = 0;
		Map map = null;
		
		mapId = actions.getIdFromUrl(request, EndpointsWithIds.MAPS);
		
		// don't continue if there's no ID
		if(mapId == 0)
		{
			errors.add(messages.getMessage("map.need_id_to_delete"));
		}
		else
		{
			try
			{
				User user = actions.authenticateUser(request);
				map = new Map().loadOneMapByUser(user.getId(),  mapId);
				if(map == null)
				{
					errors.add(messages.getMessage("map.no_match_for_id_user"));
				}
				else
				{
					map.delete();
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
	}
	
	public void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		errors.clear();
		
		MapsOptions options = new MapsOptions();
		apiOut(gson.toJson(options), response);
	}
}
