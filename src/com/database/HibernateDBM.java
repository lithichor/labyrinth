package com.database;

import org.hibernate.HibernateException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.parents.LabyrinthException;

public abstract class HibernateDBM
{
	protected Configuration config = null;
	protected ServiceRegistry serviceRegistry = null;
	protected SessionFactory sessionFactory = null;
	protected Session session = null;
	protected Transaction trans = null;

	/**
	 * initialize the hobernate database manager
	 * Create a session from the session factory
	 * 
	 */
	public HibernateDBM()
	{
		try
		{
			config = new Configuration().configure("hibernate.cfg.xml");
		}
		catch (HibernateException he)
		{
			System.out.println("Error getting configuration file: " + he.getMessage());
			he.printStackTrace();
		}

		serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
	}
	
	protected void getSession()
	{
		try
		{
			sessionFactory = new Configuration().configure().buildSessionFactory(serviceRegistry);						
		}
		catch(PropertyNotFoundException pnfe)
		{
			System.out.println("Error building factory: " + pnfe.getMessage());
			pnfe.printStackTrace();
		}
		catch(HibernateException he)
		{
			System.out.println("Error building session factory: " + he.getMessage());
			he.printStackTrace();
		}

		session = sessionFactory.getCurrentSession();
	}
	
	public abstract boolean save() throws LabyrinthException;
}
