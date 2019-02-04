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
	public static final String LOAD_SQL = "SELECT id, "
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
	public static final String SAVE_SQL = "INSERT INTO turns "
			+ "(iteration, user_id, game_id, map_id, x, y, in_combat, created_at, updated_at) "
			+ "VALUES(0, ?, ?, ?, ?, ?, ?, now(), now())";
	public static final String DELETE_SQL = "UPDATE turns SET deleted_at = now() where user_id = ?";
	private static final String UPDATE_SQL = "UPDATE turns SET "
			+ "map_id = ?, "
			+ "x = ?, "
			+ "y = ?, "
			+ "iteration = ?, "
			+ "in_combat = ?, ";
	public static final String UPDATE_IN_COMBAT_SQL = UPDATE_SQL + "combat_id = ?, updated_at = now() WHERE id = ?";
	public static final String UPDATE_NOT_IN_COMBAT_SQL = UPDATE_SQL + "updated_at = now() WHERE id = ?";
	
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

	/**
	 * A convenience method to load a Turn using IDs from User and Turn. An Exception
	 * is thrown is wither ID is less than or equal to zero
	 * 
	 * @param userId
	 * @param turnId
	 * @return
	 * @throws LabyrinthException
	 */
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

	/**
	 * A convenience method to load a Turn using IDs from User and Game. An Exception
	 * is thrown if either ID is less than or equal to zero
	 * 
	 * @param userId
	 * @param gameId
	 * @return
	 * @throws LabyrinthException
	 */
	public Turn loadByUserAndGame(Integer userId, Integer gameId) throws LabyrinthException
	{
		if(userId <= 0)
		{
			throw new LabyrinthException(messages.getMessage("turn.no_user_id"));
		}
		if(gameId <= 0)
		{
			throw new LabyrinthException(messages.getMessage("turn.no_game_id"));
		}
		return load(userId, gameId, 0);
	}
	
	/**
	 * Load a Turn with IDs from User, Game, or Turn. A userId is always
	 * required so we don't return another User's Turn
	 * 
	 * @param userId
	 * @param gameId
	 * @param turnId
	 * @return - the Turn loaded from the database
	 * @throws LabyrinthException
	 */
	public Turn load(Integer userId, Integer gameId, Integer turnId) throws LabyrinthException
	{
		// We have to have a userId to make sure we don't return another user's Turn
		if(userId <= 0)
		{
			throw new LabyrinthException(messages.getMessage("turn.no_user_id"));
		}
		
		Turn turn = null;
		ArrayList<Object> params = new ArrayList<>();
		ResultSet results = null;
		String sql = LOAD_SQL;
		
		sql += "AND user_id = ? ";
		params.add(userId);
			
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
		ArrayList<Object> params = new ArrayList<>();
		params.add(this.userId);
		params.add(this.gameId);
		params.add(this.mapId);
		params.add(this.coords.x);
		params.add(this.coords.y);
		params.add(this.inCombat ? 1 : 0);

		try
		{
			results = dbh.executeAndReturnKeys(SAVE_SQL, params);
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
		if(userId <= 0)
		{
			throw new LabyrinthException(messages.getMessage("turn.no_user_id"));
		}
		
		ArrayList<Object> params = new ArrayList<>();
		params.add(userId);
		
		try
		{
			dbh.execute(DELETE_SQL, params);
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
		String sql = "";
		
		if(this.inCombat)
		{
			sql = UPDATE_IN_COMBAT_SQL;
		}
		else
		{
			sql = UPDATE_NOT_IN_COMBAT_SQL;
		}
				
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
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		return success;
	}
}