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

public class MonstersTileServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 2695498520215830537L;
	private MonstersServletActions actions;
	private Monster monster;
	private MonstersTileOptions options;
	private APIMonster apiMonster;

	public MonstersTileServlet()
	{
		this.actions = new MonstersServletActions();
		this.monster = new Monster();
		this.options = new MonstersTileOptions();
		this.apiMonster = new APIMonster();
	}
	
	public void setActions(MonstersServletActions actions) { this.actions = actions; }
	public void setMonster(Monster monster) { this.monster = monster; }
	public void setMonstersTileOptions(MonstersTileOptions options) { this.options = options; }
	public void setAPIMonster(APIMonster apiMonster) { this.apiMonster = apiMonster; }
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		errors.clear();
		
		int tileId = actions.getIdFromUrl(request, EndpointsWithIds.MONSTERS_TILES);
		ArrayList<Monster> monsters = new ArrayList<>();
		User user = new User();
		
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
		
		if(user == null)
		{
			errors.add(messages.getMessage("user.no_such_player"));
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
			return;
		}
		
		if(tileId <= 0)
		{
			errors.add(messages.getMessage("monster.no_tile_id"));
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
			return;
		}
		try
		{
			monsters = monster.loadMonstersByUserAndTile(user.getId(), tileId);
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
			errors.add(messages.getMessage("monster.no_monster_with_tile_id"));
		}
		
		if(errors.size() > 0)
		{
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
		}
		else
		{
			// there's only going to be one monster with the given ID
			Monster m = monsters.get(0);
			apiMonster.setId(m.getId());
			apiMonster.setAttack(m.getAttack());
			apiMonster.setDefense(m.getDefense());
			apiMonster.setHealth(m.getHealth());
			apiMonster.setTileId(m.getTileId());
			
			apiOut(gson.toJson(apiMonster), response);
		}
	}
	
	public void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
		apiOut(gson.toJson(options), response);
	}
}
