package com.parents;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.helpers.Encryptor;
import com.labels.LabyrinthMessages;
import com.models.User;

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

	public Integer getGameId(User user, JsonObject data) throws LabyrinthException
	{
		int gameId = 0;
		
		// if the gameId is specified, use that
		if(data != null && data.has("gameId"))
		{
			try
			{
				gameId = data.get("gameId").getAsInt();
			}
			catch(NumberFormatException | IllegalStateException  | UnsupportedOperationException ex)
			{
				throw new LabyrinthException(messages.getMessage("map.gameId_not_integer"));
			}
		}
		
		return gameId;
	}
}
