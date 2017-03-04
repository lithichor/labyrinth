package com.web.api.combat;

import com.parents.LabyrinthOptions;

public class CombatsOptions extends LabyrinthOptions
{
	public CombatsOptions()
	{
		basics = "Combat occurs when a Hero meets a Monster. The Hero has some options "
				+ "when this occurs; attack the monster, cast a spell, or run away. "
				+ "The choices available to the Monster are more limited.";
		
		fields.put("id", "Integer");
		fields.put("userId", "Integer");
		fields.put("heroId", "Integer");
		fields.put("monsterId", "Integer");
		
		get.put("general", "The Get verb allows you to retrieve the information "
				+ "for a particular Combat round. Combat ID is required.");
		get.put("url", "/api/combats/:id");
		
		put.put("general", "The Put verb allows the Hero to choose what action to take "
				+ "when confronting a Monster. The coices are Attack, Cast, or Run. These "
				+ "are case insensitive and can be abbreviated with the first letter (a, c, r).");
		put.put("url", "/api/combats/:id");
		put.put("data example", "{action: Attack}");
		
		delete = null;
		post = null;
		seeAlso = null;
	}
}
