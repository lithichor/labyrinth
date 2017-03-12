package com.parents;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.helpers.Encryptor;
import com.labels.LabyrinthMessages;
import com.web.api.user.User;

public class LabyrinthServletActions
{
	protected Gson gson = new Gson();
	protected LabyrinthMessages messages = new LabyrinthMessages();

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
				throw new LabyrinthException(messages.getMessage("unknown.malformed_json"));
			}
		}
		
		return data;
	}

	public User authenticateUser(HttpServletRequest request) throws LabyrinthException
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
			throw new LabyrinthException(messages.getMessage("user.no_authorization"));
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
				throw le;
			}
		}
		return u;
	}
	
	public String splitUrl(String url, String endpoint)
	{
		String[] parsedUrl = url.split(endpoint + "/");
		String idString = "";
		
		if(parsedUrl.length > 1)
		{
			idString = parsedUrl[1];
		}
		
		return idString;
	}
	
	public int parseIdFromString(String idString)
	{
		int id = 0;
		
		try
		{
			id = Integer.parseInt(idString);
		}
		catch(NumberFormatException nfe)
		{
			// id was not a valid integer - ignore it
			id = 0;
		}
		
		return id;
	}

	public int getIdFromUrl(HttpServletRequest request, String endpoint)
	{
		int id = 0;
		
		String idStr = splitUrl(request.getRequestURI(), endpoint);
		
		// if there is a string after the endpoint
		if(idStr.length() > 0)
		{
			id = parseIdFromString(idStr);
		}
		
		return id;
	}
}
