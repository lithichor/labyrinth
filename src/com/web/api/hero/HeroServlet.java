package com.web.api.hero;

import java.io.IOException;
import java.util.ArrayList;

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
		
		User user;
		int gameId = 0;
		ArrayList<Hero> heros = new ArrayList<>();
		HeroServletActions actions = new HeroServletActions();
		
		try
		{
			// might still need a check here
			user = actions.authenticateUser(request);
			
			// if we have a gameId, load a single Hero with it
			// TODO: there will no longer be a game ID; this will
			// take a hero ID instead (Labyrinth #139)
			if(gameId > 0)
			{
				hero = new Hero().load(gameId);
				apiOut(gson.toJson(new APIHero(hero)), response);
			}
			// otherwise, load an array of Heros with the userId
			else
			{
				heros = new Hero().loadByUser(user.getId());
				
				// if only one Hero found, do not return an array list
				if(heros.size() == 1)
				{
					apiOut(gson.toJson(new APIHero(heros.get(0))), response);
				}
				else
				{
					// make array list of apiHeros and output it
					ArrayList<APIHero> apiHeros = new ArrayList<APIHero>();
					for(Hero h: heros)
					{
						apiHeros.add(new APIHero(h));
					}
					apiOut(gson.toJson(apiHeros), response);
				}
			}
		}
		catch(LabyrinthException le)
		{
			errors.add(le.getMessage());
		}
		
		if(errors.size() > 0)
		{
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
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
			user = actions.authenticateUser(request);
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
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
		}
		else
		{
			apiOut(gson.toJson(new APIHero(hero)), response);
		}
	}

}
