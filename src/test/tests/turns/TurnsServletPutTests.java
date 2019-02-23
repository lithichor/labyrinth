package test.tests.turns;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.web.api.turn.Turn;
import com.web.api.turn.TurnsServlet;
import com.web.api.turn.TurnsServletActions;
import com.web.api.user.User;

import test.parents.LabyrinthHttpServletTest;

public class TurnsServletPutTests extends LabyrinthHttpServletTest
{
	private User user;
	private Turn turn;
	private TurnsServlet servlet;
	private TurnsServletActions actions;
	
	@Before
	public void setup() throws IOException
	{
		super.testSetup();
		
		turn = mock(Turn.class);
		actions = mock(TurnsServletActions.class);
		
		user = new User();
		servlet = new TurnsServlet();
		servlet.setTurn(turn);
		servlet.setActions(actions);
	}
	
	@Test
	public void testTurnsDoPut() throws ServletException, IOException, LabyrinthException
	{
		int userId = 2;
		int gameId = 2;
		int turnId = 5;
		int combatId = 55;
		user.setId(userId);

		Turn t = new Turn();
		t.setId(turnId);
		t.setUserId(userId);
		t.setGameId(gameId);
		t.setInCombat(false);
		
		Turn t2 = mock(Turn.class);
		when(t2.getId()).thenReturn(turnId);
		when(t2.getUserId()).thenReturn(userId);
		when(t2.isInCombat()).thenReturn(true);
		when(t2.getCombatId()).thenReturn(combatId);
		
		String dataStr = "{direction: w}";
		JsonObject dataObj = gson.fromJson(dataStr, JsonObject.class);
		
		when(actions.getIdFromUrl(request, EndpointsWithIds.TURNS)).thenReturn(turnId);
		when(actions.authenticateUser(request)).thenReturn(user);
		when(turn.loadByUserAndTurn(userId, turnId)).thenReturn(t);
		when(actions.getData(request)).thenReturn(dataObj);
		when(actions.makeMove("w", t)).thenReturn(t2);

		servlet.doPut(request, response);
		
		String returnStr = strWriter.getBuffer().toString();
		
		System.out.println(returnStr);
	}
	
	@Test
	public void testLoadTurnThrowsException() throws ServletException, IOException, LabyrinthException
	{
		String errorMessage = "Exception";
		
		when(actions.authenticateUser(request)).thenReturn(user);
		when(turn.loadByUserAndTurn(null, 0)).thenThrow(new LabyrinthException("Exception"));
		
		servlet.doPut(request, response);

		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testLoadTurnReturnsNull() throws ServletException, IOException, LabyrinthException
	{
		String errorMessage = messages.getMessage("turn.no_turn_for_id");

		when(actions.authenticateUser(request)).thenReturn(user);
		when(turn.loadByUserAndTurn(null, 0)).thenReturn(null);

		servlet.doPut(request, response);

		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testTurnIsInCombat() throws ServletException, IOException, LabyrinthException
	{
		String errorMessage = messages.getMessage("turn.in_combat");

		when(turn.isInCombat()).thenReturn(true);
		when(actions.authenticateUser(request)).thenReturn(user);
		when(turn.loadByUserAndTurn(null, 0)).thenReturn(turn);

		servlet.doPut(request, response);

		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testGetDataReturnsNull() throws ServletException, IOException, LabyrinthException
	{
		String errorMessage = messages.getMessage("turn.no_data");

		when(actions.authenticateUser(request)).thenReturn(user);
		when(turn.loadByUserAndTurn(null, 0)).thenReturn(turn);

		servlet.doPut(request, response);

		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testGetDataThrowsException() throws ServletException, IOException, LabyrinthException
	{
		String errorMessage = "Exception";

		when(actions.getData(request)).thenThrow(new LabyrinthException("Exception"));
		when(actions.authenticateUser(request)).thenReturn(user);
		when(turn.loadByUserAndTurn(null, 0)).thenReturn(turn);

		servlet.doPut(request, response);

		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testDataHasHashForString() throws ServletException, IOException, LabyrinthException
	{
		String errorMessage = messages.getMessage("turn.direction_not_string");
		JsonObject hashForString = gson.fromJson("{direction: {a: a}}", JsonObject.class);

		when(actions.getData(request)).thenReturn(hashForString);
		when(actions.authenticateUser(request)).thenReturn(user);
		when(turn.loadByUserAndTurn(null, 0)).thenReturn(turn);

		servlet.doPut(request, response);

		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}

	@Test
	public void testDataHasArrayForString() throws ServletException, IOException, LabyrinthException
	{
		String errorMessage = messages.getMessage("turn.direction_not_string");
		JsonObject arrayForString = gson.fromJson("{direction: [a, b, c]}", JsonObject.class);

		when(actions.getData(request)).thenReturn(arrayForString);
		when(actions.authenticateUser(request)).thenReturn(user);
		when(turn.loadByUserAndTurn(null, 0)).thenReturn(turn);

		servlet.doPut(request, response);

		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}
}
