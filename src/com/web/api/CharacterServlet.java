package com.web.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.parents.LabyrinthHttpServlet;

public class CharacterServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 2828781477059201765L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().println("Character Stuff");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
	}

	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
	}

	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
	}

}
