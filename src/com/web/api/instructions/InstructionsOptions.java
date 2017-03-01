package com.web.api.instructions;

import com.parents.LabyrinthOptions;

public class InstructionsOptions extends LabyrinthOptions
{
	public InstructionsOptions()
	{
		basics = "The Instructions endpoint returns basic instructions on how to use this API";
		
		get.put("general", "The Get verb returns the instructions");
		get.put("url", "/api/instructions");
		
		delete = null;
		fields = null;
		post = null;
		put = null;
		seeAlso = null;
	}
}
