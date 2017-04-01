package test.tests.user;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	UserServletPostTests.class,
	UserServletGetTests.class,
	UserServletPutTests.class,
	UserServletPutTests.class,
	UserValidationHelperTests.class})
public class RunAllUserTests
{

}
