package com.web.api.monster;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.Monster;
import com.models.api.APIErrorMessage;
import com.models.api.APIMonster;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;
import com.web.api.user.User;

public class MonstersServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 2502557358566091760L;

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
		
		int monsterId = getIdFromUrl(request, EndpointsWithIds.MONSTERS);
		MonstersServletActions actions = new MonstersServletActions();
		ArrayList<Monster> monsters = new ArrayList<>();
		
		try
		{
			User user = actions.authenticateUser(request);
			
			// no ID means no monsters
			if(monsterId <= 0)
			{
				errors.add(messages.getMessage("monster.no_monster_id"));
			}
			else
			{
				monsters = new Monster().loadMonstersByUserAndMonster(user.getId(), monsterId);
				if(monsters.size() == 0)
				{
					errors.add(messages.getMessage("monster.no_monster_found"));
				}
			}
		}
		catch(LabyrinthException le)
		{
			errors.add(le.getMessage());
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
	
}
