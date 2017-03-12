package test.tests.combats;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.gson.JsonObject;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.web.api.combat.Combat;
import com.web.api.combat.CombatsServlet;
import com.web.api.combat.CombatsServletActions;
import com.web.api.user.User;

import test.parents.LabyrinthHttpTest;

public class CombatsServletPutTests extends LabyrinthHttpTest
{
	private CombatsServlet combatsServlet;
	private CombatsServletActions combatActions;
	
	private User u = new User();
	private Combat mockedCombat = mock(Combat.class);
	private JsonObject data = gson.fromJson("{action: run}", JsonObject.class);
	private String endpoint = "api/combats/14";
	
	@Before
	public void setup()
	{
		combatsServlet = new CombatsServlet();
		combatActions = mock(CombatsServletActions.class);
		
		u.setId(rand.nextInt(1000));
	}
	
	@Test
	public void testDoPutOutput() throws IOException, ServletException, LabyrinthException
	{
		String expected = "This is expected";

		// set objects for mocking
		combatsServlet.setActions(combatActions);
		combatsServlet.setCombat(mockedCombat);
		
		when(request.getRequestURI()).thenReturn(endpoint);
		when(response.getWriter()).thenReturn(printer);
		when(combatActions.authenticateUser(request)).thenReturn(u);
		when(mockedCombat.load(u.getId(), 14)).thenReturn(mockedCombat);
		when(combatActions.getData(request)).thenReturn(data);
		when(combatActions.performCombatAction(data)).thenReturn(expected);
		when(combatActions.getIdFromUrl(request, EndpointsWithIds.COMBATS)).thenReturn(14);
		
		combatsServlet.doPut(request, response);
		
		// output has new line at end and is surrounded by quotes
		String servletOutput = strWriter.getBuffer().toString().trim().replace("\"", "");
		
		assertEquals(expected, servletOutput);
	}
	
	@Test
	public void testDoPutNoAuthError() throws IOException, ServletException, LabyrinthException
	{
		// set objects for mocking
		combatsServlet.setActions(combatActions);
		combatsServlet.setCombat(mockedCombat);
		
		when(request.getRequestURI()).thenReturn(endpoint);
		when(response.getWriter()).thenReturn(printer);
		when(combatActions.authenticateUser(request)).thenThrow(new LabyrinthException());
		when(combatActions.getIdFromUrl(request, EndpointsWithIds.COMBATS)).thenReturn(14);
		
		combatsServlet.doPut(request, response);
		
		String servletOutput = strWriter.getBuffer().toString().trim();
		String message = gson.fromJson(servletOutput, JsonObject.class).get("message").getAsString();
		
		assertEquals(message, messages.getMessage("user.no_authorization"));
	}
	
	@Test
	public void testDoPutNoDataError() throws IOException, ServletException, LabyrinthException
	{
		// set objects for mocking
		combatsServlet.setActions(combatActions);
		combatsServlet.setCombat(mockedCombat);
		
		when(request.getRequestURI()).thenReturn(endpoint);
		when(response.getWriter()).thenReturn(printer);
		when(combatActions.authenticateUser(request)).thenReturn(u);
		when(combatActions.getData(request)).thenReturn(null);
		when(combatActions.getIdFromUrl(request, EndpointsWithIds.COMBATS)).thenReturn(14);
		when(mockedCombat.load(u.getId(), 14)).thenReturn(mockedCombat);
		
		combatsServlet.doPut(request, response);
		
		String servletOutput = strWriter.getBuffer().toString().trim();
		String message = gson.fromJson(servletOutput, JsonObject.class).get("message").getAsString();
		
		assertEquals(message, messages.getMessage("combat.no_data"));
	}
}
