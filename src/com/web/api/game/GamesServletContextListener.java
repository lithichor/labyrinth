package com.web.api.game;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import com.parents.LabyrinthServletContextListener;
import com.web.api.user.User;

@WebListener
public class GamesServletContextListener extends LabyrinthServletContextListener
{
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		ServletContext context = event.getServletContext();
		context.addServlet("gamesServlet", new GamesServlet(new Game(), new User())).addMapping("/api/games/*");
	}

}
