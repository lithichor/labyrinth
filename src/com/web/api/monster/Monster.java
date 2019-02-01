package com.web.api.monster;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.helpers.AttackHelper;
import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class Monster extends LabyrinthModel
{
	private Integer id;
	private Integer tileId = 0;
	private Integer health = AttackHelper.initializeAttributes();
	private Integer attack = AttackHelper.initializeAttributes();
	private Integer defense = AttackHelper.initializeAttributes();
	
	public static final String SAVE_SQL = "INSERT INTO monsters (tile_id, health, attack, defense, created_at, updated_at) "
			+ "VALUES (?, ?, ?, ?, now(), now())";
	public static final String LOAD_BY_USER_SQL = "SELECT m.id, "
			+ "tile_id, "
			+ "health, "
			+ "attack, "
			+ "defense, "
			+ "m.created_at, "
			+ "m.updated_at "
			+ "FROM monsters m "
			+ "LEFT JOIN tiles t ON t.id = m.tile_id "
			+ "LEFT JOIN maps ma ON ma.id = t.map_id "
			+ "LEFT JOIN games g ON g.id = ma.game_id "
			+ "WHERE g.deleted_at IS NULL "
			+ "AND m.deleted_at IS NULL "
			+ "AND t.deleted_at IS NULL "
			+ "AND ma.deleted_at IS NULL "
			+ "AND user_id = ? ";
	public static final String LOAD_BY_USER_AND_MONSTER_SQL = LOAD_BY_USER_SQL + "AND m.id = ? ";
	public static final String LOAD_BY_USER_AND_TILE_SQL = LOAD_BY_USER_SQL + "AND t.id = ? ";
	/**
	 * This constant requires a question mark for each parameter and closing parenthesis before it is used in a query
	 */
	public static final String DELETE_SQL_PARTIAL = "UPDATE monsters SET deleted_at = now(), updated_at = now() WHERE tile_id in (";
	
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
		
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(this.tileId);
		params.add(this.health);
		params.add(this.attack);
		params.add(this.defense);
		
		try
		{
			results = dbh.executeAndReturnKeys(SAVE_SQL, params);
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
	
	public ArrayList<Monster> loadMonstersByUserAndTile(Integer userId, Integer tileId) throws LabyrinthException
	{
		return loadMonstersByUser(userId, tileId, 0);
	}

	public ArrayList<Monster> loadMonstersByUserAndMonster(Integer userId, Integer monsterId) throws LabyrinthException
	{
		return loadMonstersByUser(userId, 0, monsterId);
	}

	public ArrayList<Monster> loadMonstersByUser(Integer userId, Integer tileId, Integer monsterId) throws LabyrinthException
	{
		ArrayList<Monster> monsters = new ArrayList<>();
		ArrayList<Object> params = new ArrayList<>();
		ResultSet results = null;
		String sql = LOAD_BY_USER_SQL;
		
		params.add(userId);
		if(monsterId > 0)
		{
			sql = LOAD_BY_USER_AND_MONSTER_SQL;
			params.add(monsterId);
		}
		if(tileId > 0)
		{
			sql = LOAD_BY_USER_AND_TILE_SQL;
			params.add(tileId);
		}
		
		try
		{
			results = dbh.executeQuery(sql, params);
			
			while(results.next())
			{
				Monster m = new Monster();
				
				m.setId(results.getInt("m.id"));
				m.setTileId(results.getInt("tile_id"));
				m.setHealth(results.getInt("health"));
				m.setAttack(results.getInt("attack"));
				m.setDefense(results.getInt("defense"));
				m.setCreatedAt(new Date(results.getTimestamp("m.created_at").getTime()));
				m.setUpdatedAt(new Date(results.getTimestamp("m.updated_at").getTime()));
				
				monsters.add(m);
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
		String sql = DELETE_SQL_PARTIAL;
		ArrayList<Object> params = new ArrayList<>();

		if(tileIds == null)
		{
			return success;
		}
		
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
