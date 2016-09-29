package com.web.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.LabyrinthConstants;
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
		Enumeration<String>requestHeaders = request.getHeaderNames();
		
		if(debug)
		{
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

}
