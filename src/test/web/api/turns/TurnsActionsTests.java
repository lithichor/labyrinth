package test.web.api.turns;

import java.awt.Point;
import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.parents.LabyrinthException;
import com.web.api.combat.Combat;
import com.web.api.hero.Hero;
import com.web.api.maps.Map;
import com.web.api.monster.Monster;
import com.web.api.tile.Tile;
import com.web.api.tile.Tile.Boundary;
import com.web.api.turn.Turn;
import com.web.api.turn.TurnsServletActions;

import test.parents.LabyrinthJUnitTest;

public class TurnsActionsTests extends LabyrinthJUnitTest
{
	private TurnsServletActions actions;
	private Turn turn;
	private Map map;
	
	@Before
	public void setup()
	{
		actions = new TurnsServletActions();
		turn = new Turn();
		
		map = mock(Map.class);
		
		actions.setMap(map);
	}
	
	@Test
	public void testMakeMoveIterationIncremented() throws LabyrinthException
	{
		String direction = "east";
		Point pointOne = new Point(0, 0);
		Point pointTwo = new Point(1, 0);
		Tile tile = new Tile(0, 0, null);
		
		tile.setEast(Boundary.OPENING);
		tile.setHasMonster(false);
		turn.setCoords(pointOne);
		turn.setIteration(0);
		ArrayList<Map> maps = new ArrayList<>();
		maps.add(map);
		
		when(map.load(null, null)).thenReturn(maps);
		when(map.getTileByCoords(pointOne, null)).thenReturn(tile);
		when(map.getTileByCoords(pointTwo, null)).thenReturn(tile);
		
		turn = actions.makeMove(direction, turn);
		
		assertEquals(new Integer(1), turn.getIteration());
	}
	
	@Test
	public void testMakeMoveEast() throws LabyrinthException
	{
		String direction = "east";
		Point pointOne = new Point(0, 0);
		Point pointTwo = new Point(1, 0);
		Tile tile = new Tile(0, 0, null);
		
		tile.setEast(Boundary.OPENING);
		tile.setHasMonster(false);
		turn.setCoords(pointOne);
		turn.setIteration(0);
		ArrayList<Map> maps = new ArrayList<>();
		maps.add(map);
		
		when(map.load(null, null)).thenReturn(maps);
		when(map.getTileByCoords(pointOne, null)).thenReturn(tile);
		when(map.getTileByCoords(pointTwo, null)).thenReturn(tile);
		
		turn = actions.makeMove(direction, turn);
		
		assertEquals(1, turn.getCoords().x);
	}
	
	@Test
	public void testMakeMoveWest() throws LabyrinthException
	{
		String direction = "west";
		Point pointOne = new Point(0, 0);
		Point pointTwo = new Point(-1, 0);
		Tile tile = new Tile(0, 0, null);
		
		tile.setEast(Boundary.OPENING);
		tile.setHasMonster(false);
		turn.setCoords(pointOne);
		turn.setIteration(0);
		ArrayList<Map> maps = new ArrayList<>();
		maps.add(map);
		
		when(map.load(null, null)).thenReturn(maps);
		when(map.getTileByCoords(pointOne, null)).thenReturn(tile);
		when(map.getTileByCoords(pointTwo, null)).thenReturn(tile);
		
		turn = actions.makeMove(direction, turn);
		
		assertEquals(-1, turn.getCoords().x);
	}
	
	@Test
	public void testMakeMoveNorth() throws LabyrinthException
	{
		String direction = "n";
		Point pointOne = new Point(0, 0);
		Point pointTwo = new Point(0, -1);
		Tile tile = new Tile(0, 0, null);
		
		tile.setEast(Boundary.OPENING);
		tile.setHasMonster(false);
		turn.setCoords(pointOne);
		turn.setIteration(0);
		ArrayList<Map> maps = new ArrayList<>();
		maps.add(map);
		
		when(map.load(null, null)).thenReturn(maps);
		when(map.getTileByCoords(pointOne, null)).thenReturn(tile);
		when(map.getTileByCoords(pointTwo, null)).thenReturn(tile);
		
		turn = actions.makeMove(direction, turn);
		
		assertEquals(-1, turn.getCoords().y);
	}
	
	@Test
	public void testMakeMoveSouth() throws LabyrinthException
	{
		String direction = "s";
		Point pointOne = new Point(0, 0);
		Point pointTwo = new Point(0, 1);
		Tile tile = new Tile(0, 0, null);
		
		tile.setEast(Boundary.OPENING);
		tile.setHasMonster(false);
		turn.setCoords(pointOne);
		turn.setIteration(0);
		ArrayList<Map> maps = new ArrayList<>();
		maps.add(map);
		
		when(map.load(null, null)).thenReturn(maps);
		when(map.getTileByCoords(pointOne, null)).thenReturn(tile);
		when(map.getTileByCoords(pointTwo, null)).thenReturn(tile);
		
		turn = actions.makeMove(direction, turn);
		
		assertEquals(1, turn.getCoords().y);
	}
	
	@Test
	public void testMakeMoveEnterCombat() throws LabyrinthException
	{
		String direction = "s";
		Point pointOne = new Point(0, 0);
		Point pointTwo = new Point(0, 1);
		Tile tile = new Tile(0, 0, null);
		
		Combat combat = mock(Combat.class);
		Hero hero = mock(Hero.class);
		Monster monster = mock(Monster.class);
		
		actions.setCombat(combat);
		actions.setHero(hero);
		actions.setMonster(monster);
		
		when(hero.getHeroId(null)).thenReturn(1);
		when(monster.save()).thenReturn(true);
		when(combat.save()).thenReturn(true);

		tile.setEast(Boundary.OPENING);
		tile.setHasMonster(true);
		turn.setCoords(pointOne);
		turn.setIteration(0);
		ArrayList<Map> maps = new ArrayList<>();
		maps.add(map);
		
		when(map.load(null, null)).thenReturn(maps);
		when(map.getTileByCoords(pointOne, null)).thenReturn(tile);
		when(map.getTileByCoords(pointTwo, null)).thenReturn(tile);
		
		turn = actions.makeMove(direction, turn);
		
		assertTrue(turn.isInCombat());
	}
}
