package test.tests.user;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.web.api.user.User;
import com.web.api.user.UserServlet;
import com.web.api.user.UserValidationHelper;

import test.parents.LabyrinthHttpTest;
import test.utils.RandomStrings;

public class UserServletPostTests extends LabyrinthHttpTest
{
	private UserServlet create;
	private RandomStrings rand;
	private User user;
	
	@Before
	public void setup()
	{
		user = mock(User.class);
		create = new UserServlet(user);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		rand = new RandomStrings();
	}
	
	@Test(expected=JsonSyntaxException.class)
	public void testUserCreationWithBadJson() throws Exception
	{
		String firstName = rand.oneWord();
		String lastName = rand.oneWord();
		String email = rand.oneWord();
		String password = rand.oneWord();
		boolean exceptionThrown = false;
		
		// extra comma at the end of the json
		String data = "{\"firstName\": \"" + firstName + "\","
				+ "\"lastName\": \"" + lastName + "\","
				+ "\"email\": \"" + email + "\","
				+ "\"password\": \"" + password + "\", }";

		UserValidationHelper validation = mock(UserValidationHelper.class);
		BufferedReader br = mock(BufferedReader.class);
		PrintWriter pw = mock(PrintWriter.class);
		
		when(request.getReader()).thenReturn(br);
		when(br.readLine()).thenReturn(data);
		when(validation.validateApi(gson.fromJson(data, JsonObject.class))).thenReturn(user);
		when(response.getWriter()).thenReturn(pw);
		
		try
		{
			create.doPost(request, response);
		}
		catch(JsonSyntaxException jse)
		{
			exceptionThrown = true;
		}
		
		assertTrue(exceptionThrown);
	}
}
