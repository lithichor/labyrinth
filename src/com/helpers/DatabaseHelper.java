package com.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class DatabaseHelper
{
	Connection conn = null;
	Properties props = new Properties();
	String databaseUrl = "jdbc:mysql://localhost:3306/labyrinth";
	private String username = "root";
	private String password = "";
	
	public DatabaseHelper()
	{
		props.put("user", username);
		props.put("password", password);
		
		try
		{
			conn = DriverManager.getConnection(databaseUrl, props);
		}
		catch(SQLException sqe)
		{
			sqe.printStackTrace();
		}
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
		}
		
		return prep.execute();
	}
}
