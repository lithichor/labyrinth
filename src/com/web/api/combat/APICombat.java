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
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((heroId == null) ? 0 : heroId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((monsterId == null) ? 0 : monsterId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		APICombat other = (APICombat) obj;
		if(heroId == null)
		{
			if(other.heroId != null)
				return false;
		}
		else if(!heroId.equals(other.heroId))
			return false;
		if(id == null)
		{
			if(other.id != null)
				return false;
		}
		else if(!id.equals(other.id))
			return false;
		if(monsterId == null)
		{
			if(other.monsterId != null)
				return false;
		}
		else if(!monsterId.equals(other.monsterId))
			return false;
		if(userId == null)
		{
			if(other.userId != null)
				return false;
		}
		else if(!userId.equals(other.userId))
			return false;
		return true;
	}
}
