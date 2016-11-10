package test.parents;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class LabyrinthHttpTest extends LabyrinthJUnitTest
{
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;

}
