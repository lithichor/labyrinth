package test.tests.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.database.DatabaseHelper;
import com.parents.LabyrinthException;
import com.web.api.game.Game;
import com.web.api.user.User;

import test.parents.LabyrinthJUnitTest;

public class UserModelTests extends LabyrinthJUnitTest
{
	private User user;
	private DatabaseHelper dbh;
	private ResultSet results;
	
	@Before
	public void setup()
	{
		user = new User();
		
		dbh = mock(DatabaseHelper.class);
		results = mock(ResultSet.class);
		
		user.setDbh(dbh);
	}

	@Test
	public void testUserSave() throws SQLException, LabyrinthException
	{
		String sql1 = "INSERT INTO users (first_name, last_name, email, password, created_at, updated_at) "
				+ "VALUES(?, ?, ?, ?, now(), now())";
		ArrayList<Object> params1 = new ArrayList<>();
		params1.add(null);
		params1.add(null);
		params1.add(null);
		params1.add(null);
		
		String sql2 = "SELECT id FROM users WHERE email like ? AND deleted_at IS NULL";
		ArrayList<Object> params2 = new ArrayList<>();
		params2.add(null);
		
		int userId = rand.nextInt(1000);
		boolean successful = false;
		
		when(dbh.execute(sql1, params1)).thenReturn(true);
		when(dbh.executeQuery(sql2, params2)).thenReturn(results);
		when(results.next()).thenReturn(true).thenReturn(false);
		when(results.getInt("id")).thenReturn(userId);
		
		successful = user.save();
		
		assertTrue(successful);
	}

	@Test
	public void testUserUpdateNoFields() throws SQLException, LabyrinthException
	{
		String sql = "UPDATE users SET , updated_at = now() WHERE id = ?";
		ArrayList<Object> params = new ArrayList<>();
		params.add(null);
		
		boolean successful = false;
		
		when(dbh.execute(sql, params)).thenReturn(true);
		
		successful = user.update();
		
		assertTrue(successful);
	}

	@Test
	public void testUserUpdateOneField() throws SQLException, LabyrinthException
	{
		String firstName = randStrings.oneWord();
		String lastName = randStrings.oneWord();
		String password = randStrings.oneWord();
		user.setId(rand.nextInt(1000));
		String sql = "";
		ArrayList<Object> params = new ArrayList<>();
		boolean successful = false;
		String field = "";
		
		switch(rand.nextInt(3))
		{
		case 0:
			user.setFirstName(firstName);
			sql = "UPDATE users SET first_name = ? , updated_at = now() WHERE id = ?";
			params.add(firstName);
			field = "first name";
			break;
		case 1:
			user.setLastName(lastName);
			sql = "UPDATE users SET last_name = ? , updated_at = now() WHERE id = ?";
			params.add(lastName);
			field = "last name";
			break;
		case 2:
			user.setPassword(password);
			sql = "UPDATE users SET password = ? , updated_at = now() WHERE id = ?";
			params.add(password);
			field = "password";
			break;
		}
		
		params.add(user.getId());
		
		when(dbh.execute(sql, params)).thenReturn(true);
		
		successful = user.update();
		
		// since this is nondeterministic, output the field that failed
		assertTrue("Failed field: " + field + "\nSQL: " + sql, successful);
	}

	@Test
	public void testUserUpdateAllFields() throws SQLException, LabyrinthException
	{
		String firstName = randStrings.oneWord();
		String lastName = randStrings.oneWord();
		String password = randStrings.oneWord();
		ArrayList<Object> params = new ArrayList<>();
		boolean successful = false;
		int userId = rand.nextInt(1000);
		
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setPassword(password);
		user.setId(userId);
		
		params.add(firstName);
		params.add(lastName);
		params.add(password);
		params.add(userId);
		
		String sql = "UPDATE users SET first_name = ? , last_name = ? , password = ? , updated_at = now() WHERE id = ?";
		
		when(dbh.execute(sql, params)).thenReturn(true);
		
		successful = user.update();
		
		assertTrue(successful);
	}

	@Test
	public void testDeleteUser() throws SQLException, LabyrinthException
	{
		boolean successful = false;
		String sql = "UPDATE users SET updated_at = now(), deleted_at = now() WHERE id = ?";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(null);
		
		Game game = mock(Game.class);
		user.setGame(game);
		
		when(dbh.execute(sql, params)).thenReturn(true);

		successful = user.deleteUser();
		
		assertTrue(successful);
	}

	@Test
	public void testDeleteUserLoadGameThrowsException() throws SQLException, LabyrinthException
	{
		String exceptionMessage = messages.getMessage("game.no_games");
		
		boolean successful = false;
		String sql = "UPDATE users SET updated_at = now(), deleted_at = now() WHERE id = ?";
		ArrayList<Object> params = new ArrayList<Object>();
		params.add(null);
		
		Game game = mock(Game.class);
		user.setGame(game);
		
		when(game.load(null, 0)).thenThrow(new LabyrinthException(exceptionMessage));
		when(dbh.execute(sql, params)).thenReturn(true);

		successful = user.deleteUser();
		
		assertTrue(successful);
	}

	@Test
	public void testLogin() throws LabyrinthException, SQLException
	{
		String email = randStrings.oneWord() + "@" + randStrings.oneWord() + ".corn";
		String password = randStrings.oneWord();
		String firstName = randStrings.oneWord();
		String lastName = randStrings.oneWord();
		String sql = "SELECT id, first_name, last_name, email"
				+ " FROM users WHERE email = ? AND password = ? AND deleted_at IS NULL";
		ArrayList<Object> params = new ArrayList<Object>();
		int userId = rand.nextInt(1000);

		params.add(email);
		params.add(password);
		
		user.setEmail(email);
		user.setPassword(password);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		
		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenReturn(true).thenReturn(false);
		when(results.getInt("id")).thenReturn(userId);
		when(results.getString("first_name")).thenReturn(firstName);
		when(results.getString("last_name")).thenReturn(lastName);
		when(results.getString("email")).thenReturn(email);

		User u = user.login();
		
		assertTrue(u.equals(user));
	}
	
	@Rule
	public ExpectedException ee = ExpectedException.none();

	@Test
	public void testLoginNoResultsReturned() throws SQLException, LabyrinthException
	{
		String email = randStrings.oneWord() + "@" + randStrings.oneWord() + ".corn";
		String password = randStrings.oneWord();
		String sql = "SELECT id, first_name, last_name, email"
				+ " FROM users WHERE email = ? AND password = ? AND deleted_at IS NULL";
		ArrayList<Object> params = new ArrayList<Object>();

		params.add(email);
		params.add(password);
		
		user.setEmail(email);
		user.setPassword(password);
		
		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenReturn(false);
		
		ee.expect(LabyrinthException.class);
		ee.expectMessage(messages.getMessage("user.no_such_player"));
		user.login();
	}

	@Test
	public void testDuplicateEmailTrue() throws LabyrinthException, SQLException
	{
		String email = randStrings.oneWord() + "@" + randStrings.oneWord() + ".corn";
		String sql = "SELECT email FROM users WHERE email like ?";
		ArrayList<Object> params = new ArrayList<Object>();
		boolean duplicate = false;

		user.setEmail(email);
		params.add(email);
		
		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenReturn(true).thenReturn(false);

		duplicate = user.duplicateEmail();
		
		assertTrue(duplicate);
	}

	@Test
	public void testDuplicateEmailFalse() throws LabyrinthException, SQLException
	{
		String email = randStrings.oneWord() + "@" + randStrings.oneWord() + ".corn";
		String sql = "SELECT email FROM users WHERE email like ?";
		ArrayList<Object> params = new ArrayList<Object>();
		boolean duplicate = true;

		user.setEmail(email);
		params.add(email);

		when(dbh.executeQuery(sql, params)).thenReturn(results);
		when(results.next()).thenReturn(false);

		duplicate = user.duplicateEmail();
		
		assertFalse(duplicate);
	}

	@Test
	public void testMergeAllFields()
	{
		int userId = rand.nextInt(1000);
		String firstName = randStrings.oneWord();
		String lastName = randStrings.oneWord();
		String password = randStrings.oneWord();

		User u = new User();
		u.setId(userId);
		u.setFirstName(firstName);
		u.setLastName(lastName);
		u.setPassword(password);
		
		user.merge(u);

		assertTrue(u.equals(user));
	}

	@Test
	// Empty User means no fields were set before merge
	public void testMergeOneFieldEmptyUser()
	{
		int userId = rand.nextInt(1000);
		String firstName = randStrings.oneWord();
		String lastName = randStrings.oneWord();
		String password = randStrings.oneWord();
		String changedField = "";

		User u = new User();

		switch(rand.nextInt(4))
		{
		case 0:
			u.setId(userId);
			changedField = "id";
			break;
		case 1:
			u.setFirstName(firstName);
			changedField = "firstName";
			break;
		case 2:
			u.setLastName(lastName);
			changedField = "lastName";
			break;
		case 3:
			u.setPassword(password);
			changedField = "password";
			break;
		}

		user.merge(u);

		assertTrue("Field tested: " + changedField, u.equals(user));
	}

	@Test
	// Full user means the user had all fields set before merge
	public void testMergeOneFieldFullUser()
	{
		String firstName = randStrings.oneWord();
		String lastName = randStrings.oneWord();
		String password = randStrings.oneWord();
		String changedField = "";

		user.setId(rand.nextInt(1000));
		user.setFirstName(randStrings.oneWord());
		user.setLastName(randStrings.oneWord());
		user.setPassword(randStrings.oneWord());

		User u = new User();

		switch(rand.nextInt(3))
		{
		case 0:
			u.setFirstName(firstName);
			changedField = "firstName";
			break;
		case 1:
			u.setLastName(lastName);
			changedField = "lastName";
			break;
		case 2:
			u.setPassword(password);
			changedField = "password";
			break;
		}

		user.merge(u);
		
		// does the merged field match the original?
		// ID does not take the other value in a merge
		boolean match = false;
		switch(changedField)
		{
		case "firstName":
			match = (u.getFirstName().equals(user.getFirstName()));
			break;
		case "lastName":
			match = (u.getLastName().equals(user.getLastName()));
			break;
		case "password":
			match = (u.getPassword().equals(user.getPassword()));
			break;
			
		}

		assertTrue("Field tested: " + changedField, match);
	}
}