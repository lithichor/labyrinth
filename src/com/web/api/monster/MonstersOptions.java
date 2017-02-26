package com.web.api.monster;

import com.parents.LabyrinthOptions;

public class MonstersOptions extends LabyrinthOptions
{
	public MonstersOptions()
	{
		basics = "A Monster exists on a Tile inside a Map. The Monster is determined by the Map type.";
		
		get.put("general", "The Get verb returns the Monster matching the given ID");
		get.put("url", "/api/mosters/:id");
		
		seeAlso.put("/api/monsters/tile/:tileId", "Returns the Monster for a given Tile");
		
		delete = null;
		fields = null;
		put = null;
		post = null;
	}
}
