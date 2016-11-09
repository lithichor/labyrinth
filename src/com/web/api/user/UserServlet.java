package com.web.api.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.models.Game;
import com.models.User;
import com.models.api.APIErrorMessage;
import com.models.api.APIUser;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;

public class UserServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 3194656746956466374L;
	private User user;

	public UserServlet(User user)
	{
		this.user = user;
	}

	/**
	 * /api/user - the GET method returns the information for the user whose
	 * credentials are used for authentication. A user cannot see other users'
	 * information.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();

		boolean debug = false;

		if(debug)
		{
			Enumeration<String> requestHeaders = request.getHeaderNames();
			while(requestHeaders.hasMoreElements())
			{
				String element = requestHeaders.nextElement();
				String header = request.getHeader(element);
				System.out.println(element + ": " + header);
			}
		}

		user = null;

		try
		{
			user = this.authenticateUser(request, response);
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
			response.getWriter().println(gson.toJson(new APIErrorMessage(errors)));
			return;
		}

		boolean authenticated = (user != null);

		if(authenticated)
		{
			APIUser u = new APIUser(user);
			try
			{
				ArrayList<Game> games = new Game().load(user.getId(), 0);
				for(Game game: games)
				{
					u.addGame(game.getId());
				}
			}
			catch(LabyrinthException le)
			{
				// don't throw an error if there are no games; just add
				// an empty array to the user
				u.setGameIds(new ArrayList<Integer>());
			}

			response.getWriter().println(gson.toJson(u));
		}
		else
		{
			errors.add(messages.getMessage("user.no_such_player"));
		}
		if(errors.size() > 0)
		{
			response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
		}
	}

	/**
	 * api/user - the POST method creates a new user; used by clients for signup when
	 * there is no signup page.
	 * 
	 * TODO: GAME #26 - Add requirement that admin authentication is required for signup
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();

		// use reader to get data
		BufferedReader br = request.getReader();
		String line = "";
		JsonObject data = null;
		UserValidationHelper validation = new UserValidationHelper();

		while(line != null)
		{
			line = br.readLine();
			try
			{
				data = gson.fromJson(line, JsonObject.class);
				line = null;
			}
			catch(JsonSyntaxException jse)
			{
				errors.add(messages.getMessage("unknown.malformed_json"));
			}
		}

		if(data != null)
		{
			// user returns null here if it fails validation
			user = validation.validateApi(data);

			if(user != null)
			{
				// check to see if the email has already been used. If so, return with an error
				try
				{
					if(user.duplicateEmail())
					{
						errors.add(messages.getMessage("signup.user_exists"));
						user = null;
					}
				}
				catch(LabyrinthException le)
				{
					le.printStackTrace();
					errors.add(messages.getMessage("unknown.horribly_wrong"));
					user = null;
				}
			}
		}
		else
		{
			// if the data is null, set the user to null so we skip the
			// next block of code, and add an error message so we have
			// something to return
			user = null;
			errors.add(messages.getMessage("signup.user_has_no_data"));
		}

		// if the user is null, then either there were errors during
		// validation, an exception when parsing the data, or an error
		// while checking for duplicates. That means we need to return
		// with the errors
		if(user == null)
		{
			errors.addAll(validation.getErrors());
		}
		else
		{
			try
			{
				user.save();
				APIUser u = new APIUser(user);
				// gameIds is empty because it's a new user
				u.setGameIds(new ArrayList<Integer>());
				response.getWriter().write(gson.toJson(u));
			}
			catch(LabyrinthException le)
			{
				le.printStackTrace();
				errors.add(messages.getMessage("unknown.horribly_wrong"));
			}
		}
		if(errors.size() > 0)
		{
			response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
		}
	}

	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();

		UserServletPutActions actions = new UserServletPutActions();

		// use reader to get data
		JsonObject data = null;
		UserValidationHelper validator = new UserValidationHelper();

		user = null;

		try
		{
			user = actions.authenticateUser(request, response);
		}
		catch(LabyrinthException le)
		{
			if(le.getMessage().contains(messages.getMessage("user.no_authorization")))
			{
				errors.add(messages.getMessage("user.no_authorization"));
			}
			else
			{
				le.printStackTrace();
				errors.add(messages.getMessage("unknown.unknown_error"));
			}
		}

		if(user == null)
		{
			if(errors.size() == 0)
			{
				errors.add(messages.getMessage("user.no_such_player"));
			}
		}
		else
		{
			try
			{
				data = actions.getData(request);
			}
			catch(LabyrinthException le)
			{
				errors.add(le.getMessage());
			}

			if(data != null)
			{
				validator.validateApiPut(user, data);
				try
				{
					// save the user
					user.update();
					APIUser u = new APIUser(user);
					try
					{
						// get the games for this user
						ArrayList<Game> gs = new Game().load(user.getId(), 0);
						ArrayList<Integer> gids = new ArrayList<Integer>();
						// create a list of the game ids
						for(int x = 0; x < gs.size(); x++)
						{
							gids.add(gs.get(x).getId());
						}
						// and put the list in the API user object
						u.setGameIds(gids);
					}
					catch(LabyrinthException le)
					{
						// unless there are no games; then add an empty list
						u.setGameIds(new ArrayList<Integer>());
					}
					response.getWriter().write(gson.toJson(u));
				}
				catch(LabyrinthException le)
				{
					le.printStackTrace();
					errors.add(messages.getMessage("unknown.horribly_wrong"));
				}
			}
		}

		if(errors.size() > 0)
		{
			response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
		}
	}

	/**
	 * api/user -DELETE
	 * 
	 * This method deletes the authenticated user with a soft delete; the
	 * deleted_at field is set to now().
	 */
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();

		user = null;

		try
		{
			user = this.authenticateUser(request, response);
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
			response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
			return;

		}
		boolean authenticated = (user != null);

		if(authenticated)
		{
			try
			{
				user.deleteUser();
			}
			catch(LabyrinthException sqle)
			{
				errors.add(messages.getMessage("unknown.horribly_wrong"));
				sqle.printStackTrace();
			}
		}
		else
		{
			errors.add(messages.getMessage("user.no_such_player"));
		}

		if(errors.size() > 0)
		{
			response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
		}
	}
}
