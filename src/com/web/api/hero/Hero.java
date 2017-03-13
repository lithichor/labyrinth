package com.web.api.hero;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class Hero extends LabyrinthModel
{
	private Integer id;
	private Integer gameId;
	
	private Integer health = 0;
	private Integer maxHealth = 0;
	private Integer strength = 0;
	private Integer magic = 0;
	private Integer attack = 0;
	private Integer defense = 0;
	private Integer experience = 0;

	public Integer getHealth() { return this.health; }
	public void setHealth(Integer health) { this.health = health; }
	public Integer getMaxHealth() { return this.maxHealth; }
	public void setMaxHealth(Integer maxHealth) { this.maxHealth = maxHealth; }
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getGameId() { return gameId; }
	public void setGameId(Integer gameId) { this.gameId = gameId; }
	public Integer getStrength() { return strength; }
	public void setStrength(Integer strength) { this.strength = strength; }
	public Integer getMagic() { return magic; }
	public void setMagic(Integer magic) { this.magic = magic; }
	public Integer getAttack() { return attack; }
	public void setAttack(Integer attack) { this.attack = attack; }
	public Integer getDefense() { return defense; }
	public void setDefense(Integer defense) { this.defense = defense; }
	public Integer getExperience() { return experience; }
	public void setExperience(Integer experience) { this.experience = experience; }
	
	public boolean save() throws LabyrinthException
	{
		boolean success = false;
		String sql = "INSERT INTO heros (game_id, health, max_health, strength, "
				+ "magic, attack, defense, experience, created_at, updated_at) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, now(), now())";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(this.gameId);
		params.add(this.health);
		params.add(this.maxHealth);
		params.add(this.strength);
		params.add(this.magic);
		params.add(this.attack);
		params.add(this.defense);
		params.add(this.experience);
		
		try
		{
			success = dbh.execute(sql, params);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		this.id = this.retrieveId();
		
		return success;
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
				+ " health,"
				+ " max_health,"
				+ " strength,"
				+ " magic,"
				+ " attack,"
				+ " defense,"
				+ " experience"
				+ " FROM heros WHERE deleted_at IS NULL";
		
		if(heroId > 0)
		{
			sql += " AND id = ?";
			params.add(heroId);
		}
		if(gameId > 0)
		{
			sql += " AND game_id = ?";
			params.add(gameId);
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
				hero.setHealth(results.getInt("health"));
				hero.setMaxHealth(results.getInt("max_health"));
				hero.setStrength(results.getInt("strength"));
				hero.setMagic(results.getInt("magic"));
				hero.setAttack(results.getInt("attack"));
				hero.setDefense(results.getInt("defense"));
				hero.setDefense(results.getInt("experience"));
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
	
	public ArrayList<Hero> loadByUser(Integer userId) throws LabyrinthException
	{
		ArrayList<Hero> heros = new ArrayList<>();
		String sql = "SELECT h.id, game_id, "
				+ "health, max_health, strength, "
				+ "magic, attack, defense, experience, "
				+ "h.created_at, h.updated_at "
				+ "FROM heros h\n\t"
				+ "LEFT JOIN games g on g.id = h.game_id\n\t"
				+ "WHERE user_id = ? AND g.deleted_at IS NULL";
		ArrayList<Object> params = new ArrayList<>();
		params.add(userId);
		ResultSet results = null;
		
		if(userId <= 0)
		{
			throw new LabyrinthException(messages.getMessage("hero.no_user_id"));
		}
		
		try
		{
			results = dbh.executeQuery(sql, params);
			while(results.next())
			{
				Hero hero = new Hero();
				hero.setId(results.getInt("h.id"));
				hero.setGameId(results.getInt("game_id"));
				hero.setCreatedAt(new Date(results.getTimestamp("h.created_at").getTime()));
				hero.setUpdatedAt(new Date(results.getTimestamp("h.updated_at").getTime()));
				hero.setHealth(results.getInt("health"));
				hero.setMaxHealth(results.getInt("max_health"));
				hero.setStrength(results.getInt("strength"));
				hero.setMagic(results.getInt("magic"));
				hero.setAttack(results.getInt("attack"));
				hero.setDefense(results.getInt("defense"));
				hero.setDefense(results.getInt("experience"));
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
	
	public boolean update() throws LabyrinthException
	{
		boolean success = false;
		boolean first = true;
		String sql = "UPDATE heros SET ";
		ArrayList<Object> params = new ArrayList<Object>();
		
		
		if(this.getHealth() != null && !(this.getHealth() == 0))
		{
			sql += "health = ? ";
			params.add(this.getHealth());
			first = false;
		}
		if(this.getMaxHealth() != null && !(this.getMaxHealth() == 0))
		{
			if(!first)
			{
				sql += ", ";
				first = false;
			}
			sql += "max_health = ? ";
			params.add(this.getMaxHealth());
			first = false;
		}
		if(this.getStrength() != null && !(this.getStrength() == 0))
		{
			if(!first)
			{
				sql += ", ";
				first = false;
			}
			sql += "strength = ? ";
			first = false;
			params.add(this.getStrength());
		}
		if(this.getMagic() != null && !(this.getMagic() == 0))
		{
			if(!first)
			{
				sql += ", ";
				first = false;
			}
			sql += "magic = ? ";
			first = false;
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
			first = false;
			params.add(this.getAttack());
		}
		if(this.getExperience() != null && !(this.getExperience() == 0))
		{
			if(!first)
			{
				sql += ", ";
				first = false;
			}
			sql += "experience = ? ";
			first = false;
			params.add(this.getExperience());
		}
		if(this.getDefense() != null && !(this.getDefense() == 0))
		{
			if(!first)
			{
				sql += ", ";
				first = false;
			}
			sql += "defense = ? ";
			first = false;
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
		if(other.getHealth() != null && other.getHealth() != 0)
		{
			this.health = other.getHealth();
		}
		if(other.getMaxHealth() != null && other.getMaxHealth() != 0)
		{
			this.maxHealth = other.getMaxHealth();
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
		if(other.getExperience() != null && other.getExperience() != 0)
		{
			this.experience = other.getExperience();
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
