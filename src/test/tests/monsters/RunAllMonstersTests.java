package test.tests.monsters;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	MonstersServletGetTests.class,
	MonstersTileServletGetTests.class,
	MonsterModelTests.class
	})
public class RunAllMonstersTests
{

}
