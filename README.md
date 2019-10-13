# rideways

This project contains the solution for the rides application.

This project is built using Apache Maven version 3.2.0 which installs the junit and org.json dependencies.

The project SDK is Java 1.8 

## Running the application using the command line

In the `rides` folder run the command `mvn clean package` to compile and package the code.

To start the application and run the code, use the command

`java -cp target/rides-1.0-SNAPSHOT.jar com.booking.rides.App` in the same `rides` folder.

We're running the App.java file which is in the com.booking.rides package. We also need to specify the path to the jar created with the previous command using `-cp`.

This is how exercises in Part 1 are run.

The user will be prompted to input the pickup and dropoff locations which need to be in the `51.470020,-0.454295` format to be parsed as strings.

Optionally, the user can specify the number of passengers for Dave's Taxis results. If filtering the results is not desired, the `ENTER` key should be pressed.

The user will be able to see either the filtered or non-filtered options from Dave's Taxis. Following this, using the same pickup and dropoff locations, the cheapest option for each car type is shown from all suppliers.

## Further improvements

The following are some improvements that I would've liked to implement:

- Input validation: The user inputs should be validated so that the application runs correctly. At the moment if the locations are not in the `51.470020,-0.454295` format, the application will throw an exception.

- Make use of the MVC pattern to better structure and organise the code, especially for Part 2 of the challenge.

- Refactor current methods for readability and improve current implementation with more efficient solutions.

- Exception handling: Improved custom exception handling.

- For a better user experience, allow the user to retry different inputs or run parts of the application separately without having to rerun the application.
