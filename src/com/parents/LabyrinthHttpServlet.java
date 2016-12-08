package com.parents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.helpers.Encryptor;
import com.labels.LabyrinthMessages;
import com.models.User;
import com.models.api.APIErrorMessage;

public abstract class LabyrinthHttpServlet extends HttpServlet
{
	private static final long serialVersionUID = 6063276226361967817L;
	protected ArrayList<String> errors = new ArrayList<String>();
	protected Gson gson = new Gson();
	protected LabyrinthMessages messages = new LabyrinthMessages();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(messages.getMessage("method.no_get"));
		response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(messages.getMessage("method.no_post"));
		response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
	}
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(messages.getMessage("method.no_delete"));
		response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
	}
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(messages.getMessage("method.no_put"));
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
		errors.add(messages.getMessage("method.no_options"));
		response.getWriter().write(gson.toJson(new APIErrorMessage(errors)));
	}
	// Tomcat seems to disallow this method; not sure if
	// that would happen with other web containers or not
	public void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(messages.getMessage("method.no_trace"));
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
			throw new LabyrinthException(messages.getMessage("user.no_authorization"));
			
		}

		if(authorization != null && authorization[0].contains("Basic"))
		{
			String auth = e.decrypt(authorization[1]);
			String email = auth.split(":")[0];
			String password = auth.split(":")[1];
			if(email == null || "".equals(email) || password == null || "".equals(password))
			{
				throw new LabyrinthException(messages.getMessage("user.partial_authorization"));
			}
			u = new User();
			u.setEmail(email);
			u.setPassword(e.encrypt(password));
			try
			{
				u = u.login();
			}
			catch(LabyrinthException le)
			{
				throw le;
			}
		}
		return u;
	}
	
	public void apiOut(String output, HttpServletResponse response) throws IOException
	{
		response.getWriter().println(output);
	}

	protected String splitUrl(String url, String endpoint)
	{
		String[] parsedUrl = url.split(endpoint + "/");
		String idString = "";
		
		if(parsedUrl.length > 1)
		{
			idString = parsedUrl[1];
		}
		
		return idString;
	}
	
	protected int parseIdFromString(String idString)
	{
		int id = 0;
		
		try
		{
			id = Integer.parseInt(idString);
		}
		catch(NumberFormatException nfe)
		{
			// id was not a valid integer - ignore it
			id = 0;
		}
		
		return id;
	}
}
