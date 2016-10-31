package com.web.api.hero;

import java.util.HashMap;

import com.LabyrinthConstants;
import com.google.gson.JsonObject;
import com.models.Hero;
import com.parents.LabyrinthException;
import com.parents.LabyrinthValidationHelper;

public class HeroValidationHelper extends LabyrinthValidationHelper
{
	public boolean validate(HashMap<String, String> params)
	{
		boolean valid = true;
		
		// keep these as place holders until we do Labyrinth #67
		if(params.get("firstName") == null || "".equalsIgnoreCase(params.get("firstName")))
		{
			if(!errors.contains(LabyrinthConstants.USER_NEEDS_FIRST_NAME))
			{
				errors.add(LabyrinthConstants.USER_NEEDS_FIRST_NAME);
			}
			valid = false;
		}
		if(params.get("lastName") == null || "".equalsIgnoreCase(params.get("lastName")))
		{
			if(!errors.contains(LabyrinthConstants.USER_NEEDS_LAST_NAME))
			{
				errors.add(LabyrinthConstants.USER_NEEDS_LAST_NAME);
			}
			valid = false;
		}
		
		return valid;
	}
	
	public Hero validateApi(JsonObject data)
	{
		Hero hero = new Hero();
		HashMap<String, String> params = new HashMap<String, String>();
		
		// don't bother validating if the data is null (although
		// it shouldn't be)
		if(data == null)
		{
			return null;
		}
		
		// return the hero only if it passes validation
		if(this.validate(params))
		{
			return hero;
		}
		else
		{
			return null;
		}
	}
	
	public Hero validateApiPut(JsonObject data, Integer heroId) throws LabyrinthException
	{
		Hero hero = new Hero();
		if(heroId == null || heroId == 0)
		{
			throw new LabyrinthException(LabyrinthConstants.HERO_NEEDS_ID);
		}
		
		if(data.has("strength"))
		{
			hero.setStrength(data.get("strength").getAsInt());
		}
		if(data.has("magic"))
		{
			hero.setMagic(data.get("magic").getAsInt());
		}
		if(data.has("attack"))
		{
			hero.setAttack(data.get("attack").getAsInt());
		}
		if(data.has("defense"))
		{
			hero.setDefense(data.get("defense").getAsInt());
		}
		
		return hero;
	}
}
