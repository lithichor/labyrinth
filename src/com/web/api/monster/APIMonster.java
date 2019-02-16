package com.web.api.monster;

import com.parents.LabyrinthAPIModel;

public class APIMonster extends LabyrinthAPIModel
{
	private Integer id;
	private Integer tileId;
	private Integer health;
	private Integer attack;
	private Integer defense;
	
	public APIMonster(){}
	public APIMonster(Monster m)
	{
		this.id = m.getId();
		this.tileId = m.getTileId();
		this.health = m.getHealth();
		this.attack = m.getAttack();
		this.defense = m.getDefense();
	}

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getTileId() { return tileId; }
	public void setTileId(Integer tileId) { this.tileId = tileId; }
	public Integer getHealth() { return health; }
	public void setHealth(Integer health) { this.health = health; }
	public Integer getAttack() { return attack; }
	public void setAttack(Integer attack) { this.attack = attack; }
	public Integer getDefense() { return defense; }
	public void setDefense(Integer defense) { this.defense = defense; }
}
