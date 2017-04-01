package test.utils;

import java.util.Random;

public class RandomStrings
{
	private int max = 8;
	private int min = 3;
	private Random rand = new Random();
	private int lettersPerWord = rand.nextInt((max - min) + 1) + min;
	
	//TODO: TEST #14 - do this using letter frequencies
	public String oneWord()
	{
		lettersPerWord = rand.nextInt((max - min) + 1) + min;
		String word = "";
		for(int x = 0; x < lettersPerWord; x++)
		{
			word = word += (char) (rand.nextInt((122 - 97) + 1) + 97);
		}
		return word;
	}
	
	public String sentence(int words)
	{
		String sentence = this.oneWord();
		sentence = Character.toString(sentence.charAt(0)).toUpperCase() + sentence.substring(1);
		for(int x = 0; x < words - 1; x++)
		{
			sentence += " " + this.oneWord();
		}
		return sentence + ".";
	}
	
	public static void main(String[] a)
	{
		RandomStrings rs = new RandomStrings();
		System.out.println(rs.randomInteger());
	}
	
	public int randomInteger()
	{
		return rand.nextInt(10);
	}
	
	public char getOneCharacter()
	{
		return (char) (rand.nextInt((122 - 97) + 1) + 97);
	}
}
