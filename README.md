# TechFix – Computer & Mobile Repair Management System

TechFix is an Android-based repair management application developed to support a computer and mobile phone repair business. The system provides separate functionality for **customers, administrators, and technicians**, allowing repair bookings, appointment management, repair tracking, technician updates, payments, and service management through a centralized SQLite database.

## Features

### Customer Module

* Customer registration and login
* Customer profile management
* Browse repair services
* View service details and pricing
* Book repair appointments
* Select device category and service
* Enter device model and problem description
* Upload device images
* Find the nearest TechFix branch using location services
* Track active repair requests
* View repair progress and status
* View assigned technician information
* View repair history
* View payment history
* Make repair payments
* Cancel pending repair bookings

### Admin Module

* Admin authentication
* Admin dashboard
* Manage TechFix branches
* Manage device categories
* Manage repair services
* Manage technicians
* Manage spare parts
* Manage repair samples
* Manage repair appointments
* Assign technicians to repair appointments
* Update appointment status
* Set repair prices
* Manage payment records
* View repair and customer information
* Monitor repair statistics

### Technician Module

* Technician login
* Technician-specific dashboard
* View assigned repairs
* View complete repair details
* View customer information
* View device and service information
* View assigned branch information
* Update repair status
* Add technician repair notes
* Record spare parts used
* Record spare part quantities
* Capture repair work photos
* Maintain technician repair update history
* View technician-specific repair history
* Dashboard statistics based on technician assignments

## Technologies Used

* **Java**
* **Android Studio**
* **SQLite**
* **Android SDK**
* **RecyclerView**
* **Material Components**
* **Android Activity Result APIs**
* **Location Services / GPS**
* **Camera**
* **Git & GitHub**

## Database

The application uses a local **SQLite database** called `TechFix.db`.

The main database entities include:

* Users
* Customers
* Branches
* Categories
* Services
* Repairs
* Payments
* Repair Samples
* Spare Parts
* Repair Updates

The database is managed through a centralized `DatabaseHelper` class, which provides database creation, upgrades, CRUD operations, authentication, repair management, technician assignment, payment management, and repair history functionality.

## System Roles

### Customer

```text
Customer
   ↓
Login / Register
   ↓
Browse Services
   ↓
Book Repair
   ↓
Track Repair
   ↓
View Technician Updates
   ↓
Payment
   ↓
Repair History
```

### Administrator

```text
Admin
   ↓
Admin Dashboard
   ├── Branches
   ├── Categories
   ├── Services
   ├── Technicians
   ├── Spare Parts
   ├── Repair Appointments
   ├── Repair Samples
   └── Payments
```

### Technician

```text
Technician
   ↓
Technician Dashboard
   ↓
Assigned Repairs
   ↓
Repair Details
   ↓
Update Repair
   ├── Status
   ├── Notes
   ├── Spare Parts
   ├── Quantity
   └── Repair Photo
   ↓
Repair History
```

## Advanced Android Features

The application demonstrates several Android technologies:

### SQLite Database

SQLite is used for persistent local storage and management of customers, services, branches, repairs, technicians, payments, spare parts, and repair updates.

### GPS / Location

Location services are used to identify the nearest TechFix branch based on the customer's current location.

### Camera

Technicians can capture repair work photographs during the repair update process.

### Image Upload

Customers can upload device images when submitting a repair request.

### RecyclerView

RecyclerView is used for displaying dynamic lists such as services, technicians, appointments, repairs, spare parts, and repair history.

### Activity Result API

Modern Android Activity Result APIs are used for operations such as launching forms, selecting images, and capturing camera previews.

## Project Structure

```text
TechFix-Repair-App
│
├── app
│   └── src
│       └── main
│           ├── java
│           │   └── com.up9.techfix
│           │       ├── ActorCustomer
│           │       ├── Technician
│           │       ├── admin
│           │       └── data
│           │
│           ├── res
│           │   ├── drawable
│           │   ├── layout
│           │   ├── mipmap
│           │   └── values
│           │
│           └── AndroidManifest.xml
│
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Installation

### Requirements

* Android Studio
* Android SDK
* Java Development Kit
* Android device or emulator
* Git

### Setup

1. Clone the repository:

```bash
git clone https://github.com/udath9/TechFix-Repair-App.git
```

2. Open the project in Android Studio.

3. Allow Gradle to synchronize the project.

4. Connect an Android device or start an emulator.

5. Build and run the application.

## Default Accounts

The application contains default accounts for development and testing.

### Administrator

```text
Email: admin@techfix.com
Password: admin123
Role: ADMIN
```

Customers can create accounts through the registration screen.

> For a production application, passwords should not be stored as plain text.

## Application Workflow

The overall repair workflow is:

```text
Customer creates repair request
            ↓
Admin receives repair appointment
            ↓
Admin reviews repair
            ↓
Admin assigns branch and technician
            ↓
Technician receives assigned repair
            ↓
Technician updates repair status
            ↓
Technician records notes / spare parts / photos
            ↓
Customer tracks repair progress
            ↓
Repair completed
            ↓
Customer makes payment
            ↓
Repair appears in history
```

## GitHub Repository

**Repository:**
https://github.com/udath9/TechFix-Repair-App

## Development Approach

The project was developed using Git and GitHub for version control and collaborative development. Separate branches were used for different system modules before integration into the main project branch.

Major modules were integrated into the `integration` branch and subsequently merged into `main`.

## Future Improvements

Possible future improvements include:

* Cloud-based backend and remote database
* Firebase authentication
* Push notifications
* Online payment gateway integration
* Technician availability management
* Real-time repair tracking
* Automated appointment notifications
* Secure password hashing
* Cloud image storage
* Role-based access control improvements

## Academic Project

This application was developed as part of the **Higher National Diploma in Software Engineering** coursework at the **National Institute of Business Management (NIBM)**.

The project focuses on applying Android development, database management, user interface design, software engineering principles, and collaborative version control to a real-world repair management scenario.

## License

This project was developed for academic and educational purposes.
