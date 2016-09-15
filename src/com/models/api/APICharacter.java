package com.models.api;

import com.models.Character;
import com.parents.LabyrinthAPIModel;

public class APICharacter extends LabyrinthAPIModel
{
	private Integer id;
	private Integer gameId;
	
	public APICharacter(Character c)
	{
		this.id = c.getId();
		this.gameId = c.getGameId();
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
