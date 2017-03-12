package test.tests.combats;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.gson.JsonObject;
import com.models.constants.EndpointsWithIds;
import com.parents.LabyrinthException;
import com.web.api.combat.APICombat;
import com.web.api.combat.Combat;
import com.web.api.combat.CombatsServlet;
import com.web.api.combat.CombatsServletActions;
import com.web.api.user.User;

import test.parents.LabyrinthHttpTest;

public class CombatsServletGetTests extends LabyrinthHttpTest
{
	private CombatsServlet combatsServlet;
	private CombatsServletActions combatActions;
	
	@Before
	public void setup()
	{
		combatsServlet = new CombatsServlet();
		combatActions = mock(CombatsServletActions.class);
	}
	
	@Test
	public void testDoGetOutputIsNoAuthError() throws IOException, ServletException, LabyrinthException
	{
		when(request.getRequestURI()).thenReturn("api/combats/14");
		when(response.getWriter()).thenReturn(printer);

		combatsServlet.doGet(request, response);
		
		// get the errors the servlet is sending in the response
		JsonObject messageObj = gson.fromJson(strWriter.getBuffer().toString(), JsonObject.class);
		String message = messageObj.get("message").getAsString();
		
		assertEquals("The message doesn't match what we expected", message, messages.getMessage("user.no_authorization"));
	}
	
	@Test
	public void testDoGetOutputIsNoIDErrorStrID() throws IOException, ServletException, LabyrinthException
	{
		when(request.getRequestURI()).thenReturn("api/combats/wahwah");
		when(response.getWriter()).thenReturn(printer);

		combatsServlet.doGet(request, response);
		
		// get the errors the servlet is sending in the response
		JsonObject messageObj = gson.fromJson(strWriter.getBuffer().toString(), JsonObject.class);
		String message = messageObj.get("message").getAsString();
		
		assertEquals("The message doesn't match what we expected", message, messages.getMessage("combat.no_id"));
	}
	
	@Test
	public void testDoGetOutputIsNoIDError() throws IOException, ServletException, LabyrinthException
	{
		when(request.getRequestURI()).thenReturn("");
		when(response.getWriter()).thenReturn(printer);

		combatsServlet.doGet(request, response);
		
		// get the errors the servlet is sending in the response
		JsonObject messageObj = gson.fromJson(strWriter.getBuffer().toString(), JsonObject.class);
		String message = messageObj.get("message").getAsString();
		
		assertEquals("The message doesn't match what we expected", message, messages.getMessage("combat.no_id"));
	}
	
	@Test
	public void testDoGetOutputIsApiCombatObj() throws IOException, ServletException, LabyrinthException
	{
		User u = new User();
		u.setId(rand.nextInt(1000));
		Combat c = new Combat();
		c.setId(14);
		c.setUserId(u.getId());
		APICombat apiCombat = new APICombat(c);
		
		combatsServlet.setActions(combatActions);
		
		when(request.getRequestURI()).thenReturn("api/combats/14");
		when(response.getWriter()).thenReturn(printer);
		when(combatActions.getIdFromUrl(request, EndpointsWithIds.COMBATS)).thenReturn(14);
		when(combatActions.authenticateUser(request)).thenReturn(u);
		when(combatActions.getApiCombat(u.getId(), 14)).thenReturn(apiCombat);
		
		combatsServlet.doGet(request, response);
		
		String combatStr = gson.toJson(apiCombat);

		// strWriter has a new line at the end
		assertTrue(combatStr.equals(strWriter.getBuffer().toString().trim()));
	}
}
