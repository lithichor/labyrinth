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

public class TurnsServletGetTests extends LabyrinthHttpServletTest
{
	private TurnsServlet servlet;
	private TurnsServletActions actions;
	private Turn turn;
	private User user;
	
	@Before
	public void setup() throws IOException, LabyrinthException
	{
		super.testSetup();
		
		actions = mock(TurnsServletActions.class);
		turn = mock(Turn.class);
		
		user = new User();
		servlet = new TurnsServlet();
		servlet.setActions(actions);
		servlet.setTurn(turn);
		
		when(actions.authenticateUser(request)).thenReturn(user);
	}
	
	@Test
	public void testTurnsDoGet() throws ServletException, IOException, LabyrinthException
	{
		int userId = 4;
		int turnId = 3;
		Turn t = new Turn();
		t.setId(turnId);
		t.setUserId(userId);
		
		user.setId(userId);
		when(actions.getIdFromUrl(request, EndpointsWithIds.TURNS)).thenReturn(turnId);
		when(turn.loadByUserAndTurn(userId, turnId)).thenReturn(t);
		
		servlet.doGet(request, response);
		
		String returnStr = strWriter.getBuffer().toString();
		JsonObject apiJson = gson.fromJson(returnStr, JsonObject.class);
		
		assertEquals(false, apiJson.get("inCombat").getAsBoolean());
		assertEquals(userId, apiJson.get("userId").getAsInt());
		assertEquals(turnId, apiJson.get("id").getAsInt());
		System.out.println(returnStr);
	}
	
	@Test
	public void testLoadTurnThrowsException() throws ServletException, IOException, LabyrinthException
	{
		String errorMessage = "Exception";
		
		when(turn.loadByUserAndTurn(null, 0)).thenThrow(new LabyrinthException(errorMessage));
		
		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}
	
	@Test
	public void testLoadTurnReturnsNull() throws ServletException, IOException, LabyrinthException
	{
		String errorMessage = messages.getMessage("turn.no_turn_for_id");
		
		when(turn.loadByUserAndTurn(null, 0)).thenReturn(null);
		
		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}
	
	@Test
	public void testAuthenticateUserThrowsException() throws ServletException, IOException, LabyrinthException
	{
		String errorMessage = "Exception";
		
		when(turn.loadByUserAndTurn(null, 0)).thenReturn(turn);
		when(actions.authenticateUser(request)).thenThrow(new LabyrinthException(errorMessage));
		
		servlet.doGet(request, response);
		
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}
}
