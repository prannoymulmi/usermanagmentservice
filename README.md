# user-data-service
This is a simple CRUD application written in Spring boot framework. This Application allows one to save a 
users data and query them using their.

## Tests
This project compromises of unit tests and integration tests. They are separated in different modules test and
integration test. The modules are separated in such a manner so that the stages can be separately run in a 
CI/CD Pipeline.

## Swagger
The Server is running in the default port 8080
When the application is started the post request can be sent using the swagger ui present in the application itself.
[Swagger UI local link](http://localhost:8080/swagger-ui.html). It is important that the application to be started before to use
swagger. A Screenshot of the Swagger UI can be seen below.

![Swagger UI for the Rest API](./swagger.PNG)also possible to list the users and search for user names.

## CI/CD Design
A CI/CD is also very crucial toward the development and release of the software. The CI/CD should allow to test and deploy
the application in the shortest amount of time. Below in a design of how a CI/CI pipeline could be realized.


![CI/CD concept](./CI_CD.PNG)

The design principle is very simple and straight forward. The pipeline will be highly automated as possible,
what this means is the pipeline will checkout, build test and deploy the application in the same pipeline as
seen in the picture. 



## Cloud Architecture 
For the simple concept I will use AWS cloud and its services required to make the app running in the cloud
* AWS Fargate to deploy the service in a scalable manner without much operational overhead.
* AWS Application Loadbalancer to distribute the traffic to all the healthy task, so the load wont be unequal.
* AWS ECR to store our built images which will be deployed in fargate.
* Use AWS Aurora Postgres or Mysql database for the app
* Route53 to manage a proper domain to use the service instead of its default domain.
* Cloudwatch or datadog to store and monitor the application metrics
* SNS to send alarms or message in case of any unwanted events occurring

All of the infrastructure will be rolled out using an IaC(i.e. terraform, cdk etc). In this way the service and
all its dependent services can be deployed using a CI/CD pipeline. 

The same can also be realized using AWS Lambdas and could also be advantageous as we can save on running costs
as Lambda does not have to run continuously like fargate servers but in the downside running Java (JVM) in the
lightweight lambda seems to be the bottleneck where I think it is better to use a Fargate service.


##Architecture Decision Records


Some decisions that I took while implemented this service are documented using ADR. [Link to ADR](./doc/adr)

-------------------------
# cc-user-data-service - A simple User Data Service deployed to the cloud
A coding/design challenge to provide a User Data Service to other teams
in the company. The service should offer the common CRUD operations. It should be
also possible to list the users and search for user names.
Your task is to build a small user management service which stores and retrieves
users' data.

The users' data consist of the following pieces:

* user ID, provided as an UUID;
* user name;
* first name;
* last name;
* address (street, city);
* postal code;
* country as a ISO 3166-1 alpha-2 country code (e.g. 'de' or 'gb').

The service should offer the basic CRUD operations for users. The primary key is
the user's ID.

The service should be fully functional. As a database you may use any storage
you like, for example:
* embedded database (e.g. H2);
* in-memory map;
* simple CSV file.

The storage implementation should be very simple. But keep in mind that the
storage will be migrated to some bigger solution like an elastic search or a
relational database, so design the code accordingly.

Authentication and authorization for the service can be ignored.

The expected time frame to use for this challenge is between 4 to 6 hours.

Do not hesitate to contact us if you have any questions.

## Your Tasks

* Build the REST Service as specified. You can use any language or framework you
  prefer. Please describe briefly your choice to help us to understand your solution.
* Do not forget tests!
* Provide a concept on how you would setup CI/CD.
  It should be possible to update the service several times a day.
* Provide a simple concept how you would run and deploy the service in a cloud
  of your choice (AWS, GCP, Azure, etc.)

## The Results

You should submit a working sample of the described service with an exposed API.
Please document everything in a README file. The code has to be working, in
order to run the tests, start it and insert and query some user data.
