package com.web.api.hero;

import com.parents.LabyrinthOptions;

public class HerosOptions extends LabyrinthOptions
{
	public HerosOptions()
	{
		basics = "A Hero does the things you want to do. There is one Hero per "
				+ "Game.";
		
		fields.put("id",  "Integer");
		fields.put("gameId",  "Integer");
		fields.put("health",  "Integer");
		fields.put("strength",  "Integer");
		fields.put("magic",  "Integer");
		fields.put("attack",  "Integer");
		fields.put("defense",  "Integer");
		
		get.put("general",  "The Get verb returns a single Hero or all Heroes for the User");
		get.put("url - one", "/api/heros/:id");
		get.put("url - all", "/api/heros");
		
		put.put("general",  "In general, a Hero will only be updated in the background, "
				+ "for instance when leveling up. The Put verb is provided so that the "
				+ "developer can add signup bonuses to the inplementation. Health, Strength, "
				+ "Magic, Attack, and Defense, can be changed, but none are required.");
		put.put("data example",  "{health: 64, strength: 15, magic: 25, attack: 9, defense: 12}");
		
		seeAlso.put("/api/heros/game/:gameId",  "Gives you the Hero for the specified Game");
		
		delete = null;
		post = null;
	}
}
