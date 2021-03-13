
Feature: user should logout
  Background:
    Given User is on login page



  @Scenario
  Scenario: User should logout from library page
    When I login using "librarian17@library" and "tXqOoIOS"
    And User click on logout button
    Then User should be on login home page


  @ScenarioOutline
  Scenario Outline: User should logout from library page
    When I login using "<username>" and "<password>"
    And User click on logout button
    Then User should be on login home page

    Examples:
      | username           | password |
      | student106@library | Yerj6ZHm |
      | student107@library | zZr8iI13 |
      | student108@library | uFg5d3nH |