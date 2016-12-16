package com.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

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
	
	private Integer strength = 0;
	private Integer magic = 0;
	private Integer attack = 0;
	private Integer defense = 0;

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
	public Integer getStrength() { return strength; }
	public void setStrength(Integer strength) { this.strength = strength; }
	public Integer getMagic() { return magic; }
	public void setMagic(Integer magic) { this.magic = magic; }
	public Integer getAttack() { return attack; }
	public void setAttack(Integer attack) { this.attack = attack; }
	public Integer getDefense() { return defense; }
	public void setDefense(Integer defense) { this.defense = defense; }
	
	public boolean save() throws LabyrinthException
	{
		boolean success = false;
		String sql = "INSERT INTO heros (game_id, strength, magic, attack, defense, created_at, updated_at) "
				+ "VALUES(?, ?, ?, ?, ?, now(), now())";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(this.gameId);
		params.add(this.strength);
		params.add(this.magic);
		params.add(this.attack);
		params.add(this.defense);
		
		try
		{
			success = dbh.execute(sql, params);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}
		
		this.id = this.retrieveId();
		
		return success;
	}
	
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
		String sql = "SELECT id,"
				+ " game_id,"
				+ " created_at,"
				+ " updated_at,"
				+ " strength,"
				+ " magic,"
				+ " attack,"
				+ " defense"
				+ " FROM heros WHERE game_id = ? AND deleted_at IS NULL";
		params.add(gameId);
		
		if(heroId > 0)
		{
			sql += " AND id = ?";
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
				hero.setCreatedAt(new Date(results.getTimestamp("created_at").getTime()));
				hero.setUpdatedAt(new Date(results.getTimestamp("updated_at").getTime()));
				hero.setStrength(results.getInt("strength"));
				hero.setMagic(results.getInt("magic"));
				hero.setAttack(results.getInt("attack"));
				hero.setDefense(results.getInt("defense"));
				heros.add(hero);
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
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
	
	public boolean update() throws LabyrinthException
	{
		boolean success = false;
		boolean first = true;
		String sql = "UPDATE heros SET ";
		ArrayList<Object> params = new ArrayList<Object>();
		
		if(this.getStrength() != null && !(this.getStrength() == 0))
		{
			sql += "strength = ? ";
			params.add(this.getStrength());
			first = false;
		}
		if(this.getMagic() != null && !(this.getMagic() == 0))
		{
			if(!first)
			{
				sql += ", ";
				first = false;
			}
			sql += "magic = ? ";
			params.add(this.getMagic());
		}
		if(this.getAttack() != null && !(this.getAttack() == 0))
		{
			if(!first)
			{
				sql += ", ";
				first = false;
			}
			sql += "attack = ? ";
			params.add(this.getAttack());
		}
		if(this.getDefense() != null && !(this.getDefense() == 0))
		{
			if(!first)
			{
				sql += ", ";
				first = false;
			}
			sql += "defense = ? ";
			params.add(this.getDefense());
		}
		
		sql += ", updated_at = now() WHERE id = ?";
		params.add(this.getId());
		
		try
		{
			success = dbh.execute(sql, params);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}

		return success;
	}
	
	public void deleteHero(Integer gameId) throws LabyrinthException
	{
		String sql = "UPDATE heros SET updated_at = now(), deleted_at = now() WHERE game_id = ?";
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

	/**
	 * This method merges a Hero into the current Hero. New
	 * fields take precedence, except for id, createdAt, and
	 * gameId; these are immutable, but might be set on a
	 * new Hero object from values loaded from disk
	 * 
	 * @param other - another Hero object
	 */
	public void merge(Hero other)
	{
		if(this.id == null || this.id == 0)
		{
			this.id = other.getId();
		}
		if(this.createdAt == null)
		{
			this.createdAt = other.getCreatedAt();
		}
		if(this.gameId == null || this.gameId == 0)
		{
			this.gameId = other.getGameId();
		}
		if(other.getStrength() != null && other.getStrength() != 0)
		{
			this.strength = other.getStrength();
		}
		if(other.getMagic() != null && other.getMagic() != 0)
		{
			this.magic = other.getMagic();
		}
		if(other.getAttack() != null && other.getAttack() != 0)
		{
			this.attack = other.getAttack();
		}
		if(other.getDefense() != null && other.getDefense() != 0)
		{
			this.defense = other.getDefense();
		}
	}
	
	private Integer retrieveId() throws LabyrinthException
	{
		String sql = "SELECT id FROM heros WHERE game_id = ?";
		ArrayList<Object> params = new ArrayList<>();
		params.add(this.gameId);
		ResultSet results = null;
		int heroId = 0;
		
		try
		{
			results = dbh.executeQuery(sql, params);
			
			while(results.next())
			{
				heroId = results.getInt("id");
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.unknown_error"));
		}
		
		return heroId;
	}
}
