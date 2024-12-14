# Digital Wallet Service

## Overview

This is a Spring Boot application for managing digital wallet transactions.

## Prerequisites

- Docker
- Docker Compose

## Installation

1. Clone the repository:

    ```sh
    git clone https://github.com/yourusername/digital-wallet.git
    cd digital-wallet
    ```

2. Build and run the Docker containers:

    ```sh
    docker-compose up --build
    ```

## Configuration

The application uses environment variables to configure the database connection. These are set in the `docker-compose.yml` file.

## Running the Application

The application will be available at `http://localhost:8080/api/v1/swagger-ui/index.html`.

## Design Choices

### Clean Architecture

The application follows the principles of Clean Architecture to ensure a clear separation of concerns, making the codebase more maintainable and testable. This approach allows for independent development and testing of each layer, promoting high cohesion and low coupling.

### PostgreSQL Database

PostgreSQL was chosen as the database for its robustness, scalability, and support for advanced features such as JSONB, which can be useful for storing semi-structured data. It is also open-source and has a strong community, ensuring long-term support and continuous improvements.

### Spring Boot

Spring Boot was selected for its ability to simplify the development of Java applications. It provides a wide range of features out-of-the-box, such as dependency injection, security, and data access, which accelerates development and reduces boilerplate code. Additionally, Spring Boot's extensive ecosystem and community support make it a reliable choice for building enterprise applications.

### UUID for Identifiers

UUIDs (Universally Unique Identifiers) are used for entity identifiers to ensure uniqueness across distributed systems. This choice helps prevent identifier collisions and makes it easier to merge data from different sources. UUIDs are particularly useful in microservices architectures where entities might be created independently in different services.

## How the Implementation Meets Requirements

### Functional Requirements

- **Transaction Management**: The application provides endpoints for managing digital wallet transactions, ensuring that all operations are atomic and consistent.
- **User Authentication**: JWT (JSON Web Token) is used for secure authentication, ensuring that only authorized users can access the endpoints.

### Non-Functional Requirements

- **Scalability**: The use of Docker and Docker Compose allows the application to be easily scaled horizontally by adding more instances of the service.
- **Maintainability**: The Clean Architecture approach ensures that the codebase is modular and easy to maintain, with clear separation of concerns.
- **Performance**: PostgreSQL is optimized for performance, and the use of UUIDs ensures efficient handling of identifiers.
- **Security**: Spring Boot's security features, combined with JWT, provide a robust security framework for the application.