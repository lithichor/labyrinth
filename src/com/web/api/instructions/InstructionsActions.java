package com.web.api.instructions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.parents.LabyrinthException;
import com.parents.LabyrinthServletActions;

public class InstructionsActions extends LabyrinthServletActions
{
	public ArrayList<String> getFileNames(String apiFolder) throws LabyrinthException
	{
		ArrayList<String> fileNames = new ArrayList<>();
		try(Stream<Path> paths = Files.walk(Paths.get(apiFolder)))
		{
			// get all files ending in Servlet.class (this is searching
			// the compiled classes, not the java files). We're only
			// interested in the child servlets, not the parent class.
			paths.filter(s -> s.toString().contains("Servlet.class"))
			.filter(s -> !(s.toString().contains("LabyrinthHttpServlet")))
			.forEach(f -> fileNames.add(formatFileNames(f.getFileName().toString())));
		}
		catch (IOException ioe)
		{
			// this means something terrible happened while scanning the file structure
			ioe.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		return fileNames;
	}
	
	private String formatFileNames(String fileName)
	{
		String sep = System.getProperty("file.separator");
		String file = "";
		
		// split the name using uppercase letters and remove the unwanted part
		String[] ff = fileName.replace("Servlet.class", "").split("(?=\\p{Upper})");
		
		// reassemble the class name by inserting slashes between words
		for(int x = 0; x < ff.length; x++)
		{
			file += sep + ff[x];
		}
		
		// prepend "api" and return it (it's now in URL format)
		return "api" + file.toLowerCase();
	}
}
