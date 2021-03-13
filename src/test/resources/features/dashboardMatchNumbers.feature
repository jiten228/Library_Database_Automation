@smoke
Feature: compare data with library app.
  Agile Story: from the library dashboard page match numbers of
               users,books and borrow_books from UI with library1 database

  @Librarian
  Scenario: Login as a librarian
    Given I am on the login page
    When I login using "librarian67@library" and "5ktpB2e5"
    Then dashboard should be displayed "dashboard"
    And Verify users,books and borrowed books from database