package com.web.api.user;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonObject;
import com.helpers.Encryptor;
import com.parents.LabyrinthException;
import com.parents.LabyrinthValidationHelper;

public class UserValidationHelper extends LabyrinthValidationHelper
{
	Encryptor encryptor = Encryptor.getInstance();
	
	public boolean validate(HashMap<String, String> params)
	{
		boolean valid = true;
		boolean password = true;
		
		if(!validateParameterLength(params))
		{
			valid = false;
		}
		
		if(params.get("firstName") == null || "".equalsIgnoreCase(params.get("firstName")))
		{
			if(!errors.contains(messages.getMessage("signup.user_needs_first_name")))
			{
				errors.add(messages.getMessage("signup.user_needs_first_name"));
			}
			valid = false;
		}
		if(params.get("lastName") == null || "".equalsIgnoreCase(params.get("lastName")))
		{
			if(!errors.contains(messages.getMessage("signup.user_needs_last_name")))
			{
				errors.add(messages.getMessage("signup.user_needs_last_name"));
			}
			valid = false;
		}
		if(params.get("email") == null || "".equalsIgnoreCase(params.get("email")))
		{
			if(!errors.contains(messages.getMessage("signup.user_needs_email")))
			{
				errors.add(messages.getMessage("signup.user_needs_email"));
			}
			valid = false;
		}
		else
		{
			if(!params.get("email").contains(".") || !params.get("email").contains("@"))
			{
				errors.add(messages.getMessage("signup.malformed_email"));
				valid = false;
			}
		}
		if(params.get("password") == null || "".equalsIgnoreCase(params.get("password")))
		{
			if(!errors.contains(messages.getMessage("signup.user_needs_password")))
			{
				errors.add(messages.getMessage("signup.user_needs_password"));
			}
			valid = false;
			password = false;
		}
		else if (!validatePassword(params.get("password")))
		{
			valid = false;
			password = false;
		}
		if(params.get("confirm") == null || "".equalsIgnoreCase(params.get("confirm")))
		{
			// there will not be a duplicate of this error
			errors.add(messages.getMessage("signup.password_needs_confirmation"));
			valid = false;
			password = false;
		}
		if(password && (!params.get("confirm").equals(params.get("password"))))
		{
			// there will not be a duplicate of this error
			errors.add(messages.getMessage("signup.passwords_do_not_match"));
			valid = false;
		}
		
		return valid;
	}
	
	private boolean validateParameterLength(HashMap<String, String> params)
	{
		boolean valid = true;
		
		for(String key: params.keySet())
		{
			if(!"confirm".equalsIgnoreCase(params.get(key)) && params.get(key).length() > 250)
			{
				valid = false;
				errors.add("The " + key + " " + messages.getMessage("signup.parameter_too_long"));
			}
		}
		
		return valid;
	}

	public User validateApi(JsonObject data)
	{
		User user = new User();
		HashMap<String, String> params = new HashMap<String, String>();
		
		// don't bother validating if the data is null (although
		// it shouldn't be)
		if(data == null)
		{
			return null;
		}
		
		if(data.has("firstName"))
		{
			try
			{
				user.setFirstName(data.get("firstName").getAsString());
				params.put("firstName", user.getFirstName());
			}
			catch(IllegalStateException  | UnsupportedOperationException ex)
			{
				errors.add(messages.getMessage("signup.user_needs_first_name"));
			}
		}
		else
		{
			errors.add(messages.getMessage("signup.user_needs_first_name"));
		}
		if(data.has("lastName"))
		{
			try
			{
				user.setLastName(data.get("lastName").getAsString());
				params.put("lastName",  user.getLastName());
			}
			catch(IllegalStateException  | UnsupportedOperationException ex)
			{
				errors.add(messages.getMessage("signup.user_needs_last_name"));
			}
		}
		else
		{
			errors.add(messages.getMessage("signup.user_needs_last_name"));
		}
		if(data.has("email"))
		{
			try
			{
				user.setEmail(data.get("email").getAsString());
				params.put("email", user.getEmail());
			}
			catch(IllegalStateException  | UnsupportedOperationException ex)
			{
				errors.add(messages.getMessage("signup.user_needs_email"));
			}
		}
		else
		{
			errors.add(messages.getMessage("signup.user_needs_email"));
		}
		if(data.has("password"))
		{
			try
			{
				user.setPassword(encryptor.encrypt(data.get("password").getAsString()));
				params.put("password", data.get("password").getAsString());
				params.put("confirm", data.get("password").getAsString());
			}
			catch(IllegalStateException | UnsupportedOperationException ex)
			{
				errors.add(messages.getMessage("signup.user_needs_password"));
			}
		}
		else
		{
			errors.add(messages.getMessage("signup.user_needs_password"));
		}
		
		// return the user only if it passes validation
		if(this.validate(params))
		{
			return user;
		}
		else
		{
			// if we're creating a user from the API endpoint, we don't need to
			// check for a password confirmation
			// this will happen if the password provided is null or an empty string
			if(errors.contains(messages.getMessage("signup.password_needs_confirmation")))
			{
				errors.remove(messages.getMessage("signup.password_needs_confirmation"));
			}
			
			return null;
		}
	}
	
	public User validateApiPut(JsonObject data, Integer userId) throws LabyrinthException
	{
		User user = new User();
		
		if(data.has("firstName"))
		{
			try
			{
				user.setFirstName(data.get("firstName").getAsString());
			}
			catch(UnsupportedOperationException | IllegalStateException ex)
			{
				errors.add(messages.getMessage("user.first_name_not_a_string"));
				user = null;
			}
		}
		if(data.has("lastName"))
		{
			try
			{
				user.setLastName(data.get("lastName").getAsString());
			}
			catch(UnsupportedOperationException | IllegalStateException ex)
			{
				errors.add(messages.getMessage("user.last_name_not_a_string"));
				user = null;
			}
		}
		if(data.has("password"))
		{
			String password = null;
			try
			{
				password = data.get("password").getAsString();
				user.setPassword(encryptor.encrypt(password));
				if(!validatePassword(password))
				{
					throw new LabyrinthException(messages.getMessage("signup.password_reqs_not_met"));
				}
			}
			catch(UnsupportedOperationException | IllegalStateException ex)
			{
				errors.add(messages.getMessage("user.password_not_a_string"));
				user = null;
			}
		}
		
		return user;
	}
	
	public boolean validatePassword(String password)
	{
		boolean valid = true;
		
		// need at least one digit, one lowercase letter, and one uppercase letter
		String regex = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{6,})";
		
		// this solution found here:
		// http://stackoverflow.com/questions/8923398/regex-doesnt-work-in-string-matches
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(password);
		
		if(password.length() < 6)
		{
			errors.add(messages.getMessage("signup.password_too_short"));
			valid = false;
		}
		else if(!match.find())
		{
			errors.add(messages.getMessage("signup.password_reqs_not_met"));
			valid = false;
		}
		
		return valid;
	}
}
