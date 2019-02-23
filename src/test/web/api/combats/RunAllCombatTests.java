package test.web.api.combats;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({CombatsActionsJUnitTests.class, CombatsServletGetTests.class, CombatsServletPutTests.class})
public class RunAllCombatTests
{

}
