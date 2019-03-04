package com.web.api.turn;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRegistration.Dynamic;

import com.parents.LabyrinthServletContextListener;

public class TurnsServletContextListener extends LabyrinthServletContextListener
{
	private TurnsServlet turnsServlet;
	private TurnsGameServlet turnsGameServlet;
	
	public TurnsServletContextListener()
	{
		this.turnsServlet = new TurnsServlet();
		this.turnsGameServlet = new TurnsGameServlet();
	}
	
	public void setTurnsServlet(TurnsServlet turnsServlet){ this.turnsServlet = turnsServlet; }
	public void setTurnsGameServlet(TurnsGameServlet turnsGameServlet) { this.turnsGameServlet = turnsGameServlet; }
	
	public void contextInitialized(ServletContextEvent event)
	{
		ServletContext context = event.getServletContext();
		
		Dynamic turnsDynamic = context.addServlet("turnsServlet", turnsServlet);
		Dynamic turnsGameDynamic = context.addServlet("turnsGameServlet", turnsGameServlet);
		
		turnsDynamic.addMapping("/api/turns/*");
		turnsGameDynamic.addMapping("/api/turns/game/*");
	}
}
