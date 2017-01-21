package com.web.api.turn;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.parents.LabyrinthServletContextListener;

public class TurnServletContextListener extends LabyrinthServletContextListener
{
	public void contextInitialized(ServletContextEvent event)
	{
		ServletContext context = event.getServletContext();
		context.addServlet("turnServlet", new TurnServlet()).addMapping("/api/turns/*");
		context.addServlet("turnGameServlet", new TurnGameServlet()).addMapping("/api/turns/game/*");
	}
}
