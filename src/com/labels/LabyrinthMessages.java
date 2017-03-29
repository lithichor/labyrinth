package com.labels;

import java.util.ResourceBundle;

/**
 * This loads resource bundles for labels. The no argument constructor
 * loads error messages, but other label files can be loaded by passing
 * in a String of the fully qualified file name.
 * 
 * A particular label can then be retrieved by using the getMessage method.
 * 
 * @author spiralgyre
 *
 */
public class LabyrinthMessages
{
	public ResourceBundle messages;
	
	public LabyrinthMessages()
	{
		messages = ResourceBundle.getBundle("com.labels.errorMessages");
	}
	public LabyrinthMessages(String type)
	{
		messages = ResourceBundle.getBundle(type);
	}
	
	/**
	 * Public method to get the message we want. ResourceBundle already
	 * throws an exception when the key can't be found in the resource,
	 * so we don't need to add any checks for it
	 * 
	 * @param messageKey - a String representing the key for the message
	 * we want to retrieve
	 * 
	 * @return the message value associated with messageKey
	 */
	public String getMessage(String messageKey)
	{
		return messages.getString(messageKey);
	}
	
	/**
	 * Get a message with a parameter
	 * @param messageKey
	 * @param replacement
	 * @return
	 */
	public String getMessage(String messageKey, String replacement)
	{
		String str = messages.getString(messageKey);
		str.replace("{0}", replacement);
		return str;
	}
	
	/**
	 * Get a message with a parameter
	 * @param messageKey
	 * @param replacement
	 * @return
	 */
	public String getMessage(String messageKey, Integer replacement)
	{
		String str = messages.getString(messageKey);
		str.replace("{0}", replacement.toString());
		return str;
	}
}
