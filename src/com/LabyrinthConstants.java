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
	// Unknown errors
	public final static String MALFORMED_JSON = "It looks like there was a problem with your JSON; see if you can fix it, then try again";
	public final static String UNKNOWN_ERROR = "Something went wrong with your request";
	public final static String HORRIBLY_WRONG = "Yikes! Something went horribly wrong with your request";
	
	// Game error messages
	public final static String NO_GAME_ID = "You must provide an id to delete a Game";
	public final static String NO_GAME = "This Player has no active Games";
	public final static String NO_GAME_WITH_THAT_ID = "This Player doesn't have an active Game with that ID";
	public final static String TOO_MANY_GAMES = "This Player has too many active Games. Delete some before trying to create another";
	
	// User error messages
	public final static String NO_SUCH_USER = "There is no User matching that email-password combination";
	public final static String NO_SUCH_PLAYER = "There is no Player matching that email-password combination";
	public final static String NO_AUTHORIZATION = "You must include authorization";
	
	// User login error messages
	public final static String MUST_ENTER_PASSWORD = "You can't leave the password blank";
	public final static String MUST_ENTER_EMAIL = "You have to enter your email address";
	
	
	//User signup validation constants
	public final static String USER_NEEDS_FIRST_NAME = "The Player needs to have a first name";
	public final static String USER_NEEDS_LAST_NAME = "The Player needs to have a last name";
	public final static String USER_NEEDS_EMAIL = "You need to include an email address";
	public final static String USER_NEEDS_PASSWORD = "The Player needs a password";
	public final static String PASSWORD_NEEDS_CONFIRMATION = "The password needs to be confirmed";
	public final static String PROBLEM_SAVING_USER = "There was a problem saving the Player";
}
