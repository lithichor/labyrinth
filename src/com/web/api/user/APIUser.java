package com.web.api.user;

import java.util.ArrayList;

import com.parents.LabyrinthAPIModel;


public class APIUser extends LabyrinthAPIModel
{
	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private ArrayList<Integer> gameIds;

	public APIUser(User u)
	{
		this.id = u.getId();
		this.firstName = u.getFirstName();
		this.lastName = u.getLastName();
		this.email = u.getEmail();
	}

	public void addGame(Integer gameId)
	{
		if(gameIds == null)
		{ gameIds = new ArrayList<Integer>(); }
		gameIds.add(gameId);
	}

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public ArrayList<Integer> getGameIds(){ return this.gameIds; }
	public void setGameIds(ArrayList<Integer> games) { this.gameIds = games; }
}
