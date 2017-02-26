package com.web.api.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class User extends LabyrinthModel
{
	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private transient String password;
	
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

	public boolean save() throws LabyrinthException
	{
		boolean success = false;
		String sql = "INSERT INTO users (first_name, last_name, email, password, created_at, updated_at) "
				+ "VALUES(?, ?, ?, ?, ?, ?)";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(this.firstName);
		params.add(this.lastName);
		params.add(this.email);
		params.add(this.password);
		params.add(this.createdAt);
		params.add(this.updatedAt);
		
		try
		{
			success = dbh.execute(sql, params);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}
		
		this.id = this.retrieveId();
		
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
		if(this.getLastName() != null && !"".equalsIgnoreCase(this.getLastName()))
		{
			if(!first)
			{
				sql += ", ";
				first = false;
			}
			sql += "last_name = ? ";
			params.add(this.getLastName());
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
		
		sql += ", updated_at = now() WHERE id = ?";
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
		boolean hasResults = false;
		
		params.add(email);
		params.add(password);
		
		try
		{
			results = dbh.executeQuery(sql, params);
			while(results.next())
			{
				hasResults = true;
				user.setId(results.getInt("id"));
				// set the ID for this object so we can record the login time
				this.setId(results.getInt("id"));
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
		
		// if the query returns nothing, throw exception
		if(!hasResults)
		{
			throw new LabyrinthException(messages.getMessage("user.no_such_player"));
		}
		
		setLoginTime();
		return user;
	}
	
	public boolean duplicateEmail() throws LabyrinthException
	{
		boolean hasDupe = false;
		
		String sql = "SELECT email FROM users WHERE email like ?";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(this.email);
		ResultSet results = null;
		
		try
		{
			results = this.getDbh().executeQuery(sql, params);
			
			while(results.next())
			{
				hasDupe = true;
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}
		
		return hasDupe;
	}
	
	/**
	 * This method merges a User into the current User. New
	 * fields take precedence, except for id and createdAt;
	 * these are immutable, but might be set on a new User
	 * object from values loaded from disk. We never keep the
	 * password in memory after authentication.
	 * 
	 * @param other - User object to be merged into this
	 */
	public void merge(User other)
	{
		if(this.id == null || this.id == 0)
		{
			this.id = other.getId();
		}
		if(this.createdAt == null)
		{
			this.createdAt = other.getCreatedAt();
		}
		if(other.firstName != null && !"".equals(other.firstName))
		{
			this.firstName = other.getFirstName();
		}
		if(other.lastName != null && !"".equals(other.lastName))
		{
			this.lastName = other.getLastName();
		}
		if(other.password != null && !"".equals(other.password))
		{
			this.password = other.getPassword();
		}
	}
	
	private Integer retrieveId()
	{
		String sql = "SELECT id FROM users WHERE email like ? AND deleted_at IS NULL";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(this.getEmail());
		int id = 0;
		ResultSet results = null;
		
		try
		{
			results = dbh.executeQuery(sql, params);
			
			while(results.next())
			{
				id = results.getInt("id");
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
		}
		
		return id;
	}
	
	public void setLoginTime() throws LabyrinthException
	{
		String sql = "UPDATE users SET last_login = now() WHERE id = ?";
		ArrayList<Object> params = new ArrayList<>();
		params.add(this.id);
		try
		{
			dbh.execute(sql, params);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}
	}
}
