Feature: As a user, I should be able to login to the library app.

  @Librarian
  Scenario: Login as a librarian
    Given I am on the login page
    When I login using "librarian67@library" and "5ktpB2e5"
    Then dashboard should be displayed "dashboard"
    And Verify users,books and borrowed books from database


  @Student
  Scenario Outline: Login as a student
    Given I am on the login page
    When I login using "<username>" and "<password>"
    And dashboard should be displayed "books"

    Examples:
      | username           | password |
      | student106@library | Yerj6ZHm |
      | student107@library | zZr8iI13 |
      | student108@library | uFg5d3nH |
