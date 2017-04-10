package test.tests.turns;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TurnsGameServletGetTests.class,
	TurnsActionsTests.class,
	TurnsServletPutTests.class,
	TurnsServletGetTests.class
	})
public class RunAllTurnsTests
{

}
