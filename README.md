# My Spring Boot Project

This is a simple Spring Boot application that serves as a demonstration of how to set up a Spring Boot project using Maven.

## Project Structure

```
weka-service
├──├──+---src
├──¦   +---main
├──¦   ¦   +---java
├──¦   ¦   ¦   +---com
├──¦   ¦   ¦       +---example
├──¦   ¦   ¦           +---recommendation
├──¦   ¦   ¦               ¦   RecommendationEngineApplication.java
├──¦   ¦   ¦               ¦   
├──¦   ¦   ¦               +---config
├──¦   ¦   ¦               ¦       DataInitializer.java
├──¦   ¦   ¦               ¦       
├──¦   ¦   ¦               +---controller
├──¦   ¦   ¦               ¦       RecommendationController.java
├──¦   ¦   ¦               ¦       
├──¦   ¦   ¦               +---entity
├──¦   ¦   ¦               ¦       Item.java
├──¦   ¦   ¦               ¦       Rating.java
├──¦   ¦   ¦               ¦       User.java
├──¦   ¦   ¦               ¦       
├──¦   ¦   ¦               +---repository
├──¦   ¦   ¦               ¦       ItemRepository.java
├──¦   ¦   ¦               ¦       RatingRepository.java
├──¦   ¦   ¦               ¦       UserRepository.java
├──¦   ¦   ¦               ¦       
├──¦   ¦   ¦               +---service
├──¦   ¦   ¦                       RecommendationService.java
├──¦   ¦   ¦                       
├──¦   ¦   +---resources
├──¦   ¦           application.properties
├──¦   ¦           
├──¦   +---test
├──¦       +---java
├──¦       ¦   +---com
├──¦       ¦       +---example
├──¦       ¦           ¦   ApplicationTests.java
├──¦       ¦           ¦   
├──¦       ¦           +---recommendation
├──¦       ¦               +---controller
├──¦       ¦                       RecommendationControllerTest.java
├──¦       ¦                       
├──¦       +---resources
├──¦               application.properties
├──¦               
├──+---target
├──    ¦   recommendation-engine-0.0.1-SNAPSHOT.jar
├──    ¦   
├──    +---classes
├──    ¦   ¦   application.properties
├──    ¦   ¦   
├──    ¦   +---com
├──    ¦       +---example
├──    ¦           +---recommendation
├──    ¦               ¦   RecommendationEngineApplication.class
├──    ¦               ¦   
├──    ¦               +---config
├──    ¦               ¦       DataInitializer.class
├──    ¦               ¦       
├──    ¦               +---controller
├──    ¦               ¦       RecommendationController.class
├──    ¦               ¦       
├──    ¦               +---entity
├──    ¦               ¦       Item.class
├──    ¦               ¦       Rating.class
├──    ¦               ¦       User.class
├──    ¦               ¦       
├──    ¦               +---repository
├──    ¦               ¦       ItemRepository.class
├──    ¦               ¦       RatingRepository.class
├──    ¦               ¦       UserRepository.class
├──    ¦               ¦       
├──    ¦               +---service
├──    ¦                       RecommendationService$ItemPrediction.class
├──    ¦                       RecommendationService.class
├──    ¦                       
├──   +---generated-sources
├──    ¦   +---annotations
├──    +---generated-test-sources
├──    ¦   +---test-annotations
├──    +---maven-archiver
├──    ¦       pom.properties
├──    ¦       
├──    +---maven-status
├──    ¦   +---maven-compiler-plugin
├──    ¦       +---compile
├──    ¦       ¦   +---default-compile
├──    ¦       ¦           createdFiles.lst
├──    ¦       ¦           inputFiles.lst
├──    ¦       ¦           
├──    ¦       +---testCompile
├──    ¦           +---default-testCompile
├──    ¦                   createdFiles.lst
├──    ¦                   inputFiles.lst
├──    ¦                   
├──   +---test-classes
├──        ¦   application.properties
├──        ¦   
├──        +---com
├──            +---example
├──                +---recommendation
├──                    +---controller
├──                            RecommendationControllerTest.class
├──
├── pom.xml
└── README.md
```

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Building the Project

To build the project, navigate to the project directory and run the following command:

```
mvn clean install
```

## Running the Application

To run the application, use the following command:

```
mvn spring-boot:run
```

The application will start on the default port 8080. You can access it at `http://localhost:8080`.

## Running Tests

To run the tests, execute the following command:

```
mvn test
```

## Configuration

You can configure the application by modifying the `src/main/resources/application.properties` file. This file allows you to set various properties such as server port and database configurations.

## License

This project is licensed under the MIT License."# weka-service" 
