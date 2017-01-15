package com.web.api.monster;

import javax.servlet.ServletContextEvent;

import com.parents.LabyrinthServletContextListener;

public class MonsterServletContextListener extends LabyrinthServletContextListener
{
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		event.getServletContext().addServlet("monsterServlet", new MonsterServlet()).addMapping("/api/monsters/*");
		event.getServletContext().addServlet("monsterTileServlet", new MonsterTileServlet()).addMapping("/api/monsters/tile/*");
	}
}
