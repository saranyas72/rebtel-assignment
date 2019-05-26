package rebtel;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestBase {

    protected static WebDriver driver;
    protected static WebDriverWait wait;
   
	@BeforeClass
	public static void setUp() throws Exception {
		System.setProperty("webdriver.chrome.driver","src/resources/chromedriver");
		driver = new ChromeDriver(getChromeOptions());
		wait = new WebDriverWait(driver, 10);
	}
	
	@AfterClass
	public static void tearDown() throws Exception {
		//driver.quit();
	}

	
	public static ChromeOptions getChromeOptions() {
		return new ChromeOptions();
	}
	
	/**
	  * Returns the browser version
	  * @return
	  */
	public String getBrowserVersion()
	{
	    return ((RemoteWebDriver)driver).getCapabilities().getVersion();
	}
	
	/**
	  * Returns the browser name
	  * @return
	  */
	public String getBrowserName()
	{
	    return ((RemoteWebDriver)driver).getCapabilities().getBrowserName();
	}
	
	public static WebDriver getDriver() {
		return driver;
	}

}
