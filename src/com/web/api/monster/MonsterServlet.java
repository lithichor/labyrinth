package com.web.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.parents.LabyrinthHttpServlet;

public class MonsterServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 2502557358566091760L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().println("Monster Info");
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
