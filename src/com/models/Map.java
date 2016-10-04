package com.models;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.hibernate.HibernateException;

import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class Map extends LabyrinthModel
{
	private static final long serialVersionUID = 8887895302650203935L;

	private Integer id;
	private Integer gameId;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

	public Map()
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
	
	// TODO: GAME #27 - make this able to choose between load all, load all for a game,
	//  and load one (now it only loads all for a given game)
	public ArrayList<Map> load(Integer gameId, Integer mapId) throws LabyrinthException
	{
		this.getSession();
		ArrayList<Map> maps = null;
		
		try
		{
			trans = session.beginTransaction();
			
			String query = "FROM Map WHERE game_id = :game_id AND deleted_at is null";
			maps = (ArrayList<Map>)session.createQuery(query)
					.setParameter("game_id", gameId).list();

			trans.commit();
		}
		catch(HibernateException he)
		{
			maps = null;
			if(trans != null)
			{
				trans.rollback();
			}
			throw new LabyrinthException(he);
		}
		catch(Exception e)
		{
			maps = null;
			if(trans != null)
			{
				trans.rollback();
			}
			throw new LabyrinthException(e);
		}
		
		return maps;
	}

	public void deleteMaps(Integer gameId) throws LabyrinthException
	{
		String sql = "UPDATE maps SET deleted_at = now() WHERE game_id = ?";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(gameId);
		
		try
		{
			dbh.execute(sql, params);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}
	}
}
