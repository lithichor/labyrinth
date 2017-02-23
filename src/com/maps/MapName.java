package com.maps;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import com.parents.LabyrinthException;

public class MapName
{
	/**
	 * Static method to get a randomly generated Map name
	 * @return
	 * @throws LabyrinthException
	 */
	public static String[] getMapName() throws LabyrinthException
	{
		Random rand = new Random();
		URL nouns = ClassLoader.getSystemResource("com/maps/nouns.txt");
		URL adjectives = ClassLoader.getSystemResource("com/maps/adjectives.txt");
		
		String nounStr = "";
		String adjStr = "";

		try
		{
			nounStr = new Scanner(new File(nouns.getFile())).useDelimiter("\\Z").next();
			adjStr = new Scanner(new File(adjectives.getFile())).useDelimiter("\\Z").next();
			
		}
		catch(FileNotFoundException fnfe)
		{
			fnfe.printStackTrace();
			throw new LabyrinthException("Error finding the map name files: " + fnfe.getMessage());
		}
		
		ArrayList<String> nounAL = new ArrayList<String>(Arrays.asList(nounStr.trim().split("\n")));
		ArrayList<String> adjAL = new ArrayList<String>(Arrays.asList(adjStr.trim().split("\n")));
		
		String n = nounAL.get(rand.nextInt(nounAL.size()));
		String a = adjAL.get(rand.nextInt(adjAL.size()));
		
		String[] mapName = {a, n};
		return mapName;
	}
}
