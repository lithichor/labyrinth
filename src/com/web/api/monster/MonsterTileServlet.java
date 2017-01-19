package com.web.api.monster;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.Monster;
import com.models.User;
import com.models.api.APIErrorMessage;
import com.models.api.APIMonster;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;

public class MonsterTileServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 2695498520215830537L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		errors.clear();
		
		int tileId = getIdFromUrl(request, EndpointsWithIds.MONSTERS_TILES);
		MonsterServletActions actions = new MonsterServletActions();
		ArrayList<Monster> monsters = new ArrayList<>();
		
		try
		{
			User user = actions.authenticateUser(request);

			if(tileId == 0)
			{
				errors.add(messages.getMessage("monster.no_tile_id"));
			}
			else
			{
				monsters = new Monster().loadMonstersByUserAndTile(user.getId(), tileId);
				if(monsters.size() == 0)
				{
					errors.add(messages.getMessage("monster.no_monster_with_tile_id"));
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
