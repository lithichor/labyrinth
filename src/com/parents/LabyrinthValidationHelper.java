package com.parents;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonObject;

public abstract class LabyrinthValidationHelper
{
	protected ArrayList<String> errors = new ArrayList<String>();
	
	public ArrayList<String> getErrors()
	{
		return errors;
	}
	

	public abstract boolean validate(HashMap<String, String> params);
	
	public abstract LabyrinthModel validateApi(JsonObject json);
}
