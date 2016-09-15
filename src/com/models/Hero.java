package com.models;

import com.parents.LabyrinthModel;

public class Hero extends LabyrinthModel
{
	private static final long serialVersionUID = 3345291415215532854L;

	private Integer id;
	private Integer gameId;
	
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
