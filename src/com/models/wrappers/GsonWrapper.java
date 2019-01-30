package com.models.wrappers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GsonWrapper
{
	private Gson gson = new Gson();
	
	public String toJson(Object object)
	{
		return gson.toJson(object);
	}
	
	public JsonObject fromJson(String input, Class<JsonObject> classOfT)
	{
		return gson.fromJson(input, classOfT);
	}
}
