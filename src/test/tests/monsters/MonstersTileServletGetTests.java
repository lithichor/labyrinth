package test.tests.monsters;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

import com.models.constants.EndpointsWithIds;
import com.models.wrappers.GsonWrapper;
import com.parents.LabyrinthException;
import com.web.api.maps.Map;
import com.web.api.monster.Monster;
import com.web.api.monster.MonstersServletActions;
import com.web.api.monster.MonstersTileServlet;
import com.web.api.tile.Tile;
import com.web.api.user.User;

import test.parents.LabyrinthHttpServletTest;

public class MonstersTileServletGetTests extends LabyrinthHttpServletTest
{
	private MonstersTileServlet servlet;
	private MonstersServletActions actions;
	private GsonWrapper gson;
	
	private User user;
	private Monster monster;
	private Map map;
	private Tile tile;
	
	@Before
	public void setup() throws IOException
	{
		servlet = new MonstersTileServlet();
		actions = mock(MonstersServletActions.class);
		gson = mock(GsonWrapper.class);
		
		user = new User();
		user.setId(1);
		monster = mock(Monster.class);
		map = new Map();
		map.setId(1337);
		tile = new Tile(4, 2, map.getId());
		tile.setId(3);
		
		servlet.setActions(actions);
		servlet.setMonster(monster);
		servlet.setGson(gson);
		
		when(monster.getId()).thenReturn(42);
		when(response.getWriter()).thenReturn(printerTwo);
	}

	@Test
	/**
	 * Verify that the doGet method executes with no errors
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * @throws LabyrinthException
	 */
	public void testMonstersTileDoGet() throws IOException, ServletException, LabyrinthException
	{
		Monster m = new Monster();
		ArrayList<Monster> monsters = new ArrayList<>();
		monsters.add(m);
		
		when(actions.authenticateUser(request)).thenReturn(user);
		when(actions.getIdFromUrl(request, EndpointsWithIds.MONSTERS_TILES)).thenReturn(tile.getId());
		when(monster.loadMonstersByUserAndTile(user.getId(), tile.getId())).thenReturn(monsters);
		
		servlet.doGet(request, response);
		
		ArrayList<String> errors = servlet.getErrors();
		
		assertTrue(errors.size() == 0);
	}
	
	@Test
	/**
	 * Verify that the server returns the given error when
	 * the authentication method throws an error
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @throws LabyrinthException
	 */
	public void testMonstersTileDoGetAuthThrowsException() throws IOException, ServletException, LabyrinthException
	{
		String errorMessage = "Exception Thrown";
		
		when(actions.authenticateUser(request)).thenThrow(new LabyrinthException(errorMessage));
		
		servlet.doGet(request, response);

		ArrayList<String> errors = servlet.getErrors();

		assertTrue("Expected one error, but found " + errors.size(), errors.size() == 1);
		assertTrue(errors.get(0).contains(errorMessage));
	}
	
	@Test
	/**
	 * Verify that when the authentication method returns a null object
	 * the appropriate error message is returned from the server
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @throws LabyrinthException
	 */
	public void testMonstersTileDoGetAuthReturnsNull() throws ServletException, IOException, LabyrinthException
	{
		String errorMessage = messages.getMessage("user.no_such_player");
		when(actions.authenticateUser(request)).thenReturn(null);

		servlet.doGet(request, response);

		ArrayList<String> errors = servlet.getErrors();

		assertTrue("Expected one error, but found " + errors.size(), errors.size() == 1);
		assertTrue(errors.get(0).contains(errorMessage));
	}
	
	@Test
	/**
	 * Verify that if there is no tileID then the appropriate error
	 * message is returned from the server
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * @throws LabyrinthException
	 */
	public void testMonstersTileNoTileId() throws IOException, ServletException, LabyrinthException
	{
		String errorMessage = messages.getMessage("monster.no_tile_id");

		when(actions.authenticateUser(request)).thenReturn(user);
		when(actions.getIdFromUrl(request, EndpointsWithIds.MONSTERS_TILES)).thenReturn(0);
		
		servlet.doGet(request, response);
		
		ArrayList<String> errors = servlet.getErrors();
		
		assertTrue("Expected one error, but got " + errors.size(), errors.size() == 1);
		assertTrue("Expected message \""+ errorMessage + "\" but got " + errors.get(0),
				errors.get(0).contains(errorMessage));
	}

	@Test
	/**
	 * Verify that when the load monsters method throws an exception the
	 * server returns an unknown error
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @throws LabyrinthException
	 */
	public void testMonstersTileDoGetLoadMonstersThrowsException() throws ServletException, IOException, LabyrinthException
	{
		int userId = 2;
		String errorMessage = messages.getMessage("unknown.unknown_error");
		User user = new User();
		user.setId(userId);

		when(actions.authenticateUser(request)).thenReturn(user);
		when(monster.loadMonstersByUserAndTile(userId, tile.getId())).thenThrow(new LabyrinthException());
		when(actions.getIdFromUrl(request, EndpointsWithIds.MONSTERS_TILES)).thenReturn(tile.getId());

		servlet.doGet(request, response);

		ArrayList<String> errors = servlet.getErrors();

		assertTrue("Expected one error, but got " + errors.size(), errors.size() == 1);
		assertTrue("Expected message \""+ errorMessage + "\" but got " + errors.get(0),
				errors.get(0).contains(errorMessage));
	}

	@Test
	/**
	 * Verify that when the load monsters method returns an empty list
	 * the server returns an appropriate error message
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @throws LabyrinthException
	 */
	public void testMonstersTileDoGetLoadMonstersReturnsNoMonsters() throws ServletException, IOException, LabyrinthException
	{
		int userId = 2;
		String errorMessage = messages.getMessage("monster.no_monster_with_tile_id");
		User user = new User();
		user.setId(userId);
		ArrayList<Monster> monsters = new ArrayList<>();

		when(actions.getIdFromUrl(request, EndpointsWithIds.MONSTERS_TILES)).thenReturn(tile.getId());
		when(actions.authenticateUser(request)).thenReturn(user);
		when(monster.loadMonstersByUserAndTile(userId, tile.getId())).thenReturn(monsters);

		servlet.doGet(request, response);

		ArrayList<String> errors = servlet.getErrors();

		assertTrue("Expected one error, but got " + errors.size(), errors.size() == 1);
		assertTrue("Expected message \""+ errorMessage + "\" but got " + errors.get(0),
				errors.get(0).contains(errorMessage));
	}
}
