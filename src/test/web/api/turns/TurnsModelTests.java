package test.web.api.turns;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.database.DatabaseHelper;
import com.parents.LabyrinthException;
import com.web.api.turn.Turn;

import test.parents.LabyrinthJUnitTest;

public class TurnsModelTests extends LabyrinthJUnitTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private Turn turn = new Turn();
	private DatabaseHelper dbh;
	private ResultSet results;
	Timestamp timestamp;

	@Before
	public void setup() throws SQLException
	{
		dbh = mock(DatabaseHelper.class);
		results = mock(ResultSet.class);
		timestamp = mock(Timestamp.class);
		
		when(results.getTimestamp(anyString())).thenReturn(timestamp);
		when(timestamp.getTime()).thenReturn(1337L, 7331L);

		turn.setDbh(dbh);
	}
	
	@Test
	/**
	 * Verify we can load a Turn by userId and turnId
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsLoadByUserAndTurn() throws LabyrinthException, SQLException
	{
		int userId = 1;
		int turnId = 4;
		String sql = Turn.LOAD_SQL + "AND user_id = ? AND id = ? ";
		ArrayList<Object> params = new ArrayList<>();
		params.add(userId);
		params.add(turnId);
		
		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false);
		when(results.getInt(anyString())).thenReturn(8);
		
		Turn t = turn.loadByUserAndTurn(userId, turnId);
		
		assertTrue("Expected the method to return a non-null value", t != null);
	}

	@Test
	/**
	 * Verify an exception is thrown if we call loadByUserAndTurn() with no userId
	 * 
	 * @throws LabyrinthException
	 */
	public void testTurnsLoadByUserAndTurnUserIdZero() throws LabyrinthException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("turn.no_user_id"));
		
		int userId = 0;
		int turnId = 3;
		
		turn.loadByUserAndTurn(userId, turnId);
	}

	@Test
	/**
	 * Verify an exception is thrown if we call loadByUserAndTurn() with no turnId
	 * 
	 * @throws LabyrinthException
	 */
	public void testTurnsLoadByUserAndTurnTurnIdZero() throws LabyrinthException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("turn.no_turn_id"));
		
		int userId = 3;
		int turnId = 0;
		
		turn.loadByUserAndTurn(userId, turnId);
	}
	
	@Test
	/**
	 * Verify we can load a Turn by userId and gameId
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsLoadByUserAndGame() throws LabyrinthException, SQLException
	{
		int userId = 7;
		int gameId = 3;
		String sql = Turn.LOAD_SQL + "AND user_id = ? AND game_id = ? ";
		ArrayList<Object> params = new ArrayList<>();
		params.add(userId);
		params.add(gameId);
		
		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false);
		when(results.getInt(anyString())).thenReturn(12);
		
		Turn t = turn.loadByUserAndGame(userId, gameId);
		
		assertTrue("Expected the method to return a non-null value", t != null);
	}
	
	@Test
	/**
	 * Verify an exception is thrown if we call loadByUserAndGame() with no userId
	 * 
	 * @throws LabyrinthException
	 */
	public void testTurnsLoadByUserAndGameUserIdZero() throws LabyrinthException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("turn.no_user_id"));
		
		int userId = 0;
		int gameId = 4;
		
		turn.loadByUserAndGame(userId, gameId);
	}

	@Test
	/**
	 * Verify an exception is thrown if we call loadByUserAndGame() with no gameId
	 * 
	 * @throws LabyrinthException
	 */
	public void testTurnsLoadByUserAndGameGameIdZero() throws LabyrinthException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("turn.no_game_id"));
		
		int userId = 4;
		int gameId = 0;
		
		turn.loadByUserAndGame(userId, gameId);
	}

	@Test
	/**
	 * Verify we can load a Turn
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsLoad() throws LabyrinthException, SQLException
	{
		int userId = 1;
		int gameId = 2;
		int turnId = 3;
		String sql = Turn.LOAD_SQL + "AND user_id = ? AND game_id = ? AND id = ? ";
		ArrayList<Object> params = new ArrayList<>();
		params.add(userId);
		params.add(gameId);
		params.add(turnId);
		
		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false);
		// these are mocked individually so we can make sure each
		// field is set correctly
		when(results.getInt("id")).thenReturn(turnId);
		when(results.getInt("user_id")).thenReturn(userId);
		when(results.getInt("game_id")).thenReturn(gameId);
		when(results.getInt("iteration")).thenReturn(1);
		when(results.getInt("map_id")).thenReturn(2);
		when(results.getInt("x")).thenReturn(3);
		when(results.getInt("y")).thenReturn(4);
		when(results.getInt("in_combat")).thenReturn(0);
		when(results.getInt("combat_id")).thenReturn(6);

		Turn t = turn.load(userId, gameId, turnId);

		assertTrue("Expected Iteration to be 1, but got " + t.getId(), t.getId() == turnId);
		assertTrue("Expected ID to be " + userId + ", but got " + t.getUserId(), t.getUserId() == userId);
		assertTrue("Expected GameId to be " + gameId + ", but got " + t.getGameId(), t.getGameId() == gameId);
		assertTrue("Expected Iteration to be 1, but got " + t.getIteration(), t.getIteration() == 1);
		assertTrue("Expected MapId to be 2, but got " + t.getMapId(), t.getMapId() == 2);
		assertTrue("Expected X-Coord to be 3, but got " + t.getCoords().getX(), t.getCoords().getX() == 3);
		assertTrue("Expected Y-Coord to be 4, but got " + t.getCoords().getY(), t.getCoords().getY() == 4);
		assertFalse("Expected IsInCombat to be false", t.isInCombat());
		assertTrue("Expected CombatId to be 6, but got " + t.getCombatId(), t.getCombatId() == 6);
	}
	
	@Test
	/**
	 * Verify an exception is thrown when load() is called with a userId
	 * less than or equal to zero
	 * 
	 * @throws LabyrinthException
	 */
	public void testTurnsLoadUserIdZero() throws LabyrinthException
	{
		int userId = 0;
		String errorMessage = messages.getMessage("turn.no_user_id");
		
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(errorMessage);
		
		turn.load(userId, 1, 1);
	}
	
	@Test
	/**
	 * Verify an exception is thrown when the database helper throws a SQLException
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsLoadDbhExecuteThrowsException() throws LabyrinthException, SQLException
	{
		int userId = 9;
		String errorMessage = messages.getMessage("unknown.horribly_wrong");
		String sql = Turn.LOAD_SQL + "AND user_id = ? ";
		ArrayList<Object> params = new ArrayList<>();
		params.add(userId);

		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(errorMessage);

		when(dbh.executeQuery(sql, params)).thenThrow(new SQLException());
		
		turn.load(userId, 0, 0);
	}

	@Test
	/**
	 * Verify a labyrinth exception is thrown when the next() method throws a sql exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsLoadNextThrowsException() throws LabyrinthException, SQLException
	{
		int userId = 9;
		String errorMessage = messages.getMessage("unknown.horribly_wrong");
		String sql = Turn.LOAD_SQL + "AND user_id = ? ";
		ArrayList<Object> params = new ArrayList<>();
		params.add(userId);

		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(errorMessage);

		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenThrow(new SQLException());
		
		turn.load(userId, 0, 0);
	}

	@Test
	/**
	 * Verify a labyrinth exception is thrown when the getInt() method throws a sql exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsLoadGetIntThrowsException() throws LabyrinthException, SQLException
	{
		int userId = 9;
		String errorMessage = messages.getMessage("unknown.horribly_wrong");
		String sql = Turn.LOAD_SQL + "AND user_id = ? ";
		ArrayList<Object> params = new ArrayList<>();
		params.add(userId);

		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(errorMessage);

		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenReturn(true);
		when(results.getInt(anyString())).thenThrow(new SQLException());
		
		turn.load(userId, 0, 0);
	}
	
	/**
	 * Perform the setup for the save() tests
	 * 
	 * @param t - a Turn object with the dbh assigned and the isInCombat flag set
	 * @return
	 */
	private ArrayList<Object> setupForSaveTests(Turn t)
	{
		int userId = 1;
		int gameId = 2;
		int mapId = 3;
		int x = 4;
		int y = 5;
		ArrayList<Object> params = new ArrayList<>();
		
		t.setUserId(userId);
		t.setGameId(gameId);
		t.setMapId(mapId);
		t.setCoords(new Point(x, y));
		
		params.add(t.getUserId());
		params.add(t.getGameId());
		params.add(t.getMapId());
		params.add(t.getCoords().x);
		params.add(t.getCoords().y);
		params.add(t.isInCombat() ? 1 : 0);
		
		return params;
	}
	
	@Test
	/**
	 * Verify we can save a Turn
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsSave() throws LabyrinthException, SQLException
	{
		Turn t = new Turn();
		t.setInCombat(true);
		t.setDbh(dbh);
		int turnId = 55;
		ArrayList<Object> params = setupForSaveTests(t);

		when(dbh.executeAndReturnKeys(Turn.SAVE_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false);
		when(results.getInt(1)).thenReturn(turnId);

		boolean success = t.save();
		
		assertTrue("Expected save() to return true", success);
		assertEquals("Expected turnId to be 55 but was " + t.getId() + "\n", turnId, t.getId().intValue());
	}
	
	@Test
	/**
	 * Verify a labyrinth exception is thrown when the the dbh throws a sql exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsSaveDbhExecuteThrowsException() throws LabyrinthException, SQLException
	{
		String errorMessage = messages.getMessage("unknown.horribly_wrong");
		
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(errorMessage);

		Turn t = new Turn();
		t.setInCombat(false);
		t.setDbh(dbh);
		ArrayList<Object> params = setupForSaveTests(t);

		when(dbh.executeAndReturnKeys(Turn.SAVE_SQL, params)).thenThrow(new SQLException());
		
		t.save();
	}
	
	@Test
	/**
	 * Verify an exception is thrown when no results are returned from the database
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsSaveNoResultsFromDbh() throws LabyrinthException, SQLException
	{
		String errorMessage = messages.getMessage("unknown.no_id_returned");
		Turn t = new Turn();
		t.setInCombat(false);
		t.setDbh(dbh);
		ArrayList<Object> params = setupForSaveTests(t);
		
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(errorMessage);

		when(dbh.executeAndReturnKeys(Turn.SAVE_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(false);
		
		t.save();
	}
	
	@Test
	/**
	 * Verify a labyrinth exception is thrown when the result set method
	 * next() throws a sql exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsSaveNextThrowsException() throws LabyrinthException, SQLException
	{
		String errorMessage = messages.getMessage("unknown.horribly_wrong");
		Turn t = new Turn();
		t.setInCombat(false);
		t.setDbh(dbh);
		ArrayList<Object> params = setupForSaveTests(t);
		
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(errorMessage);

		when(dbh.executeAndReturnKeys(Turn.SAVE_SQL, params)).thenReturn(results);
		when(results.next()).thenThrow(new SQLException());
		
		t.save();
	}
	
	@Test
	/**
	 * Verify a labyrinth exception is thrown when the getInt() method of the result set
	 * throws a sql exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsSaveGetIntThrowsException() throws LabyrinthException, SQLException
	{
		String errorMessage = messages.getMessage("unknown.horribly_wrong");
		Turn t = new Turn();
		t.setInCombat(false);
		t.setDbh(dbh);
		ArrayList<Object> params = setupForSaveTests(t);
		
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(errorMessage);

		when(dbh.executeAndReturnKeys(Turn.SAVE_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(true);
		when(results.getInt(1)).thenThrow(new SQLException());
		
		t.save();
	}
	
	@Test
	/**
	 * Verify we can delete a Turn and that execute is called once
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsDelete() throws LabyrinthException, SQLException
	{
		int userId = 3;
		ArrayList<Object> params = new ArrayList<>();
		params.add(userId);
		
		when(dbh.execute(Turn.DELETE_SQL, params)).thenReturn(true);
		
		turn.delete(userId);
		
		verify(dbh, times(1)).execute(Turn.DELETE_SQL, params);
	}
	
	@Test
	/**
	 * Verify a LabyrinthException is thrown when delete() is called
	 * with no userId
	 * 
	 * @throws LabyrinthException
	 */
	public void testTurnsDeleteNoUserId() throws LabyrinthException
	{
		int userId = 0;
		
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("turn.no_user_id"));
		
		turn.delete(userId);
	}
	
	@Test
	/**
	 * Verify a LabyrinthException is thrown when the dbh method execute() throws
	 * a SQLException
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsDeleteDbhExecuteThrowsException() throws LabyrinthException, SQLException
	{
		int userId = 3;
		ArrayList<Object> params = new ArrayList<>();
		params.add(userId);
		
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));
		
		when(dbh.execute(Turn.DELETE_SQL, params)).thenThrow(new SQLException());
		
		turn.delete(userId);
	}
	
	/**
	 * Helper method to prepare tests for the update() method.
	 * 
	 * @param t - A Turn with the dbh assigned and the isInCombat flag set
	 * @return
	 */
	private ArrayList<Object> setupForUpdateTests(Turn t)
	{
		int turnId = 99;
		int mapId = 1;
		int x = 2;
		int y = 3;
		int iteration = 4;
		int combatId = 6;
		ArrayList<Object> params = new ArrayList<>();
		
		t.setId(turnId);
		t.setMapId(mapId);
		t.setCoords(new Point(x, y));
		t.setIteration(iteration);
		t.setCombatId(combatId);
		
		params.add(t.getMapId());
		params.add(t.getCoords().x);
		params.add(t.getCoords().y);
		params.add(t.getIteration());
		
		return params;
	}
	
	@Test
	/**
	 * Verify we can update a Turn and the return value is true
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsUpdate() throws LabyrinthException, SQLException
	{
		Turn t = new Turn();
		t.setDbh(dbh);
		t.setInCombat(true);
		ArrayList<Object> params = setupForUpdateTests(t);
		params.add(t.isInCombat() ? 1 : 0);
		params.add(t.getCombatId());
		params.add(t.getId());
		
		when(dbh.execute(Turn.UPDATE_IN_COMBAT_SQL, params)).thenReturn(true);
		
		boolean success = t.update();
		
		assertTrue(success);
	}
	
	@Test
	/**
	 * Verify that when the dbh execute() method throws a sql exception the update()
	 * method throws a labyrinth exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsUpdateDbhThrowsException() throws LabyrinthException, SQLException
	{
		String errorMessage = messages.getMessage("unknown.horribly_wrong");
		Turn t = new Turn();
		t.setDbh(dbh);
		t.setInCombat(false);
		ArrayList<Object> params = setupForUpdateTests(t);
		params.add(t.isInCombat() ? 1 : 0);
		params.add(t.getId());
		
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(errorMessage);

		when(dbh.execute(Turn.UPDATE_NOT_IN_COMBAT_SQL, params)).thenThrow(new SQLException());
		
		t.update();
	}
	
	@Test
	/**
	 * Verify that when the dbh returns a false result, the update() method also returns false
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTurnsUpdateDbhReturnsFalse() throws LabyrinthException, SQLException
	{
		Turn t = new Turn();
		t.setDbh(dbh);
		t.setInCombat(true);
		ArrayList<Object> params = setupForUpdateTests(t);
		params.add(t.isInCombat() ? 1 : 0);
		params.add(t.getCombatId());
		params.add(t.getId());
		
		when(dbh.execute(Turn.UPDATE_IN_COMBAT_SQL, params)).thenReturn(false);
		
		boolean success = t.update();
		
		assertFalse(success);
	}
}