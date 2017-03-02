package com.web.api.turn;

import com.parents.LabyrinthOptions;

public class TurnsOptions extends LabyrinthOptions
{
	public TurnsOptions()
	{
		basics = "The Turn object is what keeps track of time in The Labyrinth; every "
				+ "Turn represents one unit of time. There is one Turn object per Game, "
				+ "and the iteration field tracks how many times the Turn has been updated.";
		
		get.put("general", "The Get verb returns the Turn represented by the ID");
		get.put("url", "api/turns/:id");
		
		put.put("general", "The Put verb updates the Turn by moving the Player in the direction "
				+ "indicated in the data. The direction data field is case insensitive, and can "
				+ "be abbreviated by using the first letter; i.e., N, S, E, W");
		put.put("url", "api/turns/:id");
		put.put("data example", "{direction: west}");
		
		seeAlso.put("api/turns/game/:gameId", "Returns the Turn associated with the Game");

		delete = null;
		fields = null;
		post = null;
	}
}
