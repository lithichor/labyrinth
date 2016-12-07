package com.parents;

import java.io.Serializable;

import com.database.DatabaseHelper;
import com.labels.LabyrinthMessages;

public abstract class LabyrinthModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected DatabaseHelper dbh;
	protected LabyrinthMessages messages = new LabyrinthMessages();
	
	public LabyrinthModel()
	{
		dbh = DatabaseHelper.getInstance();		
	}
	
	protected DatabaseHelper getDbh()
	{
		return this.dbh;
	}
}
