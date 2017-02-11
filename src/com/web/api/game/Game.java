package com.web.api.game;

import java.util.Date;
import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.models.Hero;
import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;
import com.web.api.map.Map;
import com.web.api.turn.Turn;

public class Game extends LabyrinthModel
{
	private Integer id;
	private Integer userId;

	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }
	
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
				game.setCreatedAt(new Date(results.getTimestamp("created_at").getTime()));
				game.setUpdatedAt(new Date(results.getTimestamp("updated_at").getTime()));
				games.add(game);
			}
			if(games == null || games.size() == 0)
			{
				if(gameId == 0)
				{
					throw new LabyrinthException(messages.getMessage("game.no_games"));
				}
				else
				{
					throw new LabyrinthException(messages.getMessage("game.no_game_with_that_id"));
				}
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
			map.generateMap();
			
			Turn turn = new Turn();
			turn.setGameId(game.getId());
			turn.setUserId(game.getUserId());
			turn.setMapId(map.getId());
			turn.setCoords(new Point(0, 0));
			turn.save();

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
	
	/**
	 * Delete a Game. The ID must be set on the Game object to successfully
	 * delete it. This is a soft deleted; it changes the deleted_at
	 * field to the current time, but does not destroy data.
	 * @throws LabyrinthException
	 */
	public void deleteGame() throws LabyrinthException
	{
		this.setDeletedAt(new Date());
		this.setUpdatedAt(new Date());
		
		String sql = "UPDATE games SET updated_at = now(), deleted_at = now() WHERE id = ?";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(this.id);
		
		try
		{
			dbh.execute(sql, params);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}
				
		try
		{
			new Hero().deleteHero(this.getId());
			new Map().deleteMaps(this.getId());
			new Turn().delete(this.getUserId());
		}
		catch (LabyrinthException le)
		{
			throw new LabyrinthException(le);
		}
	}
	
	/**
	 * Save a new Game. Not applicable for games that have already been
	 * created; for those use update(). Sets the created_at and updated_at
	 * fields to the current time.
	 * 
	 * @throws LabyrinthException
	 */
	public boolean save() throws LabyrinthException
	{
		boolean success = false;
		int gameId = 0;
		ResultSet results = null;
		String sql = "INSERT INTO games (user_id, created_at, updated_at) VALUES(?, now(), now())";
		ArrayList<Object> params = new ArrayList<>();
		params.add(this.userId);
		
		try
		{
			results = dbh.executeAndReturnKeys(sql, params);
			while(results.next())
			{
				gameId = results.getInt(1);
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}
		
		if(gameId > 0)
		{
			success = true;
			this.id = gameId;
		}
		else
		{
			success = false;
			throw new LabyrinthException(messages.getMessage("unknown.no_id_returned"));
		}
		
		return success;
	}
	
	/**
	 * Updates a Game with new information. Changes the updated_at
	 * field to the current time.
	 * 
	 * @throws LabyrinthException
	 */
	public boolean update() throws LabyrinthException
	{
		boolean success = false;
		String sql = "UPDATE games SET ";
		ArrayList<Object> params = new ArrayList<>();
		
		if(this.getUserId() != null && !(this.getUserId() == 0))
		{
			sql += "user_id = ? ";
			params.add(this.getUserId());
		}
		
		sql += ", updated_at = now() WHERE id = ?";
		params.add(this.id);
		
		try
		{
			success = dbh.execute(sql, params);
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			throw new LabyrinthException(sqle);
		}

		return success;
	}
}
