package test.tests.user;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	UserServletDeleteTests.class,
	UserServletGetTests.class,
	UserServletPostTests.class,
	UserServletPutTests.class,
	UserValidationHelperTests.class,
	UserModelTests.class
	})
public class RunAllUserTests
{

}
