package rebtel;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RebtelWebTests extends TestBase {
	
	private static String URL = "https://rebtel.com";
	
	private static String SEARCH_INPUT_FIELD_ID = "rate-search-light";
	private static String PRODUCT_UNLIMITED_ID = "product-unlimited";
	private static String PRODUCT_UNLIMITED_BUY_LINK_ID = "link-buy-unlimited";
	private static String PRODUCT_UNLIMITED_PRICE_ID = "unlimited_price";
	private static String CART_PRODUCT_CONTENT_ID = "basket-content";
	private static String CART_LOGIN_LINK_ID = "login";
	private static String CART_LOGIN_CONTINUE_LINK_ID = "button-signup-continue";
	private static String CART_PHONE_INPUT_ID = "phone";
	private static String CART_PASSWORD_INPUT_ID = "password";
	private static String CART_LOGIN_BUTTON_ID = "button-login";
	private static String CURRENCY_DROPDOWN_ID = "currency_drop";
	
	private static String CART_PRODUCT_PRICE_XPATH = "//*[@id=\"basket-content\"]/ul/div/li[1]/span[2]";
	private static String COOKIE_CONSENT_BUTTON_XPATH = "//*[@id=\"cookie_consent\"]/button";
	private static String CURRENCY_DROPDOWN_OPTIONS_XPATH = "//*[@id=\"currency\"]/li/a";
	
	private static String TEXT_INPUT = "Sweden";
	private static String LOGIN_PHONE = "702808112";
	private static String LOGIN_PASSWORD = "2667";
	
	List<String> currency = Arrays.asList("EUR", "USD", "PLN","AUD","GBP");
	
	@Test
	public void openRebtelPage() {
		driver.navigate().to(URL);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(COOKIE_CONSENT_BUTTON_XPATH)));
		setCurrency(currency.get(0));				
		
		// Wait for search input box to be ready
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(SEARCH_INPUT_FIELD_ID)));
		
		// Get search input box
		WebElement searchInputElement =  driver.findElement(By.id(SEARCH_INPUT_FIELD_ID));
		
		// Input search term and press Enter
		searchInputElement.sendKeys(TEXT_INPUT);
		searchInputElement.sendKeys(Keys.ENTER);
		
		// Wait for search results
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(PRODUCT_UNLIMITED_ID)));
		
		// Get text title for first card
		WebElement textElement =  driver.findElement(By.id(PRODUCT_UNLIMITED_ID));
		
		String unlimitedTitle = textElement.getText();
		
		assertEquals(unlimitedTitle, "Sweden Unlimited");
		
		// Get price element
		WebElement priceElementSearchPage = driver.findElement(By.id(PRODUCT_UNLIMITED_PRICE_ID));
		
		String priceInSearchPage = getPrice(priceElementSearchPage.getText());
		
		// Click 'Try One week free' link
		WebElement buyLink = driver.findElement(By.id(PRODUCT_UNLIMITED_BUY_LINK_ID));
		
		buyLink.click();
		
		// Wait for search results
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CART_PRODUCT_CONTENT_ID)));
		
		// Get price element
		WebElement priceElementCartPage = driver.findElement(By.xpath(CART_PRODUCT_PRICE_XPATH));
		
		String priceInCartPage = getPrice(priceElementCartPage.getText());
		
		assertEquals(priceInSearchPage, priceInCartPage);
		
		WebElement loginLinkElement = driver.findElement(By.id(CART_LOGIN_LINK_ID));
		
		loginLinkElement.click();
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CART_PHONE_INPUT_ID)));
		
		WebElement phoneInputElement = driver.findElement(By.id(CART_PHONE_INPUT_ID));
		
		phoneInputElement.sendKeys(LOGIN_PHONE);
		
		WebElement loginContinueLinkElement = driver.findElement(By.id(CART_LOGIN_CONTINUE_LINK_ID));
		
		loginContinueLinkElement.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CART_PASSWORD_INPUT_ID)));
		
		WebElement passwordInputElement = driver.findElement(By.id(CART_PASSWORD_INPUT_ID));
		
		passwordInputElement.sendKeys(LOGIN_PASSWORD);
		
		WebElement loginButtonElement = driver.findElement(By.id(CART_LOGIN_BUTTON_ID));
		
		loginButtonElement.click();
		
		// Wait for search results
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(CART_PRODUCT_CONTENT_ID)));
		
		// Get price element
		priceElementCartPage = driver.findElement(By.xpath(CART_PRODUCT_PRICE_XPATH));
		
		String priceInCartPageAfterLogin = getPrice(priceElementCartPage.getText());
		
		assertEquals(priceInCartPage, priceInCartPageAfterLogin); 
	}
	
	public void setCurrency(String currencyCode) {
		
		driver.findElement(By.tagName("body")).sendKeys(Keys.PAGE_DOWN);
		
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(COOKIE_CONSENT_BUTTON_XPATH)));
		WebElement cookiePolicyCloseButton = driver.findElement(By.xpath(COOKIE_CONSENT_BUTTON_XPATH));
		cookiePolicyCloseButton.click();
		
		WebElement currencyDropdown = driver.findElement(By.id(CURRENCY_DROPDOWN_ID));
		currencyDropdown.click();
		
		List<WebElement> options = currencyDropdown.findElements(By.xpath(CURRENCY_DROPDOWN_OPTIONS_XPATH));
		for (WebElement option : options) {
		    if (option.getText().equals(currencyCode)) {
		        option.click();
		        break;
		    }
		}
	}
	
	public String getPrice(String priceString) {
		if (priceString == null || priceString == "") {
			return "";
		}
		String[] priceSplit = priceString.split(" ");
		return priceSplit[0];
	}
	
	public String getCurrency(String priceString) {
		if (priceString == null || priceString == "") {
			return "";
		}
		String[] priceSplit = priceString.split(" ");
		return priceSplit.length > 1 ? priceSplit[1] : "";
	}

}
