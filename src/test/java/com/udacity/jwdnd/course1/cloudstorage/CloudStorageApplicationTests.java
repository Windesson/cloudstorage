package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

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

		driver.get(baseURL + "/signup");

		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Peter", "Zastoupil", username, password);

		driver.get(baseURL + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		Assertions.assertEquals("Home", driver.getTitle());

		// logout
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

		//signup
		driver.get(baseURL + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Peter", "Zastoupil", username, password);
		driver.get(baseURL + "/login");

		//login
		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		String originalNoteTitle = "title";
		String originalNoteDescription = "description";
		NotePage notePage = new NotePage(driver);

		//creates a note, and verifies it is displayed.
		notePage.createNote(originalNoteTitle, originalNoteDescription);

		WebElement baseTable = driver.findElement(By.cssSelector("#userTable"));
		WebElement noteOneRow  = baseTable.findElements(By.tagName("tr")).get(1);
		String actualNoteTitle = noteOneRow.findElement(By.tagName("th")).getText();
		String actualNoteDescription = noteOneRow.findElements(By.tagName("td")).get(1).getText();

		Assertions.assertEquals(originalNoteTitle, actualNoteTitle);
		Assertions.assertEquals(originalNoteDescription, actualNoteDescription);

		WebElement editButton = noteOneRow.findElement(By.cssSelector(".btn-success"));
		editButton.click();
		Thread.sleep(2000);

		//edits an existing note and verifies that the changes are displayed
		String updateNoteTitle = originalNoteTitle + "-updated";
		String updateNoteDescription = originalNoteDescription + "-updated";
		notePage.updateFirstNote(updateNoteTitle,updateNoteDescription);

		baseTable = driver.findElement(By.cssSelector("#userTable"));
		noteOneRow  = baseTable.findElements(By.cssSelector("tr")).get(1);

		actualNoteTitle = noteOneRow.findElement(By.tagName("th")).getText();
		actualNoteDescription = noteOneRow.findElements(By.tagName("td")).get(1).getText();

		Assertions.assertEquals(updateNoteTitle, actualNoteTitle);
		Assertions.assertEquals(updateNoteDescription, actualNoteDescription);

		//deletes a note and verifies that the note is no longer displayed.
		WebElement deleteButton = noteOneRow.findElement(By.cssSelector(".btn-danger"));
		deleteButton.click();

		baseTable = driver.findElement(By.cssSelector("#userTable"));
		Integer tableRows  = baseTable.findElements(By.tagName("tr")).size();

		Assertions.assertEquals(1,tableRows); // row 1 is the header
	}

}
