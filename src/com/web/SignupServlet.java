package com.web;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.helpers.Encryptor;
import com.models.User;
import com.parents.LabyrinthException;
import com.parents.LabyrinthHttpServlet;
import com.web.api.user.UserValidationHelper;

public class SignupServlet extends LabyrinthHttpServlet
{
	private static final long serialVersionUID = 4557984748642459582L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		User user = (User) request.getSession().getAttribute("user");
		String action = request.getParameter("action");
		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirm = request.getParameter("confirm");
		HashMap<String, String> params = new HashMap<String, String>();

		errors.clear();
		if(user != null)
		{
			this.forward(request, response, "/");
		}
		else
		{
			if(action == null)
			{
				//not logged in, clicked signup link
				this.forward(request, response, "/jsp/user/signup.jsp");
			}
			else if(action.equalsIgnoreCase("submit"))
			{
				params.put("firstName", firstname);
				params.put("lastName", lastname);
				params.put("email", email);
				params.put("password", password);
				params.put("confirm", confirm);
				this.validate(params);

				// create the user and check to see if the email is a dupe
				User u = new User();
				Encryptor e = Encryptor.getInstance();

				u.setFirstName(firstname);
				u.setLastName(lastname);
				u.setEmail(email);
				u.setPassword(e.encrypt(password));
				try
				{
					if(u.duplicateEmail())
					{
						errors.add(messages.getMessage("signup.user_exists"));
					}
				}
				catch(LabyrinthException le1)
				{
					le1.printStackTrace();
				}

				if(errors.size() > 0)
				{
					request.setAttribute("firstname", firstname);
					request.setAttribute("lastname", lastname);
					request.setAttribute("email", email);
					request.setAttribute("password", password);
					request.setAttribute("confirm", confirm);
					this.forward(request, response, "/jsp/user/signup.jsp");
				}
				else
				{
					// save the user, and add it to session
					try
					{
						this.save(u);
					}
					catch(LabyrinthException le2)
					{
						System.out.println("Error saving user: " + le2.getMessage());
						le2.printStackTrace();
						errors.add(messages.getMessage("signup.problem_saving_user"));
					}

					if(errors.size() > 0)
					{
						request.setAttribute("firstname", firstname);
						request.setAttribute("lastname", lastname);
						request.setAttribute("email", email);
						request.setAttribute("password", password);
						request.setAttribute("confirm", confirm);
						this.forward(request, response, "/jsp/user/signup.jsp");
					}
					else
					{
						request.getSession().setAttribute("user", u);
						this.forward(request, response, "/");
					}
				}
			}
		}	//end of user == null
	}
	
	public boolean validate(HashMap<String, String> params)
	{
		UserValidationHelper validator = new UserValidationHelper();
		validator.validate(params);
		errors.addAll(validator.getErrors());
		return (validator.getErrors().size() == 0);
	}
	
	public boolean save(User user) throws LabyrinthException
	{
		return user.save();
	}

}
