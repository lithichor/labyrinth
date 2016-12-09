package com.models.api;

import java.util.ArrayList;

import com.parents.LabyrinthAPIModel;

public class APIErrorMessage extends LabyrinthAPIModel
{
	private String message = null;
	private ArrayList<String> messages = null;
	
	public APIErrorMessage(){}
	public APIErrorMessage(String message)
	{
		this.message = "";
		this.message = message;
	}
	public APIErrorMessage(ArrayList<String> messages)
	{
		if(messages.size() == 1)
		{
			this.message = messages.get(0);
		}
		else
		{
			this.messages = new ArrayList<String>();
			this.messages = messages;
		}
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
