package com.web.api.combat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.models.api.APIErrorMessage;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;
import com.web.api.user.User;

public class CombatsServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = -125705493904670894L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		errors.clear();
		Combat combat = null;
		APICombat apiCombat = null;
		CombatsServletActions actions = new CombatsServletActions();
		Integer combatId = getIdFromUrl(request, EndpointsWithIds.COMBATS);
		User user = null;

		if(combatId <= 0)
		{
			errors.add(messages.getMessage("combat.no_id"));
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
			return;
		}
		
		try
		{
			user = actions.authenticateUser(request);
		}
		catch(LabyrinthException le)
		{
			errors.add(messages.getMessage("user.no_authorization"));
		}

		if(user != null)
		{
			try
			{
				combat = new Combat().load(user.getId(), combatId);
				if(combat == null)
				{
					errors.add(messages.getMessage("combat.no_id_for_user"));
				}
				else
				{
					apiCombat = new APICombat(combat);
				}
			}
			catch(LabyrinthException le)
			{
				errors.add(messages.getMessage("unknown.horribly_wrong"));
			}
		}

		if(errors.size() > 0)
		{
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
		}
		else
		{
			apiOut(gson.toJson(apiCombat), response);
		}
	}
	
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		errors.clear();
		Combat combat = null;
		CombatsServletActions actions = new CombatsServletActions();
		Integer combatId = getIdFromUrl(request, EndpointsWithIds.COMBATS);
		User user = null;
		JsonObject data = null;
		String results = "";

		if(combatId <= 0)
		{
			errors.add(messages.getMessage("combat.no_id"));
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
			return;
		}
		
		try
		{
			user = actions.authenticateUser(request);
		}
		catch(LabyrinthException le)
		{
			errors.add(messages.getMessage("user.no_authorization"));
		}
		
		if(user != null)
		{
			try
			{
				try
				{
					combat = new Combat().load(user.getId(), combatId);
				}
				catch(LabyrinthException le)
				{
					throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
				}
				
				
				if(combat == null)
				{
					errors.add(messages.getMessage("combat.no_id_for_user"));
				}
				else
				{
					try
					{
						data = actions.getData(request);
						if(data != null)
						{
							results = actions.performCombatAction(data);
							// update the combat
							// if monster or hero dies or hero runs away, delete the combat
							// might need a CombatResults class (string + hero + monster)
							if(results.equals("Run away!"))
							{
								combat.delete();
							}
						}
						else
						{
							errors.add(messages.getMessage("combat.no_data"));
						}
					}
					catch(LabyrinthException le)
					{
						throw new LabyrinthException(le.getMessage());
					}
				}
			}
			catch(LabyrinthException le)
			{
				errors.add(le.getMessage());
			}
		}

		if(errors.size() > 0)
		{
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
		}
		else
		{
			apiOut(gson.toJson(results), response);
		}
	}
	
	public void doOptions(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		errors.clear();
		
		CombatsOptions options = new CombatsOptions();
		apiOut(gson.toJson(options), response);
	}

	// TEMP ONLY - will be removed with #201
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		Combat c = new Combat();
		c.setUserId(1);
		c.setHeroId(2293);
		c.setMonsterId(1930);

		try
		{
			c.save();
		}
		catch (LabyrinthException le)
		{
			le.printStackTrace();
			apiOut("oops", response);
		}
	}
}
