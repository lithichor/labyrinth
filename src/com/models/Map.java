package com.models;

import com.parents.LabyrinthModel;

public class Map extends LabyrinthModel
{
	private static final long serialVersionUID = 8887895302650203935L;

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
