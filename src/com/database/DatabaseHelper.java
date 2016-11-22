package com.database;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import com.mysql.jdbc.Statement;

public class DatabaseHelper implements Serializable
{
	private static final long serialVersionUID = -9175632877354809313L;
	private Connection conn = null;
	private Properties props = new Properties();
	private String databaseUrl = "jdbc:mysql://localhost:3306/labyrinth";
	private static DatabaseHelper instance = null;
	
	private String username = "root";
	private String password = "";
	
	private DatabaseHelper()
	{
		props.put("user", username);
		props.put("password", password);
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(databaseUrl, props);
		}
		catch(SQLException sqe)
		{
			sqe.printStackTrace();
		}
		catch(ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
	}
	
	public static DatabaseHelper getInstance()
	{
		if(instance == null)
		{
			instance = new DatabaseHelper();
		}
		return instance;
	}
	
	public ResultSet executeQuery(String sql) throws SQLException
	{
		PreparedStatement prep = conn.prepareStatement(sql);

		ResultSet results = prep.executeQuery();
		return results;
	}

	public ResultSet executeQuery(String sql, ArrayList<Object> params) throws SQLException
	{
		System.out.println("QUERY: " + sql);
		
		PreparedStatement prep = conn.prepareStatement(sql);
		for(int x = 0; x < params.size(); x++)
		{
			if(params.get(x) instanceof String)
			{
				prep.setString(x+1, (String) params.get(x));
			}
			if(params.get(x) instanceof Integer)
			{
				prep.setInt(x+1, (Integer)params.get(x));
			}
		}
		
		return prep.executeQuery();
	}

	public boolean execute(String sql, ArrayList<Object> params) throws SQLException
	{
		System.out.println("QUERY: " + sql);
		
		PreparedStatement prep = conn.prepareStatement(sql);
		for(int x = 0; x < params.size(); x++)
		{
			if(params.get(x) instanceof String)
			{
				prep.setString(x+1, (String) params.get(x));
			}
			if(params.get(x) instanceof Integer)
			{
				prep.setInt(x+1, (Integer)params.get(x));
			}
			if(params.get(x) instanceof Date)
			{
				long time = ((Date)params.get(x)).getTime();
				prep.setTimestamp(x+1, new Timestamp(time));
			}
		}
		
		return prep.execute();
	}

	/**
	 * This method is generally for use when saving an object that doesn't
	 * have a distinct set of fields that can be used instead of the primary
	 * key. In that situation we have to get the ID assigned to the inserted
	 * record rather than query for it using other  fields.
	 * 
	 * This may be more efficient than making two queries, so it might be
	 * useful to research this as the main way of saving objects.
	 * 
	 * Found this technique on StackOverflow:
	 * http://stackoverflow.com/questions/1915166/how-to-get-the-insert-id-in-jdbc
	 * 
	 * @param sql - String representation of the SQL query
	 * @param params - the parameters for the prepared statement
	 * @return A resultSet of the generated keys (IDs)
	 * @throws SQLException
	 */
	public ResultSet executeAndReturnKeys(String sql, ArrayList<Object> params) throws SQLException
	{
		System.out.println("QUERY: " + sql);
		
		PreparedStatement prep = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		for(int x = 0; x < params.size(); x++)
		{
			if(params.get(x) instanceof String)
			{
				prep.setString(x+1, (String) params.get(x));
			}
			if(params.get(x) instanceof Integer)
			{
				prep.setInt(x+1, (Integer)params.get(x));
			}
			if(params.get(x) instanceof Date)
			{
				long time = ((Date)params.get(x)).getTime();
				prep.setTimestamp(x+1, new Timestamp(time));
			}
		}
		
		prep.execute();
		ResultSet keys = prep.getGeneratedKeys();
		return keys;
	}
}
