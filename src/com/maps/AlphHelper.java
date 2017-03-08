package com.maps;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Stream;

public class AlphHelper
{
	private String sep = System.getProperty("file.separator");
	private String mf = System.getProperty("user.dir");
	
	public ArrayList<String> getListOfFiles()
	{
		ArrayList<String> fileNames = new ArrayList<>();
		// put this in a general helper file
		String mapsFolder = mf + sep + "src" + sep + "com" + sep + "maps";
		
		try(Stream<Path> paths = Files.walk(Paths.get(mapsFolder)))
		{
			paths.filter(s -> s.toString().contains(".txt"))
			.forEach(f -> fileNames.add(f.toString()));
		}
		catch (IOException ioe)
		{
			// this means something terrible happened while scanning the file structure
			ioe.printStackTrace();
			System.out.println("Oh noes! Error while scanning files: " + ioe.getMessage());
		}
		
		return fileNames;
	}
	
	public ArrayList<String> loadFileAndSort(String f)
	{
		String shortenedPath = f.replace(mf + sep + "src" + sep, "");
		// we have to have user.dir and src to get the file's
		// path, but when we load it we need to remove them.
		URL file = ClassLoader.getSystemResource(shortenedPath);
		String contentStr = "";
		
		try(Scanner scan = new Scanner(new File(file.getFile())))
		{
			contentStr = scan.useDelimiter("\\Z").next();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
			System.out.println("Error scanning file: " + ioe.getMessage());
		}
		catch(NoSuchElementException nsee)
		{
			// the file is empty
			System.out.println("This file is empty: " + shortenedPath);
			return null;
		}
		
		ArrayList<String> contents = new ArrayList<String>(Arrays.asList(contentStr.split("\n")));
//		for(String str: contents)
//		{
//			str = str.trim();
//		}
		
		Collections.sort(contents, new Comparator<String>()
		{
			public int compare(String one, String two)
			{
				return one.compareTo(two);
			}
		});

		return contents;
	}
	
	public ArrayList<String> capitalizeNames(ArrayList<String> contents)
	{
		ArrayList<String> sortedAndCapitalized = new ArrayList<>();

		for(String s: contents)
		{
			if(s.length() > 0)
			{
				String[] strArray = s.toLowerCase().split(" ");
				s = "";
				for(String ss: strArray)
				{
					if(ss.length() > 0)
					{
						ss = ss.substring(0, 1).toUpperCase() + ss.substring(1);
						s += " " + ss;
					}
				}
				
				sortedAndCapitalized.add(s.trim());
			}
		}

		return sortedAndCapitalized;
	}

	public ArrayList<String> alphabetize(String f)
	{
		ArrayList<String> contents = loadFileAndSort(f);
		
		if(contents != null)
		{
			contents = capitalizeNames(contents);
			
			for(String s: contents)
			{
				System.out.println(s);
			}
		}
		
		return contents;
	}
	
	public void writeToFile(ArrayList<String> list, String fileName)
	{
		try
		{
			Files.write(Paths.get(fileName), list, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
		}
		catch(IOException ioe)
		{
			System.out.println("Yikes! - " + ioe.getMessage());
		}
		catch(NullPointerException npe)
		{
			System.out.println("looks like an empty file got in there: " + fileName);
		}
	}
}
