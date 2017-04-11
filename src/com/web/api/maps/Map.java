package com.web.api.maps;

import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.models.constants.GeneralConstants;
import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;
import com.web.api.tile.Tile;
import com.web.api.tile.Tile.Boundary;

public class Map extends LabyrinthModel implements Cloneable
{
	private Integer id;
	private Integer gameId;
	private ArrayList<ArrayList<Tile>> grid = new ArrayList<>();
	private Integer gridSize;
	private Integer firstTileId;
	private String name;
	private String type;

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getGameId() { return gameId; }
	public void setGameId(Integer gameId) { this.gameId = gameId; }
	public ArrayList<ArrayList<Tile>> getGrid() { return this.grid; }
	public void setGrid(ArrayList<ArrayList<Tile>> grid) { this.grid = grid; }
	public Integer getGridSize() { return this.gridSize; }
	public void setGridSize(Integer gridSize) { this.gridSize = gridSize; }
	public Integer getFirstTileId() { return this.firstTileId; }
	public void setFirstTileId(Integer firstTileId) { this.firstTileId = firstTileId; }
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	public String getType() { return this.type; }
	public void setType(String type) { this.type = type; }
	
	public ArrayList<Map> load(Integer gameId, Integer mapId) throws LabyrinthException
	{
		ArrayList<Map> maps = new ArrayList<>();
		ArrayList<Object> params = new ArrayList<>();
		ResultSet results = null;
		String sql = "SELECT id, "
				+ "game_id, "
				+ "created_at, "
				+ "updated_at, "
				+ "grid_size, "
				+ "name, "
				+ "type, "
				+ "first_tile_id "
				+ "FROM maps ";
		// if both are provided
		if(gameId > 0 && mapId > 0)
		{
			sql += "WHERE game_id = ? AND id = ? ";
			params.add(gameId);
			params.add(mapId);
		}
		// if a gameId is provided
		else if(gameId > 0)
		{
			sql += "WHERE game_id = ? ";
			params.add(gameId);			
		}
		// if a mapId is provided
		else if(mapId > 0)
		{
			sql += "WHERE id = ? ";
			params.add(mapId);
		}
		// throw an exception if neither is provided
		else
		{
			throw new LabyrinthException(messages.getMessage("map.no_ids"));
		}
		sql += "AND deleted_at IS NULL ORDER BY id DESC";

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
				int gridSize = results.getInt("grid_size") == 0 ?
						GeneralConstants.GRID_SIZE : results.getInt("grid_size");
				map.setGridSize(gridSize);
				int firstTileId = results.getInt("first_tile_id") == 0 ?
						getFirstTile(map.getId()) : results.getInt("first_tile_id");
				map.setFirstTileId(firstTileId);
				map.setName(results.getString("name"));
				map.setType(results.getString("type"));

				maps.add(map);
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}

		if(maps.size() == 0)
		{
			throw new LabyrinthException(messages.getMessage("map.no_maps_for_game"));
		}
		
		return maps;
	}
	
	/**
	 * Load all Maps for the User, then return the Map with the
	 * desired Map ID
	 * 
	 * @param userId
	 * @param mapId
	 * @return
	 * @throws LabyrinthException
	 */
	public Map loadOneMapByUser(Integer userId, Integer mapId) throws LabyrinthException
	{
		ArrayList<Map> maps = loadByUser(userId);
		
		for(Map m: maps)
		{
			if(m.getId().equals(mapId))
			{
				return m;
			}
		}
		return null;
	}
	
	public ArrayList<Map> loadOneMapByUserAndGame(Integer userId, Integer gameId) throws LabyrinthException
	{
		ArrayList<Map> maps = loadByUser(userId);
		ArrayList<Map> mapsOut = new ArrayList<>();
		
		for(Map m: maps)
		{
			if(m.getGameId().equals(gameId))
			{
				mapsOut.add(m);
			}
		}
		
		return mapsOut;
	}
	
	/**
	 * Load all Maps for the User
	 * 
	 * @param userId
	 * @return
	 * @throws LabyrinthException
	 */
	public ArrayList<Map> loadByUser(Integer userId) throws LabyrinthException
	{
		ArrayList<Map> maps = new ArrayList<>();
		ArrayList<Object> params = new ArrayList<>();
		ResultSet results = null;
		String sql = "SELECT m.* FROM maps m "
				+ "LEFT JOIN games g ON g.id = m.game_id "
				+ "WHERE user_id = ? "
				+ "AND g.deleted_at IS NULL "
				+ "AND m.deleted_at IS NULL";
		params.add(userId);
		
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
				int gridSize = results.getInt("grid_size") == 0 ?
						GeneralConstants.GRID_SIZE : results.getInt("grid_size");
				map.setGridSize(gridSize);
				int firstTileId = results.getInt("first_tile_id") == 0 ?
						getFirstTile(map.getId()) : results.getInt("first_tile_id");
				map.setFirstTileId(firstTileId);
				map.setName(results.getString("name"));
				map.setType(results.getString("type"));
				
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
	
	/**
	 * Delete the Maps belonging to a Game.
	 * 
	 * @param gameId
	 * @throws LabyrinthException
	 */
	public void deleteMaps(Integer gameId) throws LabyrinthException
	{
		// delete the tiles of the maps we're going to delete
		new Tile(0, 0, null).deleteTiles(gameId);
		
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
	
	/**
	 * Delete this Map
	 * @throws LabyrinthException
	 */
	public void delete() throws LabyrinthException
	{
		// delete the tiles of the maps we're going to delete
		new Tile(0, 0, null).deleteTiles(gameId);
		
		String sql = "UPDATE maps SET deleted_at = now(), updated_at = now()"
				+ "WHERE id = ?";
		ArrayList<Object> params = new ArrayList<>();
		params.add(this.id);
		
		try
		{
			dbh.execute(sql, params);
		}
		catch(SQLException sqle)
		{
			throw new LabyrinthException(sqle);
		}
	}
	
	public boolean save() throws LabyrinthException
	{
		boolean success = false;
		String sql = "INSERT INTO maps (game_id, grid_size, name, type, first_tile_id, created_at, updated_at) "
				+ "VALUES(?, ?, ?, ?, ?, now(), now())";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(this.gameId);
		params.add(GeneralConstants.GRID_SIZE);
		params.add(this.name);
		params.add(this.type);
		params.add(0);
		ResultSet keys = null;
		
		try
		{
			keys = dbh.executeAndReturnKeys(sql, params);
			while(keys.next())
			{
				this.id = keys.getInt(1);
				success = true;
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}
		
		return success;
	}
	
	public Tile getTileByCoords(Point p, Integer userId) throws LabyrinthException
	{
		return getTileByCoords(p.x, p.y, userId);
	}
	
	public Tile getTileByCoords(int x, int y, Integer userId) throws LabyrinthException
	{
		ArrayList<Tile> tiles = new Tile(0, 0, null).load(this.getId(), 0, userId);
		for(Tile t: tiles)
		{
			if(t.getCoords().x == x && t.getCoords().y == y)
			{
				return t;
			}
		}
		
		return null;
	}
	
	public boolean saveFirstTileId() throws LabyrinthException
	{
		this.firstTileId = getFirstTile(this.id);
		
		boolean success = false;
		String sql = "UPDATE maps SET first_tile_id = ? WHERE id = ?";
		ArrayList<Object> params = new ArrayList<>();
		params.add(this.firstTileId);
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
	
	private Integer getFirstTile(Integer mapId) throws LabyrinthException
	{
		String sql = "SELECT id FROM tiles WHERE map_id = ? ORDER BY ID ASC LIMIT 1";
		ArrayList<Object> params = new ArrayList<>();
		params.add(mapId);
		ResultSet results = null;
		int tileId = 0;
		
		try
		{
			results = dbh.executeQuery(sql, params);
			while(results.next())
			{
				tileId = results.getInt("id");
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		return tileId;
	}

	public Map generateMap(Map mapIn) throws LabyrinthException
	{
		Map map = new Map();
		MapHelper helper = new MapHelper();
		
		map = helper.getMapType(mapIn);
		map = helper.generateMapGrid(map);
		map = helper.saveMap(map);
		
		return map;
	}

	public String toString()
	{
		String southWalls = "";
		String eastWalls = "";
		String northWalls = "";
		String westWalls = "";
		String monsters = "";
		
		for(int x = 0; x < grid.size(); x++)
		{
			ArrayList<Tile> col = grid.get(x);
			for(int y = 0; y < col.size(); y++)
			{
				Tile t = col.get(y);
				if(t.getSouth() == Boundary.WALL)
				{
					southWalls += "(" + x + ", " + y + "), ";
				}
				if(t.getEast() == Boundary.WALL)
				{
					eastWalls += "(" + x + ", " + y + "), ";
				}
				if(t.getNorth() == Boundary.WALL)
				{
					northWalls += "(" + x + ", " + y + "), ";
				}
				if(t.getWest() == Boundary.WALL)
				{
					westWalls += "(" + x + ", " + y + "), ";
				}
				if(t.hasMonster())
				{
					monsters += "(" + x + ", " + y + "), ";
				}
			}
		}
		
		return "SOUTH: " + southWalls +
				"\nNORTH: " + northWalls +
				"\nEAST: " + eastWalls +
				"\nWEST: " + westWalls +
				"\nMONSTERS: " + monsters;
	}
	
	/**
	 * return a clone of this Map, or null if it throws an exception
	 */
	public Map clone()
	{
		try
		{
			return (Map)super.clone();
		}
		catch(CloneNotSupportedException cnse)
		{
			return null;
		}
	}
}
