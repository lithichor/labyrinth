package com.web.api.turn;

import com.parents.LabyrinthOptions;

public class TurnsGameOptions extends LabyrinthOptions
{
	public TurnsGameOptions()
	{
		get.put("general", "The Get verb returns the Turn associated with the Game ID");
		get.put("url", "/api/turns/game/:gameId");
		
		basics = null;
		put = null;
		delete = null;
		fields = null;
		post = null;
		seeAlso = null;
	}
}
