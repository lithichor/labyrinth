package com.helpers.validation;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonObject;
import com.parents.LabyrinthModel;

public abstract class ValidationHelper
{
	protected ArrayList<String> errors = new ArrayList<String>();
	
	public ArrayList<String> getErrors()
	{
		// clear the errors when returning them
		ArrayList<String> retErr = errors;
		this.errors.clear();
		return retErr;
	}
	

	public abstract boolean validate(HashMap<String, String> params);
	
	public abstract LabyrinthModel validateApi(JsonObject json);
}
