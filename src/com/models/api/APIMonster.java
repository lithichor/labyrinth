package com.models.api;

import com.models.Monster;
import com.parents.LabyrinthAPIModel;

public class APIMonster extends LabyrinthAPIModel
{
	private Integer health;
	private Integer attack;
	private Integer defense;
	
	public APIMonster(Monster m)
	{
		this.health = m.getHealth();
		this.attack = m.getAttack();
		this.defense = m.getDefense();
	}

	public Integer getHealth() { return health; }
	public void setHealth(Integer health) { this.health = health; }
	public Integer getAttack() { return attack; }
	public void setAttack(Integer attack) { this.attack = attack; }
	public Integer getDefense() { return defense; }
	public void setDefense(Integer defense) { this.defense = defense; }
}
