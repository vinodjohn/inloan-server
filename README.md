# Loan Server Application

## Overview

The **Loan Server Application** is a service-oriented application designed to manage loan-related data and provide 
functionalities like loan offers and loan contract management. It is built using various modern Java technologies 
including Spring Boot, Spring Data JPA, Spring MVC. This project is intended to serve as a backend service 
that interacts with a database for user and loan management.

_Please note that the initial task was to build only loan decision engine with single endpoint. But to add some 
additional features, the app has been developed with security through ID code, saving loan offer, application and contracts to 
the DB. Also, Admin user can modify the credit_modifier, loan limits etc. through the front-end application._

The front-end application can be found **here** - [GitHub](https://github.com/vinodjohn/inloan-client)

**View this app hosted in AWS here:**  http://54.93.218.156:8080/

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Authors](#authors)
- [License](#license)

## Features

- **User Authentication**: Manage user login and registration.
- **Loan Offer Management**: Provide loan offers based on user data.
- **Data Initialization**: Populate initial data for testing and development.
- **Sorting and Validation Utilities**: Helper utilities for various functionalities.
- **Security Configurations**: Set up application-wide security measures using JWT.

## Technologies Used

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **Spring MVC**
- **Spring Security**
- **PostgreSQL**

**Note:** _Lombok_ library has been used in this project. So, Lombok plugin may needed to run this project. In case of Lombok crash, re-enable and run again.

## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21 SDK**
- **Maven** (for dependency management and build)
- **PostgreSQL 16**
- **Docker** and **Docker Compose** (for containerized deployment)

### Installation

1. **Clone the repository:**
    ```sh
    git clone https://github.com/vinodjohn/inloan-server.git
    cd loan-server
    ```

2. **Install dependencies:**
    ```sh
    mvn clean install
    ```

### Running the Application

1. **Using Docker Compose:**

   Ensure Docker is installed and running.

    ```sh
    docker-compose up --build
    ```

2. **Running Local

   Ensure you have a running instance of your database configured in `application.properties`.

    ```sh
    mvn spring-boot:run -Dspring-boot.run.jvmArguments="--enable-preview"
    ```
   
_**Note:** This app doesn't have any preview features of Java._
   
### Key Files

- `LoanServerApplication.java`: Entry point for Spring Boot application.
- `SecurityConfiguration.java`: Configuration for security aspects.
- `LoanServiceImpl.java`: Implementation of loan decision service
- `LoanOfferServiceImpl.java`: Implementation of loan offer services.
- `AuthController.java`: REST controller for authentication.
- `LoanUtils.java`: Utility class for various functionalities.
- `application.properties`: Configuration properties for the application.

## Configuration

Configuration for the application can be found in the `application.properties` file located in `src/main/resources`. Key properties include:

```properties
# Sample database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/inloan
spring.datasource.username=root
spring.datasource.password=root

# Hibernate properties
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Make sure to adjust these settings according to your local environment or Docker configuration.

## Authors

- **Vinod John** - [GitHub](https://github.com/vinodjohn)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
