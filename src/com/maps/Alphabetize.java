package com.maps;

import java.util.ArrayList;

/**
 * When you add a new monster to one of the maps or add a new noun
 * or adjective to the map names files, this utility keeps the files
 * in alphabetical order.
 * 
 * @author spiralgyre
 */
public class Alphabetize
{
	private static AlphHelper ah = new AlphHelper();
	
	public static void main(String[] args)
	{
		ArrayList<String> fileNames = ah.getListOfFiles();
		ArrayList<String> names = new ArrayList<>();
		
		for(String f: fileNames)
		{
			System.out.println("\n" + f);
			names = ah.alphabetize(f);
			ah.writeToFile(names, f);
		}
	}
	
}
