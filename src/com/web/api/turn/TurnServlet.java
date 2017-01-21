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

public class TurnServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 4222322558201838792L;

	/**
	 * Return the current state of the Player
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		Turn turn = new Turn();
		APITurn apiTurn = null;
		TurnServletActions actions = new TurnServletActions();
		Integer turnId = getIdFromUrl(request, EndpointsWithIds.TURNS);
		
		try
		{
			User user = actions.authenticateUser(request);
			turn = turn.loadByUserAndTurn(user.getId(), turnId);
			apiTurn = new APITurn(turn);
			
			if(turn == null)
			{
				errors.add(messages.getMessage("turn.no_turn_for_id"));
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
			apiOut(gson.toJson(apiTurn), response);
		}
	}

	/**
	 * Used by the player to make a move
	 */
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
	}

}
