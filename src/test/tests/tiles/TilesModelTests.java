package test.tests.tiles;

import static org.junit.Assert.assertEquals;
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
import com.web.api.tile.Tile;

import test.parents.LabyrinthJUnitTest;

public class TilesModelTests extends LabyrinthJUnitTest
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	Tile tile;
	private DatabaseHelper dbh;
	private ResultSet results;
	private Timestamp timestamp;
	private Monster monster;
	
	@Before
	public void setup() throws SQLException, LabyrinthException
	{
		int mapId = 4;
		int x = 0;
		int y = 0;
		
		dbh = mock(DatabaseHelper.class);
		results = mock(ResultSet.class);
		timestamp = mock(Timestamp.class);
		monster = mock(Monster.class);

		when(results.getTimestamp(anyString())).thenReturn(timestamp);
		when(timestamp.getTime()).thenReturn(1337L, 7331L);
		when(monster.delete(any())).thenReturn(true);
		
		tile = new Tile(x, y, mapId);
		tile.setDbh(dbh);
		tile.setMonster(monster);
	}
	
	private ArrayList<Object> getParamsForSave()
	{
		ArrayList<Object> params = new ArrayList<>();
		params.add(tile.getCoords().x);
		params.add(tile.getCoords().y);
		params.add(tile.getHasMonsterInt());
		params.add(tile.getWasVisitedInt());
		params.add(tile.getMapId());
		params.add(tile.getNorth());
		params.add(tile.getSouth());
		params.add(tile.getEast());
		params.add(tile.getWest());
		
		return params;
	}
	
	@Test
	/**
	 * Verify we can save a Tile. Check the return value and the tileId
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesSave() throws LabyrinthException, SQLException
	{
		int tileId = 37;
		ArrayList<Object> params = getParamsForSave();
		
		when(dbh.executeAndReturnKeys(Tile.SAVE_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false);
		when(results.getInt(1)).thenReturn(tileId);
		
		boolean success = tile.save();
		
		assertTrue("Expected save() to return true", success);
		assertEquals("Expected the tile ID to be " + tileId + " but instead it was " + tile.getId(),
				tileId, tile.getId().intValue());
	}
	
	@Test
	public void testTilesSaveTileIdZero() throws LabyrinthException, SQLException
	{
		int tileId = 0;
		ArrayList<Object> params = getParamsForSave();
		
		when(dbh.executeAndReturnKeys(Tile.SAVE_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false);
		when(results.getInt(1)).thenReturn(tileId);
		
		boolean success = tile.save();
		
		assertFalse("Expected save() to return true", success);
	}
	
	@Test
	/**
	 * Verify that when the dbh throws a SQLException we get a LabyrinthException
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesSaveDbhThrowsException() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));
		ArrayList<Object> params = getParamsForSave();
		
		when(dbh.executeAndReturnKeys(Tile.SAVE_SQL, params)).thenThrow(new SQLException());
		
		tile.save();
	}
	
	@Test
	/**
	 * Verify that when the next() method throws a SQLException we get a LabyrinthException
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesSaveNextThrowsException() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));
		ArrayList<Object> params = getParamsForSave();
		
		when(dbh.executeAndReturnKeys(Tile.SAVE_SQL, params)).thenReturn(results);
		when(results.next()).thenThrow(new SQLException());

		tile.save();
	}
	
	@Test
	/**
	 * Verify that when the getInt() method throws a SQLException we get a LabyrinthException
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesSaveGetIntThrowsException() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));
		ArrayList<Object> params = getParamsForSave();
		
		when(dbh.executeAndReturnKeys(Tile.SAVE_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(true);
		when(results.getInt(1)).thenThrow(new SQLException());

		tile.save();
	}
	
	@Test
	/**
	 * Verify we can load a Tile; the size of the list should be one and
	 * the ID of the tile in the list should match what was given
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesLoad() throws LabyrinthException, SQLException
	{
		ArrayList<Object> params = new ArrayList<>();
		String sql = Tile.LOAD_SQL + "\n\tuser_id = ?\n\tAND t.deleted_at IS NULL "
				+ "AND map_id = ? AND t.id = ? ";
		int mapId = 1;
		int tileId = 2;
		int userId = 4;
		int x = 0;
		int y = 0;
		
		params.add(userId);
		params.add(mapId);
		params.add(tileId);
		
		String north = "wall";
		String south = "DOOR";
		String east = "opeingin";
		String west = "opening";
		
		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false);
		
		when(results.getInt("x")).thenReturn(x);
		when(results.getInt("y")).thenReturn(y);
		when(results.getInt("map_id")).thenReturn(mapId);
		when(results.getInt("id")).thenReturn(tileId);
		
		when(results.getString("north")).thenReturn(north);
		when(results.getString("south")).thenReturn(south);
		when(results.getString("east")).thenReturn(east);
		when(results.getString("west")).thenReturn(west);
		
		when(results.getInt("has_monster")).thenReturn(1);
		when(results.getInt("visited")).thenReturn(1);
		
		ArrayList<Tile> tiles = tile.load(mapId, tileId, userId);
		
		assertTrue(tiles.size() == 1);
		assertEquals(tileId, tiles.get(0).getId().intValue());
	}

	@Test
	/**
	 * Verify we can load a Tile with userId and mapId
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testLoadTileWithUserAndMap() throws LabyrinthException, SQLException
	{
		ArrayList<Object> params = new ArrayList<>();
		String sql = Tile.LOAD_SQL + "\n\tuser_id = ?\n\tAND t.deleted_at IS NULL "
				+ "AND map_id = ? ";
		int mapId = 2;
		int tileId = 1;
		int userId = 4;
		
		params.add(userId);
		params.add(mapId);
		
		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false);
		
		when(results.getInt(any())).thenReturn(1);
		when(results.getString(any())).thenReturn("OPENING");
		
		ArrayList<Tile> tiles = tile.load(mapId, 0, userId);
		
		assertTrue(tiles.size() == 1);
		assertEquals(tileId, tiles.get(0).getId().intValue());
	}

	@Test
	/**
	 * Verify that when no tiles are returned from the db when using
	 * a mapId, we get a labyrinth exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesLoadNoTilesFoundForMapId() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("tile.no_tiles_for_map"));
		
		ArrayList<Object> params = new ArrayList<>();
		String sql = Tile.LOAD_SQL + "\n\tuser_id = ?\n\tAND t.deleted_at IS NULL "
				+ "AND map_id = ? ";
		int mapId = 2;
		int userId = 4;
		
		params.add(userId);
		params.add(mapId);
		
		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenReturn(false);
		
		tile.load(mapId, 0, userId);
	}
	
	@Test
	/**
	 * Verify we can load a Tile with userId and tileId
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testLoadTileWithUserAndTile() throws LabyrinthException, SQLException
	{
		ArrayList<Object> params = new ArrayList<>();
		String sql = Tile.LOAD_SQL + "\n\tuser_id = ?\n\tAND t.deleted_at IS NULL "
				+ "AND t.id = ? ";
		int tileId = 1;
		int userId = 4;
		
		params.add(userId);
		params.add(tileId);
		
		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false);
		
		when(results.getInt(any())).thenReturn(1);
		when(results.getString(any())).thenReturn("OPENING");
		
		ArrayList<Tile> tiles = tile.load(0, tileId, userId);
		
		assertTrue(tiles.size() == 1);
		assertEquals(tileId, tiles.get(0).getId().intValue());
	}
	
	@Test
	/**
	 * Verify that when no tiles are returned from the db when using
	 * a tileId, we get a labyrinth exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesLoadNoTilesFoundForTileId() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("tile.no_tiles_found"));
		
		ArrayList<Object> params = new ArrayList<>();
		String sql = Tile.LOAD_SQL + "\n\tuser_id = ?\n\tAND t.deleted_at IS NULL "
				+ "AND t.id = ? ";
		int tileId = 1;
		int userId = 4;
		
		params.add(userId);
		params.add(tileId);
		
		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenReturn(false);
		
		tile.load(0, tileId, userId);
	}
	
	@Test
	public void testLoadTileMapIdAndTileIdZero() throws LabyrinthException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("tile.no_ids"));
		
		tile.load(0, 0, 4);
	}
	
	@Test
	/**
	 * Verify we get a labyrinth exception if the userId is not a positive integer
	 * 
	 * @throws LabyrinthException
	 */
	public void testTilesLoadUserIdZero() throws LabyrinthException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("tile.no_user_id"));
		
		tile.load(7, 3, 0);
	}
	
	@Test
	/**
	 * Verify that when the dbh throws a sql exception we get a labyrinth exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesLoadDbhThrowsException() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));
		
		int userId = 2;
		int tileId = 3;
		String sql = Tile.LOAD_SQL + "\n\tuser_id = ?\n\tAND t.deleted_at IS NULL AND t.id = ? ";
		ArrayList<Object> params = new ArrayList<>();
		params.add(userId);
		params.add(tileId);

		when(dbh.executeQuery(sql, params)).thenThrow(new SQLException());
		
		tile.load(0, tileId, userId);
	}
	
	@Test
	/**
	 * Verify that when next() throws a SQLException we get a LabyrinthException 
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesLoadNextThrowsException() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));
		
		int userId = 2;
		int tileId = 3;
		String sql = Tile.LOAD_SQL + "\n\tuser_id = ?\n\tAND t.deleted_at IS NULL AND t.id = ? ";
		ArrayList<Object> params = new ArrayList<>();
		params.add(userId);
		params.add(tileId);

		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenThrow(new SQLException());
		
		tile.load(0, tileId, userId);
	}

	/**
	 * Verify that when getInt() throws a SQLException we get a LabyrinthException 
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	@Test
	public void testTilesLoadGetIntThrowsException() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));
		
		int userId = 2;
		int tileId = 3;
		String sql = Tile.LOAD_SQL + "\n\tuser_id = ?\n\tAND t.deleted_at IS NULL AND t.id = ? ";
		ArrayList<Object> params = new ArrayList<>();
		params.add(userId);
		params.add(tileId);

		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenReturn(true);
		when(results.getInt(anyString())).thenThrow(new SQLException());
		
		tile.load(0, tileId, userId);
	}
	
	@Test
	/**
	 * Verify we can delete a Tile. Check that the three DBH method calls are
	 * performed, and the Monster is deleted at the end.
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesDelete() throws LabyrinthException, SQLException
	{
		int gameId = 3;
		int mapId1 = 2;
		int mapId2 = 3;
		
		// this covers all the queries
		when(results.next()).thenReturn(true, true, false, true, false);
		when(results.getInt("id")).thenReturn(mapId1, mapId2, 5);
		
		// for getting the mapIds
		ArrayList<Object> params = new ArrayList<>();
		params.add(gameId);
		when(dbh.executeQuery(Tile.GET_MAPS_TO_DELETE_SQL, params)).thenReturn(results);
		
		// for deleting tiles
		ArrayList<Object> mapIdParams = new ArrayList<>();
		mapIdParams.add(mapId1);
		mapIdParams.add(mapId2);
		when(dbh.execute(Tile.DELETE_TILES_SQL + "?, ?)", mapIdParams)).thenReturn(true);
		
		// for getting tileIds
		when(dbh.executeQuery(Tile.GET_TILE_IDS + "?, ?)", mapIdParams)).thenReturn(results);
		
		tile.deleteTiles(gameId);
		
		// make sure we call all the dbh methods; the first is called twice
		verify(dbh, times(2)).executeQuery(anyString(), any());
		verify(dbh).execute(Tile.DELETE_TILES_SQL + "?, ?)", mapIdParams);
		// this is the last call in the delete() method - make sure we got to it
		verify(monster).delete(any());
	}
	
	@Test
	/**
	 * Verify that when no gameId is provided we get a LabyrinthException
	 * 
	 * @throws LabyrinthException
	 */
	public void testTilesDeleteNoGameId() throws LabyrinthException
	{
		int gameId = 0;
		String errorMessage = messages.getMessage("tile.no_game_id");
		
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(errorMessage);
		
		tile.deleteTiles(gameId);
	}
	
	@Test
	/**
	 * Verify we return when no mapIds found. Check that the executeQuery method
	 * is called once and that execute and monster.delete() are never called
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesDeleteNoMapIdsFound() throws LabyrinthException, SQLException
	{
		int gameId = 3;
		ArrayList<Object> params = new ArrayList<>();
		params.add(gameId);
		
		when(dbh.executeQuery(Tile.GET_MAPS_TO_DELETE_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(false);
		
		tile.deleteTiles(gameId);
		
		verify(dbh, times(1)).executeQuery(anyString(), any());
		verify(dbh, never()).execute(anyString(), any());
		verify(monster, never()).delete(any());
	}
	
	@Test
	/**
	 * Verify a LabyrinthException is thrown when no Tiles are found
	 * for a given set of mapIds
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesDeleteNoTileIdsReturned() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("tile.no_tiles_for_map"));
		
		int gameId = 3;
		int mapId = 9;
		ArrayList<Object> params = new ArrayList<>();
		params.add(gameId);
		
		when(dbh.executeQuery(Tile.GET_MAPS_TO_DELETE_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false, false);
		
		// for deleting tiles
		ArrayList<Object> mapIdParams = new ArrayList<>();
		mapIdParams.add(mapId);
		when(dbh.execute(Tile.DELETE_TILES_SQL + "?)", mapIdParams)).thenReturn(true);
		when(results.getInt("id")).thenReturn(mapId);
		
		// for getting tileIds
		when(dbh.executeQuery(Tile.GET_TILE_IDS + "?)", mapIdParams)).thenReturn(results);
		
		tile.deleteTiles(gameId);
	}
	
	@Test
	/**
	 * Verify that when the first call to the dbh throws a sql exception we get a
	 * labyrinth exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesDeleteFirstExecuteQueryThrowsException() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));
		
		int gameId = 4;
		ArrayList<Object> params = new ArrayList<>();
		params.add(gameId);
		
		when(dbh.executeQuery(Tile.GET_MAPS_TO_DELETE_SQL, params)).thenThrow(new SQLException());
		
		tile.deleteTiles(gameId);
	}

	@Test
	/**
	 * Verify that when the first call to next throws a sql exception we
	 * get a labyrinth exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesDeleteFirstNextThrowsException() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));
		
		int gameId = 1;
		ArrayList<Object> params = new ArrayList<>();
		params.add(gameId);
		
		when(dbh.executeQuery(Tile.GET_MAPS_TO_DELETE_SQL, params)).thenReturn(results);
		when(results.next()).thenThrow(new SQLException());
		
		tile.deleteTiles(gameId);
	}
	
	@Test
	/**
	 * Verify that when the first call to getInt throws a sql exception we get a
	 * labyrinth exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesDeleteFirstGetIntThrowsException() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));
		
		int gameId = 1;
		ArrayList<Object> params = new ArrayList<>();
		params.add(gameId);
		
		when(dbh.executeQuery(Tile.GET_MAPS_TO_DELETE_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false);
		when(results.getInt("id")).thenThrow(new SQLException());

		tile.deleteTiles(gameId);
	}
	
	@Test
	/**
	 * Verify that when dbh.execute() throws a sql exception we get a labyrinth exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesDeleteExecuteThrowsException() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));
		
		int gameId = 1;
		int mapId = 3;
		ArrayList<Object> params = new ArrayList<>();
		params.add(gameId);
		ArrayList<Object> mapParams = new ArrayList<>();
		mapParams.add(mapId);
		
		when(dbh.executeQuery(Tile.GET_MAPS_TO_DELETE_SQL, params)).thenReturn(results);
		when(results.next()).thenReturn(true, false);
		when(results.getInt("id")).thenReturn(mapId);
		when(dbh.execute(Tile.DELETE_TILES_SQL + "?)", mapParams)).thenThrow(new SQLException());

		tile.deleteTiles(gameId);
	}
	
	@Test
	/**
	 * Verify that when the second call to dbh.eecuteQuery() throws a sql exception we get
	 * a labyrinth exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesDeleteSecondExecuteQueryThrowsException() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));
		
		int gameId = 1;
		int mapId = 3;
		ArrayList<Object> params = new ArrayList<>();
		params.add(gameId);
		ArrayList<Object> mapParams = new ArrayList<>();
		mapParams.add(mapId);

		when(dbh.executeQuery(anyString(), any())).thenReturn(results).thenThrow(new SQLException());
		when(results.next()).thenReturn(true, false);
		when(results.getInt("id")).thenReturn(mapId);
		when(dbh.execute(Tile.DELETE_TILES_SQL + "?)", mapParams)).thenReturn(true);

		tile.deleteTiles(gameId);
	}

	@Test
	/**
	 * Verify that when the second call to next() throws a sql exception we get a
	 * labyrinth exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesDeleteSecondNextThrowsException() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));
		
		int gameId = 1;
		int mapId = 3;
		ArrayList<Object> params = new ArrayList<>();
		params.add(gameId);
		ArrayList<Object> mapParams = new ArrayList<>();
		mapParams.add(mapId);
		
		when(dbh.executeQuery(anyString(), any())).thenReturn(results);
		when(results.next()).thenReturn(true, false).thenThrow(new SQLException());
		when(results.getInt("id")).thenReturn(mapId);
		when(dbh.execute(Tile.DELETE_TILES_SQL + "?)", mapParams)).thenReturn(true);
		
		tile.deleteTiles(gameId);
	}
	
	@Test
	/**
	 * Verify that when the second call to getInt() throws a sql exception we get a
	 * labyrinth exception
	 * 
	 * @throws LabyrinthException
	 * @throws SQLException
	 */
	public void testTilesDeleteSecondGetIntThrowsException() throws LabyrinthException, SQLException
	{
		thrown.expect(LabyrinthException.class);
		thrown.expectMessage(messages.getMessage("unknown.horribly_wrong"));
		
		int gameId = 1;
		int mapId = 3;
		ArrayList<Object> params = new ArrayList<>();
		params.add(gameId);
		ArrayList<Object> mapParams = new ArrayList<>();
		mapParams.add(mapId);
		
		when(dbh.executeQuery(anyString(), any())).thenReturn(results);
		when(results.next()).thenReturn(true, false, true);
		when(results.getInt("id")).thenReturn(mapId).thenThrow(new SQLException());
		when(dbh.execute(Tile.DELETE_TILES_SQL + "?)", mapParams)).thenReturn(true);

		tile.deleteTiles(gameId);
	}
}
