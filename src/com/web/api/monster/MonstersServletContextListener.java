package com.web.api.monster;

import javax.servlet.ServletContextEvent;

import com.parents.LabyrinthServletContextListener;

public class MonstersServletContextListener extends LabyrinthServletContextListener
{
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		event.getServletContext().addServlet("monsterServlet", new MonstersServlet()).addMapping("/api/monsters/*");
		event.getServletContext().addServlet("monsterTileServlet", new MonstersTileServlet()).addMapping("/api/monsters/tile/*");
	}
}
