package com;

/*
 * TODO: GAME #29 - setup localization file
 * In the future, it might be better to use:
 * http://docs.oracle.com/javaee/6/tutorial/doc/bnaxw.html
 * 
 * Constants are okay for now, though
 */
public class LabyrinthConstants
{
	public final static String HORRIBLY_WRONG = "Yikes! Something went horribly wrong with your request";
	
	public final static String NO_GAME_ID = "You must provide an id to delete a game";
	public final static String NO_GAME = "There is no Game associated with this User";
	public final static String UNKNOWN_ERROR = "Something went wrong with your request";
	public final static String NO_SUCH_USER = "There is no user mahching those credentials";
	public final static String NO_SUCH_PLAYER = "There is no player matching those credentials";
	
	//User validation constants
	public final static String USER_NEEDS_FIRST_NAME = "The Player needs to have a first name";
	public final static String USER_NEEDS_LAST_NAME = "The Player needs to have a last name";
	public final static String USER_NEEDS_EMAIL = "You need to include an email address";
	public final static String USER_NEEDS_PASSWORD = "The Player needs a password";
	public final static String PASSWORD_NEEDS_CONFIRMATION = "The passwords needs to be confirmed";
	public final static String MALFORMED_JSON = "It looks like there was a problem with your JSON; see if you can fix it, then try again";
}
