package com.web.api.hero;

import java.util.HashMap;

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
			if(!errors.contains(messages.getMessage("signup.user_needs_first_name")))
			{
				errors.add(messages.getMessage("signup.user_needs_first_name"));
			}
			valid = false;
		}
		if(params.get("lastName") == null || "".equalsIgnoreCase(params.get("lastName")))
		{
			if(!errors.contains(messages.getMessage("signup.user_needs_last_name")))
			{
				errors.add(messages.getMessage("signup.user_needs_last_name"));
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
			throw new LabyrinthException(messages.getMessage("hero.hero_needs_id"));
		}
		
		if(data.has("strength"))
		{
			try
			{
				hero.setStrength(data.get("strength").getAsInt());
			}
			catch(NumberFormatException nfe)
			{
				errors.add(messages.getMessage("hero.strength_is_not_a_number"));
			}
		}
		if(data.has("magic"))
		{
			try
			{
				hero.setMagic(data.get("magic").getAsInt());
			}
			catch(NumberFormatException nfe)
			{
				errors.add(messages.getMessage("hero.magic_is_not_a_number"));
			}
		}
		if(data.has("attack"))
		{
			try
			{
				hero.setAttack(data.get("attack").getAsInt());
			}
			catch(NumberFormatException nfe)
			{
				errors.add(messages.getMessage("hero.attack_is_not_a_number"));
			}
		}
		if(data.has("defense"))
		{
			try
			{
				hero.setDefense(data.get("defense").getAsInt());
			}
			catch(NumberFormatException nfe)
			{
				errors.add(messages.getMessage("hero.defense_is_not_a_number"));
			}
		}
		if(errors.size() > 0)
		{
			throw new LabyrinthException(messages.getMessage("hero.bad_attributes"));
		}
		
		return hero;
	}
}
