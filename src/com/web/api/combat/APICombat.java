package com.web.api.combat;

import com.parents.LabyrinthAPIModel;

public class APICombat extends LabyrinthAPIModel
{
	private Integer id;
	private Integer userId;
	private Integer heroId;
	private Integer monsterId;
	
	public APICombat(Combat combat)
	{
		this.id = combat.getId();
		this.userId = combat.getUserId();
		this.heroId = combat.getHeroId();
		this.monsterId = combat.getMonsterId();
	}

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }
	public Integer getHeroId() { return heroId; }
	public void setHeroId(Integer heroId) { this.heroId = heroId; }
	public Integer getMonsterId() { return monsterId; }
	public void setMonsterId(Integer monsterId) { this.monsterId = monsterId; }
}
