package com.parents;

import java.util.HashMap;

/**
 * Some of the ideas for using the Options verb were taken from
 * http://zacstewart.com/2012/04/14/http-options-method.html
 * 
 */
public class LabyrinthOptions
{
	protected String basics;
	protected HashMap<String, String> get = new HashMap<>();
	protected HashMap<String, String> delete = new HashMap<>();
	protected HashMap<String, String> post = new HashMap<>();
	protected HashMap<String, String> put = new HashMap<>();
	
	public String getBasics() { return this.basics; }
	public HashMap<String, String> getDelete() { return this.delete; }
	public HashMap<String, String> getGet() { return this.get; }
	public HashMap<String, String> getPost() { return this.post; }
	public HashMap<String, String> getPut() { return this.put; }
}
