package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
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

		driver.get(baseURL + "/signup");

		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Peter", "Zastoupil", username, password);

		driver.get(baseURL + "/login");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(username, password);

		String noteTitle = "title-test";
		String noteDescription = "-test1";
		NotePage notePage = new NotePage(driver);
		notePage.createNote(noteTitle, noteDescription);

		Assertions.assertEquals("Home", driver.getTitle());
	}

}
