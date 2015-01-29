  Feature: Search: As a user I want to be able to search for a term

  @smoke
  Scenario: User search
    Given User views page http://stackoverflow.com/
    Then page http://stackoverflow.com/ is displayed
    And page contains a search input field
    When user searches for cucumber
    Then 15 search results are displayed
    When user searches for non_existing_search_term
    Then 0 search results are displayed
