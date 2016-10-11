package com.models;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class User extends LabyrinthModel implements Serializable
{
	private static final long serialVersionUID = 7786071431298988718L;
	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private transient String password;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
	
	public User()
	{
		this.setCreatedAt(new Date());
		this.setUpdatedAt(new Date());
	}
	
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public Date getCreatedAt() { return createdAt; }
	public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
	public Date getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
	public Date getDeletedAt() { return deletedAt; }
	public void setDeletedAt(Date deletedAt) { this.deletedAt = deletedAt; }

	public boolean save() throws LabyrinthException
	{
		boolean success = super.save();
		this.setPassword(null);
		return success;
	}
	
	public boolean update() throws LabyrinthException
	{
		boolean success = false;
		boolean first = true;
		String sql = "UPDATE users SET ";
		ArrayList<Object> params = new ArrayList<Object>();
		
		if(this.getFirstName() != null && !"".equalsIgnoreCase(this.getFirstName()))
		{
			sql += "first_name = ? ";
			params.add(this.getFirstName());
			first = false;
		}
		if(this.getFirstName() != null && !"".equalsIgnoreCase(this.getFirstName()))
		{
			if(!first)
			{
				sql += ", ";
				first = false;
			}
			sql += "last_name = ? ";
			params.add(this.getFirstName());
		}
		if(this.getEmail() != null && !"".equalsIgnoreCase(this.getEmail()))
		{
			if(!first)
			{
				sql += ", ";
				first = false;
			}
			sql += "email = ? ";
			params.add(this.getEmail());
		}
		if(this.getPassword() != null && !"".equalsIgnoreCase(this.getPassword()))
		{
			if(!first)
			{
				sql += ", ";
				first = false;
			}
			sql += "password = ? ";
			params.add(this.getPassword());
		}
		
		sql += "WHERE id = ?";
		params.add(this.getId());
		
		try
		{
			success = dbh.execute(sql, params);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}
		
		return success;
	}
	
	public boolean deleteUser() throws LabyrinthException
	{
		String sql = "UPDATE users SET updated_at = now(), deleted_at = now() WHERE id = ?";
		
		ArrayList<Object> parameters = new ArrayList<Object>();
		parameters.add(this.getId());
		
		boolean success = false;
		
		try
		{
			success = dbh.execute(sql, parameters);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}
		
		return success;
	}
	
	public User login() throws LabyrinthException
	{
		return this.login(this.email, this.password);
	}
	
	public User login(String email, String password) throws LabyrinthException
	{
		User user = new User();
		ResultSet results = null;
		String sql = "SELECT id, first_name, last_name, email"
				+ " FROM users WHERE email = ? AND password = ? AND deleted_at IS NULL";
		ArrayList<Object> params = new ArrayList<Object>();
		
		params.add(email);
		params.add(password);
		
		try
		{
			results = user.getDbh().executeQuery(sql, params);
			while(results.next())
			{
				user.setId(results.getInt("id"));
				user.setFirstName(results.getString("first_name"));
				user.setLastName(results.getString("last_name"));
				user.setEmail(results.getString("email"));
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}
		
		return user;
	}
	
	public void _deleteUser() throws LabyrinthException
	{
		this.setDeletedAt(new Date());
		this.setUpdatedAt(new Date());
		try
		{
			this.save();
		}
		catch (LabyrinthException le)
		{
			throw new LabyrinthException(le);
		}
	}
	
	public boolean _update() throws LabyrinthException
	{
		boolean success = this.save();
		this.setUpdatedAt(new Date());
		this.setPassword(null);
		return success;
	}
}
