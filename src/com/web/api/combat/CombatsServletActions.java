package com.web.api.combat;

import com.google.gson.JsonObject;
import com.parents.LabyrinthException;
import com.parents.LabyrinthServletActions;

public class CombatsServletActions extends LabyrinthServletActions
{
	private static final String ATTACK = "attack";
	private static final String CAST = "cast";
	private static final String RUN = "run";
	private Combat comb = new Combat();
	
	public void setCombat(Combat c) { this.comb = c; }
	
	public String performCombatAction(JsonObject data) throws LabyrinthException
	{
		String action = "";
		String result = "";

		try
		{
			if(data.has("action"))
			{
				action = data.get("action").getAsString();
			}
		}
		catch(IllegalStateException | UnsupportedOperationException ise_uoe)
		{
			throw new LabyrinthException(messages.getMessage("combat.data_invalid"));
		}

		// TODO: will need to call the combat prototype and return a result
		switch(action.toLowerCase())
		{
		case ATTACK:
		case "a":
			result = "Attack!";
			break;
		case CAST:
		case "c":
			result = "Abracadabra";
			break;
		case RUN:
		case "r":
			result = "Run away!";
			break;
		default:
			throw new LabyrinthException(messages.getMessage("combat.not_a_valid_combat_action"));
		}
		
		return result;
	}
	
	public APICombat getApiCombat(Integer userId, Integer combatId) throws LabyrinthException
	{
		Combat combat = null;
		APICombat apiCombat = null;
		
		combat = comb.load(userId, combatId);
		if(combat == null)
		{
			throw new LabyrinthException(messages.getMessage("combat.no_id_for_user"));
		}
		else
		{
			apiCombat = new APICombat(combat);
		}

		return apiCombat;
	}
}
