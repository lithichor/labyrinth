package com.web.api.maps;

import java.util.HashMap;

import com.google.gson.JsonObject;
import com.parents.LabyrinthValidationHelper;

public class MapValidationHelper extends LabyrinthValidationHelper
{
	@Override
	public boolean validate(HashMap<String, String> params)
	{
		boolean valid = true;
		
		if(params.get("gameId") == null || "".equalsIgnoreCase(params.get("gameId")) || "0".equalsIgnoreCase(params.get("gameId")))
		{
			if(!errors.contains(messages.getMessage("map.map_needs_game_id")))
			{
				errors.add(messages.getMessage("map.map_needs_game_id"));
			}
			valid = false;
		}
		return valid;
	}

	@Override
	public Map validateApi(JsonObject data)
	{
		Map map = new Map();
		HashMap<String, String> params = new HashMap<>();
		
		if(data == null)
		{
			return null;
		}

		if(data.has("gameId"))
		{
			try
			{
				map.setGameId(data.get("gameId").getAsInt());
				params.put("gameId", map.getGameId().toString());
			}
			catch(NumberFormatException | IllegalStateException  | UnsupportedOperationException ex)
			{
				errors.add(messages.getMessage("map.gameId_not_integer"));
				return null;
			}
		}
		else
		{
			errors.add(messages.getMessage("map.map_needs_game_id"));
		}
		
		// return the user only if it passes validation
		if(this.validate(params))
		{
			return map;
		}
		else
		{
			return null;
		}
	}
}
