package com.web.api.hero;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.api.APIErrorMessage;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;
import com.web.api.game.Game;
import com.web.api.user.User;

public class HerosGameServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = -4317514178333261526L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
		int gameId = 0;
		ArrayList<Hero> heros = new ArrayList<>();
		ArrayList<APIHero> apiHeros = new ArrayList<>();
		HerosServletActions actions = new HerosServletActions();
		
		String idStr = splitUrl(request.getRequestURI(), EndpointsWithIds.HEROS_GAMES);
		
		// if there is a string after the endpoint
		if(idStr.length() > 0)
		{
			gameId = parseIdFromString(idStr);
		}

		try
		{
			User user = actions.authenticateUser(request);
			// throws exception if user doesn't own the game
			new Game().load(user.getId(), gameId);

			// if we have a gameId, load the Heros with it
			if(gameId > 0)
			{
				heros = new Hero().load(gameId, 0);
				// put the heros in the API list if there are any
				if(heros.size() > 0)
				{
					for(Hero hero: heros)
					{
						apiHeros.add(new APIHero(hero));
					}
					// there is only be one hero per game
					apiOut(gson.toJson(apiHeros.get(0)), response);
				}
				// if no heros found, add an error
				else
				{
					errors.add(messages.getMessage("hero.no_heros_found"));
				}
			}
			// if no game ID, add an error
			else
			{
				errors.add(messages.getMessage("hero.hero_needs_game_id"));
			}
		}
		catch(LabyrinthException le)
		{
			if(le.getMessage().contains("This Player does not have an active Game with that ID"))
			{
				errors.add(messages.getMessage("hero.no_user_auth"));
			}
			else
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
		
		HerosGameOptions options = new HerosGameOptions();
		apiOut(gson.toJson(options), response);
	}
}
