package com.web.api.hero;

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
import com.web.api.user.User;

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
		int heroId = 0;
		ArrayList<Hero> heros = new ArrayList<>();
		HeroServletActions actions = new HeroServletActions();
		
		String idStr = splitUrl(request.getRequestURI(), EndpointsWithIds.HEROS);
		
		// if there is a string after the endpoint
		if(idStr.length() > 0)
		{
			heroId = parseIdFromString(idStr);
		}

		try
		{
			// might still need a check for null here
			user = actions.authenticateUser(request);
			
			heros = new Hero().loadByUser(user.getId());
			if(heros.size() == 0)
			{
				errors.add(messages.getMessage("hero.no_user_auth"));
			}
			else
			{
				// if there is no heroId and only one hero in the list
				if((heroId <= 0) && (heros.size() == 1))
				{
					apiOut(gson.toJson(new APIHero(heros.get(0))), response);
				}
				// if there is no heroId and multiple heros in list
				else if((heroId == 0) && (heros.size() > 1))
				{
					// make array list of apiHeros and output it
					ArrayList<APIHero> apiHeros = new ArrayList<APIHero>();
					for(Hero h: heros)
					{
						apiHeros.add(new APIHero(h));
					}
					apiOut(gson.toJson(apiHeros), response);
				}
				// if there is a heroId
				else
				{
					boolean heroLoaded = false;
					// check list of heros for the heroId
					for(Hero h: heros)
					{
						if(h.getId() == heroId)
						{
							hero = h;
							heroLoaded = true;
						}
					}
					if(heroLoaded)
					{
						apiOut(gson.toJson(new APIHero(hero)), response);
					}
					else
					{
						errors.add(messages.getMessage("hero.no_user_auth"));
					}
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
		JsonObject data = null;
		int heroId = 0;

		String idStr = splitUrl(request.getRequestURI(), EndpointsWithIds.HEROS);
		
		// if there is a string after the endpoint
		if(idStr.length() > 0)
		{
			heroId = parseIdFromString(idStr);
		}
		if(heroId == 0)
		{
			errors.add(messages.getMessage("hero.hero_needs_id"));
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
			return;
		}

		try
		{
			actions.authenticateUser(request);
			data = actions.getData(request);
			
			hero = (new Hero().load(0, heroId)).get(0);
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
	
	public void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		errors.clear();
		
		HerosOptions options = new HerosOptions();
		apiOut(gson.toJson(options), response);
	}
}
