package com.web.api.maps;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.parents.LabyrinthServletContextListener;

public class MapsServletContextListener extends LabyrinthServletContextListener
{
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		ServletContext context = event.getServletContext();
		context.addServlet("mapsServlet", new MapsServlet(new ArrayList<Map>())).addMapping("/api/maps/*");
		context.addServlet("mapsGamesServlet", new MapsGameServlet(new ArrayList<Map>())).addMapping("/api/maps/game/*");
	}
}
