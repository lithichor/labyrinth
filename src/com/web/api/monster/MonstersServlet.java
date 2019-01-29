package com.web.api.monster;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.api.APIErrorMessage;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;
import com.web.api.user.User;

public class MonstersServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 2502557358566091760L;
	private MonstersServletActions actions;
	private Monster monster;

	public MonstersServlet()
	{
		this.actions = new MonstersServletActions();
	}

	public void setActions(MonstersServletActions actions) { this.actions = actions; }
	public void setMonster(Monster monster) { this.monster = monster; }

	/**
	 * api/monsters
	 * api/monsters/:id
	 * 
	 * If an ID is included, that is used to find the monster. If no
	 * ID in included the server returns an error
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();

		int monsterId = actions.getIdFromUrl(request, EndpointsWithIds.MONSTERS);
		ArrayList<Monster> monsters = new ArrayList<>();
		User user = null;

		try
		{
			user = actions.authenticateUser(request);
		}
		catch(LabyrinthException le)
		{
			errors.add(le.getMessage());
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
			return;
		}

		boolean authenticated = (user != null);

		if(authenticated)
		{
			// no ID means no monsters
			if(monsterId <= 0)
			{
				errors.add(messages.getMessage("monster.no_monster_id"));
			}
			else
			{
				try
				{
					monsters = monster.loadMonstersByUserAndMonster(user.getId(), monsterId);
				}
				catch (LabyrinthException le)
				{
					errors.add(messages.getMessage("unknown.unknown_error"));
					le.printStackTrace();
					apiOut(gson.toJson(new APIErrorMessage(errors)), response);
					return;
				}

				if(monsters.size() == 0)
				{
					errors.add(messages.getMessage("monster.no_monster_found"));
				}
			}
		}
		else
		{
			errors.add(messages.getMessage("user.no_such_player"));
		}

		if(errors.size() > 0)
		{
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
		}
		else
		{
			// there's only going to be one monster with the given ID
			apiOut(gson.toJson(new APIMonster(monsters.get(0))), response);
		}
	}

	public void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();

		MonstersOptions options = new MonstersOptions();
		apiOut(gson.toJson(options), response);
	}
}
