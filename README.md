# 🚗 Car Garage Course - STEP 1
## 📖 Overview
This is STEP 1 of the Car Garage educational project. The goal is to build a simple command-line application that manages a garage of cars with in-memory CRUD operations.
Learning Objectives
Understand separation of concerns (UI, Business Logic, Data Access)
Learn the DAO pattern (Data Access Object)
Practice interface-based programming
See the importance of decoupling layers
## 🏗️ Architecture
The application follows a layered architecture:
┌─────────────────────────────────────┐
│          CLI (View Layer)           │  ← User interaction
├─────────────────────────────────────┤
│         Controller Layer            │  ← Menu handling, orchestration
├─────────────────────────────────────┤
│         Service Layer               │  ← Business logic, validations
├─────────────────────────────────────┤
│         DAO Layer                   │  ← Data access abstraction
├─────────────────────────────────────┤
│         Model Layer                 │  ← Domain entities
└─────────────────────────────────────┘
## Components
Car (model): Domain entity representing a car
CarDao (interface): Contract for data access operations
CarArrayDao (implementation): In-memory storage using ArrayList
CarService: Business logic and validations
CarController: Handles menu flow and user actions
CarView (interface): Contract for UI operations
CliCarView (implementation): Command-line interface
Main: Application entry point
## 🚀 How to Run
mvn clean compile
Prerequisites
Java 17 or higher
Maven 3.6+
Build the project
```shell 
mvn exec:java -Dexec.mainClass="it.dst.garage.Main"
```
Run the application
```shell 
mvn exec:java -Dexec.mainClass="it.dst.garage.Main"
```

Or compile and run directly:
```shell
mvn clean package
java -jar target/car-garage-1.0-SNAPSHOT.jar
```
## 🧪 Running Tests
Run all tests:

```shell
mvn test
```

Run tests with verbose output:

```shell
mvn test -X
```

## 📋 Features
The application provides a menu with the following options:

List all cars - Display all cars in the garage
Get car details - Show details of a specific car by ID
Add new car - Create a new car entry
Update car - Modify an existing car
Delete car - Remove a car from the garage
Exit - Close the application
Validations
Car ID must be unique
License plate cannot be empty
Year must be a valid number
## 🎯 Key Design Decisions
Why interfaces?
CarDao is an interface → we can swap implementations without changing the service
CarView is an interface → we can add GUI later without touching business logic
Why separate layers?
Controller doesn't know about ArrayList → it only knows about CarDao
Service doesn't know about CLI → it only knows about CarView for output
View doesn't know about business rules → it only displays data
What to notice
No System.out.println() in Service or DAO layers
No business logic in Controller
No data structure details leak outside DAO
## 🔍 Code Structure

```
src/main/java/it/dst/garage/
├── Main.java                    # Entry point
├── model/
│   └── Car.java                 # Domain entity
├── dao/
│   ├── CarDao.java              # DAO interface
│   └── CarArrayDao.java         # ArrayList implementation
├── service/
│   └── CarService.java          # Business logic
├── controller/
│   └── CarController.java       # Menu orchestration
└── view/
    ├── CarView.java             # View interface
    └── CliCarView.java          # CLI implementation
```

## 🧩 Next Steps
After completing STEP 1, you'll move to:

STEP 2: Performance optimization (HashMap instead of ArrayList)
STEP 3: Configuration + Factory pattern
STEP 4: JDBC persistence
STEP 5: Dependency Injection
STEP 6: Proxy pattern for cross-cutting concerns
## 📚 Common Pitfalls (for students)
❌ Don't do this:

Put System.out.println() in Service or DAO
Let Controller manipulate ArrayList directly
Mix UI code with business logic
✅ Do this:

Keep layers separated
Program to interfaces, not implementations
Let each layer have a single responsibility
📝 Notes
This is a teaching project. The code includes educational comments to help understand design decisions and patterns.

Author: DST Training
Version: 1.0 - STEP 1