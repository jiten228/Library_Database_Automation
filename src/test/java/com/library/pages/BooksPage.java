package com.library.pages;


import com.library.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BooksPage extends BasePage{


    public BooksPage(){
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//a[@class='btn btn-lg btn-outline btn-primary btn-sm add_book_btn']")
    public WebElement addBook;

    @FindBy(xpath = "//input[@placeholder='Book Name']")
    public WebElement bookName;

    @FindBy(xpath = "//input[@placeholder='ISBN']" )
    public WebElement isbn;

    @FindBy(xpath = "//input[@placeholder='Year']")
    public WebElement year;

    @FindBy(xpath = "//input[@placeholder='Author']")
    public WebElement author;

    @FindBy(id = "book_group_id")
    public WebElement bookCategory;

    @FindBy(id = "description")
    public WebElement description;

    @FindBy(xpath = "//button[@type='submit']")
    public WebElement saveChangesButton;

    @FindBy (xpath = "//*[text()='The book has been created.']")
    public WebElement userCreatedMessage;
}
