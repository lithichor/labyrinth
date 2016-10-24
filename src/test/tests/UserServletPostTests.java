package test.tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
//import com.google.gson.stream.MalformedJsonException;
import com.helpers.validation.UserValidationHelper;
import com.models.User;
import com.web.api.user.UserServlet;

import test.parents.LabyrinthJUnitTest;
import test.utils.RandomStrings;

public class UserServletPostTests extends LabyrinthJUnitTest
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
	
//	// test the happy path
//	@Test
//	public void createUser() throws Exception
//	{
//		boolean userSaved = false;
//		String firstName = rand.oneWord();
//		String lastName = rand.oneWord();
//		String email = rand.oneWord();
//		String password = rand.oneWord();
//		Gson gson = new Gson();
//
//		String data = "{\"firstName\": \"" + firstName + "\","
//				+ "\"lastName\": \"" + lastName + "\","
//				+ "\"email\": \"" + email + "\","
//				+ "\"password\": \"" + password + "\" }";
//
//		UserValidationHelper validation = mock(UserValidationHelper.class);
//		BufferedReader br = mock(BufferedReader.class);
//		PrintWriter p = mock(PrintWriter.class);
//		
//		when(request.getReader()).thenReturn(br);
//		when(br.readLine()).thenReturn(data);
//		when(validation.validateApi(gson.fromJson(data, JsonObject.class))).thenReturn(user);
//		when(user.save()).thenReturn(userSaved = true);
//		when(response.getWriter()).thenReturn(p);
//		
//		create.doPost(request, response);
//		
//		assertTrue(userSaved);
//	}
	
	@Test(expected=JsonSyntaxException.class)
	public void testUserCreationWithBadJson() throws Exception
	{
		String firstName = rand.oneWord();
		String lastName = rand.oneWord();
		String email = rand.oneWord();
		String password = rand.oneWord();
		Gson gson = new Gson();
		boolean exceptionThrown = false;
		
		// extra comma at the end of the json
		String data = "{\"firstName\": \"" + firstName + "\","
				+ "\"lastName\": \"" + lastName + "\","
				+ "\"email\": \"" + email + "\","
				+ "\"password\": \"" + password + "\", }";

		UserValidationHelper validation = mock(UserValidationHelper.class);
		BufferedReader br = mock(BufferedReader.class);
		PrintWriter p = mock(PrintWriter.class);
		
		when(request.getReader()).thenReturn(br);
		when(br.readLine()).thenReturn(data);
		when(validation.validateApi(gson.fromJson(data, JsonObject.class))).thenReturn(user);
		when(response.getWriter()).thenReturn(p);
		
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
