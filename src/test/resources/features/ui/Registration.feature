Feature: Registration Functionality Feature

  In order to ensure Registration Functionality works,
  I want to run the cucumber test to verify it is working

  Background: open registration page
    Given I have opened the registration page

  @ui @registration @positive @all
  Scenario: Registration with empty user parameters
    When I register with data:
      | Email    | [blank] |
      | Password | [blank] |
    Then I should see registration error message "Registration failed"

  @ui @registration @negative @all
  Scenario Outline: Registration with one invalid user parameter
    When I register with data:
      | First Name | <first_name> |
      | Last Name  | <last_name>  |
      | City       | <city>       |
      | Telephone  | <telephone>  |
    Then I should see registration error message "Registration failed"

    Examples:
      | first_name | last_name | city    | telephone    |
      | Joe#       | Dow       | London  | +1234567890  |
      | Joe        | Dow$      | London  | +1234567890  |
      | Joe        | Dow       | L@ondon | +1234567890  |
      | Joe        | Dow       | London  | +"1234567890 |

  @ui @registration @positive @all @smoke
  Scenario: Registration as a valid user
    When I am registering as a user with the consent to the license agreement is "true"
    Then I should see a title with successful registration

  @ui @registration @positive @all
  Scenario: Registration with an already registered email
    When I register with data:
      | Email    | alexandr_ladygin@mail.ru |
      | Password | qwerty12345              |
    Then I should see registration error message "Registration failed"

  @ui @registration @positive @all
  Scenario: Registration without consent to the license agreement
    When I am registering as a user with the consent to the license agreement is "false"
    Then I should see registration error message "You must agree to the Privacy Policy!"

  @ui @registration @positive @all
  Scenario: Open the license agreement window with the corresponding title
    When I click privacy policy link
    Then I should see "Privacy Policy" pop up window
