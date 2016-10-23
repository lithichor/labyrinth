package com.helpers.validation;

import java.util.HashMap;

import com.LabyrinthConstants;
import com.google.gson.JsonObject;
import com.helpers.Encryptor;
import com.models.User;

public class UserValidationHelper extends ValidationHelper
{
	Encryptor encryptor = Encryptor.getInstance();
	
	public boolean validate(HashMap<String, String> params)
	{
		boolean valid = true;
		boolean password = true;
		
		if(params.get("firstName") == null || "".equalsIgnoreCase(params.get("firstName")))
		{
			if(!errors.contains(LabyrinthConstants.USER_NEEDS_FIRST_NAME))
			{
				errors.add(LabyrinthConstants.USER_NEEDS_FIRST_NAME);
			}
			valid = false;
		}
		if(params.get("lastName") == null || "".equalsIgnoreCase(params.get("lastName")))
		{
			if(!errors.contains(LabyrinthConstants.USER_NEEDS_LAST_NAME))
			{
				errors.add(LabyrinthConstants.USER_NEEDS_LAST_NAME);
			}
			valid = false;
		}
		if(params.get("email") == null || "".equalsIgnoreCase(params.get("email")))
		{
			if(!errors.contains(LabyrinthConstants.USER_NEEDS_EMAIL))
			{
				errors.add(LabyrinthConstants.USER_NEEDS_EMAIL);
			}
			valid = false;
		}
		else
		{
			if(!params.get("email").contains(".") || !params.get("email").contains("@"))
			{
				errors.add(LabyrinthConstants.MALFORMED_EMAIL);
				valid = false;
			}
		}
		if(params.get("password") == null || "".equalsIgnoreCase(params.get("password")))
		{
			if(!errors.contains(LabyrinthConstants.USER_NEEDS_PASSWORD))
			{
				errors.add(LabyrinthConstants.USER_NEEDS_PASSWORD);
			}
			valid = false;
			password = false;
		}
		if(params.get("confirm") == null || "".equalsIgnoreCase(params.get("confirm")))
		{
			// there will not be a duplicate of this error
			errors.add(LabyrinthConstants.PASSWORD_NEEDS_CONFIRMATION);
			valid = false;
			password = false;
		}
		if(password && (!params.get("confirm").equals(params.get("password"))))
		{
			// there will not be a duplicate of this error
			errors.add(LabyrinthConstants.PASSWORDS_DO_NOT_MATCH);
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
			errors.add(LabyrinthConstants.USER_NEEDS_FIRST_NAME);
		}
		if(data.has("lastName"))
		{
			user.setLastName(data.get("lastName").toString().replaceAll("^\"|\"$", ""));
			params.put("lastName",  user.getLastName());
		}
		else
		{
			errors.add(LabyrinthConstants.USER_NEEDS_LAST_NAME);
		}
		if(data.has("email"))
		{
			user.setEmail(data.get("email").toString().replaceAll("^\"|\"$", ""));
			params.put("email", user.getEmail());
		}
		else
		{
			errors.add(LabyrinthConstants.USER_NEEDS_EMAIL);
		}
		if(data.has("password"))
		{
			user.setPassword(encryptor.encrypt((data.get("password").toString()).replaceAll("^\"|\"$", "")));
			params.put("password", user.getPassword());
			params.put("confirm", user.getPassword());
		}
		else
		{
			errors.add(LabyrinthConstants.USER_NEEDS_PASSWORD);
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
			if(errors.contains(LabyrinthConstants.PASSWORD_NEEDS_CONFIRMATION))
			{
				errors.remove(LabyrinthConstants.PASSWORD_NEEDS_CONFIRMATION);
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
		if(data.has("email"))
		{
			user.setEmail(data.get("email").toString().replaceAll("^\"|\"$", ""));
		}
		if(data.has("password"))
		{
			String password = data.get("password").toString().replaceAll("^\"|\"$", "");
			user.setPassword(encryptor.encrypt(password));
		}
	}

}
