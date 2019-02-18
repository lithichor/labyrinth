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
	private TurnsServletActions actions;
	private TurnsGameOptions options;
	
	public TurnsGameServlet()
	{
		this.options = new TurnsGameOptions();
	}
	
	public void setTurn(Turn turn) { this.turn = turn; }
	public void setActions(TurnsServletActions actions) { this.actions = actions; }
	public void setOptions(TurnsGameOptions options) { this.options = options; }

	/**
	 * Return the turn associated with a Game ID
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		turn = (turn == null) ? new Turn() : turn;
		actions = (actions == null) ? new TurnsServletActions() : actions;
		Integer gameId = actions.getIdFromUrl(request, EndpointsWithIds.TURNS_GAMES);
		
		try
		{
			User user = actions.authenticateUser(request);
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
			apiOut(gson.toJson(new APITurn(turn)), response);
		}
	}

	public void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		
		apiOut(gson.toJson(options), response);
	}
}
