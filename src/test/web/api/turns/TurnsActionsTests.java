package test.web.api.turns;

import java.awt.Point;
import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private TurnsServletActions actions;
	private Turn turn;
	private Map map;
	private Tile tile;
	private Point pointOne;
	private int userId = 1;
	private int gameId = 2;
	private int mapId = 3;
	private ArrayList<Map> maps;
	private Monster monster;
	private Hero hero;
	private Combat combat;

	@Before
	public void setup()
	{
		pointOne = new Point(0, 0);

		actions = new TurnsServletActions();
		turn = new Turn();
		maps = new ArrayList<>();
		
		map = mock(Map.class);
		tile = mock(Tile.class);
		monster = mock(Monster.class);
		hero = mock(Hero.class);
		combat = mock(Combat.class);
		
		turn.setCoords(pointOne);
		turn.setGameId(gameId);
		turn.setMapId(mapId);
		turn.setUserId(userId);
		turn.setIteration(0);

		maps.add(map);
		
		when(tile.getEast()).thenReturn(Boundary.OPENING);
		when(tile.getWest()).thenReturn(Boundary.OPENING);
		when(tile.getNorth()).thenReturn(Boundary.OPENING);
		when(tile.getSouth()).thenReturn(Boundary.OPENING);
		
		actions.setMap(map);
		actions.setMonster(monster);
		actions.setHero(hero);
		actions.setCombat(combat);
	}
	
	@Test
	/**
	 * For the next four tests, verify the makeMove method
	 * increments the Turn and changes the coordinates to the
	 * next point when there is an opening in the direction
	 * of the move
	 * 
	 * @throws LabyrinthException
	 */
	public void testMakeMoveEast() throws LabyrinthException
	{
		String direction = "east";
		Tile t = new Tile(1, 0, mapId);
		Point pointTwo = new Point(1, 0);
		
		when(map.load(gameId, mapId)).thenReturn(maps);
		when(map.getTileByCoords(pointOne, userId)).thenReturn(tile);
		when(map.getTileByCoords(pointTwo, userId)).thenReturn(t);
		
		turn = actions.makeMove(direction, turn);
		
		assertEquals(1, turn.getIteration().intValue());
		assertEquals(turn.getCoords(), pointTwo);
	}
	
	@Test
	public void testMakeMoveWest() throws LabyrinthException
	{
		String direction = "west";
		Tile t = new Tile(-1, 0, mapId);
		Point pointTwo = new Point(-1, 0);
		
		when(map.load(gameId, mapId)).thenReturn(maps);
		when(map.getTileByCoords(pointOne, userId)).thenReturn(tile);
		when(map.getTileByCoords(pointTwo, userId)).thenReturn(t);
		
		turn = actions.makeMove(direction, turn);
		
		assertEquals(1, turn.getIteration().intValue());
		assertEquals(turn.getCoords(), pointTwo);
	}
	
	@Test
	public void testMakeMoveNorth() throws LabyrinthException
	{
		String direction = "north";
		Tile t = new Tile(0, -1, mapId);
		Point pointTwo = new Point(0, -1);
		
		when(map.load(gameId, mapId)).thenReturn(maps);
		when(map.getTileByCoords(pointOne, userId)).thenReturn(tile);
		when(map.getTileByCoords(pointTwo, userId)).thenReturn(t);
		
		turn = actions.makeMove(direction, turn);
		
		assertEquals(1, turn.getIteration().intValue());
		assertEquals(turn.getCoords(), pointTwo);
	}
	
	@Test
	public void testMakeMoveSouth() throws LabyrinthException
	{
		String direction = "south";
		Tile t = new Tile(0, 1, mapId);
		Point pointTwo = new Point(0, 1);
		
		when(map.load(gameId, mapId)).thenReturn(maps);
		when(map.getTileByCoords(pointOne, userId)).thenReturn(tile);
		when(map.getTileByCoords(pointTwo, userId)).thenReturn(t);
		
		turn = actions.makeMove(direction, turn);
		
		assertEquals(1, turn.getIteration().intValue());
		assertEquals(turn.getCoords(), pointTwo);
	}
	
	@Test
	/**
	 * For the next four tests, verify the makeMove method
	 * increments the Turn and does not change the coordinates
	 * if there is not an opening in the direction of the move
	 * 
	 * @throws LabyrinthException
	 */
	public void testMakeMoveHitsWallEast() throws LabyrinthException
	{
		String direction = "e";
		
		when(map.load(gameId, mapId)).thenReturn(maps);
		when(map.getTileByCoords(pointOne, userId)).thenReturn(tile);
		when(tile.getEast()).thenReturn(Boundary.WALL);
		
		turn = actions.makeMove(direction, turn);
		
		assertEquals(1, turn.getIteration().intValue());
		assertEquals(turn.getCoords(), pointOne);
	}

	@Test
	public void testMakeMoveHitsWallWest() throws LabyrinthException
	{
		String direction = "w";
		
		when(map.load(gameId, mapId)).thenReturn(maps);
		when(map.getTileByCoords(pointOne, userId)).thenReturn(tile);
		when(tile.getWest()).thenReturn(Boundary.WALL);
		
		turn = actions.makeMove(direction, turn);
		
		assertEquals(1, turn.getIteration().intValue());
		assertEquals(turn.getCoords(), pointOne);
	}

	@Test
	public void testMakeMoveHitsWallNorth() throws LabyrinthException
	{
		String direction = "n";
		
		when(map.load(gameId, mapId)).thenReturn(maps);
		when(map.getTileByCoords(pointOne, userId)).thenReturn(tile);
		when(tile.getNorth()).thenReturn(Boundary.WALL);
		
		turn = actions.makeMove(direction, turn);
		
		assertEquals(1, turn.getIteration().intValue());
		assertEquals(turn.getCoords(), pointOne);
	}

	@Test
	public void testMakeMoveHitsWallSouth() throws LabyrinthException
	{
		String direction = "s";
		
		when(map.load(gameId, mapId)).thenReturn(maps);
		when(map.getTileByCoords(pointOne, userId)).thenReturn(tile);
		when(tile.getSouth()).thenReturn(Boundary.WALL);
		
		turn = actions.makeMove(direction, turn);
		
		assertEquals(1, turn.getIteration().intValue());
		assertEquals(turn.getCoords(), pointOne);
	}

	@Test
	/**
	 * Verify that when the new Tile has a Monster the Monster
	 * is saved with the tileId, the Turn is set to inCombat,
	 * and the Combat is saved
	 * 
	 * @throws LabyrinthException
	 */
	public void testMakeMoveTileHasMonster() throws LabyrinthException
	{
		int tileId = 33;
		String direction = "west";
		Tile t = new Tile(0, 0, mapId);
		t.setId(tileId);
		Point pointTwo = new Point(-1, 0);
		
		t.setHasMonster(true);
		
		when(map.load(gameId, mapId)).thenReturn(maps);
		when(map.getTileByCoords(pointOne, userId)).thenReturn(tile);
		when(map.getTileByCoords(pointTwo, userId)).thenReturn(t);

		turn = actions.makeMove(direction,  turn);
		
		verify(monster).setTileId(tileId);
		verify(monster).save();
		verify(combat).save();
		assertTrue(turn.isInCombat());
	}
	
	@Test
	/**
	 * Verify an invalid direction throws an exception
	 * 
	 * @throws LabyrinthException
	 */
	public void testMakeMoveInvalidDirection() throws LabyrinthException
	{
		String errorMessage = messages.getMessage("turn.invalid_data");
		String direction = "monkey";
		
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(errorMessage);
		
		when(map.load(gameId, mapId)).thenReturn(maps);
		
		turn = actions.makeMove(direction, turn);
	}
}
