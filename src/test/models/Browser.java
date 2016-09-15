package test.models;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class Browser
{
	private WebDriver driver = null;

	public WebDriver getInstance(String type)
	{
		if (driver == null)
		{
			if ("firefox".equalsIgnoreCase(type))
			{
				driver =  new FirefoxDriver();
			}
			else if ("chrome".equalsIgnoreCase(type))
			{
				driver = new ChromeDriver();
			}
			else if ("html".equalsIgnoreCase(type))
			{
				driver = new HtmlUnitDriver();
			}
			else if("IExplorer".equalsIgnoreCase(type))
			{
				driver = new InternetExplorerDriver();
			}
			else
			{
				
			}
		}
		return driver;
	}
}

