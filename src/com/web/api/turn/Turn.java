package com.web.api.turn;

import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class Turn extends LabyrinthModel
{
	private Integer id;
	private Integer iteration;
	private Integer userId;
	private Integer gameId;
	private Integer mapId;
	private Point coords;
	private boolean inCombat;
	private Integer combatId;

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getIteration() { return iteration; }
	public void setIteration(Integer iteration) { this.iteration = iteration; }
	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }
	public Integer getGameId() { return gameId; }
	public void setGameId(Integer gameId) { this.gameId = gameId; }
	public Integer getMapId() { return mapId; }
	public void setMapId(Integer mapId) { this.mapId = mapId; }
	public Point getCoords() { return coords; }
	public void setCoords(Point coords) { this.coords = coords; }
	public boolean isInCombat() { return this.inCombat; }
	public void setInCombat(boolean inCombat) { this.inCombat = inCombat; }
	public Integer getCombatId() { return combatId; }
	public void setCombatId(Integer combatId) { this.combatId = combatId; }

	public Turn loadByUserAndTurn(Integer userId, Integer turnId) throws LabyrinthException
	{
		if(userId <= 0)
		{
			throw new LabyrinthException(messages.getMessage("turn.no_user_id"));
		}
		if(turnId <= 0)
		{
			throw new LabyrinthException(messages.getMessage("turn.no_turn_id"));
		}
		return load(userId, 0, turnId);
	}

	public Turn loadByUserAndGame(Integer userId, Integer gameId) throws LabyrinthException
	{
		return load(userId, gameId, 0);
	}
	
	private Turn load(Integer userId, Integer gameId, Integer turnId) throws LabyrinthException
	{
		Turn turn = null;
		ArrayList<Object> params = new ArrayList<>();
		ResultSet results = null;
		String sql = "SELECT id, "
				+ "iteration, "
				+ "user_id, "
				+ "game_id, "
				+ "map_id, "
				+ "x, y, "
				+ "in_combat, "
				+ "combat_id, "
				+ "created_at, "
				+ "updated_at "
				+ "FROM turns "
				+ "WHERE deleted_at IS NULL ";
		if(userId > 0)
		{
			sql += "AND user_id = ? ";
			params.add(userId);
		}
		if(gameId > 0)
		{
			sql += "AND game_id = ? ";
			params.add(gameId);
		}
		if(turnId > 0)
		{
			sql += "AND id = ? ";
			params.add(turnId);
		}
		
		if(userId <= 0 && gameId <= 0)
		{
			throw new LabyrinthException(messages.getMessage("turn.no_ids"));
		}

		try
		{
			results = dbh.executeQuery(sql, params);
			
			while(results.next())
			{
				turn = new Turn();
				
				turn.setId(results.getInt("id"));
				turn.setIteration(results.getInt("iteration"));
				turn.setUserId(results.getInt("user_id"));
				turn.setGameId(results.getInt("game_id"));
				turn.setMapId(results.getInt("map_id"));
				turn.setCoords(new Point(results.getInt("x"), results.getInt("y")));
				turn.setInCombat(results.getInt("in_combat") == 1);
				turn.setCombatId(results.getInt("combat_id"));
				turn.setCreatedAt(new Date(results.getTimestamp("created_at").getTime()));
				turn.setUpdatedAt(new Date(results.getTimestamp("updated_at").getTime()));
			}
		}
		catch (SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		return turn;
	}
	
	public boolean save() throws LabyrinthException
	{
		boolean success = true;
		int turnId = 0;
		ResultSet results = null;
		String sql = "INSERT INTO turns "
				+ "(iteration, user_id, game_id, map_id, x, y, in_combat, created_at, updated_at) "
				+ "VALUES(0, ?, ?, ?, ?, ?, ?, now(), now())";
		ArrayList<Object> params = new ArrayList<>();
		params.add(this.userId);
		params.add(this.gameId);
		params.add(this.mapId);
		params.add(this.coords.x);
		params.add(this.coords.y);
		params.add(this.inCombat ? 1 : 0);
		
		try
		{
			results = dbh.executeAndReturnKeys(sql, params);
			while(results.next())
			{
				turnId = results.getInt(1);
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		if(turnId > 0)
		{
			success = true;
			this.id = turnId;
		}
		else
		{
			success = false;
			throw new LabyrinthException(messages.getMessage("unknown.no_id_returned"));
		}

		return success;
	}
	
	public void delete(Integer userId) throws LabyrinthException
	{
		String sql = "UPDATE turns SET deleted_at = now() where user_id = ?";
		ArrayList<Object> params = new ArrayList<>();
		params.add(userId);
		
		try
		{
			dbh.execute(sql, params);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
	}
	
	/**
	 * update a Turn after a PUT
	 * Mutable attributes are map_id, coords, iteration, updated_at, and deleted_at
	 * 
	 * @return
	 */
	public boolean update() throws LabyrinthException
	{
		boolean success = true;
		String sql = "UPDATE turns SET "
				+ "map_id = ?, "
				+ "x = ?, "
				+ "y = ?, "
				+ "iteration = ?, "
				+ "in_combat = ?, ";
				if(this.inCombat)
				{
					sql += "combat_id = ?, ";
				}
				sql += "updated_at = now() "
				+ "WHERE id = ?";
		ArrayList<Object> params = new ArrayList<>();
		params.add(this.mapId);
		params.add(this.coords.x);
		params.add(this.coords.y);
		params.add(this.iteration);
		params.add(this.inCombat ? 1 : 0);
		if(this.inCombat)
		{
			params.add(this.combatId);
		}
		params.add(this.id);
		
		try
		{
			success = dbh.execute(sql, params);
		}
		catch(SQLException sqle)
		{
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		return success;
	}
}