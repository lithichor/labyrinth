package test.tests.monsters;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import com.web.api.monster.Monster;

import test.parents.LabyrinthJUnitTest;

public class MonsterModelTests extends LabyrinthJUnitTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Monster monster;
	private DatabaseHelper dbh;
	private ResultSet results;
	ArrayList<Object> params;

	@Before
	public void setup()
	{
		monster = new Monster();
		monster.setId(1);

		dbh = mock(DatabaseHelper.class);
		results = mock(ResultSet.class);

		monster.setDbh(dbh);
	}

	private ArrayList<Object> getParamsForSave()
	{
		ArrayList<Object> params = new ArrayList<>();
		params.add(monster.getTileId());
		params.add(monster.getHealth());
		params.add(monster.getAttack());
		params.add(monster.getDefense());
		return params;
	}

	@Test
	/**
	 * Verify the save method is successful and assigns the given
	 * ID to the monster
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testMonsterSave() throws LabyrinthException, SQLException
	{
		params = getParamsForSave();
		when(dbh.executeAndReturnKeys(Monster.SAVE_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false);
		when(results.getInt(1)).thenReturn(2);

		boolean success = monster.save();

		assertTrue("Save() should have returned true, but it didn't", success);
		assertTrue("Expected monsterId to be 2, but was " + monster.getId(), monster.getId() == 2);
	}

	@Test
	/**
	 * Verify a LabyrinthException is thrown when the dbh throws a SQLException
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testMonsterSaveThrowsSqlException() throws LabyrinthException, SQLException
	{
		params = getParamsForSave();

		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));

		when(dbh.executeAndReturnKeys(Monster.SAVE_SQL, params)).thenThrow(new SQLException());

		monster.save();
	}

	@Test
	/**
	 * Verify the save method returns false and the ID remains
	 * unchanged when no results are returned from the database
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testMonsterSaveNoIdsFound() throws LabyrinthException, SQLException
	{
		params = getParamsForSave();

		when(dbh.executeAndReturnKeys(Monster.SAVE_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(false);

		boolean success = monster.save();

		assertFalse("Save() should have returned false, but it didn't", success);
		assertTrue("Expected monsterId to be 1, but was " + monster.getId(), monster.getId() == 1);
	}

	@Test
	/**
	 * Verify we can load a monster by userID and tileID
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testMonsterLoadByUserAndTile() throws LabyrinthException, SQLException
	{
		int userId = 33;
		int tileId = 22;
		Timestamp timestamp = mock(Timestamp.class); 
		params = new ArrayList<>();
		params.add(userId);
		params.add(tileId);

		when(dbh.executeQuery(Monster.LOAD_BY_USER_AND_TILE_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false);
		when(results.getInt(anyString())).thenReturn(5);
		when(results.getTimestamp(anyString())).thenReturn(timestamp);
		when(timestamp.getTime()).thenReturn(1337L, 7331L);

		ArrayList<Monster> monsters = monster.loadMonstersByUserAndTile(userId, tileId);

		assertTrue("Expected 1 monster returned, but got " + monsters.size(), monsters.size() == 1);
		assertTrue("Expected monsterId to be 5, but was " + monsters.get(0).getId(),
				monsters.get(0).getId() == 5);
	}

	@Test
	/**
	 * Verify we can load a monster by userId and monsterId
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testMonsterLoadByUserAndMonster() throws LabyrinthException, SQLException
	{
		int userId = 33;
		int monsterId = 22;
		Timestamp timestamp = mock(Timestamp.class); 
		params = new ArrayList<>();
		params.add(userId);
		params.add(monsterId);

		when(dbh.executeQuery(Monster.LOAD_BY_USER_AND_MONSTER_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false);
		// have to put specific condition after general so that monsterId overrides 5
		// if the next two lines are reversed, the test fails because the id is 5
		when(results.getInt(anyString())).thenReturn(5);
		when(results.getInt("m.id")).thenReturn(monsterId);
		when(results.getTimestamp(anyString())).thenReturn(timestamp);
		when(timestamp.getTime()).thenReturn(1337L, 7331L);

		ArrayList<Monster> monsters = monster.loadMonstersByUserAndMonster(userId, monsterId);

		assertTrue("Expected 1 monster returned, but got " + monsters.size(), monsters.size() == 1);
		assertTrue("Expected attack to be 5, but was " + monster.getAttack(),
				monsters.get(0).getAttack() == 5);
		assertTrue("Expected defense to be 5, but was " + monster.getDefense(),
				monsters.get(0).getDefense() == 5);
		assertTrue("Expected health to be 5, but was " + monster.getHealth(),
				monsters.get(0).getHealth() == 5);
		assertTrue("Expected tileId to be 5, but was " + monster.getTileId(),
				monsters.get(0).getTileId() == 5);
		assertTrue("Expected monsterId to be " + monsterId + ", but was " + monsters.get(0).getId(),
				monsters.get(0).getId() == monsterId);
	}

	@Test
	/**
	 * Verify we can load a monster by userId
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testMonsterLoadByUser() throws LabyrinthException, SQLException
	{
		int userId = 42;
		Timestamp timestamp = mock(Timestamp.class);
		params = new ArrayList<>();
		params.add(userId);

		when(dbh.executeQuery(Monster.LOAD_BY_USER_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false);
		// have to put specific condition after general so that monsterId overrides 5
		// if the next two lines are reversed, the test fails because the id is 5
		when(results.getInt(anyString())).thenReturn(5);
		when(results.getTimestamp(anyString())).thenReturn(timestamp);
		when(timestamp.getTime()).thenReturn(1337L, 7331L);

		ArrayList<Monster> monsters = monster.loadMonstersByUser(userId, 0, 0);

		assertTrue("Expected 1 monster returned, but got " + monsters.size(), monsters.size() == 1);
		assertTrue("Expected monsterId to be 5, but was " + monsters.get(0).getId(),
				monsters.get(0).getId() == 5);
	}

	@Test
	/**
	 * Verify a LabyrinthException is thrown if the dbh throws a SQLException
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testMonsterLoadByUserDbhThrowsException() throws LabyrinthException, SQLException
	{
		String errorMessage = messages.getMessage("unknown.horribly_wrong");
		int userId = 42;
		params = new ArrayList<>();
		params.add(userId);

		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(errorMessage);

		when(dbh.executeQuery(Monster.LOAD_BY_USER_SQL, params)).thenThrow(new SQLException());

		monster.loadMonstersByUser(userId, 0, 0);
	}

	@Test
	/**
	 * Verify that we get an empty arraylist when the result set is empty
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testMonsterLoadbyUserResultsAreEmpty() throws LabyrinthException, SQLException
	{
		int userId = 42;
		params = new ArrayList<>();
		params.add(userId);

		when(dbh.executeQuery(Monster.LOAD_BY_USER_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(false);

		ArrayList<Monster> monsters = monster.loadMonstersByUser(userId, 0, 0);

		assertTrue("Expected 1 monster returned, but got " + monsters.size(), monsters.size() == 0);
	}

	@Test
	/**
	 * Verify we get a true value returned when calling the delete method
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testMonsterDelete() throws LabyrinthException, SQLException
	{
		ArrayList<Integer> tiles = new ArrayList<>();
		tiles.add(4);
		tiles.add(5);
		params = new ArrayList<Object>();
		params.addAll(tiles);

		when(dbh.execute(Monster.DELETE_SQL_PARTIAL + "?, ?)", params)).thenReturn(true);

		boolean success = monster.delete(tiles);

		assertTrue("Expected delete method to return true, but it didn't", success);
	}

	@Test
	/**
	 * Verify a LabyrinthException is thrown when the database helper throws
	 * a SQLException
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testMonsterDeleteDbhThrowsException() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));
		
		ArrayList<Integer> tiles = new ArrayList<>();
		tiles.add(4);

		params = new ArrayList<Object>();
		params.addAll(tiles);

		when(dbh.execute(Monster.DELETE_SQL_PARTIAL + "?)", params)).thenThrow(new SQLException());

		monster.delete(tiles);
	}
	
	@Test
	/**
	 * Verify a false value is returned when the argument to the delete
	 * method is null
	 * 
	 * @throws LabyrinthException
	 */
	public void testMonsterDeleteNullArgument() throws LabyrinthException
	{
		boolean success = monster.delete(null);
		
		assertTrue(!success);
	}
}
