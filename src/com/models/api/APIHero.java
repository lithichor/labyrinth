package com.models.api;

import com.models.Hero;
import com.parents.LabyrinthAPIModel;

public class APIHero extends LabyrinthAPIModel
{
	private Integer id;
	private Integer gameId;
	private Integer health;
	private Integer strength;
	private Integer magic;
	private Integer attack;
	private Integer defense;
	
	public APIHero(Hero hero)
	{
		this.id = hero.getId();
		this.gameId = hero.getGameId();
		this.health = hero.getHealth();
		this.strength = hero.getStrength();
		this.magic = hero.getMagic();
		this.attack = hero.getAttack();
		this.defense = hero.getDefense();
	}
	
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public Integer getGameId()
	{
		return gameId;
	}
	public void setGameId(Integer gameId)
	{
		this.gameId = gameId;
	}
	public Integer getHealth()
	{
		return health;
	}
	public void setHealth(Integer health)
	{
		this.health = health;
	}
	public Integer getStrength()
	{
		return strength;
	}
	public void setStrength(Integer strength)
	{
		this.strength = strength;
	}
	public Integer getMagic()
	{
		return magic;
	}
	public void setMagic(Integer magic)
	{
		this.magic = magic;
	}
	public Integer getAttack()
	{
		return attack;
	}
	public void setAttack(Integer attack)
	{
		this.attack = attack;
	}
	public Integer getDefense()
	{
		return defense;
	}
	public void setDefense(Integer defense)
	{
		this.defense = defense;
	}
}
