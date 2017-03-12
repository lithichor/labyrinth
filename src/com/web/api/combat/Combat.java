package com.web.api.combat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class Combat extends LabyrinthModel
{
	private Integer id;
	private Integer userId;
	private Integer heroId;
	private Integer monsterId;
	
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }
	public Integer getHeroId() { return heroId; }
	public void setHeroId(Integer heroId) { this.heroId = heroId; }
	public Integer getMonsterId() { return monsterId; }
	public void setMonsterId(Integer monsterId) { this.monsterId = monsterId; }
	
	public Combat load(Integer userId, Integer combatId) throws LabyrinthException
	{
		Combat combat = null;
		String sql = "SELECT c.id, c.user_id, c.hero_id, c.monster_id, c.created_at, c.updated_at "
				+ "FROM combats c "
				+ "LEFT JOIN games g ON c.user_id = g.user_id "
				+ "WHERE c.deleted_at IS NULL AND g.deleted_at IS NULL AND g.user_id = ? AND c.id = ? "
				+ "GROUP BY c.id";
		ArrayList<Object> params = new ArrayList<>();
		ResultSet results = null;
		
		params.add(userId);
		params.add(combatId);
		
		try
		{
			results = dbh.executeQuery(sql, params);
			
			while(results.next())
			{
				combat = new Combat();
				combat.setId(results.getInt("c.id"));
				combat.setUserId(results.getInt("c.user_id"));
				combat.setHeroId(results.getInt("c.hero_id"));
				combat.setMonsterId(results.getInt("c.monster_id"));
				combat.setCreatedAt(new Date(results.getTimestamp("created_at").getTime()));
				combat.setUpdatedAt(new Date(results.getTimestamp("updated_at").getTime()));
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		return combat;
	}
	
	public boolean save() throws LabyrinthException
	{
		ResultSet results = null;
		int combatId = 0;
		ArrayList<Object> params = new ArrayList<>();
		String sql = "INSERT INTO combats "
				+ "(user_id, hero_id, monster_id, created_at, updated_at) "
				+ "VALUES(?, ?, ?, now(), now())";
		
		params.add(this.userId);
		params.add(this.heroId);
		params.add(this.monsterId);
		
		try
		{
			results = dbh.executeAndReturnKeys(sql, params);
			
			while(results.next())
			{
				combatId = results.getInt(1);
			}
			this.setId(combatId);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		if(this.id > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean delete() throws LabyrinthException
	{
		boolean success = false;
		String sql = "UPDATE combats SET deleted_at = now(), updated_at = now() WHERE id = ?";
		ArrayList<Object> params = new ArrayList<>();
		params.add(this.id);

		try
		{
			success = dbh.execute(sql, params);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(messages.getMessage("unknown.horribly_wrong"));
		}
		
		return success;
	}
	
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
		Combat other = (Combat) obj;
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
