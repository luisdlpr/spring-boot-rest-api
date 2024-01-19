# Students API
A quick demo REST API interacting with a students database with CRUD operations. Developed in Spring Boot to learn the framework.
## Endpoints
### GET api/v1/student
Returns a list of all students in the database following the output format shown below. 
```
[
  {
    "id": Long,
    "name": String,
    "email": String,
    "age": Integer,
    "dob": LocalDate,
  },
  ...
]
```
Example usage: `curl -XGET 'localhost:8080/api/v1/student'` 

### POST api/v1/student
Registers a new student in the database given the following request body:
```
content-type: application/json
{
  "name": String,
  "email": String,
  "dob": LocalDate,
}
```
Exceptions:
- **500**: "Invalid email given", "Invalid name given", "Email taken"

Example usage: `curl -XPOST -H "Content-type: application/json" -d '{"name": "Iona", "email": "ionaq99@gmail.com", "dob": "1999-06-15"}' 'localhost:8080/api/v1/student'`

### PUT api/v1/student/{student_id}
Updates a student matching the given `student_id`. Expects query parameters of `name` and/or `email` with the value of the desired change. 

Exceptions:
- **500**: "student with id ? does not exist",

Example usage: `curl -XPUT 'localhost:8080/api/v1/student/1?name=Mary'` 

### DELETE api/v1/student/{student_id}
Deletes a student matching the given `student_id` permanently. 

Exceptions:
- **500**: "student with id ? does not exist",

Example usage: `curl -XDELETE 'localhost:8080/api/v1/student/1'` 

# Dependencies
- OpenJDK 21
- Maven
- PSQL

# Run instructions
## Docker
1. `docker compose up` will set up PSQL Database and App instance exposed on port 8080.

## Manual
1. Configure `src/main/resources/application.properties` to match your desired datasource.
2. Run `mvn install` from the root directory to build the application. Take note of the .jar file created.
3. Run `java -jar /path/to/jar/from/build` to run the server

# Testing 
`mvn test` runs tests for the Business and Data Layers of the application.
These tests are also run automatically through GitHub Actions on merge request and commit to the main branch.
