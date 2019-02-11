package com.web.api.tile;

import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;
import com.web.api.monster.Monster;

public class Tile extends LabyrinthModel
{
	private Integer id;
	private boolean hasMonster;
	private boolean visited;
	private Integer mapId;
	private Point coords;
	private Boundary north;
	private Boundary south;
	private Boundary east;
	private Boundary west;
	private Monster monster;
	
	public enum Boundary
	{
		DOOR, OPENING, WALL
	}
	
	public static final String SAVE_SQL = "INSERT INTO tiles "
			+ "(x, y, has_monster, visited, "
			+ "map_id, north, south, east, west, "
			+ "created_at, updated_at)\n"
			+ "VALUES(?, ?, ?, ?, "
			+ "?, '?', '?', '?', '?', "
			+ " now(), now())";
	
	/**
	 * This sql is incomplete; it must be finished with userId and deleted_at at a minimum
	 */
	public static final String LOAD_SQL = "SELECT t.id, x, y, map_id, has_monster, visited, "
			+ "north, south, east, west, t.created_at, t.updated_at "
			+ "FROM tiles t\n\t"
			+ "LEFT JOIN maps m ON m.id = t.map_id\n\t"
			+ "LEFT JOIN games g on g.id = m.game_id\n\t"
			+ "WHERE ";
	
	/**
	 * Get the mapIds for the game we are deleting
	 */
	public static final String GET_MAPS_TO_DELETE_SQL = "SELECT id FROM maps WHERE game_id = ? AND deleted_at IS NULL";
	
	/**
	 * This sql is incomplete; it requires holders for the mapIds and closing parentheses
	 */
	public static final String DELETE_TILES_SQL = "UPDATE tiles SET deleted_at = now() where map_id in (";
	
	/**
	 * This sql is incomplete; it requires holders for the mapIds and closing parentheses
	 */
	public static final String GET_TILE_IDS = "SELECT id FROM tiles WHERE map_id in (";
	
	/**
	 * The Tile has to have coordinates and a mapId in order to exist.
	 * By default, a Tile does not have a monster, has not been
	 * visited, and has openings on all sides.
	 * @param x
	 * @param y
	 */
	public Tile(int x, int y, Integer mapId)
	{
		this.mapId = mapId;
		coords = new Point(x, y);
		hasMonster = false;
		visited = false;
		north = Boundary.OPENING;
		south = Boundary.OPENING;
		east = Boundary.OPENING;
		west = Boundary.OPENING;
		// the monster is for when we delete the tile
		monster = new Monster();
	}
	
	public Integer getId() { return this.id; }
	public void setId(Integer id) { this.id = id; }
	public Point getCoords() { return this.coords; }
	public void setCoords(Point p) { this.coords = p; }
	public void setCoords(int x, int y) { this.coords = new Point(x, y); }
	public Integer getMapId() { return this.mapId; }
	public void setMapId(Integer mapId) { this.mapId = mapId; }
	public boolean hasMonster() { return hasMonster; }
	public int getHasMonsterInt() { return hasMonster ? 1 : 0; }
	public void setHasMonster(boolean hasMonster) { this.hasMonster = hasMonster; }
	public boolean wasVisited() { return visited; }
	public int getWasVisitedInt() { return visited ? 1 : 0; }
	public void setVisited(boolean visited) { this.visited = visited; }
	public Boundary getNorth() { return this.north; }
	public void setNorth(Boundary north) { this.north = north; }
	public Boundary getSouth() { return this.south; }
	public void setSouth(Boundary south) { this.south = south; }
	public Boundary getEast() { return this.east; }
	public void setEast(Boundary east) { this.east = east; }
	public Boundary getWest() { return this.west; }
	public void setWest(Boundary west) { this.west = west; }
	public void setMonster(Monster monster) { this.monster = monster; }
	
	/**
	 * Save a tile to the database. The query returns the ID for the tile
	 * 
	 * @return
	 * @throws LabyrinthException
	 */
	public boolean save() throws LabyrinthException
	{
		boolean success = false;
		int tileId = 0;
		ResultSet results = null;
		ArrayList<Object> params = new ArrayList<>();
		params.add(this.getCoords().x);
		params.add(this.getCoords().y);
		params.add(this.getHasMonsterInt());
		params.add(this.getWasVisitedInt());
		params.add(this.getMapId());
		params.add(this.getNorth());
		params.add(this.getSouth());
		params.add(this.getEast());
		params.add(this.getWest());

		try
		{
			results = dbh.executeAndReturnKeys(SAVE_SQL, params);
			while(results.next())
			{
				tileId = results.getInt(1);
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		if(tileId > 0)
		{
			this.setId(tileId);
			success = true;
		}
		
		return success;
	}
	
	/**
	 * Load a tile from the database. The userId is required to prevent cross-currency
	 * issues. The tile may be loaded with the mapId or the tileId
	 * 
	 * @param mapId
	 * @param tileId
	 * @param userId
	 * @return
	 * @throws LabyrinthException
	 */
	public ArrayList<Tile> load(Integer mapId, Integer tileId, Integer userId) throws LabyrinthException
	{
		ArrayList<Tile> tiles = new ArrayList<>();
		ArrayList<Object> params = new ArrayList<>();
		ResultSet results = null;
		String sql = LOAD_SQL;
		
		// what happens if userId is negative?
		if(userId <= 0)
		{
			throw new LabyrinthException(messages.getMessage("tile.no_user_id"));
		}
		params.add(userId);
		sql += "\n\tuser_id = ?\n\tAND t.deleted_at IS NULL ";
		
		if(mapId > 0)
		{
			sql += "AND map_id = ? ";
			params.add(mapId);
		}
		if(tileId > 0)
		{
			sql += "AND t.id = ? ";
			params.add(tileId);
		}
		if(mapId <= 0 && tileId <= 0)
		{
			throw new LabyrinthException(messages.getMessage("tile.no_ids"));
		}
		
		try
		{
			results = dbh.executeQuery(sql, params);
			while(results.next())
			{
				int x = results.getInt("x");
				int y = results.getInt("y");
				Integer map = results.getInt("map_id");
				
				Tile t = new Tile(x, y, map);
				
				t.setId(results.getInt("id"));
				
				t.setNorth(getBoundaryFromString(results.getString("north")));
				t.setSouth(getBoundaryFromString(results.getString("south")));
				t.setEast(getBoundaryFromString(results.getString("east")));
				t.setWest(getBoundaryFromString(results.getString("west")));
				
				t.setHasMonster(results.getInt("has_monster") == 1);
				t.setVisited(results.getInt("visited") == 1);
				t.setCreatedAt(new Date(results.getTimestamp("created_at").getTime()));
				t.setUpdatedAt(new Date(results.getTimestamp("updated_at").getTime()));
				
				tiles.add(t);
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		if(tiles.size() == 0)
		{
			// different error message depending on which ID was used
			if(mapId > 0)
			{
				throw new LabyrinthException(messages.getMessage("tile.no_tiles_for_map"));
			}
			// this is when tileId == 0
			else
			{
				throw new LabyrinthException(messages.getMessage("tile.no_tiles_found"));
			}
		}
		
		return tiles;
	}
	
	/**
	 * This deletes the Tiles associated with a game. It must be called
	 * before the Maps for the Game are deleted.
	 * 
	 * The Game ID is required
	 * 
	 * @param gameId
	 * @throws LabyrinthException 
	 */
	public void deleteTiles(Integer gameId) throws LabyrinthException
	{
		if(gameId <= 0)
		{
			throw new LabyrinthException(messages.getMessage("tile.no_game_id"));
		}
		
		// get the IDs of the maps belonging to the game
		ArrayList<Object> params = new ArrayList<>();
		params.add(gameId);
		ResultSet results = null;
		ArrayList<Integer> mapIds = new ArrayList<>();
		ArrayList<Integer> tileIds = new ArrayList<>();
		
		try
		{
			results = dbh.executeQuery(GET_MAPS_TO_DELETE_SQL, params);
			while(results.next())
			{
				mapIds.add(results.getInt("id"));
			}
			// if we don't find any Maps, return, because there are no Tiles
			if(mapIds.size() == 0)
			{
				return;
			}
			
			// now delete the Tiles
			String delete_sql = DELETE_TILES_SQL;
			params.clear();
			for(int x = 0; x < mapIds.size(); x++)
			{
				if(x == 0)
				{
					delete_sql += "?";
				}
				else
				{
					delete_sql += ", ?";
				}
				params.add(mapIds.get(x));
			}
			delete_sql += ")";
			dbh.execute(delete_sql, params);
			
			// get the IDs for the deleted Tiles so we can delete the Monsters
			String get_tile_ids = GET_TILE_IDS;
			params.clear();
			for(int x = 0; x < mapIds.size(); x++)
			{
				if(x == 0)
				{
					get_tile_ids += "?";
				}
				else
				{
					get_tile_ids += ", ?";
				}
				params.add(mapIds.get(x));
			}
			get_tile_ids += ")";
			
			results = dbh.executeQuery(get_tile_ids, params);
			while(results.next())
			{
				tileIds.add(results.getInt("id"));
			}
			// again, throw an exception if we have an empty list
			if(tileIds.size() == 0)
			{
				throw new LabyrinthException(messages.getMessage("tile.no_tiles_for_map"));
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		// need to change this for testing - don't want it going to database
		// delete any monsters on this Tile
		monster.delete(tileIds);
	}
	
	public Boundary getBoundaryFromString(String boundry)
	{
		switch(boundry.toLowerCase())
		{
		case "wall":
			return Boundary.WALL;
		case "door":
			return Boundary.DOOR;
		case "opening":
			return Boundary.OPENING;
		}
		return null;
	}
}
