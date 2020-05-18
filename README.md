# User Messaging

User messaging app has two modules frontend module in angular 8 and backend module in spring boot.
Backend module has all the static resources of frontend

## Set up

### Requirements

Backend application\
Java 8+\
Maven

Frondend application\
Node js\
npm\

### Step to make application run
Backend\
Navigate to user-message-api and run below command

```bash
mvn clean install
```
Navigate to target folder
```bash
cd target
java -jar user-messaging-api-0.0.1-SNAPSHOT.jar
```
Application will start on port 8081

Use http://localhost:8081/ to access the application.

Default admin credential is `admin` and password is `administrator`\
Default user credential is `user1` and password is `testuser`

To restart the application a property needs to changed to avoid spring jdbc loading users data again.\
Location: application.properties under src/main/resources
```java
#spring.datasource.initialization-mode=never
```
The above property needs to be uncommented

Frontend
After successful angular/cli installation (https://cli.angular.io/) navigate to user-messaging-ui\
Run the below command to run the angular app in node
```bash
ng serve
```

To build the project run the below command
```bash
ng build --prod
```
