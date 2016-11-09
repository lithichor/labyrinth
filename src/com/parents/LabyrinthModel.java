package com.parents;

import java.io.Serializable;

import org.hibernate.HibernateException;

import com.database.DatabaseHelper;
import com.database.HibernateDBM;
import com.labels.LabyrinthMessages;

public abstract class LabyrinthModel extends HibernateDBM implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected DatabaseHelper dbh;
	protected LabyrinthMessages messages = new LabyrinthMessages();
	
	public LabyrinthModel()
	{
		dbh = DatabaseHelper.getInstance();		
	}
	
	public boolean save() throws LabyrinthException
	{
		this.getSession();
		boolean good = false;
		
		try
		{
			trans = session.beginTransaction();
			session.saveOrUpdate(this);
			trans.commit();
			good = true;
		}
		catch(HibernateException he)
		{
			if(trans != null)
			{
				trans.rollback();
			}
			throw new LabyrinthException(he);
		}
		return good;
	}
	
	protected DatabaseHelper getDbh()
	{
		return this.dbh;
	}
	
	public boolean update() throws LabyrinthException
	{
		this.getSession();
		boolean good = false;
		
		try
		{
			
			// this isn't working. Look into these webpages:
			// https://www.google.com/search?q=hibernate+saveorupdate&ie=utf-8&oe=utf-8#q=hibernate+change+fields+for+object
			// http://www.journaldev.com/3481/hibernate-session-merge-vs-update-save-saveorupdate-persist-example
			trans = session.beginTransaction();
			session.save(this);
			trans.commit();
			good = true;
		}
		catch(HibernateException he)
		{
			if(trans != null)
			{
				trans.rollback();
			}
			throw new LabyrinthException(he);
		}
		return good;
	}
}
