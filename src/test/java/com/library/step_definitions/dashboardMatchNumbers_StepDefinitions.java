package com.library.step_definitions;


import com.library.pages.DashboardPage;
import com.library.pages.LoginPage;
import com.library.utilities.ConfigurationReader;
import com.library.utilities.DB_Utility;
import com.library.utilities.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class dashboardMatchNumbers_StepDefinitions {

    LoginPage loginPage = new LoginPage();
    DashboardPage dashboardPage = new DashboardPage();
    WebDriverWait wait = new WebDriverWait(Driver.getDriver(), 10);

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        String libraryUrl = ConfigurationReader.getProperty("url");
        Driver.getDriver().get(libraryUrl);

    }
    @When("I login using {string} and {string}")
    public void i_login_using_and(String username, String password) {
        loginPage.username.sendKeys(username);
        loginPage.password.sendKeys(password);
        loginPage.submitButton.click();

    }

    @Then("dashboard should be displayed {string}")
    public void dashboard_should_be_displayed(String expected) {

        wait.until(ExpectedConditions.urlContains(expected));
        String actualUrl = Driver.getDriver().getCurrentUrl();
        System.out.println("actualUrl = " + actualUrl);
        Assert.assertTrue(actualUrl.contains(expected));
    }

    @Then("Verify users,books and borrowed books from database")
    public void verify_users_books_and_borrowed_books_from_database() {

        /**
         * Connection the library1 database
         */
        String url = ConfigurationReader.getProperty("library1.database.url");
        String username = ConfigurationReader.getProperty("library1.database.username");
        String password = ConfigurationReader.getProperty("library1.database.password");

        DB_Utility.createConnection(url,username,password);

        /**
         * Compare total users from UI with database
         */
        DB_Utility.runQuery("Select count(*) from users");
        String usersDb = DB_Utility.getFirstRowFirstColumnCellData();

        String usersUi = dashboardPage.users.getText();
        System.out.println("usersDb = " + usersDb);
        System.out.println("usersUi = " + usersUi);
        Assert.assertEquals("Users Mismatch",usersUi,usersDb);

        /**
         * Compare total books from UI with database
         */
        DB_Utility.runQuery("Select count(*) from books");
        String booksDb = DB_Utility.getFirstRowFirstColumnCellData();

        String booksUi = dashboardPage.books.getText();
        System.out.println("booksUi = " + booksUi);
        System.out.println("booksDb = " + booksDb);
        Assert.assertEquals("Books Mismatch",booksUi,booksDb);

        /**
         * Compare total book_borrow from UI with database
         */
        DB_Utility.runQuery("Select count(*) from book_borrow where returned_date is null");
        String book_borrowDb = DB_Utility.getFirstRowFirstColumnCellData();

        String book_borrowUi = dashboardPage.borrowed_books.getText();
        System.out.println("book_borrowUi = " + book_borrowUi);
        System.out.println("book_borrowDb = " + book_borrowDb);
        Assert.assertEquals("book_borrows Mismatch",book_borrowUi,book_borrowDb);
    }

}
