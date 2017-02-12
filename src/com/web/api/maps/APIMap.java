package com.web.api.maps;

import com.parents.LabyrinthAPIModel;

public class APIMap extends LabyrinthAPIModel
{
	private Integer id;
	private Integer gameId;

	public APIMap(Map map)
	{
		this.id = map.getId();
		this.gameId = map.getGameId();
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
