package com.web.api.maps;

import com.parents.LabyrinthAPIModel;

public class APIMap extends LabyrinthAPIModel
{
	private Integer id;
	private Integer gameId;
	private Integer gridSize;
	private Integer firstTileId;
	private String name;
	private String type;

	public APIMap(Map map)
	{
		this.id = map.getId();
		this.gameId = map.getGameId();
		this.gridSize = map.getGridSize();
		this.firstTileId = map.getFirstTileId();
		this.name = map.getName();
		this.type = map.getType();
	}

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getGameId() { return gameId; }
	public void setGameId(Integer gameId) { this.gameId = gameId; }
	public Integer getGridSize() { return this.gridSize; }
	public void setGridSize(Integer gridSize) { this.gridSize = gridSize; }
	public Integer getFirstTileId() { return this.firstTileId; }
	public void setFirstTileId(Integer firstTileId) { this.firstTileId = firstTileId; }
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	public String getType() { return this.type; }
	public void setType(String type) { this.type = type; }
}
