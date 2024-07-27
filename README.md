# The Drone Delivery System

The Drone Delivery System is a Java application that manages drone-based delivery of medications. It provides RESTful APIs for interacting with drones, including registering drones, loading medications, and checking drone status.

## Build Instructions

1.Clone the Repository

```bash
git clone https://github.com/RakusasuDorea/TheDrone.git
```

2.Navigate to the Project Directory

```bash
cd drone
```

3.Build the Project
```bash
mvn clean install
```
## Run Instructions

1.Start the Application

After building the project, you can start the application with:

```bash
mvn spring-boot:run
```
2.Access the Application

The application will be available at `http://localhost:8080`.

3.Access the H2 Console

If you want to view the database, you can access the H2 console at http://localhost:8080/h2-console. Use the following settings to log in:

•JDBC URL: `jdbc:h2:mem:testdb`

•Username: sa

•Password: (leave blank)

## Test Instructions
1.Test it with POSTMAN

[Click here](https://www.postman.com/bjhernz08/workspace/drone/collection/37012381-1ca65d6d-7076-47f7-9a7d-94ff22338c6b?action=share&creator=37012381) to fork the POSTMAN collection to test the APIs.

## Additional Notes
•Ensure your environment meets the prerequisites: Java 17+ and Maven.

•The application uses H2 as an in-memory database and is preloaded with sample data.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

For more information about the MIT License, visit [ChooseALicense.com](https://choosealicense.com/licenses/mit/).

