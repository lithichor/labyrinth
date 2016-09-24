package com.models.api;

import java.util.ArrayList;

import com.models.Game;
import com.parents.LabyrinthAPIModel;

public class APIGame extends LabyrinthAPIModel
{
	private Integer id;
	private Integer userId;
	private Integer heroId;
	private ArrayList<Integer> mapIds;

	public APIGame(Game g)
	{
		this.id = g.getId();
		this.userId = g.getUserId();
	}
	
	public void addMapId(Integer mapId)
	{
		if(mapIds == null)
		{ mapIds = new ArrayList<Integer>(); }
		mapIds.add(mapId);
	}

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }
	public Integer getHeroId() { return heroId; }
	public void setHeroId(Integer heroId) { this.heroId = heroId; }
	public ArrayList<Integer> getMapIds() { return mapIds; }
	public void setMapIds(ArrayList<Integer> maps) { this.mapIds = maps; }
}
