package com.models.api;

import java.util.ArrayList;

import com.models.Game;
import com.parents.LabyrinthAPIModel;

public class APIGame extends LabyrinthAPIModel
{
	private Integer id;
	private Integer userId;
	private APICharacter character;
	private ArrayList<APIMap> maps;

	public APIGame(Game g)
	{
		this.id = g.getId();
		this.userId = g.getUserId();
	}

	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public Integer getUserId()
	{
		return userId;
	}
	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}
	public APICharacter getCharacter()
	{
		return character;
	}
	public void setCharacter(APICharacter character)
	{
		this.character = character;
	}
	public ArrayList<APIMap> getMaps()
	{
		return maps;
	}
	public void setMaps(ArrayList<APIMap> maps)
	{
		this.maps = maps;
	}
}
