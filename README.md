# GyanGroveAssgn

Tech stack:
Frontend - Swagger ui
Backend - Java Spring boot
Database - MySQL

To run this project you need to have MySQL workbench installed

MySQL username = root
MySQL password = root
MySQL port = 3306
MySQL database = gyangrove

Run the spring application on port "8081"
The default port is "8080" you can change it by updating in application.properties
sever.port = 8081

The current configuration for the Hibernate DDL auto is set to "create-drop", which means the database schema is recreated every time the application starts and dropped when it shuts down. If you prefer to persist the data between application restarts, you may consider changing the spring.jpa.hibernate.ddl-auto property from "create-drop" to "update" in application.properties.


The application will be accessible at - http://localhost:8081/swagger-ui/index.html

Endpoints : 

1. To upload the csv file - POST /uploadCSV

2. To get the events occuring in the next 14 days - GET /api/events/find

You can see these endpoints in swagger
