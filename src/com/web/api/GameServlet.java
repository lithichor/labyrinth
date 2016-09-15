package com.web.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.Game;
import com.LabyrinthConstants;
import com.models.Character;
import com.models.User;
import com.models.api.APIGame;
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
		String id = request.getRequestURI().split("games/")[1];
		if(id.length() > 0)
		{
			System.out.println("ID exists");
		};
				
		User user = this.authenticateUser(request, response);
		
		if(user != null)
		{
			try
			{
				Game game = new Game().load(user.getId());
				game.getId();
			}
			catch(LabyrinthException le)
			{
				if(le.getMessage().equalsIgnoreCase(LabyrinthConstants.NO_GAME))
				{
					response.getWriter().write(gson.toJson(LabyrinthConstants.NO_GAME));
				}
				else
				{
					String message = LabyrinthConstants.UNKNOWN_ERROR + ": " + le.getMessage();
					response.getWriter().write(gson.toJson(message));
				}
			}
		}
		else
		{
			response.getWriter().write(gson.toJson(LabyrinthConstants.NO_SUCH_USER));
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
		// if game exists, mark deleted
		// create new game instance
		// return game object to user
		
		User user = this.authenticateUser(request, response);
		boolean authenticated = user != null;
		if(authenticated)
		{
			try
			{
				// 1. check for existing game
				Game game = new Game().load(user.getId());
				APIGame g = new APIGame(game);
				
				Character character = new Character();
				character.setGameId(game.getId());
				
				response.getWriter().write(gson.toJson(g));
			}
			catch (LabyrinthException le)
			{
				if(le.getMessage().equalsIgnoreCase(LabyrinthConstants.NO_GAME))
				{
					System.out.println("Creating new game");
				// 2. create a new game
				//  a. create character
				//  b. create map
				}
				else
				{
					System.out.println(le.getMessage());
					le.printStackTrace();
				}
			}
			
			try
			{
				Game game = new Game();
				game.setUserId(user.getId());
				game.save();
			}
			catch(LabyrinthException le)
			{
				System.out.println(le.getMessage());
				le.printStackTrace();
			}
		}
	}
}
