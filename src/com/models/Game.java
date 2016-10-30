package com.models;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.hibernate.HibernateException;

import com.LabyrinthConstants;
import com.models.api.APIGame;
import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class Game extends LabyrinthModel
{
	private static final long serialVersionUID = -8019526580596155206L;
	private Integer id;
	private Integer userId;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;

	public Game()
	{
		this.setCreatedAt(new Date());
		this.setUpdatedAt(new Date());
	}

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }
	public Date getCreatedAt() { return createdAt; }
	public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
	public Date getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
	public Date getDeletedAt() { return deletedAt; }
	public void setDeletedAt(Date deletedAt) { this.deletedAt = deletedAt; }
	
	// there is no explicit ordering here - when we change to jdbc we need it 
/**
 * Load a list of games based on the id of the authenticated user
 * @param userId - the id of the authenticated user
 * @param gameId - the id of the game to load. If gameId is zero, return
 * 				   all active games for the user
 * @return a list of active games for the authenticate user
 * @throws LabyrinthException
 */
	public ArrayList<Game> load(Integer userId, int gameId) throws LabyrinthException
	{
		this.getSession();

		ArrayList<Game> games = null;

		try
		{
			trans = session.beginTransaction();
			String query = "FROM Game g WHERE user_id = :user_id AND deleted_at is null";
			if(gameId > 0)
			{
				query += " and id = :id";
				games = (ArrayList<Game>)session.createQuery(query)
						.setParameter("user_id", userId)
						.setParameter("id", gameId).list();
			}
			else
			{
				games = (ArrayList<Game>)session.createQuery(query)
						.setParameter("user_id", userId).list();
			}
			if(games == null || games.size() == 0)
			{
				throw new LabyrinthException(LabyrinthConstants.NO_GAME);
			}

			trans.commit();
		}
		catch(HibernateException he)
		{
			games = null;
			if(trans != null)
			{
				trans.rollback();
			}
			throw new LabyrinthException(he);
		}
		catch(Exception e)
		{
			games = null;
			if(trans != null)
			{
				trans.rollback();
			}
			throw new LabyrinthException(e);
		}

		return games;
	}
	
	public int getGameCount(Integer userId) throws LabyrinthException
	{
		int gameCount = 0;
		ResultSet results = null;
		String sql = "SELECT count(id) AS count FROM games WHERE user_id = ? AND deleted_at IS NULL";
		ArrayList<Object> params = new ArrayList<Object>();
		
		params.add(userId);
		
		try
		{
			results = dbh.executeQuery(sql, params);
			while(results.next())
			{
				gameCount = results.getInt("count");
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}
		
		return gameCount;
	}
	
	public APIGame startNewGame(Integer userId) throws LabyrinthException
	{
		APIGame g = null;
		try
		{
			Game game = new Game();
			game.setUserId(userId);
			game.save();

			Hero hero = new Hero();
			hero.setGameId(game.getId());

			Map map = new Map();
			map.setGameId(game.getId());

			hero.save();
			map.save();

			g = new APIGame(game);
			g.setHeroId(hero.getId());
			g.addMapId(map.getId());
		}
		catch(LabyrinthException le)
		{
			System.out.println(le.getMessage());
			le.printStackTrace();
			throw new LabyrinthException(le);
		}
		
		return g;
	}
	
	public void deleteGame() throws LabyrinthException
	{
		this.setDeletedAt(new Date());
		this.setUpdatedAt(new Date());
		try
		{
			this.save();
			new Hero().deleteHero(this.getId());
			new Map().deleteMaps(this.getId());
		}
		catch (LabyrinthException le)
		{
			throw new LabyrinthException(le);
		}
	}
	
	public boolean Equals(Game other)
	{
		if(this == other) { return true; }
		if(!(other instanceof Game)) { return false; }
		
		final Game game = (Game)other;
		
		if(!(this.getId() == game.getId())) { return false; }
		if(!(this.getUserId() == game.getUserId())) { return false; }
		
		return true;
	}
	
	public int hashCode()
	{
		return (29 * this.getUserId()) + this.getId();
	}
}
