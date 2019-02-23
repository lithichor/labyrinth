package com.web.api.turn;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.models.api.APIErrorMessage;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;
import com.web.api.user.User;

public class TurnsGameServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = -5314489077659984779L;
	private Turn turn;
	private User user;
	private TurnsServletActions actions;
	private TurnsGameOptions options;
	
	public TurnsGameServlet()
	{
		this.turn = new Turn();
		this.user = new User();
		this.actions = new TurnsServletActions();
		this.options = new TurnsGameOptions();
	}
	
	public void setTurn(Turn turn) { this.turn = turn; }
	public void setUser(User user) { this.user = user; }
	public void setActions(TurnsServletActions actions) { this.actions = actions; }
	public void setOptions(TurnsGameOptions options) { this.options = options; }

	/**
	 * Return the turn associated with a Game ID
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		Integer gameId = actions.getIdFromUrl(request, EndpointsWithIds.TURNS_GAMES);
		
		try
		{
			user = actions.authenticateUser(request);
			turn = turn.loadByUserAndGame(user.getId(), gameId);
			
			if(turn == null)
			{
				errors.add(messages.getMessage("turn.no_turn_for_game_id"));
			}
		}
		catch(LabyrinthException le)
		{
			errors.add(le.getMessage());
		}
		
		if(errors.size() > 0)
		{
			apiOut(gson.toJson(new APIErrorMessage(errors)), response);
		}
		else
		{
			APITurn apiTurn = new APITurn();
			apiTurn.setId(turn.getId());
			apiTurn.setCombatId(turn.getCombatId());
			apiTurn.setCoords(turn.getCoords());
			apiTurn.setGameId(turn.getGameId());
			apiTurn.setInCombat(turn.isInCombat());
			if(turn.getCombatId() == null || turn.getCombatId() <= 0)
			{
				apiTurn.setCombatId(turn.getCombatId());
			}
			else
			{
				apiTurn.setCombatId(null);
			}
			apiTurn.setIteration(turn.getIteration());
			apiTurn.setMapId(turn.getMapId());
			apiTurn.setUserId(turn.getUserId());
			
			apiOut(gson.toJson(apiTurn), response);
		}
	}

	public void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
		apiOut(gson.toJson(options), response);
	}
}
