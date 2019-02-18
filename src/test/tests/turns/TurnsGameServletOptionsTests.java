package test.tests.turns;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;

import com.web.api.turn.TurnsGameOptions;
import com.web.api.turn.TurnsGameServlet;

import test.parents.LabyrinthHttpServletTest;

public class TurnsGameServletOptionsTests extends LabyrinthHttpServletTest
{
	@Test
	public void testDoOptions() throws ServletException, IOException
	{
		TurnsGameServlet servlet = new TurnsGameServlet();
		TurnsGameOptions options = new TurnsGameOptions();
		servlet.setOptions(options);
		
		when(response.getWriter()).thenReturn(printer);

		servlet.doOptions(request, response);

		String messageStr = strWriter.getBuffer().toString();
		String optionsJson = gson.toJson(options);

		assertEquals(optionsJson, messageStr.trim());
	}
}
