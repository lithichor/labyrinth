package test.tests.monsters;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Before;
import org.junit.Test;

import com.models.wrappers.GsonWrapper;
import com.web.api.monster.MonstersTileOptions;
import com.web.api.monster.MonstersTileServlet;

import test.parents.LabyrinthHttpServletTest;

public class MonstersTileServletOptionsTests extends LabyrinthHttpServletTest
{
	private MonstersTileServlet servlet;
	private GsonWrapper gson;
	private MonstersTileOptions options;
	
	@Before
	public void setup() throws IOException
	{
		servlet = new MonstersTileServlet();
		gson = mock(GsonWrapper.class);
		options = mock(MonstersTileOptions.class);
		
		servlet.setGson(gson);
		servlet.setMonstersTileOptions(options);
		
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
