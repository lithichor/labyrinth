package com.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.LabyrinthConstants;
import com.helpers.Encryptor;
import com.models.User;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;

public class LoginServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 8020498799316191565L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doPost(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		String action = request.getParameter("action");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Encryptor e = Encryptor.getInstance();
		User user = (User)request.getSession().getAttribute("user");
		boolean hasCredentials = true;
		
		if("".equalsIgnoreCase(username))
		{
			errors.add(LabyrinthConstants.MUST_ENTER_EMAIL);
			hasCredentials = false;
		}
		if("".equalsIgnoreCase(password))
		{
			errors.add(LabyrinthConstants.MUST_ENTER_PASSWORD);
			hasCredentials = false;
		}
		if(action == null)
		{
			this.forward(request, response, "/");
		}
		else if(action.equalsIgnoreCase("logout"))
		{
			//logged in, clicked logout link
			request.getSession().setAttribute("user", null);
			this.forward(request, response, "/");
		}
		else if(action.equalsIgnoreCase("login") && user == null && hasCredentials)
		{
			try
			{
				user = new User().login(username, e.encrypt(password));
			}
			catch (LabyrinthException we)
			{
				System.out.println("Error logging in: " + we.getMessage());
				errors.add("That user password combination was not found");
				user = null;
			}
			
			request.getSession().setAttribute("user", user);
			if(errors.size() > 0)
			{
				this.forward(request, response, "/");
			}
			else
			{
				this.forward(request, response, "/");
			}
		}		
		else if(!hasCredentials)
		{
			this.forward(request, response, "/");
		}
		else
		{
			errors.add(LabyrinthConstants.UNKNOWN_ERROR);
			this.forward(request, response, "/");
		}
	}
}
