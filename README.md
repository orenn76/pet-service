## Pet Service API – Spring Boot CRUD Microservice

#### Description

A RESTful application built with Spring Boot for managing pets.
The application exposes APIs for creating, updating, deleting, and retrieving pet information.

## Tech Stack

* Java 21
* Spring Boot 4
* Spring Web
* (Mocked) Relational Database (H2 for development/testing)
* Maven

## Requirements

* [JDK 17+](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven 3.9+](http://maven.apache.org/download.cgi)

## Build with Maven

* [Welcome to Apache Maven](https://maven.apache.org/)
* [Building Java Projects with Maven](https://spring.io/guides/gs/maven/)

## Build

``` 
mvn clean install
``` 

## Run Tests
 
``` 
mvn clean test
``` 

## Run the Application

* Option 1: Using Maven 

```
mvn spring-boot:run
```

* Option 2: Run JAR

```
mvn clean package   
java -jar target/pets-service-1.0.0.jar
```

## API Endpoints

* Base URL: http://localhost:8080/api/pets

Create a Pet:
```
POST localhost:8080/api/pets
{
  "name": "Buddy",
  "species": "Dog",
  "age": 3,
  "ownerName": "John Doe"
}
```
Find a Pet by ID:
```
GET localhost:8080/api/pets/{id}
```
Find All Pets:
```
GET localhost:8080/api/pets
```
Search Pets by Name:
```
GET localhost:8080/api/pets?name=Buddy
```
Search Pets by ownerName:
```
GET localhost:8080/api/pets?ownerName=John Doe
```
Search Pets by age:
```
GET localhost:8080/api/pets?age=3
```
Update a Pet:
```
PUT localhost:8080/api/pets/{id}
{
  "name": "Rex",
  "species": "Dog",
  "age": 4,
  "ownerName": "John Doe"
}
```
Delete a Pet:
```
DELETE localhost:8080/api/pets/{id}
```
Delete All Pet:
```
DELETE localhost:8080/api/pets
```

## H2 Console (for development)

URL:
```
http://localhost:8080/api/h2-console/
```
Configuration:
```
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:file:./data/pets_db
User Name: sa
Password:
```

## Design Notes

* The application follows a layered architecture (Controller → Service → Repository).
* The data access layer is abstracted to allow easy replacement with a non-relational database in the future.
* Validation is applied to required fields and constraints.
* Clean and readable code with separation of concerns.
* For simplicity, I used Hibernate auto schema update. In production, I would use Flyway, Liquibase, or similar tools for versioned migrations.

## Reference Documentation

* https://spring.io/projects/spring-boot
* https://spring.io/guides/gs/rest-service/