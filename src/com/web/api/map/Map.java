package com.web.api.map;

import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.models.Monster;
import com.models.constants.GeneralConstants;
import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;
import com.web.api.tile.Tile;
import com.web.api.tile.Tile.Boundary;

public class Map extends LabyrinthModel
{
	private Integer id;
	private Integer gameId;
	private ArrayList<ArrayList<Tile>> grid = new ArrayList<>();

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getGameId() { return gameId; }
	public void setGameId(Integer gameId) { this.gameId = gameId; }
	public ArrayList<ArrayList<Tile>> getGrid() { return this.grid; }
	public void setGrid(ArrayList<ArrayList<Tile>> grid) { this.grid = grid; }
	
	public ArrayList<Map> load(Integer gameId, Integer mapId) throws LabyrinthException
	{
		ArrayList<Map> maps = new ArrayList<>();
		ArrayList<Object> params = new ArrayList<>();
		ResultSet results = null;
		String sql = "SELECT id, "
				+ "game_id, "
				+ "created_at, "
				+ "updated_at "
				+ "FROM maps ";
		// if a gameId is provided (also if both ids are provided - which
		// should never happen, but it might)
		if(gameId > 0)
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
		String sql = "INSERT INTO maps (game_id, created_at, updated_at) "
				+ "VALUES(?, now(), now())";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(this.gameId);
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
	
	/**
	 * This is the first method to generate a map. Later
	 * efforts will be more complex. The idea is to have
	 * multiple types of Labyrinths that are generated at
	 * random.
	 * @return
	 * @throws LabyrinthException
	 */
	public boolean generateMap() throws LabyrinthException
	{
		Random rand = new Random();
		// the ID is used when making the Tiles
		boolean success = this.save();
		
		// we have to create the grid first, because we
		// reference things out of order when setting the walls
		for(int x = 0; x < GeneralConstants.GRID_SIZE; x++)
		{
			ArrayList<Tile> column = new ArrayList<>();
			
			for(int y = 0; y < GeneralConstants.GRID_SIZE; y++)
			{
				Tile t = new Tile(x, y, this.id);
				column.add(t);
			}
			grid.add(column);
		}
		
		// now add the walls and monsters
		for(ArrayList<Tile> column: grid)
		{
			for(Tile t: column)
			{
				int x = t.getCoords().x;
				int y = t.getCoords().y;
				
				// set east-west walls on the perimeter
				if(x == 0)
				{
					t.setWest(Boundary.WALL);
				}
				else if(x == GeneralConstants.GRID_SIZE - 1)
				{
					t.setEast(Boundary.WALL);
				}
				// set walls randomly inside the maze
				if(x < GeneralConstants.GRID_SIZE - 1 && rand.nextInt(10) < 3)
				{
					t.setEast(Boundary.WALL);
					try
					{
						grid.get(x + 1).get(y).setWest(Boundary.WALL);
					}
					catch(ArrayIndexOutOfBoundsException oobe)
					{
						System.out.println("\n\nWoah! Error, Dude:\nX: " + x + "\nY: " + y);
						throw new ArrayIndexOutOfBoundsException();
					}
				}
				
				// set south-north walls on perimeter
				if(y == 0)
				{
					t.setNorth(Boundary.WALL);
				}
				else if(y == GeneralConstants.GRID_SIZE - 1)
				{
					t.setSouth(Boundary.WALL);
				}
				// set walls randomly inside the maze
				if(y < GeneralConstants.GRID_SIZE - 1 && rand.nextInt(10) < 3)
				{
					t.setSouth(Boundary.WALL);
					try
					{
						grid.get(x).get(y + 1).setSouth(Boundary.WALL);
					}
					catch(ArrayIndexOutOfBoundsException oobe)
					{
						System.out.println("\n\nERROR:\nX: " + x + "\nY: " + y);
						throw new ArrayIndexOutOfBoundsException();
					}
				}
				
				// add a single monster at (0, 0)
				// this will be replaced by a probability
				if(x == 0 && y == 0)
				{
					t.setHasMonster(true);
				}
				
				// save the tile
				t.save();
				
				// add a monster (just one for now)
				// must add monster after tile is saved (need tileId)
				if(t.hasMonster())
				{
					Monster m = new Monster();
					m.setTileId(t.getId());
					m.save();
				}
			}
		}
		
		System.out.println(toString());
		return success;
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
}
