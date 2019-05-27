///////////////////////////////////////////////////////////////////////////////
//
//  Author  : Saranya Seetharaman
//  Date  : May 26, 2019
//  Description : To test the Rebtel Unlimited plan for new users signed up
//
//  Execution Time : 56.779 s (avg)
//////////////////////////////////////////////////////////////////////////////

package testscripts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import config.Constants;
import config.OR;
import util.TestBase;

@RunWith(Parameterized.class)
public class RebtelWebTests extends TestBase {

	private final String currency;

	// Setting parameterized test to test the entire flow with different currency
	// values.
	@Parameters(name = "Currency = {0}")
	public static List<String> data() {
		return Arrays.asList("EUR", "USD", "PLN", "AUD", "GBP");
	}

	// Constructor for parameterized test.
	public RebtelWebTests(String currency) {
		this.currency = currency;
	}

	// Test execution begins --
	@Test
	public void testUnlimitedPurchaseFlow() {

		// STEP 1: Set Currency
		setCurrency(currency);

		// STEP 2: Search for Sweden on the Rate search box
		searchCountry(Constants.TEXT_INPUT);

		// STEP 3: Checks that the Product "Sweden Unlimited" is showing on the result
		verifyProductUnlimitedForCountry(Constants.PRODUCT_NAME);

		// Get price of unlimited plan from search page to compare
		String priceInSearchPage = getPriceInSearchResult();

		// STEP 4: Clicks in "Try one week free" link
		if (driver.findElement(By.id(OR.PRODUCT_UNLIMITED_BUY_LINK_ID)).isEnabled()) {
			WebElement buyLink = driver.findElement(By.id(OR.PRODUCT_UNLIMITED_BUY_LINK_ID));
			buyLink.click();
		}

		// STEP 5: Now the user should be redirected to the checkout page, asking to
		// signup/login
		assertTrue(driver.getTitle().contains("Sign up or Login"));

		// STEP 6: Check if the price in the order basket is correct in relation with
		// the one presented on the 3 step (rate search).
		String priceInCartPage = getPriceInCart();

		// Verify price in search page and price in cart are equal
		assertEquals(priceInSearchPage, priceInCartPage);

		// STEP 7: You should be able to login with your account
		login(Constants.LOGIN_PHONE, Constants.LOGIN_PASSWORD);

		/*
		 * STEP 8: Check again if the price is still the same, given that now you are
		 * logged in. We have different prices for new users and paying users
		 */

		String priceInCartPageAfterLogin = getPriceInCart();

		assertEquals(priceInCartPage, priceInCartPageAfterLogin);
	}

	// Set currency value in the webpage
	public void setCurrency(String currencyCode) {
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OR.COOKIE_CONSENT_BUTTON_XPATH)));

		driver.findElement(By.tagName("body")).sendKeys(Keys.PAGE_DOWN);

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(OR.COOKIE_CONSENT_BUTTON_XPATH)));

		WebElement cookiePolicyCloseButton = driver.findElement(By.xpath(OR.COOKIE_CONSENT_BUTTON_XPATH));
		cookiePolicyCloseButton.click();

		WebElement currencyDropdown = driver.findElement(By.id(OR.CURRENCY_DROPDOWN_ID));
		currencyDropdown.click();

		List<WebElement> options = currencyDropdown.findElements(By.xpath(OR.CURRENCY_DROPDOWN_OPTIONS_XPATH));
		for (WebElement option : options) {
			if (option.getText().equals(currencyCode)) {
				option.click();
				break;
			}
		}
	}

	// Login with phone number and password
	public void login(String phoneNumber, String loginPassword) {

		WebElement loginLinkElement = driver.findElement(By.id(OR.CART_LOGIN_LINK_ID));
		loginLinkElement.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.CART_PHONE_INPUT_ID)));

		WebElement phoneInputElement = driver.findElement(By.id(OR.CART_PHONE_INPUT_ID));
		phoneInputElement.sendKeys(phoneNumber);

		WebElement loginContinueLinkElement = driver.findElement(By.id(OR.CART_LOGIN_CONTINUE_LINK_ID));
		loginContinueLinkElement.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.CART_PASSWORD_INPUT_ID)));

		WebElement passwordInputElement = driver.findElement(By.id(OR.CART_PASSWORD_INPUT_ID));
		passwordInputElement.sendKeys(loginPassword);

		WebElement loginButtonElement = driver.findElement(By.id(OR.CART_LOGIN_BUTTON_ID));
		loginButtonElement.click();
	}

	// Enter country name in search
	public void searchCountry(String CountryName) {

		// Wait for search input box to be ready
		wait.until(ExpectedConditions.elementToBeClickable(By.id(OR.SEARCH_INPUT_FIELD_ID)));

		// Get search input box
		WebElement searchInputElement = driver.findElement(By.id(OR.SEARCH_INPUT_FIELD_ID));

		// Input search term and press Enter
		searchInputElement.clear();
		searchInputElement.click();
		searchInputElement.sendKeys(CountryName);
		searchInputElement.sendKeys(Keys.ENTER);
	}

	// Verify product unlimited is present for specific country
	public void verifyProductUnlimitedForCountry(String ProductName) {
		// Wait for search results
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.PRODUCT_UNLIMITED_ID)));

		// Get text title for first card
		WebElement textElement = driver.findElement(By.id(OR.PRODUCT_UNLIMITED_ID));
		String unlimitedTitle = textElement.getText();

		assertEquals(unlimitedTitle, ProductName);
	}

	// get price in cart
	public String getPriceInCart() {

		// Wait for element to appear
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(OR.CART_PRODUCT_CONTENT_ID)));

		// Get price element
		WebElement priceElementCartPage = driver.findElement(By.xpath(OR.CART_PRODUCT_PRICE_XPATH));

		// Get amount value without the currency code and return. Eg. $25.50 will return 25.50
		return getAmount(priceElementCartPage.getText());
	}

	// get price from Search result
	public String getPriceInSearchResult() {

		// Get price of unlimited plan from search result (for specific country)
		WebElement priceElementSearchPage = driver.findElement(By.id(OR.PRODUCT_UNLIMITED_PRICE_ID));

		// Get amount value without the currency code and return. Eg. $25 will return
		// 25.
		return getAmount(priceElementSearchPage.getText());
	}

	// get amount value without currency code
	public String getAmount(String priceString) {

		if (priceString == null || priceString == "") {
			return "";
		}
		return priceString.replaceAll("[^0-9,.]", "");
	}

}
