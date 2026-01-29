# Expenditure Tracker – Java Web Application

A web-based expenditure tracking application built using Java technologies.  
It allows users to manage expenses, generate PDF reports, and securely authenticate.

## Features
- User registration and login
- Add, edit, and delete expenses
- Generate PDF reports
- Session-based authentication
- Responsive UI

## Tech Stack
- Java (Servlets & JSP)
- JDBC
- HTML, CSS
- Apache Tomcat
- MySQL
- iText PDF

## Project Structure

ExpenditureTracker/
├── src/main/java/com/
│   ├── dao/                 # Database access classes
│   └── expendituretracker/  # Servlets
├── webapp/STATIC/            # CSS files
├── webapp/TEMPLATES/         # HTML templates
└── webapp/WEB-INF/           # JSP pages, libraries, and web.xml


##How to Run

1. Clone the repository
	
	```bash
	git clone https://github.com/gowthamraj28/Expenditure_tracker.git

2. Open Eclipse IDE

3. Import the project

	Go to File → Import → Existing Projects into Workspace

	Select the cloned folder

4. Configure Apache Tomcat server in Eclipse

5. Set up the database

	Create a MySQL database

	Update database credentials in the DAO classes (DB connection settings)

6. Run the project on Tomcat server

7. Open in browser

	http://localhost:8080/Expenditure_tracker/