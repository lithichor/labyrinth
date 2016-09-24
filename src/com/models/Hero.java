package com.models;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.HibernateException;

import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class Hero extends LabyrinthModel
{
	private static final long serialVersionUID = 3345291415215532854L;

	private Integer id;
	private Integer gameId;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

	public Hero()
	{
		this.setCreatedAt(new Date());
		this.setUpdatedAt(new Date());
	}

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getGameId() { return gameId; }
	public void setGameId(Integer gameId) { this.gameId = gameId; }
	public Date getCreatedAt() { return createdAt; }
	public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
	public Date getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
	public Date getDeletedAt() { return deletedAt; }
	public void setDeletedAt(Date deletedAt) { this.deletedAt = deletedAt; }
	
	// TODO: make this able to choose between load all and load
	// one (now it only loads one for a game)
	public Hero load(Integer gameId, Integer heroId) throws LabyrinthException
	{
		this.getSession();
		ArrayList<Hero> heros = null;
		
		try
		{
			trans = session.beginTransaction();
			
			String query = "FROM Hero WHERE game_id = :game_id AND deleted_at is null";
			heros = ((ArrayList<Hero>)session.createQuery(query)
					.setParameter("game_id", gameId).list());

			trans.commit();
		}
		catch(HibernateException he)
		{
			heros = null;
			if(trans != null)
			{
				trans.rollback();
			}
			throw new LabyrinthException(he);
		}
		catch(Exception e)
		{
			heros = null;
			if(trans != null)
			{
				trans.rollback();
			}
			throw new LabyrinthException(e);
		}
		
		// If we have a list, return the first one. If not, return an empty hero
		if(heros != null && heros.size() > 0)
		{
			return heros.get(0);
		}
		else
		{
			return new Hero();
		}
	}
}
