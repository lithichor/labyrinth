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

import test.parents.LabyrinthHttpServletTest;

public class UserServletDeleteTests extends LabyrinthHttpServletTest
{
	private User user;
	private UserServlet servlet;
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
	public void testDeleteUserThrowsException() throws ServletException, IOException, LabyrinthException
	{
		String error = messages.getMessage("unknown.horribly_wrong");
		LabyrinthException labex = mock(LabyrinthException.class);
		
		when(actions.authenticateUser(request)).thenReturn(user);
		when(user.deleteUser()).thenThrow(labex);
		
		servlet.doDelete(request, response);

		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(error, messageObj.get("message").getAsString());
	}
}
