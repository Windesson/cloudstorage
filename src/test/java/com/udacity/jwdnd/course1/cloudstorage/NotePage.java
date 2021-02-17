package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NotePage {

    @FindBy(css="#nav-notes-tab")
    private WebElement navNotesTab;

    @FindBy(css="#add-note-button")
    private WebElement addNoteButton;

    @FindBy(css="#noteModel-title")
    private WebElement noteModelTitle;

    @FindBy(css="#noteModel-description")
    private WebElement noteModelDescription;

    @FindBy(css="#noteSaveChanges")
    private WebElement noteSaveChanges;

    public NotePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void createNote(String title, String description) throws InterruptedException {
        this.navNotesTab.click();
        Thread.sleep(2000);

        this.addNoteButton.click();
        Thread.sleep(2000);

        noteModelTitle.sendKeys(title);
        noteModelDescription.sendKeys(description);
        noteSaveChanges.click();
    }

    public void updateOpenedNote(String title, String description) {
        noteModelTitle.sendKeys(title);
        noteModelDescription.sendKeys(description);
        noteSaveChanges.click();
    }

}
