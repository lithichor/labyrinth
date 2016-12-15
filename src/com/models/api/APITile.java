package com.models.api;

import java.awt.Point;

import com.models.Tile;
import com.models.Tile.Boundary;
import com.parents.LabyrinthAPIModel;

public class APITile extends LabyrinthAPIModel
{
	private Integer id;
	private Integer mapId;
	private boolean hasMonster;
	private boolean visited;
	private Point coords;
	private Boundary north; 
	private Boundary south; 
	private Boundary east; 
	private Boundary west; 
	
	public APITile(Tile t)
	{
		this.id = t.getId();
		this.mapId = t.getMapId();
		this.hasMonster = t.hasMonster();
		this.visited = t.wasVisited();
		this.coords = t.getCoords();
		this.north = t.getNorth();
		this.south = t.getSouth();
		this.east = t.getEast();
		this.west = t.getWest();
	}

	public Integer getMapId() { return this.mapId; }
	public void setMapId(Integer mapId) { this.mapId = mapId; }
	public Integer getId() { return this.id; }
	public void setId(Integer id) { this.id = id; }
	public Boundary getNorth() { return north; }
	public void setNorth(Boundary north) { this.north = north; }
	public Boundary getSouth() { return south; }
	public void setSouth(Boundary south) { this.south = south; }
	public Boundary getEast() { return east; }
	public void setEast(Boundary east) { this.east = east; }
	public Boundary getWest() { return west; }
	public void setWest(Boundary west) { this.west = west; }
	public boolean isHasMonster() { return hasMonster; }
	public void setHasMonster(boolean hasMonster) { this.hasMonster = hasMonster; }
	public boolean isVisited() { return visited; }
	public void setVisited(boolean visited) { this.visited = visited; }
	public Point getCoords() { return coords; }
	public void setCoords(Point coords) { this.coords = coords; }

}
