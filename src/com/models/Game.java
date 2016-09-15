package com.models;

import java.util.Date;
import java.util.ArrayList;

import org.hibernate.HibernateException;

import com.LabyrinthConstants;
import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class Game extends LabyrinthModel
{
	private static final long serialVersionUID = -8019526580596155206L;
	private Integer id;
	private Integer userId;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

	public Game()
	{
		this.setCreatedAt(new Date());
	}

	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public Integer getUserId()
	{
		return userId;
	}
	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}
	public Date getCreatedAt()
	{
		return createdAt;
	}
	public void setCreatedAt(Date createdAt)
	{
		this.createdAt = createdAt;
	}
	public Date getUpdatedAt()
	{
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt)
	{
		this.updatedAt = updatedAt;
	}
	public Date getDeletedAt()
	{
		return deletedAt;
	}
	public void setDeletedAt(Date deletedAt)
	{
		this.deletedAt = deletedAt;
	}

	public Game load(Integer userId) throws LabyrinthException
	{
		this.getSession();

		Game g = null;
		ArrayList<Game> games = null;

		try
		{
			// TODO: need to limit the list to just the current game - this query doesn't work, so find another
			trans = session.beginTransaction();
			games = (ArrayList<Game>)session.createQuery("from Game where user_id = :user_id and deleted_at is null")
					.setParameter("user_id", userId).list();
//					.setParameter("deleted_at", this.getDeletedAt()).list();
			if(games != null && games.size() > 0)
			{
				g = games.get(games.size()-1);
			}
			else
			{
				throw new LabyrinthException(LabyrinthConstants.NO_GAME);
			}

			trans.commit();
		}
		catch(HibernateException he)
		{
			g = null;
			if(trans != null)
			{
				trans.rollback();
			}
			throw new LabyrinthException(he);
		}

		return g;
	}

	public void deleteGame() throws LabyrinthException
	{
		this.setDeletedAt(new Date());
		this.setUpdatedAt(new Date());
		try
		{
			this.save();
		}
		catch (LabyrinthException le)
		{
			throw new LabyrinthException(le);
		}
	}
}
