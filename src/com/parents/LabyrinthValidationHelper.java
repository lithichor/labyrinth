package com.parents;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.labels.LabyrinthMessages;

public abstract class LabyrinthValidationHelper
{
	protected ArrayList<String> errors = new ArrayList<String>();
	protected LabyrinthMessages messages = new LabyrinthMessages();
	protected Gson gson = new Gson();
	
	public ArrayList<String> getErrors()
	{
		return errors;
	}
	
	public String getErrorsAsString()
	{
		String errorStr = "";
		
		for(String s: errors)
		{
			errorStr += s + "\n";
		}
		return errorStr;
	}

	public abstract boolean validate(HashMap<String, String> params);
	
	public abstract LabyrinthModel validateApi(JsonObject json);
	
	public abstract LabyrinthModel validateApiPut(JsonObject data, Integer id) throws LabyrinthException;
}
