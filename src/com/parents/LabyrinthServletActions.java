package com.parents;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.LabyrinthConstants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.helpers.Encryptor;
import com.models.User;

public class LabyrinthServletActions
{
	protected Gson gson = new Gson();

	public JsonObject getData(HttpServletRequest request) throws IOException, LabyrinthException
	{
		JsonObject data = null;
		BufferedReader br = request.getReader();
		String line = "";
		
		while(line != null)
		{
			line = br.readLine();
			try
			{
				data = gson.fromJson(line, JsonObject.class);
				line = null;
			}
			catch(JsonSyntaxException jse)
			{
				throw new LabyrinthException(LabyrinthConstants.MALFORMED_JSON);
			}
		}
		
		return data;
	}

	public User authenticateUser(HttpServletRequest request, HttpServletResponse response) throws LabyrinthException
	{
		boolean debug = false;
		
		Encryptor e = Encryptor.getInstance();
		String[] authorization = null;
		User u = null;
		
		if(debug)
		{
			Enumeration<String>requestHeaders = request.getHeaderNames();
			while(requestHeaders.hasMoreElements())
			{
				String element = requestHeaders.nextElement();
				String header = request.getHeader(element);
				System.out.println(element + ": " + header);
			}
		}

		try
		{
			authorization = request.getHeader("authorization").split(" ");
		}
		catch(NullPointerException npe)
		{
			// this exception is expected for the case when the user has not
			// included credentials
			throw new LabyrinthException(LabyrinthConstants.NO_AUTHORIZATION);
			
		}

		if(authorization != null && authorization[0].contains("Basic"))
		{
			String auth = e.decrypt(authorization[1]);
			String email = auth.split(":")[0];
			String password = auth.split(":")[1];
			u = new User();
			u.setEmail(email);
			u.setPassword(e.encrypt(password));
			try
			{
				u = u.login();
			}
			catch(LabyrinthException le)
			{
				u = null;
				throw new LabyrinthException(LabyrinthConstants.UNKNOWN_ERROR);
			}
		}
		return u;
	}
}
