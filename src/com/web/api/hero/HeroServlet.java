package com.web.api.hero;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.LabyrinthConstants;
import com.google.gson.JsonObject;
import com.models.Hero;
import com.models.User;
import com.models.api.APIErrorMessage;
import com.models.api.APIHero;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;

public class HeroServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 2828781477059201765L;
	private Hero hero;
	
	public HeroServlet(Hero hero)
	{
		this.hero = hero;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
		HeroServletActions actions = new HeroServletActions();
		User user;
		JsonObject data;
		int gameId = 0;
		
		try
		{
			user = this.authenticateUser(request, response);
			data = actions.getData(request);

			gameId = actions.getGameId(user, data);
			
			hero = new Hero().load(gameId);
			response.getWriter().println(gson.toJson(new APIHero(hero)));
		}
		catch(LabyrinthException le)
		{
			le.printStackTrace();
			errors.add(le.getMessage());
		}
		
		if(errors.size() > 0)
		{
			response.getWriter().println(gson.toJson(new APIErrorMessage(errors)));
		}
	}

	/**
	 * Heros are only created in the context of a game, but they
	 * will be updated during gameplay
	 */
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
//		HeroValidationHelper validation = new HeroValidationHelper();
		HeroServletActions actions = new HeroServletActions();
		User user;
		JsonObject data = null;
		int gameId = 0;

		try
		{
			user = this.authenticateUser(request, response);
			data = actions.getData(request);
			
			gameId = actions.getGameId(user, data);

			hero = new Hero().load(gameId);
		}
		catch(LabyrinthException le)
		{
			errors.add(le.getMessage());
		}
		
		if(data == null && errors.isEmpty())
		{
			errors.add(LabyrinthConstants.HERO_HAS_NO_DATA);
		}
		
		if(errors.size() > 0)
		{
			response.getWriter().println(gson.toJson(new APIErrorMessage(errors)));
		}
		else
		{
			response.getWriter().println(gson.toJson(new APIHero(hero)));
		}
	}

}
