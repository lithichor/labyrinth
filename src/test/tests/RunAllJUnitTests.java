package test.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.parents.LabyrinthJUnitTest;
import test.tests.combats.RunAllCombatTests;
import test.tests.instructions.RunAllInstructionsTests;
import test.tests.user.RunAllUserTests;

@RunWith(Suite.class)
@SuiteClasses({RunAllCombatTests.class, RunAllInstructionsTests.class, RunAllUserTests.class})
public class RunAllJUnitTests extends LabyrinthJUnitTest
{
	
}