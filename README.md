# PLMS
## Description
This repository contains the code of a Parking Lot Management Software System which was developed by our team of 6 (see table below).  

This software system aims to provide a fully functional Parking Lot Management System to a client, which would be the owner of the parking lot, so that it helps them coordinate tasks related to spot reservations, parking lot services, hiring of employees to name a few. The Parking Lot Management System allows monthly customers, employees and the owners to access their account and interact with the system in an appropriate way for their respective positions. 

Most Importantly, the system was designed to be adaptable and scalable to accommodate for different types of parking lots (different numbers of spots on each floor, different fees for the spots, etc.). 

The software system was designed to respond to a set of requirements specified by the client through a description which was translated into a set of 15 system requirements (10 functional and 5 functional) which can be seen through the GitHub issues. 

Our software system's backend can be run by executing, inside the PLMS folder, the "./gradlew bootRun" command. The app is deployed to "http://localhost:8080".

The frontend can be run by executing, inside the PLMS-Frontend, the "npm run dev" command. The frontend is deployed to your default web browser.

To run our unit and integration tests, please cd into the PLMS folder and run `./gradlew clean test`. 
To run our integration tests alone, please cd into the PLMS folder and run `./gradlew clean integrationTest`.

If the backend is run for the first time on a new database (or if the database was reset) then the parking lot is reinitialized as follows. 
- The default email for the owner is admin@mail.com with password MyParking1ot$ .
- The default parking lot has opening hours from 12:00a.m. to 11:59p.m. and rates of 20$ (large spot fees), 10$ (small spot fees), 100$ (large spot monthly flat fee), 50$ (small spot monthly flat fee).
- There are 5 default floors to the parking lot: first floor contains 20 large spots and 50 small spots, the other four floors (floors 2, 3, 4 and 5) have 100 small spots. Only floors 2 and 3 are reserved to monthly customers, the other floors can be used by guest customers.
- There are also three default services: Winter Tire Change, Car cleaning, and Oil Change.

All the mentioned default items can be updated (except for the email of the owner) and deleted (except for the parking lot and owner account).

## Our Team

| Team Member |	Programs | GitHub |
| --- | --- | --- |
| Evan Boyd | Software Engineering  | <a href="https://github.com/evanboyd31" target="_blank">evanboyd31</a> |
| Samer Abdulkarim | Computer Engineering | <a href="https://github.com/samerrak" target="_blank">samerrak</a> |
| Emilien Taisne	| Software Engineering  | <a href="https://github.com/Emilien-T" target="_blank">Emilien-T</a> |
| Walid Aissa	| Computer Engineering  | <a href="https://github.com/WalidAissa" target="_blank">WalidAissa</a> |
| Karim Al Sabbagh | Mechanical Engineering (minor in Software Engineering)  |  <a href="https://github.com/karimosabbagh" target="_blank">karimosabbagh</a>  |
| Karl Bridi	| Software Engineering | <a href="https://github.com/Kalamar136" target="_blank">Kalamar136</a>   |

## Overview Table

| Team Member |	Team Roles | Individual efforts - Sprint 1 (in hours) | Individual efforts - Sprint 2 (in hours) | Individual efforts - Sprint 3 (in hours) |
| --- | --- | --- | ---- | --- |
| Evan Boyd | Software Developer  |  20  | 60 | 20  |
| Samer Abdulkarim | Project Manager, Software Developer |  16 | 60 | 30  |
| Emilien Taisne	| Documentation Manager, Software Developer  | 16 | 60 | 17 |
| Walid Aissa	| Software Developer  | 16 | 44 | 17 |
| Karim Al Sabbagh | Testing Lead, Software Developer  | 16  | 44 | 30 |
| Karl Bridi	| Software Developer | 16 | 44 | 17 |

### Sprint 1 individual efforts: 

| Name | Contributions | Total Hours | 
| --- | --- | --- |
| Evan Boyd | Domain model design & documentation, requirements specification, FRS-8 use case diagram, persistence layer  | 20 |
| Samer Abdulkarim | Domain model design, requirements specification, FRS-3 use case diagram, repository class creation, reviewing pull requests  | 16 |
| Emilien Taisne | Domain model design & documentation, requirements specification, FRS-7 use case diagram, tests for the repository classes | 16 |
| Walid Aissa | Domain model design, requirements specification, FRS-10 use case diagram, repository classes creation |  16  |
| Karim Al Sabbagh | Domain model design, requirements specification, FRS-6 use case diagrame, tests for the repository classes  | 16  |
| Karl Bridi | Domain model design & documentation, requirements specifications, FRS-1 use case diagram | 16 |

### Sprint 2 individual efforts: 

| Name | Contributions | Total Hours | 
| --- | --- | --- |
| Evan Boyd | Floor and ServiceAppointment controller, DTO, and service classes. Unit and integration tests for Floor and ServiceAppointment classes. Domain model attribute edits and documentation. Help with unit tests for Service, GuestPass, and MonthlyPass, and integration tests for Service, GuestPass, and MonthlyPass. Documentation of unit tests and code reviews. | 60 |
| Samer Abdulkarim |MonthlyCustomer, Employee, Owner and Parking Lot controller, DTO, and service classes. Unit and integration tests for the MonthlyCustomer, Employee, Owner and Parking Lot classes.   | 60 |
| Emilien Taisne | Floor and ServiceAppointment controller, DTO, and service classes. Unit and integration tests for Floor and ServiceAppointment classes. Restful API documentation. | 60 |
| Walid Aissa | Service, Monthly Pass GuestPass controller, DTO, and service classes. Unit and integration tests for Service, Monthly Pass and GuestPass classes. |  44 |
| Karim Al Sabbagh | Service, Monthly Pass GuestPass controller, DTO, and service classes. Unit and integration tests for Service, Monthly Pass and GuestPass classes.  | 44 |
| Karl Bridi | MonthlyCustomer, Employee, Owner and Parking Lot controller, DTO, and service classes. Unit and integration tests for the MonthlyCustomer, Employee, Owner and Parking Lot classes. | 44 |

### Sprint 3 individual efforts: 

| Name | Contributions | Total Hours | 
| --- | --- | --- |
| Evan Boyd | Architecture Diagram. Owner pages. Backend fixes. In-class presentation | 24 |
| Samer Abdulkarim | All Monthly Customer Pages. Create home page for all users. Sign up. Log in page. Backend checks for duplicate emails. Log in fuctionalities. Bug fixes | 21 |
| Emilien Taisne | View monthly customers. Home page of owner. Major bug fixes with front end. Integration tests of log in controller. Documentation. In-class demo. | 21 |
| Walid Aissa | Guest Pass booking page. Management of Services for owner. In-class presentation. | 17  |
| Karim Al Sabbagh | Management of parking lot personnel. View passes. Internal booking of guest and monthly passes. Revamping of CSS styling. Monthly Pass/Guest Pass creation for customers.  | 25 |
| Karl Bridi | Architecture diagram, Key component description of the architecture diagram, parking lot frontend page, employee homepage | 20 |

 

 

 
