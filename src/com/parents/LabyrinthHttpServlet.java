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
		this.returnError(response, "GET not supported for this endpont");
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.returnError(response, "POST not supported for this endpont");
	}
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.returnError(response, "DELETE not supported for this endpont");
	}
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.returnError(response, "PUT not supported for this endpont");
	}
	public void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.returnError(response, "HEAD not supported for this endpont");
	}
	public void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.returnError(response, "OPTIONS not supported for this endpont");
	}
	public void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.returnError(response, "TRACE not supported for this endpont");
	}

	protected void forward(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException
	{
		//Always set the errors
		request.setAttribute("errors", errors);

		RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
		rd.forward(request, response);
	}

	protected void returnError(HttpServletResponse response, String error)
	{
		APIErrorMessage aem = new APIErrorMessage(error);
		try
		{
			response.getWriter().println(gson.toJson(aem));
		}
		catch(IOException ioe)
		{
			System.out.println("Something went horribly wrong while returning an error" + ioe.getMessage());
			ioe.printStackTrace();
		}
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
				this.returnError(response, "That user/password combination does not exist");
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
