package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CredentialPage {

    @FindBy(css="#nav-credentials-tab")
    private WebElement navNotesTab;

    @FindBy(css="#credentialModel-url")
    private WebElement url;

    @FindBy(css="#credentialModel-username")
    private WebElement username;

    @FindBy(css="#credentialModel-password")
    private WebElement password;

    @FindBy(css="#credentialSaveChangesButton")
    private WebElement saveCredentials;

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

        WebElement credentialSaveChangesButton = new WebDriverWait(driver, 10)
                .until(ExpectedConditions.elementToBeClickable(By.id("credentialSaveChangesButton")));

        this.url.sendKeys(url);
        this.username.sendKeys(username);
        this.password.sendKeys(password);

        credentialSaveChangesButton.click();
    }

}
