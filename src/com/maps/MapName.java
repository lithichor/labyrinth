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
		String sep = System.getProperty("file.separator");
		URL nouns = ClassLoader.getSystemResource("com" + sep + "maps" + sep + "nouns.txt");
		URL adjectives = ClassLoader.getSystemResource("com" + sep + "maps" + sep + "adjectives.txt");
		
		String nounStr = "";
		String adjStr = "";

		try(Scanner scan = new Scanner(new File(nouns.getFile())))
		{
			nounStr = scan.useDelimiter("\\Z").next();
		}
		catch(FileNotFoundException fnfe)
		{
			fnfe.printStackTrace();
			throw new LabyrinthException("Error finding the map name files: " + fnfe.getMessage());
		}
		try(Scanner scan = new Scanner(new File(adjectives.getFile())))
		{
			adjStr = scan.useDelimiter("\\Z").next();
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
	
	public static ArrayList<String> getAllNames(String type) throws LabyrinthException
	{
		String sep = System.getProperty("file.separator");
		
		if("nouns".equals(type))
		{
			URL nouns = ClassLoader.getSystemResource("com" + sep + "maps" + sep + "nouns.txt");
			String nounStr = "";
			
			try(Scanner scan = new Scanner(new File(nouns.getFile())))
			{
				nounStr = scan.useDelimiter("\\Z").next();
			}
			catch(FileNotFoundException fnfe)
			{
				fnfe.printStackTrace();
				throw new LabyrinthException("Error finding the map name files: " + fnfe.getMessage());
			}

			return new ArrayList<String>(Arrays.asList(nounStr.trim().split("\n")));
		}
		else if("adjectives".equals(type))
		{
			URL adjectives = ClassLoader.getSystemResource("com" + sep + "maps" + sep + "adjectives.txt");
			String adjStr = "";

			try(Scanner scan = new Scanner(new File(adjectives.getFile())))
			{
				adjStr = scan.useDelimiter("\\Z").next();
			}
			catch(FileNotFoundException fnfe)
			{
				fnfe.printStackTrace();
				throw new LabyrinthException("Error finding the map name files: " + fnfe.getMessage());
			}
			
			return new ArrayList<String>(Arrays.asList(adjStr.trim().split("\n")));
		}
		else
		{
			System.out.println("The Type was not correct: " + type);
			return null;
		}
	}
}
