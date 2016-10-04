package com.web.api;

import java.io.IOException;
import java.util.ArrayList;

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
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;

public class GameServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 8963309769094254259L;

	/**
	 * api/games -GET
	 * api/games/:id -GET
	 * 
	 * If a valid ID is part of the URL, then the method will
	 * parse it and retrieve just the game with that ID. If no
	 * ID is found in the URL it returns all games for the user.
	 * 
	 * This method requires user credentials
	 * 
	 * response: The game, including user id, character id, map ids, and
	 * current coordinates.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
		// this will get the id from the end of the url; need to add
		// parsing to remove any other junk, and validation to make sure
		// it's an integer. Maybe strip any extra junk by default
		
		// also need to make games before we can search for them by id
		String[] ids = request.getRequestURI().split("games/");
		String idStr = "";
		int id = 0;
		if(ids.length > 1)
		{
			idStr = ids[1];
		}
		// if there is a string after the endpoint
		if(idStr.length() > 0)
		{
			try
			{
				id = Integer.parseInt(idStr);
			}
			catch(NumberFormatException nfe)
			{
				// id was not a valid integer - ignore it
				id = 0;
			}
		}

		User user = this.authenticateUser(request, response);
		
		// the user is authenticated
		if(user != null)
		{
			try
			{
				ArrayList<Game> games = new Game().load(user.getId(), id);
				ArrayList<APIGame> gs = new ArrayList<APIGame>();
				for(Game game: games)
				{
					APIGame g = new APIGame(game);
					Hero hero = new Hero().load(game.getId(), 0);
					ArrayList<Map> maps = new Map().load(game.getId(), 0);
					g.setHeroId(hero.getId());
					for(Map m: maps)
					{
						g.addMapId(m.getId());
					}
					gs.add(g);
				}
				
				response.getWriter().write(gson.toJson(gs));
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
	 * api/games -POST
	 * 
	 * Create a new game. A player can only have four active games at a time; if
	 * this method is called and the player has four or more active games, it will return
	 * an error message saying a new game cannot be created until one or more are
	 * deleted.
	 * 
	 * This action also creates a character and a map. The map is fully populated
	 * with everything necessary to start play.
	 * 
	 * Response: The game, including user id, character id, map id, and starting
	 * coordinates.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
		User user = this.authenticateUser(request, response);
		boolean authenticated = (user != null);
		boolean abort = false;
		int numberOfGames = 0;
		APIGame g = null;
		
		try
		{
			numberOfGames = new Game().getGameCount(user.getId());
		}
		catch(LabyrinthException le)
		{
			le.printStackTrace();
			// add these errors to the error array and print that at the end
			errors.add(LabyrinthConstants.HORRIBLY_WRONG);
			abort = true;
		}
		
		if(authenticated && numberOfGames < 4 && !abort)
		{
			try
			{
				g = new Game().startNewGame(user.getId());
			}
			catch(LabyrinthException le)
			{
				le.printStackTrace();
				errors.add(LabyrinthConstants.HORRIBLY_WRONG);
				abort = true;
			}

			response.getWriter().write(gson.toJson(g));
		}
		else if(numberOfGames >= 4 && !abort)
		{
			errors.add(LabyrinthConstants.TOO_MANY_GAMES);
		}
		else if(!abort)
		{
			errors.add(LabyrinthConstants.NO_SUCH_PLAYER);
		}
		
		if(errors.size() > 0)
		{
			response.getWriter().write(gson.toJson(errors));
		}
	}
	
	/**
	 * api/games/:id -DELETE
	 * 
	 * This deletes a game for the user. It is a soft delete; the
	 * game is still present in the database, but is not available
	 * to the player.
	 */
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
		User user = this.authenticateUser(request, response);
		boolean authenticated = (user != null);
		
		String[] ids = request.getRequestURI().split("games/");
		String idStr = "";
		int id = 0;
		if(ids.length > 1)
		{
			idStr = ids[1];
		}
		// if there is a string after the endpoint
		if(idStr.length() > 0)
		{
			try
			{
				id = Integer.parseInt(idStr);
			}
			catch(NumberFormatException nfe)
			{
				// id was not a valid integer - ignore it
				id = 0;
			}
		}

		if(authenticated)
		{
			if(id == 0)
			{
				errors.add(LabyrinthConstants.NO_GAME_ID);
			}
			else
			{
				try
				{
					ArrayList<Game> games = new Game().load(user.getId(), id);
					Game game = games.get(0);
					game.deleteGame();
				}
				catch(LabyrinthException le)
				{
					// if there's no game, return that message
					if(le.getMessage().contains(LabyrinthConstants.NO_GAME))
					{
						errors.add(LabyrinthConstants.NO_GAME_WITH_THAT_ID);
					}
					// otherwise, there's a problem that needs to be fixed
					else
					{
						errors.add(LabyrinthConstants.HORRIBLY_WRONG);
						le.printStackTrace();
					}
				}
			}
		}
		else
		{
			response.getWriter().write(gson.toJson(LabyrinthConstants.NO_SUCH_PLAYER));
		}

		if(errors.size() > 0)
		{
			response.getWriter().write(gson.toJson(errors));
		}
	}
}
