package test.tests.monsters;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRegistration.Dynamic;

import org.junit.Test;

import com.web.api.monster.MonstersServlet;
import com.web.api.monster.MonstersServletContextListener;
import com.web.api.monster.MonstersTileServlet;

import test.parents.LabyrinthJUnitTest;

public class MonstersServletContextListenerTests extends LabyrinthJUnitTest
{
	@Test
	public void testContextInitialized()
	{
		MonstersServlet monsters = new MonstersServlet();
		MonstersTileServlet monstersTile = new MonstersTileServlet();
		MonstersServletContextListener listener = new MonstersServletContextListener();
		ServletContextEvent event = mock(ServletContextEvent.class);
		ServletContext context = mock(ServletContext.class);
		Dynamic dynamicOne = mock(Dynamic.class);
		Dynamic dynamicTwo = mock(Dynamic.class);
		
		listener.setMonsterServlet(monsters);
		listener.setMonstersTileServlet(monstersTile);
		
		when(event.getServletContext()).thenReturn(context);
		when(context.addServlet("monstersServlet", monsters)).thenReturn(dynamicOne);
		when(context.addServlet("monstersTileServlet", monstersTile)).thenReturn(dynamicTwo);
		
		listener.contextInitialized(event);
		
		verify(dynamicOne, times(1)).addMapping("/api/monsters/*");
		verify(dynamicTwo, times(1)).addMapping("/api/monsters/tile/*");
	}
}
