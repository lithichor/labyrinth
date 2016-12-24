package com.web.api.hero;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.models.Hero;
import com.parents.LabyrinthServletContextListener;

public class HeroServletContextListener extends LabyrinthServletContextListener
{
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		ServletContext context = event.getServletContext();
		context.addServlet("heroServlet", new HeroServlet(new Hero())).addMapping("/api/heros");
		context.addServlet("heroGameServlet", new HerosGameServlet()).addMapping("/api/heros/game/*");
	}
}
