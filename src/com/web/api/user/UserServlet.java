package com.web.api.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.LabyrinthConstants;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.helpers.validation.UserValidationHelper;
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
			if(le.getMessage().contains(LabyrinthConstants.NO_AUTHORIZATION))
			{
				response.getWriter().println(gson.toJson(new APIErrorMessage(LabyrinthConstants.NO_AUTHORIZATION)));
			}
			else
			{
				response.getWriter().println(gson.toJson(new APIErrorMessage(LabyrinthConstants.UNKNOWN_ERROR)));
			}
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
			response.getWriter().write(gson.toJson(new APIErrorMessage(LabyrinthConstants.NO_SUCH_PLAYER)));
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
				errors.add(LabyrinthConstants.MALFORMED_JSON);
			}
		}

		user = new User();
		if(data != null)
		{
			user = validation.validateApi(data);
			
			// check to see if the email has already been used. If so, return with an error
			try
			{
				if(user.duplicateEmail())
				{
					response.getWriter().write(gson.toJson(new APIErrorMessage(LabyrinthConstants.USER_EXISTS)));
					return;
				}
			}
			catch(LabyrinthException le)
			{
				le.printStackTrace();
			}
		}
		
		// if the user is null, then either there were errors during
		// validation or an exception when parsing the data. That means
		// we need to return an error.
		if(user == null)
		{
			errors.addAll(validation.getErrors());
			response.getWriter().write(gson.toJson(errors));
		}
		else
		{
			try
			{
				user.save();
				response.getWriter().write(gson.toJson(new APIUser(user)));
			}
			catch(LabyrinthException le)
			{
				le.printStackTrace();
				response.getWriter().write(gson.toJson(LabyrinthConstants.HORRIBLY_WRONG));
			}
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
			if(le.getMessage().contains(LabyrinthConstants.NO_AUTHORIZATION))
			{
				response.getWriter().write(gson.toJson(new APIErrorMessage(LabyrinthConstants.NO_AUTHORIZATION)));
			}
			else
			{
				response.getWriter().write(gson.toJson(new APIErrorMessage(LabyrinthConstants.UNKNOWN_ERROR)));
			}
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
				response.getWriter().write(gson.toJson(new APIErrorMessage(LabyrinthConstants.HORRIBLY_WRONG)));
				sqle.printStackTrace();
			}
		}
		else
		{
			response.getWriter().write(gson.toJson(new APIErrorMessage(LabyrinthConstants.NO_SUCH_PLAYER)));
		}

		if(errors.size() > 0)
		{
			response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
		}
	}
	
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
		// use reader to get data
		BufferedReader br = request.getReader();
		String line = "";
		JsonObject data = null;
		UserValidationHelper validator = new UserValidationHelper();

		user = null;
		
		try
		{
			user = this.authenticateUser(request, response);
		}
		catch(LabyrinthException le)
		{
			if(le.getMessage().contains(LabyrinthConstants.NO_AUTHORIZATION))
			{
				response.getWriter().write(gson.toJson(new APIErrorMessage(LabyrinthConstants.NO_AUTHORIZATION)));
			}
			else
			{
				response.getWriter().write(gson.toJson(new APIErrorMessage(LabyrinthConstants.UNKNOWN_ERROR)));
			}
			return;
		}
		
		if(user == null)
		{
			response.getWriter().write(gson.toJson(new APIErrorMessage(LabyrinthConstants.NO_SUCH_PLAYER)));
		}
		else
		{
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
					response.getWriter().write(gson.toJson(new APIErrorMessage(LabyrinthConstants.MALFORMED_JSON)));
				}

				validator.validateApiPut(user, data);
			}

			try
			{
				user.update();
				response.getWriter().write(gson.toJson(new APIUser(user)));
			}
			catch(LabyrinthException le)
			{
				le.printStackTrace();
				response.getWriter().write(gson.toJson(new APIErrorMessage(LabyrinthConstants.HORRIBLY_WRONG)));
			}
		}
	}
}
