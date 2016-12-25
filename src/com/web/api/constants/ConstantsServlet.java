package com.web.api.constants;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.models.constants.GeneralConstants;
import com.parents.LabyrinthHttpServlet;

public class ConstantsServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 1429499032555528442L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
	{
		// this has its own gson because static modifiers 
		// are excluded from JSON by default
		Gson gson = new GsonBuilder()
				.excludeFieldsWithModifiers()
				.create();
		
		GeneralConstants constants = new GeneralConstants();
		System.out.println(constants.toString());
		apiOut(gson.toJson(constants), response);
	}
}
