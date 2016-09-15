package com.parents;

import java.io.Serializable;

import org.hibernate.HibernateException;

import com.database.HibernateDBM;

public abstract class LabyrinthModel extends HibernateDBM implements Serializable
{
	private static final long serialVersionUID = 1L;

	public boolean save() throws LabyrinthException
	{
		this.getSession();
		
		try
		{
			trans = session.beginTransaction();
			session.saveOrUpdate(this);
			trans.commit();
		}
		catch(HibernateException he)
		{
			if(trans != null)
			{
				trans.rollback();
			}
			throw new LabyrinthException(he);
		}
		return false;
	}

}
