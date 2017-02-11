package com.web.api.map;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.parents.LabyrinthServletContextListener;

public class MapServletContextListener extends LabyrinthServletContextListener
{
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		ServletContext context = event.getServletContext();
		context.addServlet("mapServlet", new MapServlet(new ArrayList<Map>())).addMapping("/api/maps/*");
		context.addServlet("mapGamesServlet", new MapGamesServlet(new ArrayList<Map>())).addMapping("/api/maps/games/*");
	}
}
