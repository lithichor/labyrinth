package test.parents;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mockito.Mockito;

public abstract class LabyrinthJUnitTest extends Mockito
{
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;

}
