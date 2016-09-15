package com.web.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.Game;
import com.models.Hero;
import com.models.Map;
import com.LabyrinthConstants;
import com.models.User;
import com.models.api.APIErrorMessage;
import com.models.api.APIGame;
import com.models.api.APIHero;
import com.models.api.APIMap;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;

public class GameServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 8963309769094254259L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// this will get the id from the end of the url; need to add
		// parsing to remove any other junk, and validation to make sure
		// it's an integer. Maybe strip any extra junk by default
		
		// also need to make games before we can search for them by id
		String[] ids = request.getRequestURI().split("games/");
		String id = "";
		if(ids.length > 1)
		{
			id = ids[1];
		}
		if(id.length() > 0)
		{
			System.out.println("ID exists: " + id);
		}

		User user = this.authenticateUser(request, response);
		
		if(user != null)
		{
			try
			{
				Game game = new Game().load(user.getId());
				game.getId();
				// TODO: check for games after we can create them
			}
			catch(LabyrinthException le)
			{
				if(le.getMessage().equalsIgnoreCase(LabyrinthConstants.NO_GAME))
				{
					response.getWriter().write(gson.toJson(new APIErrorMessage(LabyrinthConstants.NO_GAME)));
				}
				else
				{
					String message = LabyrinthConstants.UNKNOWN_ERROR + ": " + le.getMessage();
					response.getWriter().write(gson.toJson(new APIErrorMessage(message)));
				}
			}
		}
		else
		{
			response.getWriter().write(gson.toJson(LabyrinthConstants.NO_SUCH_PLAYER));
		}
	}
	
	/**
	 * Create a new game.
	 * 
	 * This action also creates a character and a map. The map is fully populated
	 * with everything necessary to start play.
	 * 
	 * Response: The game, including user id, character id, map id, and starting
	 * coordinates.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		User user = this.authenticateUser(request, response);
		boolean authenticated = user != null;
		if(authenticated)
		{
			try
			{
				Game game = new Game();
				game.setUserId(user.getId());
				game.save();

				Hero hero = new Hero();
				hero.setGameId(game.getId());

				Map map = new Map();
				map.setGameId(game.getId());

				hero.save();
				map.save();

				APIGame g = new APIGame(game);
				g.setHero(new APIHero(hero));
				g.addMap(new APIMap(map));

				response.getWriter().write(gson.toJson(g));
			}
			catch (LabyrinthException le)
			{
				System.out.println(le.getMessage());
				le.printStackTrace();
			}
		}
		else
		{
			response.getWriter().write(gson.toJson(LabyrinthConstants.NO_SUCH_PLAYER));
		}
	}
}
