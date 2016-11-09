package com.parents;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonObject;
import com.labels.LabyrinthMessages;

public abstract class LabyrinthValidationHelper
{
	protected ArrayList<String> errors = new ArrayList<String>();
	protected LabyrinthMessages messages = new LabyrinthMessages();
	
	public ArrayList<String> getErrors()
	{
		return errors;
	}
	

	public abstract boolean validate(HashMap<String, String> params);
	
	public abstract LabyrinthModel validateApi(JsonObject json);
}
