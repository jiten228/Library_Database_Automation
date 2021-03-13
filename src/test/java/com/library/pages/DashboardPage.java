package com.library.pages;

//import com.libraryCT_database.utilities.Driver;
import com.library.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DashboardPage extends BasePage {

    public DashboardPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(id = "user_count")
    public WebElement users;

    @FindBy(id = "book_count")
    public WebElement books;

    @FindBy(id = "borrowed_books")
    public WebElement borrowed_books;




}
