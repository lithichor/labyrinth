package com.maps;

import com.parents.LabyrinthException;

public class TestMapNames
{
	public static void main(String[] args)
	{
		try
		{
			for(int x = 0; x < 1000; x++)
			{
				String[] name = MapName.getMapName();
				System.out.println(name[0] + " " + name[1]);
			}
		}
		catch (LabyrinthException le)
		{
			le.printStackTrace();
		}
	}
}
