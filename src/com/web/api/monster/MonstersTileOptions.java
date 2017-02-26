package com.web.api.monster;

import com.parents.LabyrinthOptions;

public class MonstersTileOptions extends LabyrinthOptions
{
	public MonstersTileOptions()
	{
		get.put("general", "The Get verb allows you to retrieve the Monster on a given Tile");
		get.put("url", "/api/monsters/tile/:tileId");
		
		basics = null;
		delete = null;
		fields = null;
		post = null;
		put = null;
		seeAlso = null;
	}
}
