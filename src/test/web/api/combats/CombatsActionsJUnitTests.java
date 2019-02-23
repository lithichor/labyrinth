package test.web.api.combats;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;

import com.google.gson.JsonObject;
import com.parents.LabyrinthException;
import com.web.api.combat.APICombat;
import com.web.api.combat.Combat;
import com.web.api.combat.CombatsServletActions;

import test.parents.LabyrinthJUnitTest;

public class CombatsActionsJUnitTests extends LabyrinthJUnitTest
{
	@Mock
	Combat mockedCombat;
	
	@Test
	public void testGetApiCombat() throws LabyrinthException
	{
		Combat loadedCombat = createNewCombat();
		mockedCombat = mock(Combat.class);
		when(mockedCombat.load(1,  1)).thenReturn(loadedCombat);
		
		CombatsServletActions actions = new CombatsServletActions();
		actions.setCombat(mockedCombat);
		APICombat ac = actions.getApiCombat(1, 1);
		
		assertTrue("The object from the action doesn't match what we put in",
				ac.equals(new APICombat(loadedCombat)));
	}
	
	@Test
	public void testPerformCombatActionAttack() throws LabyrinthException
	{
		CombatsServletActions actions = new CombatsServletActions();
		actions.setCombat(mockedCombat);
		String dataStr = "{action: attack}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String action = actions.performCombatAction(data);
		assertEquals(action, "Attack!");
	}
	
	@Test
	public void testPerformCombatActionCast() throws LabyrinthException
	{
		CombatsServletActions actions = new CombatsServletActions();
		actions.setCombat(mockedCombat);
		String dataStr = "{action: c}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String action = actions.performCombatAction(data);
		assertEquals(action, "Abracadabra");
	}
	
	@Test
	public void testPerformCombatActionRun() throws LabyrinthException
	{
		CombatsServletActions actions = new CombatsServletActions();
		actions.setCombat(mockedCombat);
		String dataStr = "{action: rUN}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String action = actions.performCombatAction(data);
		assertEquals(action, "Run away!");
	}
	
	@Test
	public void testPerformCombatActionInvalid() throws LabyrinthException
	{
		CombatsServletActions actions = new CombatsServletActions();
		actions.setCombat(mockedCombat);
		String dataStr = "{action: urn}";
		JsonObject data = gson.fromJson(dataStr, JsonObject.class);

		String response = "";
		
		try
		{
			actions.performCombatAction(data);
		}
		catch(LabyrinthException le)
		{
			response = le.getMessage();
		}
		
		assertEquals(response, messages.getMessage("combat.not_a_valid_combat_action"));
	}
	
	@Test
	public void testPerformCombatActionsNoData()
	{
		CombatsServletActions actions = new CombatsServletActions();
		actions.setCombat(mockedCombat);
		JsonObject data = gson.fromJson("{}", JsonObject.class);

		String response = "";
		
		try
		{
			actions.performCombatAction(data);
		}
		catch(LabyrinthException le)
		{
			response = le.getMessage();
		}
		
		assertEquals(response, messages.getMessage("combat.not_a_valid_combat_action"));
	}
	
	
	
	/**
	 * utility method
	 * @return
	 */
	private Combat createNewCombat()
	{
		Combat c = new Combat();
		
		c.setId(rand.nextInt(1000));
		c.setUserId(rand.nextInt(1000));
		c.setHeroId(rand.nextInt(1000));
		c.setMonsterId(rand.nextInt(1000));
		
		return c;
	}
}
