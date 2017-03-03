package com.web.api.maps;

import com.parents.LabyrinthOptions;

public class MapsGameOptions extends LabyrinthOptions
{
	public MapsGameOptions()
	{
		get.put("general", "The Get verb allows you to get the Maps for a given Game. If "
				+ "no ID is provided, it returns the Maps for the last Game created.");
		get.put("url - no ID", "/api/maps/game");
		get.put("url - with ID", "/api/maps/game/:gameId");
		
		basics = null;
		delete = null;
		fields = null;
		post = null;
		put = null;
		seeAlso = null;
	}
}
