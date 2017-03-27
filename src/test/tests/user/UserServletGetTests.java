package test.tests.user;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.parents.LabyrinthException;
import com.web.api.game.Game;
import com.web.api.user.User;
import com.web.api.user.UserServlet;
import com.web.api.user.UserServletActions;

import test.parents.LabyrinthHttpTest;

public class UserServletGetTests extends LabyrinthHttpTest
{
	private UserServlet servlet;
	private User user;
	private UserServletActions actions;

	@Before
	public void setup()
	{
		user = mock(User.class);
		servlet = new UserServlet(user);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		actions = mock(UserServletActions.class);
		
		servlet.setActions(actions);
	}

	@Test
	public void testUserGet() throws ServletException, IOException, LabyrinthException
	{
		// parameters for user
		String email = "test@test.corn";
		String firstName = "Test";
		String lastName = "User";
		int id = rand.nextInt(1000);
		Game game = mock(Game.class);
		
		when(user.getEmail()).thenReturn(email);
		when(user.getFirstName()).thenReturn(firstName);
		when(user.getLastName()).thenReturn(lastName);
		when(user.getId()).thenReturn(id);

		when(actions.authenticateUser(request)).thenReturn(user);
		servlet.setGame(game);

		// this simulates no games for the user
		when(game.load(1, 1)).thenThrow(new LabyrinthException());
		when(response.getWriter()).thenReturn(printer);

		servlet.doGet(request, response);
		String userStr = strWriter.getBuffer().toString();
		JsonObject userObj = gson.fromJson(userStr, JsonObject.class);

		assertEquals("The ID doesn't match", id, userObj.get("id").getAsInt());
		assertEquals("The Email doesn't match", email, userObj.get("email").getAsString());
		assertEquals("The First Name doesn't match", firstName, userObj.get("firstName").getAsString());
		assertEquals("The Last Name doesn't match", lastName, userObj.get("lastName").getAsString());
	}

	@Test
	public void testUserGetWithNullUser() throws LabyrinthException, IOException, ServletException
	{
		String message = messages.getMessage("user.no_such_player");

		when(actions.authenticateUser(request)).thenReturn(null);
		when(response.getWriter()).thenReturn(printer);

		servlet.doGet(request, response);
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals("error", message, messageObj.get("message").getAsString());
	}

	@Test
	public void testUserGetWithNoAuthentication() throws LabyrinthException, IOException, ServletException
	{
		String message = messages.getMessage("user.no_such_player");
		LabyrinthException labex = new LabyrinthException(messages.getMessage("user.no_such_player"));

		when(actions.authenticateUser(request)).thenThrow(labex);
		when(request.getHeader("authorization")).thenReturn("");

		when(response.getWriter()).thenReturn(printer);

		servlet.doGet(request, response);
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals("error", message, messageObj.get("message").getAsString());
	}
}
