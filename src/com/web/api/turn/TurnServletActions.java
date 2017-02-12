package com.web.api.turn;

import java.awt.Point;

import com.parents.LabyrinthException;
import com.parents.LabyrinthServletActions;
import com.web.api.map.Map;
import com.web.api.tile.Tile;
import com.web.api.tile.Tile.Boundary;

public class TurnServletActions extends LabyrinthServletActions
{
	public Turn makeMove(String direction, Turn turn) throws LabyrinthException
	{
		int x = turn.getCoords().x;
		int y = turn.getCoords().y;
		Map map = null;
		Tile t = null;
		
		map = new Map().load(turn.getGameId(), turn.getMapId()).get(0);
		t = map.getTileByCoords(turn.getCoords(), turn.getUserId());
		
		switch(direction)
		{
		// need to check for walls
		case "east":
		case "e":
			if(t.getEast() == Boundary.OPENING)
			{
				x++;
			}
			break;
		case "west":
		case "w":
			if(t.getWest() == Boundary.OPENING)
			{
				x--;
			}
			break;
		case "north":
		case "n":
			if(t.getNorth() == Boundary.OPENING)
			{
				y--;
			}
			break;
		case "south":
		case "s":
			if(t.getSouth() == Boundary.OPENING)
			{
				y++;
			}
			break;
		default:
			throw new LabyrinthException(messages.getMessage("turn.invalid_data"));
		}
		
		turn.setCoords(new Point(x, y));
		turn.setIteration(turn.getIteration() + 1);

		return turn;
	}
}
