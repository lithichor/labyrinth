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
		this.setUpdatedAt(new Date());
	}

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }
	public Date getCreatedAt() { return createdAt; }
	public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
	public Date getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
	public Date getDeletedAt() { return deletedAt; }
	public void setDeletedAt(Date deletedAt) { this.deletedAt = deletedAt; }
	
	public ArrayList<Game> load(Integer userId, int gameId) throws LabyrinthException
	{
		this.getSession();

		ArrayList<Game> games = null;

		try
		{
			trans = session.beginTransaction();
			String query = "FROM Game g WHERE user_id = :user_id AND deleted_at is null";
			if(gameId > 0)
			{
				query += " and id = :id";
				games = (ArrayList<Game>)session.createQuery(query)
						.setParameter("user_id", userId)
						.setParameter("id", gameId).list();
			}
			else
			{
				games = (ArrayList<Game>)session.createQuery(query)
						.setParameter("user_id", userId).list();
			}
			if(games == null || games.size() == 0)
			{
				throw new LabyrinthException(LabyrinthConstants.NO_GAME);
			}

			trans.commit();
		}
		catch(HibernateException he)
		{
			games = null;
			if(trans != null)
			{
				trans.rollback();
			}
			throw new LabyrinthException(he);
		}
		catch(Exception e)
		{
			games = null;
			if(trans != null)
			{
				trans.rollback();
			}
			throw new LabyrinthException(e);
		}

		return games;
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
