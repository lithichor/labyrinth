package test.web.api.turns;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletRegistration.Dynamic;

import org.junit.Test;

import com.web.api.turn.TurnsGameServlet;
import com.web.api.turn.TurnsServlet;
import com.web.api.turn.TurnsServletContextListener;

import test.parents.LabyrinthJUnitTest;

public class TurnsServletContextListenerTests extends LabyrinthJUnitTest
{
	@Test
	public void testContextInitialized()
	{
		TurnsServlet turnsServlet = new TurnsServlet();
		TurnsGameServlet turnsGameServlet = new TurnsGameServlet();
		TurnsServletContextListener listener = new TurnsServletContextListener();
		ServletContextEvent event = mock(ServletContextEvent.class);
		ServletContext context = mock(ServletContext.class);
		Dynamic dynamicOne = mock(Dynamic.class);
		Dynamic dynamicTwo = mock(Dynamic.class);
		
		listener.setTurnsServlet(turnsServlet);
		listener.setTurnsGameServlet(turnsGameServlet);
		
		when(event.getServletContext()).thenReturn(context);
		when(context.addServlet("turnsServlet", turnsServlet)).thenReturn(dynamicOne);
		when(context.addServlet("turnsGameServlet", turnsGameServlet)).thenReturn(dynamicTwo);
		
		listener.contextInitialized(event);
		
		verify(dynamicOne).addMapping("/api/turns/*");
		verify(dynamicTwo).addMapping("/api/turns/game/*");
	}
}
