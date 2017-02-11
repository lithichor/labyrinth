package com.web.api.game;

import com.parents.LabyrinthOptions;

public class GamesOptions extends LabyrinthOptions
{
	public GamesOptions()
	{
		basics = "A Game belongs to a User; it is where the action happens. A "
				+ "User can have up to four Games at a given time; before a "
				+ " fifth Game can be created one of the others has to be deleted.";
		
		delete.put("general", "Deleting a Game does a soft-delete, but there is "
				+ "no mechanism available to retrieve deleted Games. The Game's "
				+ "ID is required to delete it.");
		delete.put("url", "/api/games/:id");
		
		fields.put("id", "Integer");
		fields.put("userId", "Integer");
		fields.put("heroId", "Integer");
		fields.put("mapIds", "Array");
		
		get.put("general", "The Get verb returns a Game object. Get can return "
				+ "a single Game or a list of Games.");
		get.put("url - all Games",  "/api/games - returns all active Games for the User.");
		get.put("url - one Game",  "/api/games/:id - returns the Game with the specified ID.");
		
		post.put("general", "The Post verb creates a new Game. No data is required. The "
				+ "response is the Game object just created.");
		
		put.put("general", "The put verb is not supported got the Games endpoint.");
	}
}
