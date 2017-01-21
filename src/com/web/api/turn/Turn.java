package com.web.api.turn;

import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class Turn extends LabyrinthModel
{
	private Integer id;
	private Integer userId;
	private Integer gameId;
	private Integer mapId;
	private Point coords;
	
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }
	public Integer getGameId() { return gameId; }
	public void setGameId(Integer gameId) { this.gameId = gameId; }
	public Integer getMapId() { return mapId; }
	public void setMapId(Integer mapId) { this.mapId = mapId; }
	public Point getCoords() { return coords; }
	public void setCoords(Point coords) { this.coords = coords; }
	
	public Turn loadByUserAndTurn(Integer userId, Integer turnId) throws LabyrinthException
	{
		Turn turn = null;
		ResultSet results = null;
		String sql = "SELECT id, user_id, game_id, map_id, x, y, created_at, updated_at "
				+ "FROM turns WHERE id = ? AND user_id = ?";
		ArrayList<Object> params = new ArrayList<>();
		params.add(turnId);
		params.add(userId);
		
		try
		{
			results = dbh.executeQuery(sql, params);
			
			while(results.next())
			{
				turn = new Turn();
				
				turn.setId(results.getInt("id"));
				turn.setUserId(results.getInt("user_id"));
				turn.setGameId(results.getInt("game_id"));
				turn.setMapId(results.getInt("map_id"));
				turn.setCoords(new Point(results.getInt("x"), results.getInt("y")));
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		return turn;
	}
	
	public Turn loadByUserAndGame(Integer userId, Integer gameId) throws LabyrinthException
	{
		Turn turn = null;
		ArrayList<Object> params = new ArrayList<>();
		ResultSet results = null;
		String sql = "SELECT id, "
				+ "user_id, "
				+ "game_id, "
				+ "map_id, "
				+ "x, y, "
				+ "created_at,"
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
				turn.setUserId(results.getInt("user_id"));
				turn.setGameId(results.getInt("game_id"));
				turn.setMapId(results.getInt("map_id"));
				turn.setCoords(new Point(results.getInt("x"), results.getInt("y")));
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
				+ "(user_id, game_id, map_id, x, y, created_at, updated_at) "
				+ "VALUES(?, ?, ?, ?, ?, now(), now())";
		ArrayList<Object> params = new ArrayList<>();
		params.add(this.userId);
		params.add(this.gameId);
		params.add(this.mapId);
		params.add(this.coords.x);
		params.add(this.coords.y);
		
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
}