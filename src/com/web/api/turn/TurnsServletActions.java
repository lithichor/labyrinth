package com.web.api.turn;

import java.awt.Point;

import com.parents.LabyrinthException;
import com.parents.LabyrinthServletActions;
import com.web.api.combat.Combat;
import com.web.api.hero.Hero;
import com.web.api.maps.Map;
import com.web.api.monster.Monster;
import com.web.api.tile.Tile;
import com.web.api.tile.Tile.Boundary;

public class TurnsServletActions extends LabyrinthServletActions
{
	private Map map;
	private Hero hero;
	private Combat combat;
	private Monster monster;
	
	public void setMap(Map map) { this.map = map; }
	public void setHero(Hero hero) { this.hero = hero; }
	public void setCombat(Combat combat) { this.combat = combat; }
	public void setMonster(Monster monster) { this.monster = monster; }

	public Turn makeMove(String direction, Turn turn) throws LabyrinthException
	{
		int x = turn.getCoords().x;
		int y = turn.getCoords().y;
		map = (map == null) ? new Map() : map;
		Tile t = null;

		map = map.load(turn.getGameId(), turn.getMapId()).get(0);
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
		
		Tile nextTile = map.getTileByCoords(new Point(x, y), turn.getUserId());
		if(nextTile.hasMonster())
		{
			turn.setInCombat(true);
			
			combat = (combat == null) ? new Combat() : combat;
			combat.setUserId(turn.getUserId());
			
			// get the heroId
			hero = (hero == null) ? new Hero() : hero;
			int heroId = hero.getHeroId(turn.getGameId());
			combat.setHeroId(heroId);
			
			combat.setTurnId(turn.getId());
			
			// create the monster
			monster = (monster == null) ? new Monster() : monster;
			monster.setTileId(nextTile.getId());
			monster.save();

			combat.setMonsterId(monster.getId());
			combat.save();
			if(turn.isInCombat())
			{
				turn.setCombatId(combat.getId());
			}
		}

		turn.setCoords(new Point(x, y));
		turn.setIteration(turn.getIteration() + 1);

		return turn;
	}
}
