package test.tests.user;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.parents.LabyrinthException;
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

		servlet = new UserServlet(user);
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
	public void testUpdateUserWithNoData() throws ServletException, IOException, LabyrinthException
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
