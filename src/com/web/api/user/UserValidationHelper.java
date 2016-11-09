package com.web.api.user;

import java.util.HashMap;

import com.google.gson.JsonObject;
import com.helpers.Encryptor;
import com.models.User;
import com.parents.LabyrinthValidationHelper;

public class UserValidationHelper extends LabyrinthValidationHelper
{
	Encryptor encryptor = Encryptor.getInstance();
	
	public boolean validate(HashMap<String, String> params)
	{
		boolean valid = true;
		boolean password = true;
		
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
		else if(params.get("password").length() < 6)
		{
			errors.add(messages.getMessage("signup.password_too_short"));
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
			user.setFirstName(data.get("firstName").toString().replaceAll("^\"|\"$", ""));
			params.put("firstName", user.getFirstName());
		}
		else
		{
			errors.add(messages.getMessage("signup.user_needs_first_name"));
		}
		if(data.has("lastName"))
		{
			user.setLastName(data.get("lastName").toString().replaceAll("^\"|\"$", ""));
			params.put("lastName",  user.getLastName());
		}
		else
		{
			errors.add(messages.getMessage("signup.user_needs_last_name"));
		}
		if(data.has("email"))
		{
			user.setEmail(data.get("email").toString().replaceAll("^\"|\"$", ""));
			params.put("email", user.getEmail());
		}
		else
		{
			errors.add(messages.getMessage("signup.user_needs_last_name"));
		}
		if(data.has("password"))
		{
			user.setPassword(encryptor.encrypt((data.get("password").toString()).replaceAll("^\"|\"$", "")));
			params.put("password", data.get("password").toString());
			params.put("confirm", data.get("password").toString());
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
			// check for a password
			// this will happen if the password provided is null or an empty string
			if(errors.contains(messages.getMessage("signup.password_needs_confirmation")))
			{
				errors.remove(messages.getMessage("signup.password_needs_confirmation"));
			}
			
			return null;
		}
	}
	
	public void validateApiPut(User user, JsonObject data)
	{
		if(data.has("firstName"))
		{
			user.setFirstName(data.get("firstName").toString().replaceAll("^\"|\"$", ""));
		}
		if(data.has("lastName"))
		{
			user.setLastName(data.get("lastName").toString().replaceAll("^\"|\"$", ""));
		}
		if(data.has("password"))
		{
			String password = data.get("password").toString().replaceAll("^\"|\"$", "");
			user.setPassword(encryptor.encrypt(password));
		}
	}

}
