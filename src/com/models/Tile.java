package com.models;

import java.awt.Point;

import com.parents.LabyrinthModel;

public class Tile extends LabyrinthModel
{
	private static final long serialVersionUID = -3695485086454469425L;
	private boolean hasMonster;
	private boolean visited;
	private Point coords;
	private Boundary north;
	private Boundary south;
	private Boundary east;
	private Boundary west;
	
	public enum Boundary
	{
			DOOR, OPENING, WALL
	}
	
	public Point getCoords() { return this.coords; }
	public void setCoords(Point p) { this.coords = p; }
	public void setCoords(int x, int y) { this.coords = new Point(x, y); }
	public boolean hasMonster() { return hasMonster; }
	public void setHasMonster(boolean hasMonster) { this.hasMonster = hasMonster; }
	public boolean wasVisited() { return visited; }
	public void setVisited(boolean visited) { this.visited = visited; }
	public Boundary getNorth() { return this.north; }
	public void setNorth(Boundary north) { this.north = north; }
	public Boundary getSouth() { return this.south; }
	public void setSouth(Boundary south) { this.south = south; }
	public Boundary getEast() { return this.east; }
	public void setEast(Boundary east) { this.east = east; }
	public Boundary getWest() { return this.west; }
	public void setWest(Boundary west) { this.west = west; }
	
}
