package com.web.api.map;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.parents.LabyrinthHttpServlet;
import com.web.api.user.User;

public class MapTilesServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 1842795190317747544L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
		@SuppressWarnings("unused")
		User user = null;
		
		
		apiOut("SUCCESS", response);
	}
}
