package com.parents;

public class LabyrinthException extends Exception
{
	private static final long serialVersionUID = -1242191878331013412L;

	public LabyrinthException()
	{
		super();
	}
	
	public LabyrinthException(String message)
	{
		super(message);
	}
	
	public LabyrinthException(Exception e)
	{
		super(e);
	}
}
