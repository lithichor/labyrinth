package com.web.api.tile;

import com.parents.LabyrinthOptions;

public class TilesMapOptions extends LabyrinthOptions
{
	public TilesMapOptions()
	{
		get.put("general",  "The Get verb allows you to retrieve all the Tiles comprising a Map. The "
				+ "Map ID is required.");
		get.put("url",  "/api/tiles/map/:mapId");
		
		basics = null;
		put = null;
		delete = null;
		fields = null;
		post = null;
		seeAlso = null;
	}
}
