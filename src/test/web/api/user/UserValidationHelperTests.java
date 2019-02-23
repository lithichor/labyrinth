package test.web.api.user;

import java.util.HashMap;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.parents.LabyrinthException;
import com.web.api.user.User;
import com.web.api.user.UserValidationHelper;

import test.parents.LabyrinthJUnitTest;

public class UserValidationHelperTests extends LabyrinthJUnitTest
{
	private UserValidationHelper helper;
	private String firstName = "baby";
	private String lastName = "squid";
	private String email = "baby@squid.corn";
	private String password = "1QWEqwe";

	@Before
	public void setup()
	{
		helper = new UserValidationHelper();
	}

	@Test
	public void testValidateMethodWithNoParams()
	{
		String errorMessage = "The Player needs to have a first name\n" +
				"The Player needs to have a last name\n" +
				"You need to include an email address\n" +
				"The Player needs a password\n";
		HashMap<String, String> params = new HashMap<>();
		
		helper.validate(params);
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}

	@Test
	public void testValidateMethodMissingFirstName()
	{
		String errorMessage = "The Player needs to have a first name\n";
		HashMap<String, String> params = new HashMap<>();
		params.put("lastName", lastName);
		params.put("email", email);
		params.put("password", password);
		
		helper.validate(params);
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}

	@Test
	public void testValidateMethodMissingLastName()
	{
		String errorMessage = "The Player needs to have a last name\n";
		HashMap<String, String> params = new HashMap<>();
		params.put("firstName", firstName);
		params.put("email", email);
		params.put("password", password);
		
		helper.validate(params);
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}

	@Test
	public void testValidateMethodMissingEmail()
	{
		String errorMessage = "You need to include an email address\n";
		HashMap<String, String> params = new HashMap<>();
		params.put("firstName", firstName);
		params.put("lastName", lastName);
		params.put("password", password);
		
		helper.validate(params);
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}

	@Test
	public void testValidateMethodMissingPassword()
	{
		String errorMessage = "The Player needs a password\n";
		HashMap<String, String> params = new HashMap<>();
		params.put("firstName", firstName);
		params.put("lastName", lastName);
		params.put("email", email);
		
		helper.validate(params);
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}

	@Test
	public void testValidateParameterLengthMethodParamTooLong()
	{
		String errorMessage = "The sample parameter has to be less than or equal to 250 characters\n";
		HashMap<String, String> params = new HashMap<>();
		String str = "";
		for(int x = 0; x < 251; x++)
		{
			str += randStrings.getOneCharacter();
		}
		params.put("sample", str);
		
		helper.validate(params);
		
		assertTrue(helper.getErrorsAsString().contains(errorMessage));
	}

	@Test
	public void testValidateParameterLengthMethodParamCorrectLength()
	{
		String errorMessage = "The sample parameter has to be less than or equal to 250 characters\n";
		HashMap<String, String> params = new HashMap<>();
		String str = "";
		for(int x = 0; x < 250; x++)
		{
			str += randStrings.getOneCharacter();
		}
		params.put("sample", str);
		
		helper.validate(params);
		
		assertFalse(helper.getErrorsAsString().contains(errorMessage));
	}

	@Test
	public void testValidateApiDataIsNull()
	{
		JsonObject data = gson.fromJson("", JsonObject.class);
		
		User user = helper.validateApi(data);
		
		assertTrue(user == null);
	}
	
	@Test
	public void testValidateApiMissingFirstName()
	{
		String dataStr = "{"
				+ "lastName: " + lastName + ","
				+ "email: " + email + ","
				+ "password: " + password
				+ "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String errorMessage = "The Player needs to have a first name\n";

		helper.validateApi(data);
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}
	
	@Test
	public void testValidateApiMissingLastName()
	{
		String dataStr = "{"
				+ "firstName: " + firstName + ","
				+ "email: " + email + ","
				+ "password: " + password
				+ "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String errorMessage = "The Player needs to have a last name\n";

		helper.validateApi(data);
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}
	
	@Test
	public void testValidateApiMissingEmail()
	{
		String dataStr = "{"
				+ "firstName: " + firstName + ","
				+ "lastName: " + lastName + ","
				+ "password: " + password
				+ "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String errorMessage = "You need to include an email address\n";

		helper.validateApi(data);
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}
	
	@Test
	public void testValidateApiMissingPassword()
	{
		String dataStr = "{"
				+ "firstName: " + firstName + ","
				+ "lastName: " + lastName + ","
				+ "email: " + email
				+ "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String errorMessage = "The Player needs a password\n";

		helper.validateApi(data);
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}
	
	@Test
	public void testValidateApiPut() throws LabyrinthException
	{
		// randomly set an attribute; as long as everything works, the user will not be null
		String dataStr = "{";
		dataStr += (rand.nextInt(2) == 0) ? "firstName: " + firstName + "," : "";
		dataStr += (rand.nextInt(2) == 0) ? "lastName: " + lastName + "," : "";
		// email is accepted, but it doesn't change the email; this is here
		// to make sure it continues to work that way
		dataStr += (rand.nextInt(2) == 0) ? "email: " + email + "," : "";
		dataStr += (rand.nextInt(2) == 0) ? "password: " + password + "," : "";
		
		// only strip out the comma if one of the attributes is set for change
		if(dataStr.length() > 2)
		{
			dataStr = dataStr.substring(0, dataStr.length() - 1);
		}
		dataStr += "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		User user = helper.validateApiPut(data, rand.nextInt(1000));
		assertNotNull(user);
	}
	
	@Test
	public void testValidateApiPutHashForFirstName() throws LabyrinthException
	{
		String dataStr = "{";
		dataStr += "firstName: {x: xx}";
		dataStr += "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String errorMessage = "The first name field has to be a String\n";

		helper.validateApiPut(data, rand.nextInt(1000));
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}
	
	@Test
	public void testValidateApiPutArrayForFirstName() throws LabyrinthException
	{
		String dataStr = "{";
		dataStr += "firstName: [a, b, c]";
		dataStr += "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String errorMessage = "The first name field has to be a String\n";

		helper.validateApiPut(data, rand.nextInt(1000));
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}
	
	@Test
	public void testValidateApiPutHashForLastName() throws LabyrinthException
	{
		String dataStr = "{";
		dataStr += "lastName: {13: 37}";
		dataStr += "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String errorMessage = "The last name field has to be a String\n";

		helper.validateApiPut(data, rand.nextInt(1000));
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}
	
	@Test
	public void testValidateApiPutArrayForLastName() throws LabyrinthException
	{
		String dataStr = "{";
		dataStr += "lastName: [1, 2, 3]";
		dataStr += "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String errorMessage = "The last name field has to be a String\n";

		helper.validateApiPut(data, rand.nextInt(1000));
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}
	
	@Test
	public void testValidateApiPutHashForPassword() throws LabyrinthException
	{
		//start from here
		String dataStr = "{";
		dataStr += "password: {x: 99}";
		dataStr += "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String errorMessage = "The password field has to be a String\n";

		helper.validateApiPut(data, rand.nextInt(1000));
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}
	
	@Test
	public void testValidateApiPutArrayForPassword() throws LabyrinthException
	{
		String dataStr = "{";
		dataStr += "password: [1, a, 3, d]";
		dataStr += "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String errorMessage = "The password field has to be a String\n";

		helper.validateApiPut(data, rand.nextInt(1000));
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}
	
	@Test
	public void testValidatePasswordTooShort() throws LabyrinthException
	{
		String dataStr = "{";
		dataStr += "password: 1Qqqw";
		dataStr += "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String errorMessage = "The password needs to be six (6) or more characters long\n";

		helper.validateApiPut(data, rand.nextInt(1000));
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}
	
	@Test
	public void testValidatePasswordNotComplexNoUpper() throws LabyrinthException
	{
		String dataStr = "{";
		dataStr += "password: 1qweqwe";
		dataStr += "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String errorMessage = "A password has to have at least one digit (0-9), one uppercase letter, and one lowercase letter\n";

		helper.validateApiPut(data, rand.nextInt(1000));
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}
	
	@Test
	public void testValidatePasswordNotComplexNoLower() throws LabyrinthException
	{
		String dataStr = "{";
		dataStr += "password: 1QWEQWE";
		dataStr += "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String errorMessage = "A password has to have at least one digit (0-9), one uppercase letter, and one lowercase letter\n";

		helper.validateApiPut(data, rand.nextInt(1000));
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}
	
	@Test
	public void testValidatePasswordNotComplexNoDigits() throws LabyrinthException
	{
		String dataStr = "{";
		dataStr += "password: QWEqwe";
		dataStr += "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String errorMessage = "A password has to have at least one digit (0-9), one uppercase letter, and one lowercase letter\n";

		helper.validateApiPut(data, rand.nextInt(1000));
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}
	
	@Test
	public void testValidatePasswordTwoMessages() throws LabyrinthException
	{
		String dataStr = "{";
		dataStr += "password: qwe";
		dataStr += "}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String errorMessage = "The password needs to be six (6) or more characters long\n" +
				"A password has to have at least one digit (0-9), one uppercase letter, and one lowercase letter\n";

		helper.validateApiPut(data, rand.nextInt(1000));
		
		assertEquals(errorMessage, helper.getErrorsAsString());
	}
}
