package com.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.LabyrinthConstants;
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
	
	/**
	 * Load a list of games based on the id of the authenticated (or active) user
	 * @param gameId - the game reference for the hero (a hero can only
	 * belong to one game)
	 * @param heroId - the id of the hero
	 * @return a list of heros
	 * @throws LabyrinthException
	 */
	public ArrayList<Hero> load(Integer gameId, Integer heroId) throws LabyrinthException
	{
		ArrayList<Hero> heros = new ArrayList<Hero>();
		ArrayList<Object> params = new ArrayList<Object>();
		ResultSet results = null;
		String sql = "SELECT id, game_id, created_at, updated_at FROM heros WHERE game_id = ? AND deleted_at IS NULL";
		params.add(gameId);
		
		if(heroId != 0)
		{
			sql += " AND hero_id = ?";
			params.add(heroId);
		}
		
		sql += " ORDER BY id DESC";
		
		try
		{
			results = this.getDbh().executeQuery(sql, params);
			
			while(results.next())
			{
				Hero hero = new Hero();
				hero.setId(results.getInt("id"));
				hero.setGameId(results.getInt("game_id"));
				hero.setCreatedAt(results.getDate("created_at"));
				hero.setUpdatedAt(results.getDate("updated_at"));
				heros.add(hero);
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(LabyrinthConstants.HORRIBLY_WRONG);
		}
		
		return heros;
	}
	
	/**
	 * A convenience method to get the hero for a game. There is only one
	 * hero per game, but the hero belongs to the game, not the other way
	 * around
	 * @param gameId
	 * @return the hero associated with the game
	 */
	public Hero load(Integer gameId) throws LabyrinthException
	{
		ArrayList<Hero> heros = this.load(gameId, 0);
		return heros.get(heros.size() - 1);
	}
	
	public void deleteHero(Integer gameId) throws LabyrinthException
	{
		String sql = "UPDATE heros SET deleted_at = now() WHERE game_id = ?";
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
