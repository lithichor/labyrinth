package com.web.api.maps;

import java.util.ArrayList;
import java.util.Random;

import com.labels.LabyrinthMessages;
import com.models.constants.GeneralConstants;
import com.parents.LabyrinthException;
import com.web.api.monster.Monster;
import com.web.api.tile.Tile;
import com.web.api.tile.Tile.Boundary;

public class MapHelper
{
	private LabyrinthMessages messages = new LabyrinthMessages();
	private MapsServletActions mapActions = new MapsServletActions();
	private Random rand = new Random();
	
	public Map getMapType(Map mapIn) throws LabyrinthException
	{
		Map map = mapIn;
		MapType mapType = null;
		try
		{
			mapType = mapActions.getMapType();
		}
		catch(LabyrinthException le)
		{
			throw le;
		}
		map.setType(mapType.getType());
		map.setName(mapType.getName());
		
		// the ID is used when making the Tiles
		boolean success = map.save();
		if(!success)
		{
			throw new LabyrinthException(messages.getMessage("map.error_creating_map"));
		}
		// set this Map's grid size
		map.setGridSize(GeneralConstants.GRID_SIZE);

		return map;
	}
	
	public Map generateMapGrid(Map mapIn) throws LabyrinthException
	{
		Map map = mapIn.clone();
		
		// we have to create the grid before adding walls, because
		// we reference things out of order when setting the walls
		for(int x = 0; x < GeneralConstants.GRID_SIZE; x++)
		{
			ArrayList<Tile> column = new ArrayList<>();
			
			for(int y = 0; y < GeneralConstants.GRID_SIZE; y++)
			{
				Tile t = new Tile(x, y, map.getId());
				column.add(t);
			}
			map.getGrid().add(column);
		}
		map = addWallsAndMonsters(map);
		
		return map;
	}
	
	public Map addWallsAndMonsters(Map mapIn) throws LabyrinthException
	{
		Map map = mapIn.clone();
	
		// now add the walls and monsters
		for(ArrayList<Tile> column: map.getGrid())
		{
			for(Tile t: column)
			{
				int x = t.getCoords().x;
				int y = t.getCoords().y;
				
				// set east-west walls on the perimeter
				if(x == 0)
				{
					t.setWest(Boundary.WALL);
				}
				else if(x == GeneralConstants.GRID_SIZE - 1)
				{
					t.setEast(Boundary.WALL);
				}
				// set walls randomly inside the maze
				if(x < GeneralConstants.GRID_SIZE - 1 && rand.nextInt(10) < 3)
				{
					t.setEast(Boundary.WALL);
					try
					{
						map.getGrid().get(x + 1).get(y).setWest(Boundary.WALL);
					}
					catch(ArrayIndexOutOfBoundsException oobe)
					{
						System.out.println("\n\nWoah! Error, Dude:\nX: " + x + "\nY: " + y);
						throw oobe;
					}
				}
				
				// set south-north walls on perimeter
				if(y == 0)
				{
					t.setNorth(Boundary.WALL);
				}
				else if(y == GeneralConstants.GRID_SIZE - 1)
				{
					t.setSouth(Boundary.WALL);
				}
				// set walls randomly inside the maze
				if(y < GeneralConstants.GRID_SIZE - 1 && rand.nextInt(10) < 3)
				{
					t.setSouth(Boundary.WALL);
					try
					{
						map.getGrid().get(x).get(y + 1).setNorth(Boundary.WALL);
					}
					catch(ArrayIndexOutOfBoundsException oobe)
					{
						System.out.println("\n\nERROR:\nX: " + x + "\nY: " + y);
						throw oobe;
					}
				}
				
				// 5% probability of a Monster in any given Tile
				if(rand.nextInt(100) <= 5)
				{
					t.setHasMonster(true);
				}
				
				// save the tile
				t.save();
				
				// set the first tile ID - (0, 0) is the first tile
				if(x == 0 && y == 0)
				{
					map.setFirstTileId(t.getId());
				}
				
				// add a monster (just one for now)
				// must add monster after tile is saved (need tileId)
				if(t.hasMonster())
				{
					Monster m = new Monster();
					m.setTileId(t.getId());
					m.save();
				}
			}
		}

		return map;
	}

	/**
	 * Save any fields for the map that couldn't be saved when it
	 * was first created.
	 * 
	 * @param mapIn
	 * @return
	 * @throws LabyrinthException
	 */
	public Map saveMap(Map mapIn) throws LabyrinthException
	{
		Map map = mapIn.clone();
		map.saveFirstTileId();
		System.out.println(map.toString());
		return map;
	}
}
