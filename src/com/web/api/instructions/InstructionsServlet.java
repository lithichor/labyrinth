package com.web.api.instructions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;

public class InstructionsServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 3608493802099931891L;

	/**
	 * This requires no authentication; the purpose is to give the user some
	 * idea of how to use the API.
	 * 
	 * The response includes a list of endpoints the user can make calls
	 * to. These are gathered by scanning the file structure for Servlet
	 * classes. By convention, these are named the same way as the URLs
	 * that call them. Roughly.
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		ArrayList<String> fileNames = null;
		HashMap<String, Object> info = new HashMap<>();
		InstructionsActions actions = new InstructionsActions();
		
		String apiFolder = getServletContext().getRealPath(".");
		
		try
		{
			fileNames = actions.getFileNames(apiFolder);
		}
		catch(LabyrinthException le)
		{
			apiOut(gson.toJson(le.getMessage()), response);
			return;
		}
		
		info.put("instructions", "If you make an Options request for each of these endpoints "
				+ "you will get more detailed instructions on how to use the endpoints.");
		info.put("endpoints", fileNames);

		apiOut(gson.toJson(info), response);
	}

	public void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		errors.clear();
		
		InstructionsOptions options = new InstructionsOptions();
		apiOut(gson.toJson(options), response);
	}
}
