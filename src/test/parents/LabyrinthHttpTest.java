package test.parents;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.labels.LabyrinthMessages;

public abstract class LabyrinthHttpTest extends LabyrinthJUnitTest
{
	protected HttpServletRequest request = mock(HttpServletRequest.class);
	protected HttpServletResponse response = mock(HttpServletResponse.class);
	protected HttpSession session;
	
	protected Gson gson = new Gson();
	protected LabyrinthMessages messages = new LabyrinthMessages();
	
	// used to get errors from servlets
	protected StringWriter strWriter = new StringWriter();
	protected PrintWriter printer = new PrintWriter(strWriter);
}
