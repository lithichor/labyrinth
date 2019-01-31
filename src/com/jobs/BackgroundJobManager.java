package com.jobs;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;

import com.parents.LabyrinthServletContextListener;

public class BackgroundJobManager extends LabyrinthServletContextListener
{
	private ScheduledExecutorService scheduler;
	
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		System.out.println("Background Jobs Started");
		scheduler = Executors.newSingleThreadScheduledExecutor();
		// first run is 30 seconds after servlet start, then every 12 hours afterward
		scheduler.scheduleAtFixedRate(new DeleteUnusedUsers(), 30, 43200, TimeUnit.SECONDS);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event)
	{
		scheduler.shutdownNow();
	}
}
