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

public class TurnGameServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = -5314489077659984779L;

	/**
	 * Return the turn associated with a Game ID
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		Turn turn = null;
		TurnServletActions actions = new TurnServletActions();
		Integer gameId = getIdFromUrl(request, EndpointsWithIds.TURNS_GAMES);
		
		try
		{
			User user = actions.authenticateUser(request);
			turn = new Turn().loadByUserAndGame(user.getId(), gameId);
			
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
}
