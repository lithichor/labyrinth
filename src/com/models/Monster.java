package com.models;

import com.parents.LabyrinthModel;

public class Monster extends LabyrinthModel
{
	private Integer id;
	private Integer health;
	private Integer attack;
	private Integer defense;
	
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getHealth() { return health; }
	public void setHealth(Integer health) { this.health = health; }
	public Integer getAttack() { return attack; }
	public void setAttack(Integer attack) { this.attack = attack; }
	public Integer getDefense() { return defense; }
	public void setDefense(Integer defense) { this.defense = defense; }
	
	// monsters are not stored in the database, so
	// there's no need for the save and load methods
}
