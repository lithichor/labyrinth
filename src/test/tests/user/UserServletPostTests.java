package test.tests.user;

import static org.junit.Assert.*;

import java.io.BufferedReader;
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
import com.web.api.user.UserValidationHelper;

import test.parents.LabyrinthHttpTest;
import test.utils.RandomStrings;

public class UserServletPostTests extends LabyrinthHttpTest
{
	private UserServlet servlet;
	private RandomStrings rand;
	private User user;
	private UserServletActions actions;
	private BufferedReader br;
	private String line;
	private UserValidationHelper validation;
	
	@Before
	public void setup() throws IOException
	{
		br = mock(BufferedReader.class);
		user = mock(User.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		actions = mock(UserServletActions.class);
		validation = mock(UserValidationHelper.class);

		servlet = new UserServlet(user);
		servlet.setActions(actions);

		when(response.getWriter()).thenReturn(printer);

		rand = new RandomStrings();
	}

	@Test
	public void testCreateUserWithBadJson() throws ServletException, IOException
	{
		String firstName = rand.oneWord();
		String lastName = rand.oneWord();
		String email = rand.oneWord();
		String password = rand.oneWord();

		String error = messages.getMessage("unknown.malformed_json");

		// extra comma at the end of the json
		line = "{firstName: " + firstName + ","
				+ "lastName\": " + lastName + ","
				+ "email: " + email + ","
				+ "password: " + password + ", }";

		when(request.getReader()).thenReturn(br);
		when(br.readLine()).thenReturn(line).thenReturn(null);

		servlet.doPost(request, response);

		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(error, messageObj.get("message").getAsString());
	}

	@Test
	public void testDuplicateEmail() throws LabyrinthException, ServletException, IOException
	{
		String error = messages.getMessage("signup.user_exists");
		line = "{firstName: qwe, lastName: qwe, email: qwe@qwe.corn, password: 1QWEqwe}";
		JsonObject data = gson.fromJson(line, JsonObject.class);

		when(user.duplicateEmail()).thenReturn(true);
		when(request.getReader()).thenReturn(br);
		when(br.readLine()).thenReturn(line).thenReturn(null);
		when(validation.validateApi(data)).thenReturn(user);

		servlet.setValidation(validation);
		servlet.doPost(request, response);

		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(error, messageObj.get("message").getAsString());
	}
}
