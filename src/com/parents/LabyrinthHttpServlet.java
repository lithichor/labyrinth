package com.parents;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.labels.LabyrinthMessages;
import com.models.api.APIErrorMessage;

import test.tests.wrappers.GsonWrapper;

public abstract class LabyrinthHttpServlet extends HttpServlet
{
	private static final long serialVersionUID = 6063276226361967817L;
	protected ArrayList<String> errors = new ArrayList<String>();
	protected GsonWrapper gson = new GsonWrapper();
	protected LabyrinthMessages messages = new LabyrinthMessages();
	
	public void setGson(GsonWrapper gson) { this.gson = gson; }
	
	public ArrayList<String> getErrors()
	{
		return errors;
	}
	
	public String getErrorsAsString()
	{
		String errorStr = "\n";
		
		for(String error: errors)
		{
			errorStr += error + "\n";
		}
		return errorStr;
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(messages.getMessage("method.no_get"));
		apiOut(gson.toJson(new APIErrorMessage(errors)), response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(messages.getMessage("method.no_post"));
		apiOut(gson.toJson(new APIErrorMessage(errors)), response);
	}
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(messages.getMessage("method.no_delete"));
		apiOut(gson.toJson(new APIErrorMessage(errors)), response);
	}
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(messages.getMessage("method.no_put"));
		apiOut(gson.toJson(new APIErrorMessage(errors)), response);
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
		apiOut(gson.toJson(new APIErrorMessage(errors)), response);
	}
	// Tomcat seems to disallow this method; not sure if
	// that would happen with other web containers or not
	public void doTrace(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		errors.add(messages.getMessage("method.no_trace"));
		apiOut(gson.toJson(new APIErrorMessage(errors)), response);
	}

	protected void forward(HttpServletRequest request, HttpServletResponse response, String url) throws ServletException, IOException
	{
		//Always set the errors
		request.setAttribute("errors", errors);

		RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
		rd.forward(request, response);
	}

	public void apiOut(String output, HttpServletResponse response) throws IOException
	{
		response.getWriter().println(output);
	}
}
