package com.web.api.user;

import com.parents.LabyrinthOptions;

public class UserOptions extends LabyrinthOptions
{
	public UserOptions()
	{
		basics = "A User is the primary object of The Labyrinth. Nothing can be done until you "
				+ "create a User. A User is sometimes called a Player.";

		delete.put("general", "A Delete verb performs a soft delete on the authenticated user. It "
				+ "does not require the userId.");
		delete.put("url", "/api/user");
		
		fields.put("firstName", "String");
		fields.put("lastName", "String");
		fields.put("email", "String");
		fields.put("password", "String (never returned in a response)");
		
		get.put("general",  "A Get verb returns the authenticated User. A User cannot access the "
				+ "information of other Users.");
		get.put("authentication",  "The Labyrinth users Basic authentication. To authenticate a "
				+ "request you need to include the email and password in the header. If you use "
				+ "curl, it looks something like this: -u email:password");
		
		post.put("general", "A Post verb creates a new User. It has to be accompanied by a data set, "
				+ "which needs to include firstName, lastName, email, and password. This is the only "
				+ "endpoint + verb combination that does not need to be authenticated to successfully "
				+ "complete.");
		post.put("data example", "{firstName: Walter, lastName: Bishop, email: walter.bishop@thelabyrinth.com, "
				+ "password: drowssap} (All fields are required)");
		
		put.put("general",  "A Put verb has to be accompanied by a data set, just like a post verb. However, "
				+ "only the first name, last name, and password can be changed; email is immutable. This "
				+ "works on the authenticated user; the userId is not necessary.");
		put.put("data example",  "{firstName: Morland, lastName: Holmes, password: 12345} (None of the "
				+ "fields are required)");
		
		seeAlso = null;
	}
}
