# 🚀 KAM Lead Management System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

[https://kam-lead-management.onrender.com](https://kam-lead-management.onrender.com)


[Demo video](https://drive.google.com/file/d/18MlvyIGriNoQV-VzSNDsiTCaQRXHkxmZ/view?usp=sharing)

## 📋 Table of Contents
- [🚀 KAM Lead Management System](#-kam-lead-management-system)
  - [📋 Table of Contents](#-table-of-contents)
  - [🎯 Overview](#-overview)
  - [📲 Flow of the Application](#-flow-of-the-application)
    - [Introduction](#introduction)
    - [System Access Levels](#system-access-levels)
    - [User Types](#user-types)
    - [API Documentation](#api-documentation)
    - [1. Authentication](#1-authentication)
      - [Login](#login)
    - [2. Restaurant Management](#2-restaurant-management)
      - [Create Restaurant (ADMIN only)](#create-restaurant-admin-only)
      - [Restaurant Endpoints (Admin or assigned Manager)](#restaurant-endpoints-admin-or-assigned-manager)
    - [3. User Management](#3-user-management)
      - [Create User (ADMIN only)](#create-user-admin-only)
      - [User Endpoints (Admin or assigned Manager)](#user-endpoints-admin-or-assigned-manager)
    - [4. Restaurant-User Assignment](#4-restaurant-user-assignment)
      - [Assign Restaurant (ADMIN only)](#assign-restaurant-admin-only)
    - [5. Contact Management](#5-contact-management)
      - [Contact Endpoints (Admin or assigned Manager)](#contact-endpoints-admin-or-assigned-manager)
      - [Add Contact](#add-contact)
    - [6. Interaction Tracking](#6-interaction-tracking)
      - [Interaction endpoints (Admin or assigned Manager)](#interaction-endpoints-admin-or-assigned-manager)
      - [Create Interaction](#create-interaction)
      - [Interaction Endpoints](#interaction-endpoints)
    - [7. Order Management](#7-order-management)
      - [Order Endpoints (Admin or assigned Manager)](#order-endpoints-admin-or-assigned-manager)
      - [Create Order](#create-order)
      - [Order Endpoints](#order-endpoints)
    - [8. Call Schedule Management](#8-call-schedule-management)
      - [Call Schedule Endpoints (Admin or assigned Manager)](#call-schedule-endpoints-admin-or-assigned-manager)
      - [Create Schedule](#create-schedule)
    - [9. Performance Metrics](#9-performance-metrics)
      - [Performance Metrics Endpoints (Admin or assigned Manager)](#performance-metrics-endpoints-admin-or-assigned-manager)
      - [Add Metrics](#add-metrics)
  - [✨ Features](#-features)
  - [💻 System Requirements](#-system-requirements)
  - [🛠 Tech Stack](#-tech-stack)
  - [🚀 Getting Started](#-getting-started)
    - [Prerequisites](#prerequisites)
    - [Database Setup](#database-setup)
  - [🎮 Running the Application](#-running-the-application)
  - [🔐 Authentication](#-authentication)
    - [Quick Access Credentials](#quick-access-credentials)
    - [Token Usage](#token-usage)
  - [📝 Usage Examples](#-usage-examples)
    - [1. Creating a New Restaurant Lead (Admin Only)](#1-creating-a-new-restaurant-lead-admin-only)
    - [2. Adding a New User/Manager (Admin Only)(Admin Only)](#2-adding-a-new-usermanager-admin-onlyadmin-only)
      - [Request Example](#request-example)
    - [3. Adding a Contact to Restaurant](#3-adding-a-contact-to-restaurant)
  - [📄 Access Swagger documentation](#-access-swagger-documentation)
    - [Swagger UI URLs:](#swagger-ui-urls)
    - [Note: Obtaining the Auth Token](#note-obtaining-the-auth-token)
      - [Quick Access Credentials:](#quick-access-credentials-1)
  - [📄 Google docx documentation](#-google-docx-documentation)
  - [⛁ Database Schema](#-database-schema)
  - [🧪 Testing](#-testing)
    - [Key Test Classes](#key-test-classes)
    - [Running the Test Suite](#running-the-test-suite)
  - [🌐 Deployment](#-deployment)
  - [🗂️Folder Structure](#️folder-structure)

## 🎯 Overview

The KAM Lead Management System is a comprehensive B2B platform designed for Udaan's Key Account Managers (KAMs) to efficiently manage relationships with large restaurant accounts. This system streamlines lead tracking, interaction management, and performance monitoring of restaurant accounts.

## 📲 Flow of the Application

### Introduction
Udaan, a B2B e-commerce platform, requires a Lead Management System for Key Account Managers (KAMs) who manage relationships with large restaurant accounts. This system will help track and manage leads, interactions, and account performance.

### System Access Levels

### User Types
* **ADMIN**
  * Super admin with complete access
  * Can access all APIs without restriction
  * Responsible for system management

* **MANAGER**
  * Limited access based on assignments
  * Can only access assigned restaurant data
  * Restricted API access

### API Documentation

### 1. Authentication
#### Login
```json
POST api/auth/login
{
    "username": "admin",
    "password": "admin123"
}
```
* Bearer token must be included in header for all subsequent requests

### 2. Restaurant Management
#### Create Restaurant (ADMIN only)
```json
POST api/restaurant
{
    "name": "my restaurnt",
    "address": "xyz fkdsjkf ",
    "city": "patna",
    "phone": "9878456734",
    "state": "up",
    "annualRevenue": 344,
    "leadStatus": "CONTACTED",
    "email": "borsedhssiffdraj123@gmail.com"
}
```

#### Restaurant Endpoints (Admin or assigned Manager)
* Get Details: `GET /api/restaurant/{restaurant_id}`
* Update Info: `PUT /restaurants/{restaurant_id}`
* List All: `GET /api/restaurant`
* Filter Restaurants: `GET /api/restaurant?leadStatus=NEW&&city=someCity&&search=any_search&&page=1&&size=2`

### 3. User Management
#### Create User (ADMIN only)
```json
POST api/auth/register
{
    "username": "newUsername",
    "email": "user@1234.com",
    "firstName": "userFname",
    "lastName": "userLname",
    "role": "MANAGER",
    "isActive": true,
    "password": "user123"
}
```

#### User Endpoints (Admin or assigned Manager)
* List Users: `GET /users`
* Get User: `GET /users/{user_id}`
* Update User: `PUT /users/{id}`
* Delete User: `DELETE /users/{id}`
* Current User: `GET /users/current`

### 4. Restaurant-User Assignment
#### Assign Restaurant (ADMIN only)
```json
POST api/restaurant-users
{
    "restaurant": {
        "id": 1
    },
    "user": {
        "id": 4
    },
    "isActive": true
}
```

### 5. Contact Management
#### Contact Endpoints (Admin or assigned Manager)
#### Add Contact
```json
POST /api/contacts/{restaurant_id}
{
    "firstName": "Vivek",
    "lastName": "Sharma",
    "role": "chef",
    "email": "Vivek@1235.com",
    "phone": 3245789878
}
```

* List Contacts: `GET /api/contacts/{restaurant_id}/contacts`
* Update Contact: `PUT /api/contacts/{restaurant_id}/{contact_id}`
* Delete Contact: `DELETE /api/contacts/{restaurant_id}/{contact_id}`

### 6. Interaction Tracking
#### Interaction endpoints (Admin or assigned Manager)
#### Create Interaction
```json
POST /api/interactions/{restaurant_id}?requestingUserId=userId&&contactId=contactId
{
    "interactionType": "EMAIL",
    "interactionDate": "2024-12-30T14:00:00",
    "notes": "order status",
    "followUpDate": "2025-01-02T10:00:00"
}
```

#### Interaction Endpoints
* List Interactions: `GET /api/interactions/{restaurant_id}`
* Update Interaction: `PUT /api/interactions/{restaurant_id}/{interactionId}`
* Delete Interaction: `DELETE /api/interactions/{restaurant_id}/{interactionId}`

### 7. Order Management
#### Order Endpoints (Admin or assigned Manager)
#### Create Order
```json
POST /api/orders/{restaurant_id}
{
    "totalAmount": "45.62",
    "status": "DELIVERED",
    "orderDate": "2025-01-03T16:50:17"
}
```

#### Order Endpoints
* List Orders: `GET /api/orders/{restaurant_id}`
* Update Order: `PUT /api/orders/{restaurant_id}/{orderId}`
* Delete Order: `DELETE /api/orders/{restaurant_id}/{orderId}`

### 8. Call Schedule Management
#### Call Schedule Endpoints (Admin or assigned Manager)
#### Create Schedule
```json
POST /api/call-schedules/{restaurant_id}
{
    "frequencyDays": 3,
    "lastCallDate": "2025-01-01T18:30:00",
    "nextCallDate": "2025-01-10T18:30:00",
    "priorityLevel": 1
}
```

* Get Schedule: `GET /api/call-schedules/{restaurant_id}`
* Update Schedule: `PUT /api/call-schedules/{restaurant_id}/{scheduleId}`
* Delete Schedule: `DELETE /api/call-schedules/{restaurant_id}/{scheduleId}`

### 9. Performance Metrics
#### Performance Metrics Endpoints (Admin or assigned Manager)
#### Add Metrics
```json
POST /api/performance_metrics/{restaurant_id}
{
    "metricDate": "2025-01-01T10:15:00",
    "leadsCount": 3,
    "closedDeals": 2,
    "revenue": 1000.245,
    "followUpsCount": 435
}
```

* Get Metrics: `GET /api/performance_metrics/{restaurant_id}`
* Update Metrics: `PUT /api/performance_metrics/{restaurant_id}/{metricId}`
* Delete Metrics: `DELETE /api/performance_metrics/{restaurant_id}/{metricId}`

## ✨ Features

- **🏪 Restaurant Management**
  - Complete restaurant lifecycle tracking (new → contacted → qualified → negotiating → converted/lost)
  - Advanced filtering and search (status, city, name)
  - Restaurant profile management
  - Annual revenue tracking
  - Multi-location support
  - Timezone-based operations
  - Customizable lead status workflows
  - Bulk restaurant import/export
  - Automated status transitions
  - Performance metric dashboard

- **👥 User Management**
  - Role-based access control (Admin/Manager)
  - Secure authentication and authorization
  - User profile management

- **🏪 Restaurant/Lead Management**
  - Complete restaurant lifecycle management
  - Lead status tracking
  - Advanced filtering and search capabilities

- **📞 Contact Management**
  - Multiple points of contact per restaurant
  - Contact role management
  - Contact history tracking

- **🤝 Interaction Tracking**
  - Detailed interaction logging
  - Meeting and call records
  - Follow-up scheduling
  
- **📅 Call Schedule Management**
  - Customizable call frequencies per restaurant
  - Priority level assignment (1-5)
  - Automated next call date calculation 
  - Last call tracking

- **📦 Order Management**
  - Complete order lifecycle tracking
  - Multiple order statuses (pending, confirmed, delivered, cancelled)
  - Total amount calculation
  - Order history logging

- **📊 Performance Metrics**
  - Real-time performance tracking
  - Order analytics
  - Revenue monitoring

## 💻 System Requirements

- Java Development Kit (JDK) 17 or higher
- Maven 3.8+ or Gradle 7.0+
- MySQL 8.0+
- Minimum 4GB RAM
- Node 18 or higher

## 🛠 Tech Stack

- **Backend Framework:** Spring Boot 3.4.1
- **Security:** Spring Security with JWT
- **Database:** MySQL
- **API Documentation:** Swagger/OpenAPI
- **Build Tool:** Maven
- **Testing:** JUnit 5, Mockito

## 🚀 Getting Started

### Prerequisites

1. Install JDK 17
```bash
# Verify Java installation
# should be 17
java -version
```

2. Install Maven
```bash
# Verify Maven installation
mvn -version
```

```

### Installation

1. Clone the repository
```bash
git clone https://github.com/DhirajsGithub/KAM-Lead-Management
cd KAM-Lead-Management
```

2. Configure application.properties
```properties
spring.application.name=kam-lead-management

spring.jpa.hibernate.ddl-auto=update
spring.jpa.defer-datasource-initialization=true

spring.datasource.url=jdbc:postgresql://dpg-cts3dkrtq21c7395rgvg-a.oregon-postgres.render.com/my_postgre_db_drkf
spring.datasource.username=dhiraj
spring.datasource.password=fIjayLVIYBRI2zWbSBY9NjkZokEGLF1J

jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970

# Enable OAuth2 and Bearer token authentication
springdoc.api-docs.security-schemes.oauth2.bearer.token-type=BEARER
springdoc.api-docs.security-schemes.oauth2.bearer.in=header
springdoc.api-docs.security-schemes.oauth2.bearer.name=Authorization
springdoc.api-docs.security-schemes.oauth2.bearer.scheme=Bearer

```

3. Build the project
```bash
mvn clean install
```

### Database Setup
The application is pre-configured to use a PostgreSQL database hosted on Render. You don't need to set up a local database, as the configuration is already provided in the `application.properties` file. This ensures seamless integration with the database during development and production.

4. Setting up the frontend
```bash
cd frontend
npm install
npm run dev
```

1. Change the base URL of backend if you want to local, because the default one is deployed backend ur
```bash
go to frontend/constants/baseUrl.ts and change the base url to http://localhost:8080
```

1. the application is running on http://localhost:5173/


## 🎮 Running the Application

1. Start the application
```bash
mvn spring-boot:run
```

2. Access the application
- Local: http://localhost:8080
- Deployed: https://kam-lead-management.onrender.com


## 🔐 Authentication

### Quick Access Credentials

1. Admin Access
```json
{
    "username": "admin",
    "password": "admin123"
}
```

2. Manager Access
```json
{
    "username": "dhiraj",
    "password": "dhiraj"
}
```

### Token Usage

1. Obtain JWT Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

2. Use Token in Requests
```bash
curl -X GET http://localhost:8080/api/restaurants \
  -H "Authorization: Bearer your_token_here"
```

## 📝 Usage Examples

### 1. Creating a New Restaurant Lead (Admin Only)

```bash
curl -X POST http://localhost:8080/api/restaurant \
  -H "Authorization: Bearer your_token_here" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Restaurant Name",
    "address": "123 Main St",
    "city": "Mumbai",
    "state": "Maharashtra",
    "phone": "1234567890",
    "email": "restaurant@example.com",
    "annualRevenue": 344,
    "leadStatus": "NEW"
  }'
```

### 2. Adding a New User/Manager (Admin Only)(Admin Only)

Admins can create a new restaurant lead by making a `POST` request to the `/api/restaurant` endpoint. 

#### Request Example

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Authorization: Bearer your_token_here" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "dhiraj",
    "email": "dhiraj@1234.com",
    "firstName": "Dhiraj",
    "lastName": "Borse",
    "role": "MANAGER",
    "isActive": true,
    "password": "dhiraj"
  }'

```

### 3. Adding a Contact to Restaurant

```bash
curl -X POST http://localhost:8080/api/contacts/{restaurant_id} \
  -H "Authorization: Bearer your_token_here" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "role": "Manager",
    "email": "john@example.com",
    "phone": "1234567890",
    "isPrimary": true
  }'
```

and so on....

## 📄 Access Swagger documentation


### Swagger UI URLs:
- **Local Environment**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)  
- **Deployed Environment**: [https://kam-lead-management.onrender.com/swagger-ui/index.html](https://kam-lead-management.onrender.com/swagger-ui/index.html)  

By clicking the **Authorize** button in Swagger and providing the token, you can test endpoints seamlessly without manually adding the token to each request.

### Note: Obtaining the Auth Token

Before making API requests, you need to obtain an authorization token by logging in as a user or an admin. Follow these steps:

1. **Login to Get Token**:
   - Use the `/api/auth/login` endpoint to authenticate and retrieve your token.
   - The token will be required for all API requests.
   
   #### Quick Access Credentials:
   - **Admin Access**  
     ```json
     {
       "username": "admin",
       "password": "admin123"
     }
     ```

   - **Manager Access**  
     ```json
     {
       "username": "dhiraj",
       "password": "dhiraj"
     }
     ```

2. **Using Swagger for API Testing**:
   - Open the Swagger UI to explore and test the APIs interactively.
   - Click on the **Authorize** button at the top-right corner of Swagger.
   - Paste your Bearer token in the dialog box and click **Authorize** to authenticate your session.

## 📄 Google docx documentation
[Google docx documentation](https://docs.google.com/document/d/134lFCtR_9xPC8DxtG88t_1YOVsIMAfB2kEXO8ieFStA/edit?usp=sharing)

## ⛁ Database Schema

The system uses a relational database with the following core tables:
- users
- restaurants
- contacts
- interactions
- orders
- call_schedules
- performance_metrics
- restaurant_users

```mermaid
erDiagram
    users ||--o{ restaurant_users : has
    users {
        int user_id PK
        string username
        string email
        string password
        string first_name
        enum role
        string last_name
        timestamp created_at
        boolean is_active
    }

    restaurants ||--o{ restaurant_users : has
    restaurants ||--o{ contacts : has
    restaurants ||--o{ interactions : has
    restaurants ||--o{ orders : has
    restaurants ||--o{ call_schedules : has
    restaurants ||--o{ performance_metrics : has
    restaurants {
        int restaurant_id PK
        string name
        text address
        string city
        string state
        string phone
        string email
        timestamp created_at
        enum lead_status
        decimal annual_revenue
        string timezone
    }

    restaurant_users {
        int restaurant_id PK,FK
        int user_id PK,FK
        enum relationship_type
        timestamp created_at
    }

    contacts {
        int contact_id PK
        int restaurant_id FK
        string first_name
        string last_name
        string role
        string email
        string phone
        boolean is_primary
        timestamp created_at
    }

    interactions {
        int interaction_id PK
        int restaurant_id FK
        int contact_id FK
        int user_id FK
        enum interaction_type
        timestamp interaction_date
        text notes
        timestamp follow_up_date
        timestamp created_at
    }

    orders {
        int order_id PK
        int restaurant_id FK
        timestamp order_date
        decimal total_amount
        enum status
        timestamp created_at
    }

    call_schedules {
        int schedule_id PK
        int restaurant_id FK
        int frequency_days
        timestamp last_call_date
        timestamp next_call_date
        int priority_level
        timestamp created_at
    }

    performance_metrics {
        int metric_id PK
        int restaurant_id FK
        timestamp measurement_date
        int order_count
        decimal total_revenue
        decimal average_order_value
        int days_since_last_order
        timestamp created_at
    }
```

## 🧪 Testing

The project includes a comprehensive test suite with **~70 tests** covering various components of the application, including edge cases. The test suite ensures high reliability and robustness of the application.

### Key Test Classes
The test suite is organized into the following classes:

- **CallScheduleTests.java**: Tests for managing call schedules.
- **ContactTests.java**: Tests for handling contacts.
- **InteractionTests.java**: Covers various user interactions.
- **KamLeadManagementApplicationTests.java**: Integration and application context tests.
- **OrderTests.java**: Tests for managing orders.
- **PerformanceMetricTests.java**: Tests for performance metrics.
- **RestaurantTests.java**: Includes edge cases for restaurant management.
- **RestaurantUserTests.java**: Covers tests related to restaurant-user relationships.
- **UserTests.java**: Extensive tests for user management, including authentication and authorization.

### Running the Test Suite

You can run the test suite using the following commands:

```bash
# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=UserTests

# Generate a test coverage report
mvn verify
```

## 🌐 Deployment

The application is currently deployed on Render.com. Access it at:
https://kam-lead-management.onrender.com


---

## 🗂️Folder Structure


```
KAM-Lead-Management/
├── Dockerfile
├── HELP.md
├── README.md
├── pom.xml
├── .gitignore
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── udaan/
│   │   │           └── kam/
│   │   │               └── kam_lead_management/
│   │   │                   ├── KamLeadManagementApplication.java
│   │   │                   ├── config/
│   │   │                   │   └── SwaggerConfig.java
│   │   │                   ├── controller/
│   │   │                   │   ├── CallScheduleController.java
│   │   │                   │   ├── ContactController.java
│   │   │                   │   ├── InteractionController.java
│   │   │                   │   ├── OrderController.java
│   │   │                   │   ├── PerformanceMetricController.java
│   │   │                   │   ├── RestaurantController.java
│   │   │                   │   ├── RestaurantUserController.java
│   │   │                   │   └── UserController.java
│   │   │                   ├── dto/
│   │   │                   │   ├── InteractionDTO.java
│   │   │                   │   ├── RestaurantDTO.java
│   │   │                   │   ├── RestaurantDetailDTO.java
│   │   │                   │   ├── UserDTO.java
│   │   │                   │   └── UserDetailDTO.java
│   │   │                   ├── entity/
│   │   │                   │   ├── CallSchedule.java
│   │   │                   │   ├── Contact.java
│   │   │                   │   ├── Interaction.java
│   │   │                   │   ├── Order.java
│   │   │                   │   ├── PerformanceMetric.java
│   │   │                   │   ├── Restaurant.java
│   │   │                   │   ├── RestaurantUser.java
│   │   │                   │   └── User.java
│   │   │                   ├── exception/
│   │   │                   │   ├── BadRequestException.java
│   │   │                   │   ├── CallScheduleNotFoundException.java
│   │   │                   │   ├── ContactNotFoundException.java
│   │   │                   │   ├── DuplicateRelationshipException.java
│   │   │                   │   ├── ErrorResponse.java
│   │   │                   │   ├── GlobalExceptionHandler.java
│   │   │                   │   ├── InteractionNotFoundException.java
│   │   │                   │   ├── OrderNotFoundException.java
│   │   │                   │   ├── PerformanceMetricNotFoundException.java
│   │   │                   │   ├── ResourceNotFoundException.java
│   │   │                   │   ├── RestaurantNotFoundException.java
│   │   │                   │   ├── UnauthorizedAccessException.java
│   │   │                   │   ├── UserNotFoundException.java
│   │   │                   │   └── ValidationException.java
│   │   │                   ├── repository/
│   │   │                   │   ├── CallScheduleRepository.java
│   │   │                   │   ├── ContactRepository.java
│   │   │                   │   ├── InteractionRepository.java
│   │   │                   │   ├── OrderRepository.java
│   │   │                   │   ├── PerformanceMetricRepository.java
│   │   │                   │   ├── RestaurantRepository.java
│   │   │                   │   ├── RestaurantUserRepository.java
│   │   │                   │   ├── ScheduleRepository.java
│   │   │                   │   └── UserRepository.java
│   │   │                   ├── security/
│   │   │                   │   ├── AuthRequest.java
│   │   │                   │   ├── AuthResponse.java
│   │   │                   │   ├── JwtAuthenticationFilter.java
│   │   │                   │   ├── JwtService.java
│   │   │                   │   ├── SecurityConfig.java
│   │   │                   │   ├── UserDetailsImpl.java
│   │   │                   │   ├── UserDetailsServiceImpl.java
│   │   │                   │   └── CORSConfigurationSource.java
│   │   │                   ├── service/
│   │   │                   │   ├── CallScheduleService.java
│   │   │                   │   ├── ContactService.java
│   │   │                   │   ├── InteractionService.java
│   │   │                   │   ├── OrderService.java
│   │   │                   │   ├── PerformanceMetricService.java
│   │   │                   │   ├── RestaurantService.java
│   │   │                   │   ├── RestaurantUserService.java
│   │   │                   │   └── UserService.java
│   │   │                   └── util/
│   │   │                       ├── DTOConverterUtil.java
│   │   │                       └── PermissionUtils.java
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   ├── schema.sql
│   │   │   ├── static/
│   │   │   └── templates/
│   ├── test/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── udaan/
│   │   │           └── kam/
│   │   │               └── kam_lead_management/
│   │   │                   ├── CallScheduleTests.java
│   │   │                   ├── ContactTests.java
│   │   │                   ├── InteractionTests.java
│   │   │                   ├── KamLeadManagementApplicationTests.java
│   │   │                   ├── OrderTests.java
│   │   │                   ├── PerformanceMetricTests.java
│   │   │                   ├── RestaurantTests.java
│   │   │                   ├── RestaurantUserTests.java
│   │   │                   ├── UserTests.java
│   │   ├── resources/
├── target/
│   ├── classes/
│   ├── generated-sources/
│   ├── maven-status/
│   ├── surefire-reports/
│   └── test-classes/
├── frontend/
│   ├── README.md
│   ├── eslint.config.js
│   ├── index.html
│   ├── node_modules/
│   ├── package-lock.json
│   ├── package.json
│   ├── public/
│   ├── src/
│   ├── tsconfig.app.json
│   ├── tsconfig.json
│   └── vite.config.ts
└── mvnw
└── mvnw.cmd
```
