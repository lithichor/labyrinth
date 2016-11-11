package com.models;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
	
/**
 * Load a list of games based on the id of the authenticated user
 * @param userId - the id of the authenticated user
 * @param gameId - the id of the game to load. If gameId is zero, return
 * 				   all active games for the user
 * @return a list of active games for the authenticate user
 * @throws LabyrinthException
 */
	public ArrayList<Game> load(Integer userId, Integer gameId) throws LabyrinthException
	{
		ArrayList<Game> games = new ArrayList<>();
		String sql = "SELECT id, user_id, created_at, updated_at FROM games WHERE user_id = ? AND deleted_at IS NULL";
		ArrayList<Object> params = new ArrayList<>();
		ResultSet results = null;
		params.add(userId);
		
		if(gameId > 0)
		{
			sql += " AND id = ?";
			params.add(gameId);
		}
		
		sql += " ORDER BY ID ASC";

		try
		{
			results = dbh.executeQuery(sql, params);
			while(results.next())
			{
				Game game = new Game();
				game.setId(results.getInt("id"));
				game.setUserId(results.getInt("user_id"));
				game.setCreatedAt(results.getDate("created_at"));
				game.setUpdatedAt(results.getDate("updated_at"));
				games.add(game);
			}
			if(games == null || games.size() == 0)
			{
				throw new LabyrinthException(messages.getMessage("game.no_games"));
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
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
	
	/**
	 * This method merges a Game into the current Game. New
	 * fields take precedence, except for id and createdAt;
	 * these are immutable, but might be set on a new Game
	 * object from values loaded from disk
	 * 
	 * @param other - Game object to be merged into this
	 */
	public void merge(Game other)
	{
		if(this.id == null || this.id == 0)
		{
			this.id = other.getId();
		}
		if(this.createdAt == null)
		{
			this.createdAt = other.getCreatedAt();
		}
	}
	
//	public boolean Equals(Game other)
//	{
//		if(this == other) { return true; }
//		if(!(other instanceof Game)) { return false; }
//		
//		final Game game = (Game)other;
//		
//		if(!(this.getId() == game.getId())) { return false; }
//		if(!(this.getUserId() == game.getUserId())) { return false; }
//		
//		return true;
//	}
//	
//	public int hashCode()
//	{
//		return (29 * this.getUserId()) + this.getId();
//	}
}
