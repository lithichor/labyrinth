package test.web.api.instructions;

import java.util.ArrayList;

import static org.junit.Assert.*;
import org.junit.Test;

import com.parents.LabyrinthException;
import com.web.api.instructions.InstructionsActions;

import test.parents.LabyrinthJUnitTest;

public class InstructionsActionsJUnitTests extends LabyrinthJUnitTest
{
	@Test
	public void testGetFileNames() throws LabyrinthException
	{
		InstructionsActions action = new InstructionsActions();
		ArrayList<String> files = new ArrayList<>();
		String dir = System.getProperty("user.dir");

		files = action.getFileNames(dir);
		
		assertTrue(files.size() > 0);
	}
}
