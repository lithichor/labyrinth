package com.models;

import java.awt.Point;
import java.sql.SQLException;
import java.util.ArrayList;

import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class Tile extends LabyrinthModel
{
	private static final long serialVersionUID = -3695485086454469425L;
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
