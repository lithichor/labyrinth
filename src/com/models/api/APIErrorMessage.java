package com.models.api;

import java.util.ArrayList;

import com.parents.LabyrinthAPIModel;

public class APIErrorMessage extends LabyrinthAPIModel
{
	private String message;
	private ArrayList<String> messages = new ArrayList<String>();
	
	public APIErrorMessage(){}
	public APIErrorMessage(String message)
	{
		this.message = message;
	}
	public APIErrorMessage(ArrayList<String> messages)
	{
		this.messages = messages;
	}

	public void addMessage(String message)
	{
		this.messages.add(message);
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
