package com.library.step_definitions;

import com.github.javafaker.Faker;
import com.library.pages.BooksPage;
import com.library.utilities.BrowserUtils;
import com.library.utilities.ConfigurationReader;
import com.library.utilities.DB_Utility;
import com.library.utilities.Driver;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.LocalDate;
import java.util.List;

public class AddBooks_StepDefinitions {

    BooksPage booksPage = new BooksPage();
    Faker faker = new Faker();
    private static String bookName_Ui;

    WebDriverWait wait = new WebDriverWait(Driver.getDriver(), 10);

    @When("user add books with all valid info")
    public void user_add_books_with_all_valid_info() {

        booksPage.booksLink.click();
        booksPage.addBook.click();

        wait.until(ExpectedConditions.visibilityOf(booksPage.bookName));
        booksPage.bookName.click();
        booksPage.bookName.clear();
        bookName_Ui = faker.book().title();
        booksPage.bookName.sendKeys(bookName_Ui);

        booksPage.isbn.click();
        booksPage.isbn.clear();
        booksPage.isbn.sendKeys(faker.number().digit());

        booksPage.year.click();
        booksPage.year.clear();
        booksPage.year.sendKeys(String.valueOf(LocalDate.now().getYear()));

        booksPage.author.clear();
        booksPage.author.click();
        booksPage.author.sendKeys(faker.book().author());

        Select bookCategory = new Select(booksPage.bookCategory);
        bookCategory.selectByVisibleText("Action and Adventure");

        booksPage.description.clear();
        booksPage.description.click();
        booksPage.description.sendKeys(faker.book().genre());

        booksPage.saveChangesButton.click();
        BrowserUtils.wait(2);
    }
    @Then("user able to see {string} message on the page")
    public void user_able_to_see_message_on_the_page(String expected) {
        try{
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'The book has been created')]")));
            String actual = Driver.getDriver().findElement(
                    By.xpath("//*[contains(text(),'The book has been created')]")).getText();
            Assert.assertTrue(expected.equals(actual));
        }catch(Throwable e){
            System.err.println("Error while waiting for the notification to appear: "+ e.getMessage());
        }


    }
    @Then("check the book added in the database")
    public void check_the_book_added_in_the_database() {
        /**
         * Connection the library1 database
        */
        String url = ConfigurationReader.getProperty("library1.database.url");
        String username = ConfigurationReader.getProperty("library1.database.username");
        String password = ConfigurationReader.getProperty("library1.database.password");

        DB_Utility.createConnection(url,username,password);

        /**
         * Check that particular user you just added in UI also exist in database
         */
        DB_Utility.runQuery("Select * from books");
        List<String> booksNameLst = DB_Utility.getColumnDataAsList("name");

        Assert.assertTrue(booksNameLst.contains(bookName_Ui));
        DB_Utility.destroy();
    }

}
