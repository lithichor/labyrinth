package com.web.api.maps;

import com.parents.LabyrinthOptions;

public class MapsOptions extends LabyrinthOptions
{
	public MapsOptions()
	{
		basics = "A Map is where your Hero walks around. It is a randomly generated "
				+ "maze where Monsters lurk.";
		
		delete.put("general", "The Delete verb removes a Map from its Game. It requires "
				+ "the Map ID to work.");
		delete.put("url", "/api/maps/:id");
		
		fields.put("id", "Integer");
		fields.put("gameId", "Integer");
		
		get.put("general", "The Get verb returns a particular Map, or all the Maps for "
				+ "the last Game created.");
		get.put("url - one Map", "/api/maps/:id");
		get.put("url - all Maps for current Game", "/api/maps");
		
		post.put("general", "The Post verb creates a new map for the given Game. It "
				+ "requires the Game ID be provided in the data.");
		post.put("data example", "{gameId: 1234}");
		
		seeAlso.put("/api/maps/game/:gameId", "Returns the Maps for a given Game");
		
		put = null;
	}
}
