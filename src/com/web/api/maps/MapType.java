package com.web.api.maps;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class MapType extends LabyrinthModel
{
	private Integer id;
	private String adjective;
	private String noun;
	private String type;
	
	public MapType() {}
	public MapType(String name)
	{
		this.noun = name;
	}
	
	public Integer getId() { return this.id; }
	public void setId(Integer id) { this.id = id; }
	public String getAdjective() { return this.adjective; }
	public void setAdjective(String adjective) { this.adjective = adjective; }
	public String getNoun() { return this.noun; }
	public void setNoun(String noun) { this.noun = noun; }
	public String getType() { return this.type; }
	public void setType(String type) { this.type = type; }
	
	public String getName()
	{
		if(this.adjective != null && this.noun != null)
		{
			return this.adjective + " " + this.noun;
		}
		else
		{
			return "This MapType has no name";
		}
	}
	
	public MapType load() throws LabyrinthException
	{
		String maxId = "SELECT max(id) FROM map_";
		String sql = "SELECT x FROM map_ WHERE id = ?";
		int id = 0;
		ArrayList<Object> params = new ArrayList<>();
		params.add(null);
		ResultSet results = null;
		Random rand = new Random();

		try
		{
			// load the adjective and name at random
			for(String field: new String[]{"adjectives", "nouns"})
			{
				results = dbh.executeQuery(maxId.replace("map_", "map_" + field));
				while(results.next())
				{
					id = results.getInt(1);
				}
				if(id == 0)
				{
					throw new LabyrinthException(messages.getMessage("map_type.no_id"));
				}

				// add a random int (based on maxId) to the parameters
				params.set(0, rand.nextInt(id));

				results = dbh.executeQuery(sql.replace("map_", "map_" + field).replace("x", field.substring(0, field.length() - 1)), params);
				while(results.next())
				{
					String str = results.getString(1);
					// set the appropriate instance variable using reflection
					Field variable = this.getClass().getDeclaredField(field.substring(0, field.length() - 1));
					variable.setAccessible(true);
					variable.set(this, str);
				}
			}
			
			// get the type for the map's noun
			sql = "SELECT type FROM map_nouns WHERE noun LIKE ?";
			params.set(0, this.noun);
			
			results = dbh.executeQuery(sql, params);
			while(results.next())
			{
				this.type = results.getString(1);
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		catch(NoSuchFieldException nsfe)
		{
			nsfe.printStackTrace();
			System.out.println("Why doesn't the instance variable exist?" + nsfe.getMessage());
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		catch(IllegalAccessException iae)
		{
			iae.printStackTrace();
			System.out.println("Why can't we access the instance variable?" + iae.getMessage());
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		return this;
	}
	
	public static void main(String[] args)
	{
		MapType mt = new MapType();
		try
		{
			mt.load();
		}
		catch(LabyrinthException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("ADJ: " + mt.getAdjective());
		System.out.println("NOUN: " + mt.getNoun());
		System.out.println("TYPE: " + mt.getType());
	}
}
