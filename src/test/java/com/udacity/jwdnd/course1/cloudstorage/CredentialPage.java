package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CredentialPage {

    @FindBy(css="#nav-credentials-tab")
    private WebElement navNotesTab;

    @FindBy(css="#add-credential-button")
    private WebElement addCredentialButton;

    @FindBy(css="#credentialModel-url")
    private WebElement url;

    @FindBy(css="#credentialModel-username")
    private WebElement username;

    @FindBy(css="#credentialModel-password")
    private WebElement password;

    @FindBy(css="#credentialSaveChanges")
    private WebElement saveCredentials;

    private WebDriver driver;
    private WebDriverWait driverWait;

    public CredentialPage(WebDriver webDriver) {
        this.driver = webDriver;
        this.driverWait = new WebDriverWait(driver, 10);
        PageFactory.initElements(webDriver, this);
    }

    public void addCredential(String url, String username, String password){
        viewCredentialTable();
        this.addCredentialButton.click();
        this.driverWait.until(webDriver -> webDriver.findElement(By.id("credentialModal")));

        this.url.sendKeys(url);
        this.username.sendKeys(username);
        this.password.sendKeys(password);

        saveCredentials.sendKeys();
    }

    public WebElement viewCredentialTable(){
        this.navNotesTab.click();
        WebElement marker = driverWait.until(webDriver -> webDriver.findElement(By.id("credentialTable")));

        return marker;
    }
}
