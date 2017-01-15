package com.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class Monster extends LabyrinthModel
{
	private Integer id;
	private Integer tileId = 0;
	private Integer health = 0;
	private Integer attack = 0;
	private Integer defense = 0;
	
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getTileId() { return tileId; }
	public void setTileId(Integer tileId) { this.tileId = tileId; }
	public Integer getHealth() { return health; }
	public void setHealth(Integer health) { this.health = health; }
	public Integer getAttack() { return attack; }
	public void setAttack(Integer attack) { this.attack = attack; }
	public Integer getDefense() { return defense; }
	public void setDefense(Integer defense) { this.defense = defense; }
	
	public boolean save() throws LabyrinthException
	{
		boolean success = false;
		int monsterId = 0;
		ResultSet results = null;
		
		String sql = "INSERT INTO monsters (tile_id, health, attack, defense, created_at, updated_at) "
				+ "VALUES (?, ?, ?, ?, now(), now())";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(this.tileId);
		params.add(this.health);
		params.add(this.attack);
		params.add(this.defense);
		
		try
		{
			results = dbh.executeAndReturnKeys(sql, params);
			while(results.next())
			{
				monsterId = results.getInt(1);
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		if(monsterId > 0)
		{
			this.id = monsterId;
			success = true;
		}

		return success;
	}

	public ArrayList<Monster> load(Integer tileId, Integer monsterId) throws LabyrinthException
	{
		ArrayList<Monster> monsters = new ArrayList<>();
		ArrayList<Object> params = new ArrayList<>();
		ResultSet results = null;
		String sql = "SELECT id, "
				+ "tile_id, "
				+ "health, "
				+ "attack, "
				+ "defense, "
				+ "created_at, "
				+ "updated_at "
				+ "FROM monsters "
				+ "WHERE deleted_at IS NULL";
		if(monsterId > 0)
		{
			sql += " AND id = ?";
			params.add(monsterId);
		}
		if(tileId > 0)
		{
			sql += " AND tile_id = ?";
			params.add(tileId);
		}
		if(tileId <= 0 && monsterId <= 0)
		{
			throw new LabyrinthException(messages.getMessage("monster.no_ids"));
		}

		try
		{
			results = dbh.executeQuery(sql, params);
			while(results.next())
			{
				Monster monster = new Monster();
				
				monster.setId(results.getInt("id"));
				monster.setTileId(results.getInt("tile_id"));
				monster.setHealth(results.getInt("health"));
				monster.setAttack(results.getInt("attack"));
				monster.setDefense(results.getInt("defense"));
				monster.setCreatedAt(new Date(results.getTimestamp("created_at").getTime()));
				monster.setUpdatedAt(new Date(results.getTimestamp("updated_at").getTime()));
				
				monsters.add(monster);
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		return monsters;
	}
	
	public boolean delete(ArrayList<Integer> tileIds) throws LabyrinthException
	{
		boolean success = false;
		String sql = "UPDATE monsters SET deleted_at = now(), updated_at = now() WHERE tile_id in (";
		ArrayList<Object> params = new ArrayList<>();
		for(int x = 0; x < tileIds.size(); x++)
		{
			if(x == 0)
			{
				sql += "?";
			}
			else
			{
				sql += ", ?";
			}
			params.add(tileIds.get(x));
		}
		sql += ")";

		try
		{
			success = dbh.execute(sql, params);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		return success;
	}
}
