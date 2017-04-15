package com.parents;

import java.util.Date;
import java.util.Random;

import com.database.DatabaseHelper;
import com.labels.LabyrinthMessages;

public abstract class LabyrinthModel
{
	protected DatabaseHelper dbh;
	protected LabyrinthMessages messages = new LabyrinthMessages();
	
	protected Random rand = new Random();
	
	protected Date createdAt;
	protected Date updatedAt;
	protected Date deletedAt;

	public LabyrinthModel()
	{
		dbh = DatabaseHelper.getInstance();
	}
	
	public void setDbh(DatabaseHelper dbh)
	{
		this.dbh = dbh;
	}
	protected DatabaseHelper getDbh()
	{
		return this.dbh;
	}
	
	protected Date getCreatedAt() { return this.createdAt; }
	protected void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
	protected Date getUpdatedAt() { return this.updatedAt; }
	protected void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
	protected Date getDeletedAt() { return this.deletedAt; }
	protected void setDeletedAt(Date deletedAt) { this.deletedAt = deletedAt; }
}
