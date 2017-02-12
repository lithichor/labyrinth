package com.web.api.hero;

import com.parents.LabyrinthOptions;

public class HerosGameOptions extends LabyrinthOptions
{
	public HerosGameOptions()
	{
		get.put("general",  "The Get verb allows you to retrieve the Hero for a particular Game.");
		get.put("url",  "/api/heros/game/:gameId");
		
		basics = null;
		put = null;
		delete = null;
		fields = null;
		post = null;
		seeAlso = null;
	}
}
