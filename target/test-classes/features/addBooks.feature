Feature: Able to add books from books page on library dashboard.

  @addBook
  Scenario: As a librarian, I should be able to add books from books page.
    Given user is on the login page
    And with librarian login credential login successfully
    When user add books with all valid info
    Then user able to see "The book has been created." message on the page
    Then check the book added in the database