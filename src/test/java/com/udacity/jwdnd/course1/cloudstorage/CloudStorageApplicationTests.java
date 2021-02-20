package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private static WebDriver driver;
	static public String baseURL;
	static final String username = "admin";
	static final String password = "whatabadpassword";

	private static CredentialPage credentialPage;
	private static NotePage notePage;

	@BeforeAll
	public static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		credentialPage = new CredentialPage(driver);
		notePage = new NotePage(driver);
	}

	@BeforeEach
	public void beforeEach(){
		baseURL = "http://localhost:" + port;
	}

	@AfterAll
	public static void afterAll() {
		if(driver != null) driver.quit();
	}

	@Test
	@Order(1)
	public void getLoginPage_WhenUnauthorizedUser_Allow(){
		driver.get(baseURL+"/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}
	@Test
	@Order(2)
	public void getSignupPage_WhenUnauthorizedUser_Allow() {
		driver.get(baseURL+"/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	@Order(3)
	public void getHomePage_WhenUnauthorizedUser_Deny() {
		driver.get(baseURL+"/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(4)
	public void getHomePage_WhenUserHasSignup_Allow() throws InterruptedException {

		//signup
		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Peter", "Zastoupil", username, password);
		driver.get(baseURL + "/login");

		//login
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		Assertions.assertEquals("Home", driver.getTitle());
	}

	@Test
	@Order(5)
	public void getLogout_FromHomePage_RedirectToLogin() throws InterruptedException {
		//logout
		WebElement inputField = driver.findElement(By.id("logoutButton"));
		inputField.click();
		Thread.sleep(2000);

		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(6)
	public void user_WhenLogout_CannotAccessHome() throws InterruptedException {

		driver.get(baseURL + "/home");
		Thread.sleep(2000);

		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(7)
	public void note_WhenCreated_IsDisplayed() throws InterruptedException {

		//login
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		String originalNoteTitle = "title";
		String originalNoteDescription = "description";

		//creates a note, and verifies it is displayed.
		notePage.createNote(originalNoteTitle, originalNoteDescription);

		String actualNoteTitle = driver.findElement(By.cssSelector("#noteTitleTh1")).getText();
		String actualNoteDescription = driver.findElement(By.cssSelector("#noteDescriptionTd1")).getText();
		Integer noteRowCount = driver.findElements(By.cssSelector("#noteTable > tbody > tr")).size();

		Assertions.assertEquals(originalNoteTitle, actualNoteTitle);
		Assertions.assertEquals(originalNoteDescription, actualNoteDescription);
		Assertions.assertEquals(1, noteRowCount);

	}

	@Test
	@Order(8)
	public void note_WhenEdited_DisplayChanges() throws InterruptedException {

		String originalNoteTitle = "title";
		String originalNoteDescription = "description";

		WebElement editButton = driver.findElement(By.cssSelector("#noteEditButton1"));
		editButton.click();
		Thread.sleep(2000);

		//edits an existing note and verifies that the changes are displayed
		String updateNoteTitle = originalNoteTitle + "-updated";
		String updateNoteDescription = originalNoteDescription + "-updated";

		notePage.updateFirstNote(updateNoteTitle, updateNoteDescription);

		String actualNoteTitle = driver.findElement(By.cssSelector("#noteTitleTh1")).getText();
		String actualNoteDescription = driver.findElement(By.cssSelector("#noteDescriptionTd1")).getText();

		Assertions.assertEquals(updateNoteTitle, actualNoteTitle);
		Assertions.assertEquals(updateNoteDescription, actualNoteDescription);
	}

	@Test
	@Order(9)
	public void note_WhenDelete_updateTable() throws InterruptedException {
		//deletes a note and verifies that the note is no longer displayed.
		WebElement deleteButton = driver.findElement(By.cssSelector("#noteDeleteButton1"));
		deleteButton.click();

		Integer noteRowCount  = driver.findElements(By.cssSelector("#userTable > tbody > tr")).size();
		Assertions.assertEquals(0, noteRowCount);
	}

	@Test
	@Order(10)
	public void credentials_WhenCreate_UpdateCredentialTable() throws InterruptedException {

		String originalCredUrl = "https://github.com/Windesson/cloudstorage.git";
		String originalCredUsername = "admin";
		String originalCredPassword = "systems";


		driver.get(baseURL + "/home");
		Thread.sleep(2000);

		//test that creates a set of credentials
		credentialPage.addCredential(originalCredUrl, originalCredUsername, originalCredPassword);

		//verifies that they are displayed
		List<WebElement> credentialRows = driver.findElements(By.cssSelector("#credentialTable > tbody > tr"));
		Assertions.assertEquals(1, credentialRows.size());

		//verifies that the displayed password is encrypted
		String actualUrl = driver.findElement(By.cssSelector("#credentialUrlRow1")).getText();
		String actualUsername = driver.findElement(By.cssSelector("#credentialUsernameRow1")).getText();
		String actualPasswordEncrypted = driver.findElement(By.cssSelector("#credentialPasswordRow1")).getText();

		Assertions.assertEquals(originalCredUrl, actualUrl);
		Assertions.assertEquals(originalCredUsername, actualUsername);
		Assertions.assertNotEquals(originalCredPassword, actualPasswordEncrypted);

		//views an existing set of credentials
		//verifies that the viewable password is unencrypted
		Boolean isVerified = credentialPage.verifyCredential(originalCredUrl, originalCredUsername, originalCredPassword);
		Assertions.assertTrue(isVerified);

	}

	@Test
	@Order(11)
	public void credentials_WhenEdit_UpdateCredentialTable() throws InterruptedException {

		//edits the credentials, and verifies that the changes are displayed.
		String updatedCredUrl = "https://github.com/admin/cloudstorage.git";
		String updatedCredUsername = "admin-updated";
		String updatedCredPassword = "systems-updated";

		credentialPage.updateCredential(updatedCredUrl, updatedCredUsername, updatedCredPassword);
		boolean isVerified = credentialPage.verifyCredential(updatedCredUrl, updatedCredUsername, updatedCredPassword);
		Assertions.assertTrue(isVerified);
	}

	@Test
	@Order(12)
	public void credentials_WhenDelete_UpdateCredentialTable() throws InterruptedException {
		//deletes an existing set of credentials and verifies that the credentials are no longer displayed
		credentialPage.deleteCredentials();

		List<WebElement> credentialRows  = driver.findElements(By.cssSelector("#credentialTable > tbody > tr"));
		Assertions.assertEquals(0, credentialRows.size());
	}

}
