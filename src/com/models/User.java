package com.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.hibernate.HibernateException;

import com.parents.LabyrinthException;
import com.parents.LabyrinthModel;

public class User extends LabyrinthModel implements Serializable
{
	private static final long serialVersionUID = 7786071431298988718L;
	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private transient String password;
	private Date createdAt;
	private Date updatedAt;
	private Date deletedAt;
	
	public User()
	{
		this.setCreatedAt(new Date());
		this.setUpdatedAt(new Date());
	}
	
	public Integer getId() { return id; }
	public void setId(Integer id) { this.id = id; }
	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public Date getCreatedAt() { return createdAt; }
	public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
	public Date getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
	public Date getDeletedAt() { return deletedAt; }
	public void setDeletedAt(Date deletedAt) { this.deletedAt = deletedAt; }

	public boolean save() throws LabyrinthException
	{
		boolean good = super.save();
		this.setPassword(null);
		return good;
	}
	
	public User login() throws LabyrinthException
	{
		return this.login(this.email, this.password);
	}
	
	public User login(String email, String password) throws LabyrinthException
	{
		this.getSession();
		
		User u = null;
		ArrayList<User> ual = null;
		
		try
		{
			trans = session.beginTransaction();
			ual = (ArrayList<User>)session.createQuery("from User where email = :email and password = :password")
					.setParameter("email", email)
					.setParameter("password", password).list();
			if(ual != null && ual.size() > 0)
			{
				u = ual.get(0);
			}
			else
			{
				throw new LabyrinthException("The user does not exist");
			}
			
			trans.commit();
			//do not return a user with a password
			u.setPassword(null);
		}
		catch(HibernateException he)
		{
			u = null;
			if(trans != null)
			{
				trans.rollback();
			}
			throw new LabyrinthException(he);
		}
		
		return u;
	}
}
