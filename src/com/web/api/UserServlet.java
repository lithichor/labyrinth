package com.web.api;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.LabyrinthConstants;
import com.models.User;
import com.models.api.APIUser;
import com.parents.LabyrinthHttpServlet;

public class UserServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 3194656746956466374L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Enumeration<String>requestHeaders = request.getHeaderNames();
		System.out.println("OIUHGFCVBGHJ");;
		while(requestHeaders.hasMoreElements())
		{
			String element = requestHeaders.nextElement();
			String header = request.getHeader(element);
			System.out.println(element + ": " + header);
		}

		User user = this.authenticateUser(request, response);
		boolean authenticated = (user != null);
		
		if(authenticated)
		{
			APIUser u = new APIUser(user);
			
			response.getWriter().println(gson.toJson(u));
			System.out.println(gson.toJson(u));
		}
		else
		{
			response.getWriter().write(gson.toJson(LabyrinthConstants.NO_SUCH_USER));
		}
	}

}
