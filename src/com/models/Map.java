package com.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.models.Tile.Boundary;
import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class Map extends LabyrinthModel
{
	private static final long serialVersionUID = 8887895302650203935L;

	private Integer id;
	private Integer gameId;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
	private ArrayList<ArrayList<Tile>> grid = new ArrayList<>();

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getGameId() { return gameId; }
	public void setGameId(Integer gameId) { this.gameId = gameId; }
	public Date getCreatedAt() { return createdAt; }
	public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
	public Date getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
	public Date getDeletedAt() { return deletedAt; }
	public void setDeletedAt(Date deletedAt) { this.deletedAt = deletedAt; }
	public ArrayList<ArrayList<Tile>> getGrid() { return this.grid; }
	public void setGrid(ArrayList<ArrayList<Tile>> grid) { this.grid = grid; }
	
	public ArrayList<Map> load(Integer gameId, Integer mapId) throws LabyrinthException
	{
		ArrayList<Map> maps = new ArrayList<>();
		ArrayList<Object> params = new ArrayList<>();
		ResultSet results = null;
		String sql = "SELECT id, "
				+ "game_id, "
				+ "created_at, "
				+ "updated_at "
				+ "FROM maps ";
		// if a gameId is provided (also if both ids are provided - which
		// should never happen, but it might)
		if(gameId > 0)
		{
			sql += "WHERE game_id = ? ";
			params.add(gameId);			
		}
		// if a mapId is provided
		else if(mapId > 0)
		{
			sql += "WHERE id = ? ";
			params.add(mapId);
		}
		// throw an exception if neither is provided
		else
		{
			throw new LabyrinthException(messages.getMessage("map.no_ids"));
		}
		sql += "AND deleted_at IS NULL ORDER BY id DESC";

		try
		{
			results = dbh.executeQuery(sql, params);
			
			while(results.next())
			{
				Map map = new Map();
				map.setCreatedAt(new Date(results.getTimestamp("created_at").getTime()));
				map.setUpdatedAt(new Date(results.getTimestamp("updated_at").getTime()));
				map.setGameId(results.getInt("game_id"));
				map.setId(results.getInt("id"));
				
				maps.add(map);
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		if(maps.size() == 0)
		{
			throw new LabyrinthException(messages.getMessage("map.no_maps_for_game"));
		}
		
		return maps;
	}
	
	public void deleteMaps(Integer gameId) throws LabyrinthException
	{
		String sql = "UPDATE maps SET deleted_at = now() WHERE game_id = ?";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(gameId);
		
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
	
	public boolean save() throws LabyrinthException
	{
		boolean success = false;
		String sql = "INSERT INTO maps (game_id, created_at, updated_at) "
				+ "VALUES(?, now(), now())";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(this.gameId);
		ResultSet keys = null;
		
		try
		{
			keys = dbh.executeAndReturnKeys(sql, params);
			while(keys.next())
			{
				this.id = keys.getInt(1);
				success = true;
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}
		
		return success;
	}
	
	/**
	 * This is the first method to generate a map. Later
	 * efforts will be more complex. The idea is to have
	 * multiple types of Labyrinths that are generated at
	 * random.
	 * @return
	 * @throws LabyrinthException
	 */
	public boolean generateMap() throws LabyrinthException
	{
		// first save the map; this creates an ID for it.
		boolean success = save();
		
		// then generate the grid of Tiles (need the Map's ID)
		for(int x = 0; x < 10; x++)
		{
			ArrayList<Tile> column = new ArrayList<>();
			for(int y = 0; y < 10; y++)
			{
				Tile t = new Tile(x, y, this.id);
				column.add(t);
			}
			grid.add(column);
		}
		
		// finally, go through the grid, creating walls
		Random rand = new Random();
		for(int x = 0; x < grid.size(); x++)
		{
			ArrayList<Tile> column = grid.get(x);
			for(int y = 0; y < column.size(); y++)
			{
				Tile t = column.get(y);
				//north-south
				if(x == 0)
				{
					t.setNorth(Boundary.WALL);
				}
				else if(x == 9)
				{
					t.setSouth(Boundary.WALL);
				}
				else if(rand.nextInt(10) < 3)
				{
					t.setNorth(Boundary.WALL);
					try
					{
						grid.get(x - 1).get(y).setSouth(Boundary.WALL);
					}
					catch(ArrayIndexOutOfBoundsException aioob)
					{
						System.out.println("\n\nERROR:\nX: " + x + "\nY: " + y);
						throw new ArrayIndexOutOfBoundsException();
					}
				}
				//east-west
				if(y == 0)
				{
					t.setWest(Boundary.WALL);
				}
				else if(y == 9)
				{
					t.setEast(Boundary.WALL);
				}
				else if(rand.nextInt(10) < 3)
				{
					t.setWest(Boundary.WALL);
					try
					{
						column.get(y - 1).setEast(Boundary.WALL);
					}
					catch(ArrayIndexOutOfBoundsException aioob)
					{
						System.out.println("\n\nERROR:\nX: " + x + "\nY: " + y);
						throw new ArrayIndexOutOfBoundsException();
					}
				}
				// save the tile
				t.save();
			}
		}
		System.out.println(toString());
		return success;
	}
	
	public String toString()
	{
		String southWalls = "";
		String eastWalls = "";
		
		for(int x = 0; x < grid.size(); x++)
		{
			ArrayList<Tile> col = grid.get(x);
			for(int y = 0; y < col.size(); y++)
			{
				Tile t = col.get(y);
				if(t.getSouth() == Boundary.WALL)
				{
					southWalls += "(" + x + ", " + y + "), ";
				}
				if(t.getEast() == Boundary.WALL)
				{
					eastWalls += "(" + x + ", " + y + "), ";
				}
			}
		}
		
		return "SOUTH: " + southWalls + "\nEAST: " + eastWalls;
	}
}
