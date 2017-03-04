package com.web.api.instructions;

import com.parents.LabyrinthOptions;

public class InstructionsOptions extends LabyrinthOptions
{
	public InstructionsOptions()
	{
		basics = "The Instructions endpoint returns basic instructions on how to use this API";
		
		fields.put("instructions", "String");
		fields.put("endpoints", "Array");
		
		get.put("general", "The Get verb returns the instructions");
		get.put("url", "/api/instructions");
		
		delete = null;
		post = null;
		put = null;
		seeAlso = null;
	}
}
