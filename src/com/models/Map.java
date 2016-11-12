package com.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

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
	
	public ArrayList<Map> load(Integer gameId, Integer mapId) throws LabyrinthException
	{
		ArrayList<Map> maps = new ArrayList<>();
		ArrayList<Object> params = new ArrayList<>();
		ResultSet results = null;
		String sql = "SELECT id, "
				+ "game_id, "
				+ "created_at, "
				+ "updated_at "
				+ "FROM maps "
				+ "WHERE game_id = ? "
				+ "AND deleted_at IS NULL";
		params.add(gameId);
		
		if(mapId > 0)
		{
			sql += " AND id = ?";
			params.add(mapId);
		}
		sql += " ORDER BY id DESC";

		try
		{
			results = dbh.executeQuery(sql, params);
			
			while(results.next())
			{
				Map map = new Map();
				map.setCreatedAt(new Date(results.getTimestamp("created_at").getTime()));
				map.setUpdatedAt(new Date(results.getTimestamp("updated_at").getTime()));
				map.setGameId(results.getInt("game_id"));
				map.setId(results.getInt("id"));
				
				maps.add(map);
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
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
