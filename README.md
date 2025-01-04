# 🚀 KAM Lead Management System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.1-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📋 Table of Contents
- [🚀 KAM Lead Management System](#-kam-lead-management-system)
  - [📋 Table of Contents](#-table-of-contents)
  - [🎯 Overview](#-overview)
  - [✨ Features](#-features)
  - [💻 System Requirements](#-system-requirements)
  - [🛠 Tech Stack](#-tech-stack)
  - [🚀 Getting Started](#-getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Database Setup](#database-setup)
  - [🎮 Running the Application](#-running-the-application)
  - [🔐 Authentication](#-authentication)
    - [Quick Access Credentials](#quick-access-credentials)
    - [Token Usage](#token-usage)
  - [📝 Usage Examples](#-usage-examples)
    - [1. Creating a New Restaurant Lead (Admin Only)](#1-creating-a-new-restaurant-lead-admin-only)
    - [2. Adding a Contact to Restaurant](#2-adding-a-contact-to-restaurant)
  - [📊 Database Schema](#-database-schema)
  - [🧪 Testing](#-testing)
  - [🌐 Deployment](#-deployment)
    - [Environment Variables for Deployment](#environment-variables-for-deployment)

## 🎯 Overview

The KAM Lead Management System is a comprehensive B2B platform designed for Udaan's Key Account Managers (KAMs) to efficiently manage relationships with large restaurant accounts. This system streamlines lead tracking, interaction management, and performance monitoring of restaurant accounts.

## ✨ Features

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

- **📊 Performance Metrics**
  - Real-time performance tracking
  - Order analytics
  - Revenue monitoring

## 💻 System Requirements

- Java Development Kit (JDK) 17 or higher
- Maven 3.8+ or Gradle 7.0+
- MySQL 8.0+
- Minimum 4GB RAM
- 10GB available disk space

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
java -version
```

2. Install Maven
```bash
# Verify Maven installation
mvn -version
```

3. Install MySQL
```bash
# Verify MySQL installation
mysql --version
```

### Installation

1. Clone the repository
```bash
git clone https://github.com/yourusername/kam-lead-management.git
cd kam-lead-management
```

2. Configure application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/kam_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Build the project
```bash
mvn clean install
```

### Database Setup

```sql
-- Create database
CREATE DATABASE kam_db;

-- The application will automatically create tables on startup
```

## 🎮 Running the Application

1. Start the application
```bash
mvn spring-boot:run
```

2. Access the application
- Local: http://localhost:8080
- Deployed: https://kam-lead-management.onrender.com

3. Access Swagger documentation
- http://localhost:8080/swagger-ui/index.html

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
    "leadStatus": "NEW"
  }'
```

### 2. Adding a Contact to Restaurant

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

## 📊 Database Schema

The system uses a relational database with the following core tables:
- users
- restaurants
- contacts
- interactions
- orders
- call_schedules
- performance_metrics
- restaurant_users

For detailed schema information, refer to the SQL schemas provided in the project.

## 🧪 Testing

Run the test suite:
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Generate test coverage report
mvn verify
```

## 🌐 Deployment

The application is currently deployed on Render.com. Access it at:
https://kam-lead-management.onrender.com

### Environment Variables for Deployment
```
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=your_database_url
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
JWT_SECRET=your_jwt_secret
```

---



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