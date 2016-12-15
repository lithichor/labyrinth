package com.web.api.tile;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.parents.LabyrinthServletContextListener;

public class TileServletContextListener extends LabyrinthServletContextListener
{
	public void contextInitialized(ServletContextEvent event)
	{
		ServletContext context = event.getServletContext();
		context.addServlet("tileServlet", new TileServlet()).addMapping("/api/tiles/*");
		context.addServlet("tileMapServlet", new TileMapServlet()).addMapping("/api/tiles/map/*");
	}
}
