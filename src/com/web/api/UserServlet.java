package com.web.api;

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

	/**
	 * /api/user - the GET method returns the information for the user whose
	 * credentials are used for authentication. A user cannot see other users'
	 * information.
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		boolean debug = false;
		
		if(debug)
		{
			Enumeration<String>requestHeaders = request.getHeaderNames();
			while(requestHeaders.hasMoreElements())
			{
				String element = requestHeaders.nextElement();
				String header = request.getHeader(element);
				System.out.println(element + ": " + header);
			}
		}

		User user = this.authenticateUser(request, response);
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
				le.printStackTrace();
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
		// use reader to get data
		BufferedReader br = request.getReader();
		String line = "";
		JsonObject data = null;
		UserValidationHelper vhelper = new UserValidationHelper();
		errors.clear();

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
				System.out.println("Uh oh!");
				errors.add(LabyrinthConstants.MALFORMED_JSON);
			}
			
			System.out.println(line);
		}

		User user = null;
		if(data != null)
		{
			user = vhelper.validateApi(data);
		}
		
		// if the user is null, then either there were errors during
		// validation or an exception when parsing the data. That means
		// we need to return an error.
		if(user == null)
		{
			errors.addAll(vhelper.getErrors());
			response.getWriter().write(gson.toJson(errors));
		}
		else
		{
			response.getWriter().write(gson.toJson(user));
		}
		
	}
}
