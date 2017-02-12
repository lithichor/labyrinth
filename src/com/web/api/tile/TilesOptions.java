package com.web.api.tile;

import com.parents.LabyrinthOptions;

public class TilesOptions extends LabyrinthOptions
{
	public TilesOptions()
	{
		basics = "A Tile represents the individual squares that make up a Map. It "
				+ "might contain a Monster, or have Walls and Doors, and is where "
				+ "the Hero stands while in a Map.";
		
		fields.put("id",  "Integer");
		fields.put("mapId",  "Integer");
		fields.put("hasMonster",  "Boolean");
		fields.put("visited",  "Boolean");
		fields.put("coords",  "Point");
		fields.put("coords.x",  "Integer");
		fields.put("coords.y",  "Integer");
		fields.put("north",  "OPENING | WALL | DOOR");
		fields.put("south",  "OPENING | WALL | DOOR");
		fields.put("east",  "OPENING | WALL | DOOR");
		fields.put("west",  "OPENING | WALL | DOOR");
		
		get.put("general",  "The Get verb returns an individual Tile. It requires an ID.");
		get.put("url",  "/api/tiles/:id");
		
		seeAlso.put("/api/tiles/map/:mapId",  "Returns all Tiles for a given Map.");
		
		put = null;
		delete = null;
		post = null;
	}
}
