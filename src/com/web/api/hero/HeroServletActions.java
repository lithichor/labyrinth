package com.web.api.hero;

import java.util.ArrayList;

import com.google.gson.JsonObject;
import com.models.Game;
import com.models.User;
import com.parents.LabyrinthException;
import com.parents.LabyrinthServletActions;

public class HeroServletActions extends LabyrinthServletActions
{
	public Integer getGameId(User user, JsonObject data) throws LabyrinthException
	{
		int gameId = 0;
		
		// if the gameId is specified, use that
		if(data != null && data.has("gameId"))
		{
			gameId = data.get("gameId").getAsInt();
		}
		else
		{
			// otherwise load the last active game for the user
			ArrayList<Game> games = new Game().load(user.getId(), 0);
			gameId = games.get(games.size() - 1).getId();
		}
		
		return gameId;
	}
}
