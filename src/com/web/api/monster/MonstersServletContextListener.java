package com.web.api.monster;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRegistration.Dynamic;

import com.parents.LabyrinthServletContextListener;

public class MonstersServletContextListener extends LabyrinthServletContextListener
{
	private MonstersServlet monsters;
	private MonstersTileServlet monstersTile;
	
	/**
	 * Standard no argument constructor. This sets the two servlets this
	 * Listener uses
	 */
	public MonstersServletContextListener()
	{
		this.monsters = new MonstersServlet();
		this.monstersTile = new MonstersTileServlet();
	}
	
	public void setMonsterServlet(MonstersServlet monsters) { this.monsters = monsters; }
	public void setMonstersTileServlet(MonstersTileServlet monstersTile) { this.monstersTile = monstersTile; }
	
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		ServletContext servletContext = event.getServletContext();
		Dynamic monstersDynamic = servletContext.addServlet("monstersServlet", monsters);
		Dynamic monstersTileDynamic = servletContext.addServlet("monstersTileServlet", monstersTile);
		
		monstersDynamic.addMapping("/api/monsters/*");
		monstersTileDynamic.addMapping("/api/monsters/tile/*");
	}
}
