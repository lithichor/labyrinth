package com.web.api.maps;

import com.google.gson.JsonObject;
import com.parents.LabyrinthException;
import com.parents.LabyrinthServletActions;
import com.web.api.user.User;

public class MapsServletActions extends LabyrinthServletActions
{
	public Integer getGameId(User user, JsonObject data) throws LabyrinthException
	{
		int gameId = 0;
		
		// if the gameId is specified, use that
		if(data != null && data.has("gameId"))
		{
			try
			{
				gameId = data.get("gameId").getAsInt();
			}
			catch(NumberFormatException | IllegalStateException  | UnsupportedOperationException ex)
			{
				throw new LabyrinthException(messages.getMessage("map.gameId_not_integer"));
			}
		}
		
		return gameId;
	}
}
