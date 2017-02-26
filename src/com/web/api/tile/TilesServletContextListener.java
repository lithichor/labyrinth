package com.web.api.tile;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.parents.LabyrinthServletContextListener;

public class TilesServletContextListener extends LabyrinthServletContextListener
{
	public void contextInitialized(ServletContextEvent event)
	{
		ServletContext context = event.getServletContext();
		context.addServlet("tilesServlet", new TilesServlet()).addMapping("/api/tiles/*");
		context.addServlet("tilesMapServlet", new TilesMapServlet()).addMapping("/api/tiles/map/*");
	}
}
