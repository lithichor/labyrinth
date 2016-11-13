package com.web.api.hero;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
			errors.add(le.getMessage());
		}
		
		if(errors.size() > 0)
		{
			response.getWriter().println(gson.toJson(new APIErrorMessage(errors)));
		}
	}

	/**
	 * Heros are generally only updated in the context of a game. This allows for
	 * non-play upgrades to a Hero
	 */
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
		HeroValidationHelper validation = new HeroValidationHelper();
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
		
		if(data == null)
		{
			if(errors.isEmpty())
			{
				errors.add(messages.getMessage("hero.hero_has_no_data"));
			}
		}
		else
		{
			try
			{
				hero.merge(validation.validateApiPut(data, hero.getId()));
				hero.update();
			}
			catch(LabyrinthException le)
			{
				// if because of a bad attribute value, add errors to the errors array
				if(le.getMessage().contains(messages.getMessage("hero.bad_attributes")))
				{
					errors.addAll(validation.getErrors());
				}
				else
				{
					le.printStackTrace();
					// either the heroId is missing or something else went
					// kablooie, so hide the details from the end user
					errors.add(messages.getMessage("unknown.horribly_wrong"));
				}
			}
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
