package test.web.api.tiles;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	TilesMapServletGetTests.class,
	TilesServletGetTests.class,
	TilesModelTests.class
	})
public class RunAllTilesTests
{

}
