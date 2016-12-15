package com.parents;

import java.io.Serializable;
import java.util.Date;

import com.database.DatabaseHelper;
import com.labels.LabyrinthMessages;

public abstract class LabyrinthModel implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	protected DatabaseHelper dbh;
	protected LabyrinthMessages messages = new LabyrinthMessages();
	
	protected Date createdAt;
	protected Date updatedAt;
	protected Date deletedAt;

	public LabyrinthModel()
	{
		dbh = DatabaseHelper.getInstance();		
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
