package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import test.parents.LabyrinthJUnitTest;
import test.web.api.combats.RunAllCombatTests;
import test.web.api.instructions.RunAllInstructionsTests;
import test.web.api.monsters.RunAllMonstersTests;
import test.web.api.tiles.RunAllTilesTests;
import test.web.api.turns.RunAllTurnsTests;
import test.web.api.user.RunAllUserTests;

@RunWith(Suite.class)
@SuiteClasses({
	RunAllCombatTests.class,
	RunAllInstructionsTests.class,
	RunAllUserTests.class,
	RunAllTurnsTests.class,
	RunAllTilesTests.class,
	RunAllMonstersTests.class
	})
public class RunAllJUnitTests extends LabyrinthJUnitTest
{
	
}
