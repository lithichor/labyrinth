package com.web.api.hero;

import java.util.HashMap;

import com.LabyrinthConstants;
import com.google.gson.JsonObject;
import com.models.Hero;
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
}
