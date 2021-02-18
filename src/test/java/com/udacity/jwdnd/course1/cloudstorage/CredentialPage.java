package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CredentialPage {

    @FindBy(css="#nav-credentials-tab")
    private WebElement navNotesTab;

    @FindBy(css="#credentialModel-url")
    private WebElement url;

    @FindBy(css="#credentialModel-username")
    private WebElement username;

    @FindBy(css="#credentialModel-password")
    private WebElement password;

    private final WebDriver driver;

    public CredentialPage(WebDriver webDriver) {
        this.driver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void addCredential(String url, String username, String password){
        this.navNotesTab.click();

        WebElement addNewCredentialButton = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(By.id("add-credential-button")));
        addNewCredentialButton.click();

        WebElement credentialModelSaveChangesButton = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(By.id("credentialModelSaveChangesButton")));

        this.url.sendKeys(url);
        this.username.sendKeys(username);
        this.password.sendKeys(password);

        credentialModelSaveChangesButton.click();
    }

    public boolean verifyCredential(String originalCredUrl, String originalCredUsername, String originalCredPassword) throws InterruptedException {
        editableMode();

        WebElement credentialModelCloseButton = new WebDriverWait(driver, 10)
               .until(ExpectedConditions.elementToBeClickable(By.id("credentialModelCloseButton")));

       String actualUrl = this.url.getAttribute("value");
       String actualUserName = this.username.getAttribute("value");
       String actualPassword = this.password.getAttribute("value");

       credentialModelCloseButton.click();

        return actualUrl.equals(originalCredUrl) &&
                actualUserName.equals(originalCredUsername) &&
                actualPassword.equals(originalCredPassword);

    }

    public void updateCredential(String url, String username, String password) throws InterruptedException {
        editableMode();

        WebElement credentialModelSaveChangesButton = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(By.id("credentialModelSaveChangesButton")));

        this.url.clear();
        this.url.sendKeys(url);
        this.username.clear();
        this.username.sendKeys(username);
        this.password.clear();
        this.password.sendKeys(password);

        credentialModelSaveChangesButton.click();
    }

    public void deleteCredentials(){
        List<WebElement> deleteButtons = new WebDriverWait(driver, 10)
                .until(webdriver -> webdriver.findElements(By.cssSelector("#credentialTable .btn-danger ")));

        deleteButtons.forEach( btn -> btn.click());
    }

    private void editableMode() throws InterruptedException {
        List<WebElement> buttons = new WebDriverWait(driver, 10)
                .until(webdriver -> webdriver.findElements(By.cssSelector("#credentialTable .btn-success ")));

        Thread.sleep(1000);
        buttons.get(0).click();
        Thread.sleep(1000);
    }
}
