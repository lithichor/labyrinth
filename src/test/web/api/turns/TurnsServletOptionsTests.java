package test.web.api.turns;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;

import com.web.api.turn.TurnsOptions;
import com.web.api.turn.TurnsServlet;

import test.parents.LabyrinthHttpServletTest;

public class TurnsServletOptionsTests extends LabyrinthHttpServletTest
{
	@Test
	public void testTurnsDoOptions() throws ServletException, IOException
	{
		TurnsServlet servlet = new TurnsServlet();
		TurnsOptions options = new TurnsOptions();
		servlet.setOptions(options);
		
		when(response.getWriter()).thenReturn(printer);

		servlet.doOptions(request, response);

		String messageStr = strWriter.getBuffer().toString();
		String optionsJson = gson.toJson(options);

		assertEquals(optionsJson, messageStr.trim());
	}
}
