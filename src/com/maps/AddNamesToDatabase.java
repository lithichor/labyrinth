package com.maps;

import java.sql.SQLException;
import java.util.ArrayList;

import com.database.DatabaseHelper;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.parents.LabyrinthException;

public class AddNamesToDatabase
{
	public static void main(String[] args)
	{
		ArrayList<String> nouns = null;
		ArrayList<String> adjectives = null;
		
		try
		{
			nouns = MapName.getAllNames("nouns");
			adjectives = MapName.getAllNames("adjectives");
		}
		catch (LabyrinthException le)
		{
			le.printStackTrace();
		}
		
		for(String n: nouns)
		{
			insertIntoDatabase(n, "nouns");
		}
		
		System.out.println("++++++++++++++++++++++++");

		for(String a: adjectives)
		{
			insertIntoDatabase(a, "adjectives");
		}
	}
	
	private static void insertIntoDatabase(String item, String type)
	{
		DatabaseHelper dbh = DatabaseHelper.getInstance();
		String sql = "INSERT INTO map_xxx (yyy) VALUES (?)";
		ArrayList<Object> params = new ArrayList<>();
		if("adjectives".equals(type))
		{
			params.add(item);
		}
		else
		{
			sql = sql.replace("yyy", "yyy, type").replace("?", "?, ?");
			params.add(item.split(",")[0]);
			params.add(item.split(",")[1]);
		}

		try
		{
			String sql2 = sql.replace("xxx", type).replace("yyy", type.substring(0, type.length() - 1));
			dbh.execute(sql2, params);
		}
		catch(MySQLIntegrityConstraintViolationException constraintEx)
		{
			System.out.println(item + " already exists");
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
		}
		finally
		{
			System.out.println("done");
		}
	}
}
