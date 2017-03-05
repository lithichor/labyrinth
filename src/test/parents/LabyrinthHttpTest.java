package test.parents;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.labels.LabyrinthMessages;

public abstract class LabyrinthHttpTest extends LabyrinthJUnitTest
{
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected Gson gson = new Gson();
	protected LabyrinthMessages messages = new LabyrinthMessages();
}
