package com.web.api.user;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import com.models.User;
import com.parents.LabyrinthServletContextListener;

@WebListener
public class UserServletContextListener extends LabyrinthServletContextListener
{

	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		ServletContext context = event.getServletContext();
		context.addServlet("userServlet", new UserServlet(new User())).addMapping("/api/user");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event)
	{
		System.out.println("Destroy ... Destroy ... Destr-");
	}

}
