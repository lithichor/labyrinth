package com.parents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.LabyrinthConstants;
import com.google.gson.Gson;
import com.helpers.Encryptor;
import com.models.User;
import com.models.api.APIErrorMessage;

public abstract class LabyrinthHttpServlet extends HttpServlet
{
	private static final long serialVersionUID = 6063276226361967817L;
	protected ArrayList<String> errors = new ArrayList<String>();
	protected Gson gson = new Gson();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(LabyrinthConstants.NO_GET);
		response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(LabyrinthConstants.NO_POST);
		response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
	}
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(LabyrinthConstants.NO_DELETE);
		response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
	}
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(LabyrinthConstants.NO_PUT);
		response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
	}
	// HEAD is valid for all endpoints, because it doesn't
	// return a body; it returns metadata only
	public void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
	}
	public void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(LabyrinthConstants.NO_OPTIONS);
		response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
	}
	// Tomcat seems to disallow this method; not sure if
	// that would happen with other web containers or not
	public void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(LabyrinthConstants.NO_TRACE);
		response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
	}

	protected void forward(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException
	{
		//Always set the errors
		request.setAttribute("errors", errors);

		RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
		rd.forward(request, response);
	}

	protected User authenticateUser(HttpServletRequest request, HttpServletResponse response) throws LabyrinthException
	{
		boolean debug = false;
		
		Encryptor e = Encryptor.getInstance();
		String[] authorization = null;
		User u = null;
		
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

		try
		{
			authorization = request.getHeader("authorization").split(" ");
		}
		catch(NullPointerException npe)
		{
			// this exception is expected for the case when the user has not
			// included credentials
			throw new LabyrinthException(LabyrinthConstants.NO_AUTHORIZATION);
			
		}

		if(authorization != null && authorization[0].contains("Basic"))
		{
			String auth = e.decrypt(authorization[1]);
			String email = auth.split(":")[0];
			String password = auth.split(":")[1];
			u = new User();
			u.setEmail(email);
			u.setPassword(e.encrypt(password));
			try
			{
				u = u.login();
			}
			catch(LabyrinthException le)
			{
				errors.add(LabyrinthConstants.UNKNOWN_ERROR);
				u = null;
			}
		}
		return u;
	}

	public boolean validate(HashMap<String, String> params, ArrayList<String> errors)
	{
		return false;
	}

	public boolean save(LabyrinthModel model)
	{
		return false;
	}

}
