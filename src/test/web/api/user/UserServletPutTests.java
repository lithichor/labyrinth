package test.web.api.user;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.parents.LabyrinthException;
import com.web.api.game.Game;
import com.web.api.user.User;
import com.web.api.user.UserServlet;
import com.web.api.user.UserServletActions;

import test.parents.LabyrinthHttpServletTest;

public class UserServletPutTests extends LabyrinthHttpServletTest
{
	private UserServlet servlet;
	private User user;
	private UserServletActions actions;

	@Before
	public void setup() throws IOException
	{
		user = mock(User.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		actions = mock(UserServletActions.class);

		servlet = new UserServlet();
		servlet.setUser(user);
		servlet.setActions(actions);

		when(response.getWriter()).thenReturn(printer);
	}

	@Test
	public void testUpdateUser() throws ServletException, IOException, LabyrinthException
	{
		String data = "{firstName: [a, b, c]}";
		JsonObject dataObj = gson.fromJson(data, JsonObject.class);
		String message = messages.getMessage("user.first_name_not_a_string");

		when(actions.authenticateUser(request)).thenReturn(user);
		when(actions.getData(request)).thenReturn(dataObj);

		servlet.doPut(request, response);
		String response = strWriter.getBuffer().toString();
		JsonObject responseObj = gson.fromJson(response, JsonObject.class);
		String messageStr = responseObj.get("message").getAsString();

		assertEquals(message, messageStr);
	}

	@Test
	/**
	 * This tests that we get an error message when actions.getData()
	 * returns null
	 * 
	 * @throws ServletException
	 * @throws IOException
	 * @throws LabyrinthException
	 */
	public void testUserPutNullData() throws ServletException, IOException, LabyrinthException
	{
		String message = messages.getMessage("user.no_data");
		when(actions.authenticateUser(request)).thenReturn(user);

		servlet.doPut(request,  response);
		String response = strWriter.getBuffer().toString();
		JsonObject responseObj = gson.fromJson(response, JsonObject.class);
		String messageStr = responseObj.get("message").getAsString();

		assertEquals(message, messageStr);
	}
	
	@Test
	/**
	 * Verify that when the data is empty we get the correct response.
	 * Check the user ID, email, and first & last names, and an array of
	 * game IDs to make sure the same user is returned as we started with
	 * 
	 * @throws IOException
	 * @throws LabyrinthException
	 * @throws ServletException
	 */
	public void testUserPutEmptyData() throws IOException, LabyrinthException, ServletException
	{
		int userId = 3;
		String email = "user@user.corn";
		String firstName = "User";
		String lastName = "Dot Corn";
		
		when(user.getId()).thenReturn(userId);
		when(user.getEmail()).thenReturn(email);
		when(user.getFirstName()).thenReturn(firstName);
		when(user.getLastName()).thenReturn(lastName);

		// the game used to load the list of games
		Game game = mock(Game.class);
		servlet.setGame(game);

		// the list of games
		ArrayList<Game> gameList = new ArrayList<>();
		Game g1 = new Game();
		g1.setId(1);
		Game g2 = new Game();
		g2.setId(2);
		gameList.add(g1);
		gameList.add(g2);
		
		JsonObject data = gson.fromJson("{}", JsonObject.class);

		when(actions.authenticateUser(request)).thenReturn(user);
		when(actions.getData(request)).thenReturn(data);
		when(game.load(userId, 0)).thenReturn(gameList);
		
		servlet.doPut(request, response);
		
		String response = strWriter.getBuffer().toString();
		JsonObject userObj = gson.fromJson(response, JsonObject.class);
		JsonArray games = userObj.get("gameIds").getAsJsonArray();

		assertEquals(userId, userObj.get("id").getAsInt());
		assertEquals(email, userObj.get("email").getAsString());
		assertEquals(firstName, userObj.get("firstName").getAsString());
		assertEquals(lastName, userObj.get("lastName").getAsString());
		assertEquals(gameList.size(), games.size());
		assertEquals(g1.getId().intValue(), games.get(0).getAsInt());
		assertEquals(g2.getId().intValue(), games.get(1).getAsInt());
	}

	@Test
	public void testUpdateUserWithArrayForStringFirstName() throws ServletException, IOException, LabyrinthException
	{
		String data = "{firstName: [a, b, c]}";
		JsonObject dataObj = gson.fromJson(data, JsonObject.class);
		String message = messages.getMessage("user.first_name_not_a_string");

		when(actions.authenticateUser(request)).thenReturn(user);
		when(actions.getData(request)).thenReturn(dataObj);

		servlet.doPut(request, response);
		String response = strWriter.getBuffer().toString();
		JsonObject responseObj = gson.fromJson(response, JsonObject.class);
		String messageStr = responseObj.get("message").getAsString();

		assertEquals(message, messageStr);
	}

	@Test
	public void testUpdateUserWithHashForStringLastName() throws ServletException, IOException, LabyrinthException
	{
		String data = "{lastName: {hash: this}}";
		JsonObject dataObj = gson.fromJson(data, JsonObject.class);
		String message = messages.getMessage("user.last_name_not_a_string");

		when(actions.authenticateUser(request)).thenReturn(user);
		when(actions.getData(request)).thenReturn(dataObj);

		servlet.doPut(request, response);
		String response = strWriter.getBuffer().toString();
		JsonObject responseObj = gson.fromJson(response, JsonObject.class);
		String messageStr = responseObj.get("message").getAsString();

		assertEquals(message, messageStr);
	}

	@Test
	public void testUpdateUserWithNullAuthentication() throws LabyrinthException, ServletException, IOException
	{
		String message = messages.getMessage("user.no_such_player");
		when(actions.authenticateUser(request)).thenReturn(null);

		servlet.doPut(request,  response);
		String response = strWriter.getBuffer().toString();
		JsonObject responseObj = gson.fromJson(response, JsonObject.class);
		String messageStr = responseObj.get("message").getAsString();

		assertEquals(message, messageStr);
	}
}
