package test.tests.user;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.parents.LabyrinthException;
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
	public void setup() throws IOException
	{
		user = mock(User.class);
		actions = mock(UserServletActions.class);

		servlet = new UserServlet(user);
		servlet.setActions(actions);

		when(response.getWriter()).thenReturn(printer);
	}

	@Test
	public void testUserGetWithNullUser() throws LabyrinthException, IOException, ServletException
	{
		String message = messages.getMessage("user.no_such_player");

		when(actions.authenticateUser(request)).thenReturn(null);

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

		servlet.doGet(request, response);
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals("error", message, messageObj.get("message").getAsString());
	}
}
