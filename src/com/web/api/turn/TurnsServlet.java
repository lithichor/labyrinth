package com.web.api.turn;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.models.api.APIErrorMessage;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;
import com.web.api.user.User;

public class TurnsServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 4222322558201838792L;

	private Turn turn;
	private TurnsServletActions actions;
	private TurnsOptions options;
	
	public TurnsServlet()
	{
		turn = new Turn();
		actions = new TurnsServletActions();
		options = new TurnsOptions();
	}
	
	public void setTurn(Turn turn) { this.turn = turn; }
	public void setActions(TurnsServletActions actions) { this.actions = actions; }
	public void setOptions(TurnsOptions options) { this.options = options; }
	
	/**
	 * Return the current state of the Player
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		errors.clear();
		APITurn apiTurn = null;
		Integer turnId = actions.getIdFromUrl(request, EndpointsWithIds.TURNS);
		
		try
		{
			User user = actions.authenticateUser(request);
			// return the errors if the user is not authenticated
			if(user == null)
			{
				errors.add(messages.getMessage("user.no_authorization"));
				apiOut(gson.toJson(new APIErrorMessage(errors)), response);
				return;
			}
			
			turn = turn.loadByUserAndTurn(user.getId(), turnId);
			
			if(turn == null)
			{
				errors.add(messages.getMessage("turn.no_turn_for_id"));
			}
			else
			{
				apiTurn = new APITurn(turn);
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
		errors.clear();
		
		Integer turnId = actions.getIdFromUrl(request, EndpointsWithIds.TURNS);
		JsonObject data = null;
		String direction = "";
		
		try
		{
			User user = actions.authenticateUser(request);
			turn = turn.loadByUserAndTurn(user.getId(), turnId);
			if(turn == null)
			{
				errors.add(messages.getMessage("turn.no_turn_for_id"));
			}
			else if(turn.isInCombat())
			{
				errors.add(messages.getMessage("turn.in_combat"));
			}
			else
			{
				data = actions.getData(request);
				if(data == null)
				{
					throw new LabyrinthException(messages.getMessage("turn.no_data"));
				}
				if(data.has("direction"))
				{
					try
					{
						direction = data.get("direction").getAsString();
					}
					catch (UnsupportedOperationException | IllegalStateException uoe_ise)
					{
						throw new LabyrinthException(messages.getMessage("turn.direction_not_string"));
					}
					turn = actions.makeMove(direction.toLowerCase(), turn);
					turn.update();
				}
				else
				{
					errors.add(messages.getMessage("turn.invalid_data"));
				}
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
