package com.web.api.maps;

import com.parents.LabyrinthAPIModel;

public class APIMap extends LabyrinthAPIModel
{
	private Integer id;
	private Integer gameId;
	private Integer gridSize;
	private Integer firstTileId;

	public APIMap(Map map)
	{
		this.id = map.getId();
		this.gameId = map.getGameId();
		this.gridSize = map.getGridSize();
		this.firstTileId = map.getFirstTileId();
	}

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getGameId() { return gameId; }
	public void setGameId(Integer gameId) { this.gameId = gameId; }
	public Integer getGridSize() { return this.gridSize; }
	public void setGridSize(Integer gridSize) { this.gridSize = gridSize; }
	public Integer getFirstTileId() { return this.firstTileId; }
	public void setFirstTileId(Integer firstTileId) { this.firstTileId = firstTileId; }
}
