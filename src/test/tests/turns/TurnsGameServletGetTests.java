package test.tests.turns;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.parents.LabyrinthException;
import com.web.api.turn.Turn;
import com.web.api.turn.TurnsGameServlet;
import com.web.api.turn.TurnsServletActions;
import com.web.api.user.User;

import test.parents.LabyrinthHttpTest;

public class TurnsGameServletGetTests extends LabyrinthHttpTest
{
	private TurnsGameServlet servlet;
	private Turn turn;
	private User user;
	private TurnsServletActions actions;
	
	@Before
	public void setup() throws IOException
	{
		super.testSetup();
		
		user = mock(User.class);
		turn = mock(Turn.class);
		actions = mock(TurnsServletActions.class);
		
		servlet = new TurnsGameServlet();
		servlet.setTurn(turn);
		servlet.setActions(actions);
	}
	
	@Test
	public void testLoadTurnThrowsException() throws LabyrinthException, ServletException, IOException
	{
		String errorMessage = "Excepted";
		
		when(actions.authenticateUser(request)).thenReturn(user);
		when(turn.loadByUserAndGame(0, 0)).thenThrow(new LabyrinthException("Excepted"));
		
		servlet.doGet(request, response);
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}
	
	@Test
	public void testLoadTurnReturnsNull() throws LabyrinthException, ServletException, IOException
	{
		String errorMessage = messages.getMessage("turn.no_turn_for_game_id");
		
		when(actions.authenticateUser(request)).thenReturn(user);
		when(turn.loadByUserAndGame(0, 0)).thenReturn(null);
		
		servlet.doGet(request, response);
		String messageStr = strWriter.getBuffer().toString();
		JsonObject messageObj = gson.fromJson(messageStr, JsonObject.class);

		assertEquals(errorMessage, messageObj.get("message").getAsString());
	}
}
