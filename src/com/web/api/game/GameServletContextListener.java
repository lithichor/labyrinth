package com.web.api.game;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import com.models.Game;
import com.models.User;
import com.parents.LabyrinthServletContextListener;

@WebListener
public class GameServletContextListener extends LabyrinthServletContextListener
{
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		ServletContext context = event.getServletContext();
		context.addServlet("gameServlet", new GameServlet(new Game(), new User())).addMapping("/api/games/*");
	}

}
