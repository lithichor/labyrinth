package com.web.api.map;

import com.parents.LabyrinthOptions;

public class MapsGameOptions extends LabyrinthOptions
{
	public MapsGameOptions()
	{
		get.put("general", "The Get verb allows you to get the Maps for a given Game.");
		get.put("url", "/api/maps/game/:gameId");
		
		basics = null;
		delete = null;
		fields = null;
		post = null;
		put = null;
		seeAlso = null;
	}
}
