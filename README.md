# raiffaisen-accounts (Spring boot project to manage accounts)

REST endpoints
- local urls for endpoints: GET, POST, DELETE
- JSON for POST endpoint
{
  "iban": "cs2g3rgt8rs",
  "balance": 123,
  "targetDate": "2030-11-09T10:49:23.566+0000",
  "currency": "RON"
}

External online REST service used
- service for exchange rates
https://manage.exchangeratesapi.io/signup/free
dinu.cosmin@yahoo.com
free-plan-raiff
- get the API key of service for exchange rates
https://manage.exchangeratesapi.io/quickstart
- check api usage of free plan of service for exchange rates
https://manage.exchangeratesapi.io/usage

Package locally with mvn clean package does the following
- all unit and integration tests are run
- the Docker image is created locally

Start Docker containers locally
- the docker-compose.yml located in project root starts 2 containers: MySQL (from official image) and the Spring Boot project container (from the image created with the mvn clean package on the current Spring Boot project)
- containers start inside --network=spring-mysql-net so they need this Docker network to be created locally
docker network create spring-mysql-net
- the env.sh script located in scripts folder does the following: deletes all docker containers locally, creates the project image by running mvn clean package on the current Spring Boot project, created the Docker network to be created locally, starts the 2 containers mentioned previously

Applied circuit breaker library
- @CircuitBreaker present in class AccountService.java