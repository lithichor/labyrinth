package com.models;

import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class Tile extends LabyrinthModel
{
	private static final long serialVersionUID = -3695485086454469425L;
	private Integer id;
	private boolean hasMonster;
	private boolean visited;
	private Integer mapId;
	private Point coords;
	private Boundary north;
	private Boundary south;
	private Boundary east;
	private Boundary west;
	
	public enum Boundary
	{
		DOOR, OPENING, WALL
	}
	
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
	
	public boolean save() throws LabyrinthException
	{
		boolean success = true;
		String sql = "INSERT INTO tiles "
				+ "(x, y, has_monster, visited, "
				+ "map_id, north, south, east, west, "
				+ "created_at, updated_at)\n"
				+ "VALUES(" + coords.x + ", " + coords.y + ", " + getHasMonsterInt() + ", " + getWasVisitedInt() + ", "
				+ mapId + ", '" + north + "', '" + south + "', '" + east + "', '" + west + "', "
				+ " now(), now())";
		ArrayList<Object> params = new ArrayList<>();

		try
		{
			dbh.executeAndReturnKeys(sql, params);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}
		
		System.out.println(sql);
		
		return success;
	}
	
	public ArrayList<Tile> load(Integer mapId, Integer tileId, Integer userId) throws LabyrinthException
	{
		ArrayList<Tile> tiles = new ArrayList<>();
		ArrayList<Object> params = new ArrayList<>();
		ResultSet results = null;
		String sql = "SELECT t.id, x, y, map_id, has_monster, visited, "
				+ "north, south, east, west, t.created_at, t.updated_at "
				+ "FROM tiles t\n\t"
				+ "LEFT JOIN maps m ON m.id = t.map_id\n\t"
				+ "LEFT JOIN games g on g.id = m.game_id\n\t"
				+ "WHERE ";
		if(mapId > 0)
		{
			sql += "map_id = ? ";
			params.add(mapId);
		}
		else if(tileId > 0)
		{
			sql += "t.id = ? ";
			params.add(tileId);
		}
		else
		{
			throw new LabyrinthException(messages.getMessage("tile.no_ids"));
		}
		sql += "\n\tAND user_id = ?\n\tAND t.deleted_at IS NULL";
		params.add(userId);
		
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
				
				t.setNorth(getBoundary(results.getString("north")));
				t.setSouth(getBoundary(results.getString("south")));
				t.setEast(getBoundary(results.getString("east")));
				t.setWest(getBoundary(results.getString("west")));
				
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
			if(tileId > 0)
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
	 * @param gameId
	 * @throws LabyrinthException 
	 */
	public void deleteTiles(Integer gameId) throws LabyrinthException
	{
		// get the IDs of the maps belonging to the game
		String sql = "SELECT id FROM maps WHERE game_id = ? AND deleted_at IS NULL";
		ArrayList<Object> params = new ArrayList<>();
		params.add(gameId);
		ResultSet results = null;
		ArrayList<Integer> mapIds = new ArrayList<>();
		
		try
		{
			results = dbh.executeQuery(sql, params);
			while(results.next())
			{
				mapIds.add(results.getInt("id"));
			}
			// if we don't find any Maps, throw an exception
			if(mapIds.size() == 0)
			{
				throw new LabyrinthException(messages.getMessage("map.no_maps_for_game"));
			}
			
			// now delete the Tiles
			sql = "UPDATE tiles SET deleted_at = now() where map_id in (";
			params.clear();
			for(int x = 0; x < mapIds.size(); x++)
			{
				if(x == 0)
				{
					sql += "?";
				}
				else
				{
					sql += ", ?";
				}
				params.add(mapIds.get(x));
			}
			sql += ")";
			dbh.execute(sql, params);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}
	}
	
	private Boundary getBoundary(String bound)
	{
		switch(bound)
		{
		case "WALL":
			return Boundary.WALL;
		case "DOOR":
			return Boundary.DOOR;
		case "OPENING":
			return Boundary.OPENING;
		}
		return null;
	}
	
	public static void main(String[] args) throws LabyrinthException
	{
		Tile t = new Tile(1, 2, 0);
		t.setEast(Boundary.DOOR);
		t.setNorth(Boundary.WALL);
		t.setSouth(Boundary.OPENING);
		t.setWest(Boundary.OPENING);
		t.save();
	}
}
