### Identity service
This microservice is responsible for:
###
Onboarding users
Roles and permissions
Authentication
Tech stack
Build tool: maven >= 3.9.5
Java: 21
Framework: Spring boot 3.2.x
DBMS: MySQL
Prerequisites
Java SDK 21
A MySQL server
### Start application
mvn spring-boot:run

### Build application
mvn clean package

### Docker guideline

### Build docker image
docker build -t <account>/identity-service:0.9.0 .

### Push docker image to Docker Hub
docker image push <account>/identity-service:0.9.0

### Create network:
docker network create devteria-network

### Run your application in devteria-network
docker run -d identity-service:0.0.1
docker run --name identity-service --network devteria-network -p 8080:8080 identity-service:0.0.1