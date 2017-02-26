package com.web.api.game;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.api.APIErrorMessage;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;
import com.web.api.hero.Hero;
import com.web.api.maps.Map;
import com.web.api.user.User;

public class GamesServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 8963309769094254259L;
	private Game game;
	private User user;
	
	public GamesServlet(Game game, User user)
	{
		this.game = game;
		this.user = user;
	}
	
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
		GamesServletActions actions = new GamesServletActions();
		
		// this will get the id from the end of the url
		int id = 0;
		
		String idStr = splitUrl(request.getRequestURI(), EndpointsWithIds.GAMES);
		
		// if there is a string after the endpoint
		if(idStr.length() > 0)
		{
			id = parseIdFromString(idStr);
		}
		
		user = null;
		
		try
		{
			user = actions.authenticateUser(request);
		}
		catch(LabyrinthException le)
		{
			if(le.getMessage().contains(messages.getMessage("user.no_authorization")))
			{
				errors.add(messages.getMessage("user.no_authorization"));
			}
			else
			{
				errors.add(messages.getMessage("unknown.unknown_error"));
			}
		}
		
		// the user is authenticated
		if(user != null)
		{
			try
			{
				ArrayList<Game> games = new Game().load(user.getId(), id);
				ArrayList<APIGame> gs = new ArrayList<APIGame>();
				
				if("last".equalsIgnoreCase(idStr))
				{
					// get the last game from the array
					game = games.get(games.size() - 1);
					
					// create an api game, add hero and maps
					APIGame g = new APIGame(game);
					Hero hero = new Hero().load(game.getId(), 0).get(0);
					ArrayList<Map> maps = new Map().load(game.getId(), 0);
					g.setHeroId(hero.getId());
					for(Map m: maps)
					{
						g.addMapId(m.getId());
					}
					// return it as an array
					apiOut(gson.toJson(g), response);
				}
				else
				{
					for(Game game: games)
					{
						APIGame g = new APIGame(game);
						Hero hero = new Hero().load(game.getId(), 0).get(0);
						ArrayList<Map> maps = new Map().load(game.getId(), 0);
						g.setHeroId(hero.getId());
						for(Map m: maps)
						{
							g.addMapId(m.getId());
						}
						gs.add(g);
					}
					// if we're only returning one game, don't return an array
					if(gs.size() == 1)
					{
						apiOut(gson.toJson(gs.get(0)), response);
					}
					else
					{
						apiOut(gson.toJson(gs), response);
					}
					
				}
				
			}
			catch(LabyrinthException le)
			{
				if(le.getMessage().contains(messages.getMessage("game.no_games")) && id == 0)
				{
					errors.add(messages.getMessage("game.no_games"));
				}
				else if (le.getMessage().contains(messages.getMessage("game.no_games")) ||
						le.getMessage().contains(messages.getMessage("game.no_game_with_that_id")))
				{
					errors.add(messages.getMessage("game.no_game_with_that_id"));
				}
				else
				{
					errors.add(messages.getMessage("unknown.unknown_error"));
				}
			}
		}
		else
		{
			if(errors.size() == 0)
			{
				errors.add(messages.getMessage("user.no_such_player"));
			}
		}
		
		if(errors.size() > 0)
		{
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
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
	 * This action also creates a hero and a map. The map is fully populated
	 * with everything necessary to start play.
	 * 
	 * Response: The game, including user id, character id, map id, and starting
	 * coordinates.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
		int numberOfGames = 0;
		APIGame g = null;
		GamesServletActions actions = new GamesServletActions();
		
		user = null;
		
		try
		{
			user = actions.authenticateUser(request);
		}
		catch(LabyrinthException le)
		{
			if(le.getMessage().contains(messages.getMessage("user.no_authorization")) ||
					le.getMessage().contains(messages.getMessage("user.no_such_player")))
			{
				errors.add(le.getMessage());
			}
			else
			{
				errors.add(messages.getMessage("unknown.unknown_error"));
			}
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
			return;
		}
		
		boolean authenticated = (user != null);
		
		if(authenticated)
		{
			try
			{
				numberOfGames = game.getGameCount(user.getId());
			}
			catch(LabyrinthException le)
			{
				le.printStackTrace();
				errors.add(messages.getMessage("unknown.unknown_error"));
				apiOut(gson.toJson(new APIErrorMessage(errors)), response);
				return;
			}
		}
		
		if(authenticated && numberOfGames < 4)
		{
			try
			{
				g = game.startNewGame(user.getId());
			}
			catch(LabyrinthException le)
			{
				le.printStackTrace();
				errors.add(messages.getMessage("unknown.horribly_wrong"));
				apiOut(gson.toJson(new APIErrorMessage(errors)), response);
				return;
			}

			apiOut(gson.toJson(g), response);
		}
		else if(numberOfGames >= 4)
		{
			errors.add(messages.getMessage("game.too_many_games"));
		}
		else
		{
			errors.add(messages.getMessage("user.no_such_player"));
		}
		
		if(errors.size() > 0)
		{
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
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
		user = null;
		GamesServletActions actions = new GamesServletActions();
		
		try
		{
			user = actions.authenticateUser(request);
		}
		catch(LabyrinthException le)
		{
			if(le.getMessage().contains(messages.getMessage("user.no_authorization")))
			{
				errors.add(messages.getMessage("user.no_authorization"));
			}
			else
			{
				errors.add(messages.getMessage("unknown.unknown_error"));
			}
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
			return;
		}
		
		boolean authenticated = (user != null);
		int id = 0;
		
		String idStr = splitUrl(request.getRequestURI(), EndpointsWithIds.GAMES);
		
		// if there is a string after the endpoint
		if(idStr.length() > 0)
		{
			id = parseIdFromString(idStr);
		}

		if(authenticated)
		{
			if(id == 0)
			{
				errors.add(messages.getMessage("game.no_game_id_delete"));
			}
			else
			{
				try
				{
					ArrayList<Game> games = new Game().load(user.getId(), id);
					game = games.get(0);
					game.deleteGame();
				}
				catch(LabyrinthException le)
				{
					// if there's no game, return that message
					if(le.getMessage().contains(messages.getMessage("game.no_games")) ||
							le.getMessage().contains(messages.getMessage("game.no_game_with_that_id")))
					{
						errors.add(messages.getMessage("game.no_game_with_that_id"));
					}
					// otherwise, there's a problem that needs to be fixed
					else
					{
						errors.add(messages.getMessage("unknown.horribly_wrong"));
						le.printStackTrace();
					}
				}
			}
		}
		else
		{
			errors.add(messages.getMessage("user.no_such_player"));
		}

		if(errors.size() > 0)
		{
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
		}
	}
	
	public void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		errors.clear();
		
		GamesOptions options = new GamesOptions();
		apiOut(gson.toJson(options), response);
	}
}
