package test.tests.monsters;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

import com.models.wrappers.GsonWrapper;
import com.web.api.monster.MonstersOptions;
import com.web.api.monster.MonstersServlet;

import test.parents.LabyrinthHttpServletTest;

public class MonstersServletOptionsTests extends LabyrinthHttpServletTest
{
	private MonstersServlet servlet;
	private GsonWrapper gson;
	private MonstersOptions options;
	
	@Before
	public void setup() throws IOException
	{
		servlet = new MonstersServlet();
		gson = mock(GsonWrapper.class);
		options = mock(MonstersOptions.class);
		
		servlet.setGson(gson);
		servlet.setMonstersOptions(options);
		
		when(response.getWriter()).thenReturn(printer);
	}
	
	@Test
	/**
	 * Verify the doOptions method calls gson.toJson() with the options
	 * as an argument
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	public void testDoOptions() throws ServletException, IOException
	{
		servlet.doOptions(request, response);

		verify(gson).toJson(options);
	}
}
