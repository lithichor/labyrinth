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
import com.web.api.monster.Monster;
import com.web.api.monster.MonstersServlet;
import com.web.api.monster.MonstersServletActions;
import com.web.api.user.User;

import test.parents.LabyrinthHttpServletTest;

public class MonstersServletGetTests extends LabyrinthHttpServletTest
{
	private MonstersServlet servlet = new MonstersServlet();
	private MonstersServletActions actions = mock(MonstersServletActions.class);
	private User user = new User();
	private GsonWrapper gsonWrapper = mock(GsonWrapper.class);

	@Before
	public void setup() throws IOException
	{
		user.setId(4);
		servlet.setActions(actions);
		servlet.setGson(gsonWrapper);

		when(response.getWriter()).thenReturn(printerTwo);
	}

	@Test
	/**
	 * Verify the doGet method executes with no errors
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @throws LabyrinthException
	 */
	public void testMonstersDoGet() throws ServletException, IOException, LabyrinthException
	{
		int monsterId = 4;
		Monster monster = mock(Monster.class);
		ArrayList<Monster> monsters = new ArrayList<>();
		Monster m = mock(Monster.class);
		monsters.add(m);

		servlet.setMonster(monster);

		when(actions.getIdFromUrl(request, EndpointsWithIds.MONSTERS)).thenReturn(monsterId);
		when(actions.authenticateUser(request)).thenReturn(user);
		when(monster.loadMonstersByUserAndMonster(4, 4)).thenReturn(monsters);

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
	public void testMonstersDoGetAuthenticateThrowsException() throws ServletException, IOException, LabyrinthException
	{
		String errorMessage = "Exception Thrown";
		
		when(actions.authenticateUser(request)).thenThrow(new LabyrinthException(errorMessage));

		servlet.doGet(request, response);

		ArrayList<String> errors = servlet.getErrors();

		assertTrue(errors.size() == 1);
		assertTrue(errors.get(0).contains(errorMessage));
	}

	@Test
	/**
	 * Verify that when the authentication method returns a null object
	 * that the no_such_player error is returned from the server
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @throws LabyrinthException
	 */
	public void testMonstersDoGetAuthReturnsNull() throws ServletException, IOException, LabyrinthException
	{
		String errorMessage = messages.getMessage("user.no_such_player");
		when(actions.authenticateUser(request)).thenReturn(null);

		servlet.doGet(request, response);

		ArrayList<String> errors = servlet.getErrors();

		assertTrue(errors.size() == 1);
		assertTrue(errors.get(0).contains(errorMessage));
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
	public void testMonstersDoGetLoadMonstersThrowsException() throws ServletException, IOException, LabyrinthException
	{
		int monsterId = 1;
		int userId = 2;
		String errorMessage = messages.getMessage("unknown.unknown_error");
		Monster monster = mock(Monster.class);
		User user = new User();
		user.setId(userId);

		servlet.setMonster(monster);

		when(actions.getIdFromUrl(request, EndpointsWithIds.MONSTERS)).thenReturn(monsterId);
		when(actions.authenticateUser(request)).thenReturn(user);
		when(monster.loadMonstersByUserAndMonster(userId, monsterId)).thenThrow(new LabyrinthException());

		servlet.doGet(request, response);

		ArrayList<String> errors = servlet.getErrors();

		assertTrue(errors.size() == 1);
		assertTrue(errors.get(0).contains(errorMessage));
	}

	@Test
	/**
	 * Verify that when the load monsters method returns an empty list
	 * the server returns an error message saying no monsters found
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @throws LabyrinthException
	 */
	public void testMonstersDoGetLoadMonstersReturnsNoMonsters() throws ServletException, IOException, LabyrinthException
	{
		int monsterId = 1;
		int userId = 2;
		String errorMessage = messages.getMessage("monster.no_monster_found");
		Monster monster = mock(Monster.class);
		User user = new User();
		user.setId(userId);
		ArrayList<Monster> monsters = new ArrayList<>();

		servlet.setMonster(monster);

		when(actions.getIdFromUrl(request, EndpointsWithIds.MONSTERS)).thenReturn(monsterId);
		when(actions.authenticateUser(request)).thenReturn(user);
		when(monster.loadMonstersByUserAndMonster(userId, monsterId)).thenReturn(monsters);

		servlet.doGet(request, response);

		ArrayList<String> errors = servlet.getErrors();

		assertTrue(errors.size() == 1);
		assertTrue(errors.get(0).contains(errorMessage));
	}

	@Test
	/**
	 * Verify that when the URL does not contain an ID the server
	 * returns an error saying there is no monster ID
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @throws LabyrinthException
	 */
	public void testMonstersDoGetNoIdInUrl() throws ServletException, IOException, LabyrinthException
	{
		String errorMessage = messages.getMessage("monster.no_monster_id");
		User user = new User();
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.MONSTERS)).thenReturn(0);
		when(actions.authenticateUser(request)).thenReturn(user);
		
		servlet.doGet(request, response);

		ArrayList<String> errors = servlet.getErrors();

		assertTrue(errors.size() == 1);
		assertTrue(errors.get(0).contains(errorMessage));
	}
}
