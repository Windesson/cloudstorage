package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	public String baseURL;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {

		this.driver = new ChromeDriver();
		baseURL = "http://localhost:" + port;
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get(baseURL+"/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void getSignupPage() {
		driver.get(baseURL+"/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void getHomePage() throws InterruptedException {
		String username = "admin1";
		String password = "whatabadpassword";

		//signup
		getHomePage(username, password);

		Assertions.assertEquals("Home", driver.getTitle());

		//logout
		WebElement inputField = driver.findElement(By.id("logoutButton"));
		inputField.click();
		Thread.sleep(2000);

		Assertions.assertEquals("Login", driver.getTitle());

		//verifies that the home page is no longer accessible
		driver.get(baseURL + "/home");
		Thread.sleep(2000);

		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void noteTest() throws InterruptedException {
		String username = "admin2";
		String password = "whatabadpassword";
		getHomePage(username, password);

		String originalNoteTitle = "title";
		String originalNoteDescription = "description";
		NotePage notePage = new NotePage(driver);

		//creates a note, and verifies it is displayed.
		notePage.createNote(originalNoteTitle, originalNoteDescription);

		String actualNoteTitle = driver.findElement(By.cssSelector("#noteTitleTh1")).getText();
		String actualNoteDescription = driver.findElement(By.cssSelector("#noteDescriptionTd1")).getText();
		Integer noteRowCount  = driver.findElements(By.cssSelector("#noteTable > tbody > tr")).size();

		Assertions.assertEquals(originalNoteTitle, actualNoteTitle);
		Assertions.assertEquals(originalNoteDescription, actualNoteDescription);
		Assertions.assertEquals(1, noteRowCount);

		WebElement editButton = driver.findElement(By.cssSelector("#noteEditButton1"));
		editButton.click();
		Thread.sleep(2000);

		//edits an existing note and verifies that the changes are displayed
		String updateNoteTitle = originalNoteTitle + "-updated";
		String updateNoteDescription = originalNoteDescription + "-updated";
		notePage.updateFirstNote(updateNoteTitle,updateNoteDescription);


		actualNoteTitle = driver.findElement(By.cssSelector("#noteTitleTh1")).getText();
		actualNoteDescription = driver.findElement(By.cssSelector("#noteDescriptionTd1")).getText();

		Assertions.assertEquals(updateNoteTitle, actualNoteTitle);
		Assertions.assertEquals(updateNoteDescription, actualNoteDescription);

		//deletes a note and verifies that the note is no longer displayed.
		WebElement deleteButton = driver.findElement(By.cssSelector("#noteDeleteButton1"));
		deleteButton.click();

		noteRowCount  = driver.findElements(By.cssSelector("#userTable > tbody > tr")).size();

		Assertions.assertEquals(0, noteRowCount);
	}

	@Test
	public void credentialsTest() throws InterruptedException {
		String username = "admin3";
		String password = "whatabadpassword";
		getHomePage(username, password);

		String originalCredUrl = "https://github.com/Windesson/cloudstorage.git";
		String originalCredUsername = "admin";
		String originalCredPassword = "systems";

		//test that creates a set of credentials
		CredentialPage credentialPage = new CredentialPage(driver);
		credentialPage.addCredential(originalCredUrl, originalCredUsername, originalCredPassword);

		// verifies that they are displayed
		List<WebElement> credentialRows  = driver.findElements(By.cssSelector("#credentialTable > tbody > tr"));
		Assertions.assertEquals(1, credentialRows.size());

		// verifies that the displayed password is encrypted
		String actualUrl = driver.findElement(By.cssSelector("#credentialUrlRow1")).getText();
		String actualUsername = driver.findElement(By.cssSelector("#credentialUsernameRow1")).getText();
		String actualPasswordEncrypted = driver.findElement(By.cssSelector("#credentialPasswordRow1")).getText();

		Assertions.assertEquals(originalCredUrl, actualUrl);
		Assertions.assertEquals(originalCredUsername, actualUsername);
	}

	private void getHomePage(String username, String password) throws InterruptedException {
		//signup
		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Peter", "Zastoupil", username, password);
		driver.get(baseURL + "/login");

		//login
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);
	}

}
