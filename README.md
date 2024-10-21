# Daily Expenses Application

## Description
- The Daily Expenses Application is a Spring Boot-based RESTful API that allows users to track their daily expenses.
- Users can add,update,retrieve and download expense reports.
- The application provides a seamless way to manage expenses and generate balance sheets in PDF format.

## Features
- Add new expenses
- Update existing expenses by expense ID
- Retrieve expenses by user ID
- Get all expenses
- Download user-specific balance sheets as PDFs
- Download balance sheets for all users as PDFs

## Technologies Used
- **Java 21**
- **Spring Boot 3.3.4**
- **Spring Data JPA**
- **H2 Database**
- **PDF Generation with Apache PDFBox**

## Getting Started

### 1. Clone the repository
```bash
   git clone https://github.com/Partha6864/Daily-Expenses.git
```

### 2. Build the Project
```bash
   mvn clean install
```

### 3. Run the Application
```bash
   mvn spring-boot:run
```

### 4. Accessing H2 Database Console
```bash
   http://localhost:8080/h2-console
```

### 5. Accessing Swagger UI
```bash
   http://localhost:8080/swagger-ui/index.html
```
