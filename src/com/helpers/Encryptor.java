package com.helpers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import sun.misc.BASE64Encoder;

public class Encryptor
{
	private static Encryptor instance = null;
	private MessageDigest md = null;

	private Encryptor(){}
	
	public synchronized String encrypt(String text)
	{
		try
		{
			md = MessageDigest.getInstance("SHA");
			md.update(text.getBytes("UTF-8"));
		}
		catch(NoSuchAlgorithmException nsae)
		{
			System.out.println("Error getting encryption algorithm: " + nsae.getMessage());
			nsae.printStackTrace();
		}
		catch(UnsupportedEncodingException uee)
		{
			System.out.println("Error encoding string: " + uee.getMessage());
			uee.printStackTrace();
		}
		
		byte raw[] = md.digest();
		String hash = (new BASE64Encoder()).encode(raw);
		
		return hash;
	}
	
	public synchronized String decrypt(String text)
	{
//	String auth = Base64.decodeBase64(authorization[1]).toString();
//	String auth1 = DatatypeConverter.parseBase64Binary(authorization[1]).toString();
		
		//after much toil and trouble, I discovered this works. Don't know why auth and auth1 don't
		//http://stackoverflow.com/a/10319155
		return StringUtils.newStringUtf8(Base64.decodeBase64(text));
	}

	public static Encryptor getInstance()
	{
		if(Encryptor.instance == null)
		{
			instance = new Encryptor();
		}
		return Encryptor.instance;
	}
}
