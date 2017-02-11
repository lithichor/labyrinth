package com.web.api.instructions;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import com.parents.LabyrinthServletContextListener;

public class InstructionsServletContextListener
		extends LabyrinthServletContextListener
{
	public void contextInitialized(ServletContextEvent event)
	{
		ServletContext context = event.getServletContext();
		context.addServlet("instructionsServlet", new InstructionsServlet()).addMapping("/api/instructions");
	}
}
