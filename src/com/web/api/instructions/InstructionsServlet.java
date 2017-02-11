package com.web.api.instructions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.parents.LabyrinthHttpServlet;

public class InstructionsServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 3608493802099931891L;

	/**
	 * This requires no authentication; the purpose is to give the user some
	 * idea of how to use the API.
	 * 
	 * The response includes a list of endpoints the user can make calls
	 * to. These are gathered by scanning the file structure for Servlet
	 * classes. By convention, these are named the same way as the URLs
	 * that call them. Roughly.
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		ArrayList<String> fileNames = new ArrayList<>();
		HashMap<String, Object> info = new HashMap<>();
		
		// get all files ending in Servlet.class (this is searching
		// the compiled classes, not the java files). We're only
		// interested in the child servlets, not the parent class.
		String apiFolder = getServletContext().getRealPath(".");
		
		try(Stream<Path> paths = Files.walk(Paths.get(apiFolder)))
		{
			paths.filter(s -> s.toString().contains("Servlet.class"))
			.filter(s -> !(s.toString().contains("LabyrinthHttpServlet")))
			.forEach(f -> fileNames.add(formatFileNames(f.getFileName().toString())));
		}
		catch (IOException ioe)
		{
			// this means something terrible happened while scanning the file structure
			ioe.printStackTrace();
			apiOut(gson.toJson(messages.getMessage("unknown.horribly_wrong")), response);
			return;
		}
		
		info.put("endpoints", fileNames);

		apiOut(gson.toJson(info), response);
	}
	
	private String formatFileNames(String fileName)
	{
		String file = "";
		
		// split the name using uppercase letters and remove the unwanted part
		String[] ff = fileName.replace("Servlet.class", "").split("(?=\\p{Upper})");
		
		// reassemble the class name by inserting slashes between words
		for(int x = 0; x < ff.length; x++)
		{
			file += "/" + ff[x];
		}
		
		// prepend "api" and return it (it's now in URL format)
		return "api" + file.toLowerCase();
	}
}
