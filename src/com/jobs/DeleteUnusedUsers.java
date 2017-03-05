package com.jobs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.database.DatabaseHelper;
import com.parents.LabyrinthException;
import com.web.api.user.User;

public class DeleteUnusedUsers implements Runnable
{
	public void run()
	{
		String sql = "SELECT id from users "
				+ "WHERE last_login < DATE_SUB(now(), INTERVAL 3 MONTH) "
				+ "AND deleted_at IS NULL "
				+ "LIMIT 100";
		DatabaseHelper dbh = DatabaseHelper.getInstance();
		ResultSet results = null;
		ArrayList<Integer> ids = new ArrayList<>();

		try
		{
			results = dbh.executeQuery(sql);
			
			while(results.next())
			{
				ids.add(results.getInt("id"));
			}
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
		}

		for(int id: ids)
		{
			User u = new User();
			u.setId(id);
			try
			{
				u.deleteUser();
			}
			catch(LabyrinthException le)
			{
				le.printStackTrace();
				System.out.println(le.getMessage());
			}
			System.out.println(id);
		}
	}
}
