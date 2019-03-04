package test.web.api.turns;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TurnsGameServletGetTests.class,
	TurnsActionsTests.class,
	TurnsServletPutTests.class,
	TurnsServletGetTests.class,
	TurnsModelTests.class,
	TurnsServletOptionsTests.class,
	TurnsGameServletOptionsTests.class,
	TurnsServletContextListenerTests.class
	})
public class RunAllTurnsTests
{

}
