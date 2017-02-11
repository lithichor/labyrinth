package com.web.api.map;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.Map;
import com.models.api.APIErrorMessage;
import com.models.api.APIMap;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;
import com.web.api.game.Game;
import com.web.api.user.User;

public class MapGamesServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 3158009229795172043L;
	ArrayList<Map> maps;

	public MapGamesServlet(ArrayList<Map> map)
	{
		this.maps = map;
	}
	
	/**
	 * This endpoint allows the user to get a Map for a specified Game. If
	 * the ID is not in a valid format, the endpoint defaults to returning
	 * the Maps for the most recent Game.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		errors.clear();
		
		MapServletActions actions = new MapServletActions();
		User user;
		int gameId = 0;
		String idStr = splitUrl(request.getRequestURI(), EndpointsWithIds.MAPS_GAMES);
		ArrayList<APIMap> apiMaps = new ArrayList<APIMap>();
		
		if(idStr.length() > 0)
		{
			gameId = parseIdFromString(idStr);
		}

		try
		{
			user = actions.authenticateUser(request);

			// if no gameId is provided, get the most recent
			// gameId for the user as the default action
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
		
		if(apiMaps.size() == 1)
		{
			apiOut(gson.toJson(apiMaps.get(0)), response);
		}
		else
		{
			apiOut(gson.toJson(apiMaps), response);
		}
	}
}
