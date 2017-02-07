package com.web.api.turn;

import java.awt.Point;

import com.parents.LabyrinthAPIModel;

public class APITurn extends LabyrinthAPIModel
{
	private Integer id;
	private Integer iteration;
	private Integer userId;
	private Integer gameId;
	private Integer mapId;
	private Point coords;
	
	public APITurn(Turn turn)
	{
		this.id = turn.getId();
		this.iteration = turn.getIteration();
		this.userId = turn.getUserId();
		this.gameId = turn.getGameId();
		this.mapId = turn.getMapId();
		this.coords = turn.getCoords();
	}

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getIteration() { return iteration; }
	public void setIteration(Integer iteration) { this.iteration = iteration; }
	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }
	public Integer getGameId() { return gameId; }
	public void setGameId(Integer gameId) { this.gameId = gameId; }
	public Integer getMapId() { return mapId; }
	public void setMapId(Integer mapId) { this.mapId = mapId; }
	public Point getCoords() { return coords; }
	public void setCoords(Point coords) { this.coords = coords; }
}
