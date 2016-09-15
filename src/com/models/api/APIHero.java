package com.models.api;

import com.models.Hero;
import com.parents.LabyrinthAPIModel;

public class APIHero extends LabyrinthAPIModel
{
	private Integer id;
	private Integer gameId;
	
	public APIHero(Hero hero)
	{
		this.id = hero.getId();
		this.gameId = hero.getGameId();
	}
	
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public Integer getGameId()
	{
		return gameId;
	}
	public void setGameId(Integer gameId)
	{
		this.gameId = gameId;
	}
}
