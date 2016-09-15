package com.models.api;

import com.parents.LabyrinthAPIModel;

public class APIErrorMessage extends LabyrinthAPIModel
{
	private String message;
	
	public APIErrorMessage(){}
	public APIErrorMessage(String message)
	{
		this.message = message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
	public String getMessage()
	{
		return this.message;
	}
}
