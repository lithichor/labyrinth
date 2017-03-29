package com.helpers;

import java.util.Random;

public class AttackHelper
{
	private static Random rand = new Random();
	
	public static Integer initializeAttributes()
	{
		int init = 1;
		for(int x = 0; x < 4; x++)
		{
			init += rand.nextInt(5) + 1;
		}
		return new Integer(init);
	}
}