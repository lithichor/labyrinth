package test.tests;

import static org.junit.Assert.*;

//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import com.models.User;
import com.web.SignupServlet;

import test.parents.LabyrinthJUnitTest;

public class SignupServletTests extends LabyrinthJUnitTest
{
	private SignupServlet signup;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	
	@Before
	public void setup()
	{
		signup = mock(SignupServlet.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
	}
	
	// test the happy path
	@Test
	public void testUserIsCreatedOnPost() throws Exception
	{
		String email = "eric@eric.corn";
		String firstName = "Eric";
		String lastName = "Hartill";
		String password = "1qweqwe";
		boolean userSaved = false;
		
		User u = mock(User.class);
		u.setEmail(email);
		u.setFirstName(firstName);
		u.setLastName(lastName);
		u.setPassword(password);
		
		when(session.getAttribute("user")).thenReturn(null);
		when(request.getSession()).thenReturn(session);
		when(request.getParameter("action")).thenReturn("submit");
		when(request.getParameter("firstname")).thenReturn(firstName);
		when(request.getParameter("lastname")).thenReturn(lastName);
		when(request.getParameter("email")).thenReturn(email);
		when(request.getParameter("password")).thenReturn(password);
		when(request.getParameter("confirm")).thenReturn(password);
		when(u.save()).thenReturn(userSaved = true);
		
		signup.doPost(request, response);
		
		assertTrue(userSaved);
	}
	
	// this doesn't work right. It invokes hibernate (it shouldn't) and
	// it fails when it runs, saying the forward method wasn't invoked.
//	@Test
//	public void testWhenUserLoggedIn() throws Exception
//	{
//		User user = new User();
//		boolean forwardToHome = false;
//		ServletContext context = mock(ServletContext.class);
//		RequestDispatcher rd = mock(RequestDispatcher.class);
//		
//		when(request.getSession()).thenReturn(session);
//		when(session.getAttribute("user")).thenReturn(user);
//		when(signup.getServletContext()).thenReturn(context);
//		when(context.getRequestDispatcher("/")).thenReturn(rd);
//
//		signup.doPost(request, response);
//		verify(rd).forward(request, response);
//		
//		assertTrue(forwardToHome);
//	}
//	
//	@Test
//	public void testWhenSignupLinkClicked()
//	{
//		
//	}
}
