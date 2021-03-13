package com.library.step_definitions;

import com.github.javafaker.Faker;
//import com.libraryCT_database.pages.LoginPage;
//import com.libraryCT_database.pages.UsersPage;
//import com.libraryCT_database.utilities.BrowserUtils;
//import com.libraryCT_database.utilities.ConfigurationReader;
//import com.libraryCT_database.utilities.DB_Utility;
//import com.libraryCT_database.utilities.Driver;
import com.library.pages.LoginPage;
import com.library.pages.UsersPage;
import com.library.utilities.BrowserUtils;
import com.library.utilities.ConfigurationReader;
import com.library.utilities.DB_Utility;
import com.library.utilities.Driver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AddUsers_StepDefinitions {
    LoginPage loginPage = new LoginPage();
    UsersPage userPage = new UsersPage();
    Faker faker = new Faker();

    private static String fullName_Ui;

    WebDriverWait wait = new WebDriverWait(Driver.getDriver(), 10);

    @Given("user is on the login page")
    public void user_is_on_the_login_page() {
        String libraryUrl = ConfigurationReader.getProperty("url");
        Driver.getDriver().get(libraryUrl);
    }

    @Given("with librarian login credential login successfully")
    public void with_librarian_login_credential_login_successfully() {
        loginPage.login_As_A_Librarian();
        BrowserUtils.wait(2);
    }

    @When("user add users with all valid info")
    public void user_add_users_with_all_valid_info() {

        userPage.usersLink.click();
        userPage.addUserButton.click();

        wait.until(ExpectedConditions.visibilityOf(userPage.fullName));
        userPage.fullName.click();
        userPage.fullName.clear();
        fullName_Ui = faker.name().fullName();
        userPage.fullName.sendKeys(fullName_Ui);

        userPage.password.click();
        userPage.password.clear();
        userPage.password.sendKeys(faker.lorem().characters());

        userPage.email.click();
        userPage.email.clear();
        userPage.email.sendKeys(faker.internet().safeEmailAddress());

        Select selectUserGroup = new Select(userPage.userGroup);
        selectUserGroup.selectByVisibleText("Librarian");

        Select status = new Select(userPage.status);
        status.selectByVisibleText("ACTIVE");

        userPage.address.click();
        userPage.address.clear();
        userPage.address.sendKeys(faker.address().fullAddress());
        userPage.saveChangesButton.click();
        BrowserUtils.wait(2);

    }

    @Then("user able to see {string} message on page")
    public void user_able_to_see_message_on_page(String expected) {
        wait.until(ExpectedConditions.visibilityOf(userPage.userCreatedMessage));
        String actual = userPage.userCreatedMessage.getText();
        System.out.println("actual = " + actual);
        Assert.assertTrue(expected.equals(actual));
    }

    @Then("check that user in database")
    public void check_that_user_added_in_database() {
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
        DB_Utility.runQuery("Select * from users");
        List<String> fullNameLst = DB_Utility.getColumnDataAsList("full_name");

        Assert.assertTrue(fullNameLst.contains(fullName_Ui));
        DB_Utility.destroy();
    }

    @When("user click edit user button")
    public void user_click_button() {

        userPage.usersLink.click();
        userPage.editUserButton.click();
        BrowserUtils.wait(2);
        userPage.fullName.click();
        userPage.fullName.clear();
        fullName_Ui = faker.name().fullName();
        userPage.fullName.sendKeys(fullName_Ui);
        userPage.saveChangesButton.click();
    }

    @Then("user able to see {string} message")
    public void user_able_to_see_message(String expected) {
        wait.until(ExpectedConditions.visibilityOf(userPage.userUpdatedMessage));
        String actual = userPage.userUpdatedMessage.getText();
        System.out.println("actual = " + actual);
        Assert.assertTrue(expected.equals(actual));
    }
}
