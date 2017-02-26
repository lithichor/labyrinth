package com.web.api.turn;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.parents.LabyrinthServletContextListener;

public class TurnsServletContextListener extends LabyrinthServletContextListener
{
	public void contextInitialized(ServletContextEvent event)
	{
		ServletContext context = event.getServletContext();
		context.addServlet("turnServlet", new TurnsServlet()).addMapping("/api/turns/*");
		context.addServlet("turnGameServlet", new TurnsGameServlet()).addMapping("/api/turns/game/*");
	}
}
