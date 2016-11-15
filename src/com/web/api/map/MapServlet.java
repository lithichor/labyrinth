package com.web.api.map;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.models.Map;
import com.models.User;
import com.models.api.APIErrorMessage;
import com.models.api.APIMap;
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

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
		MapServletActions actions = new MapServletActions();
		User user;
		JsonObject data;
		int gameId;
		ArrayList<APIMap> apiMaps = new ArrayList<APIMap>();
		
		try
		{
			user = this.authenticateUser(request, response);
			data = actions.getData(request);
			
			gameId = actions.getGameId(user, data);
			
			maps = new Map().load(gameId, 0);
			for(Map m: maps)
			{
				APIMap am = new APIMap(m);
				apiMaps.add(am);
			}
			apiOut(gson.toJson(apiMaps), response);
		}
		catch(LabyrinthException le)
		{
			errors.add(le.getMessage());
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
		}
	}

	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
	}
}
