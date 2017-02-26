package com.web.api.hero;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.parents.LabyrinthServletContextListener;

public class HerosServletContextListener extends LabyrinthServletContextListener
{
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		ServletContext context = event.getServletContext();
		context.addServlet("heroServlet", new HerosServlet(new Hero())).addMapping("/api/heros/*");
		context.addServlet("heroGameServlet", new HerosGameServlet()).addMapping("/api/heros/game/*");
	}
}
