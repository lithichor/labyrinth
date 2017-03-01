package com.web.api.constants;

import com.parents.LabyrinthOptions;

public class ConstantsOptions extends LabyrinthOptions
{
	public ConstantsOptions()
	{
		basics = "The Constants endpoint returns all the constant values used in The Labyrinth";
		
		get.put("general", "The Get verb returns the constants");
		get.put("url", "/api/constants");
		
		delete = null;
		fields = null;
		post = null;
		put = null;
		seeAlso = null;
	}
}
