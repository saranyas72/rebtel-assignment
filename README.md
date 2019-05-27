# rebtel-assignment
Sample test for Rebtel

## Test flow that is automated

    - Go to the page www.rebtel.com
    - Makes a search for Sweden on the Rate search box
    - Checks that the Product "Sweden Unlimited" is showing on the result
    - Clicks in "Try one week free"
    - Now the user should be redirected to the checkout page, asking to signup/login
    - Check if the price in the order basket is correct in relation with the one presented on the 3 step (rate search).
    - You should be able to login with your account
    - Check again if the price is still the same, given that now you are logged in. (We have different prices for new users and paying users)

## User Instructions to run the test

    1. Clone the repository
    2. To run the project in IDE 
    
      - open the IDE
      - create a new java project
      - file import -> select existing project into workspace
      - select src/test/testscripts/RebtelWebTests.java and set run configurations to run as JUnit Test
      - run RebtelWebTests.java 
      
      [Note: Re-run if required]
      
 For any queries reach out to - saranyas72@gmail.com
