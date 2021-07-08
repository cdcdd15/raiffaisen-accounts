## raiffaisen-accounts (Spring boot project to manage accounts and use exchange rates from external online service)


### Build project and start containers

Package locally with mvn clean package does the following
- all unit tests are run
- the Docker image is created locally

Start Docker containers locally
- the docker-compose.yml located in project root starts 2 containers: MySQL (from official image) and the Spring Boot project container (from the image created with the mvn clean package on the current Spring Boot project)
- containers start inside --network=spring-mysql-net so they need this Docker network to be created locally
- the env.sh script (located in scripts folder) does the following: stops deletes all docker containers locally (if the third line is uncommneted it will delete all your images in the local Docker too), creates the project image by running mvn clean package on the current Spring Boot project, creates the Docker network locally, starts the 2 containers mentioned previously (DB and app)

### Production flow

Production flow (after containers are started and app is running), where [r1] ... [r6] are detailed in the next paragraph
- get one record by iban [r1]: 404
- insert one record [r2]: 201
- insert one record [r2]: 400
- insert one record with iban changed [r3]: 201
- get one record by iban [r1](* DB): 200
- get one record by iban [r1](* cache): 200
- evict cache [r4]: 200
- get one record by iban [r1](* DB): 200
- get all records [r5]: 200
- delete all records [r6]: 204
- get one record by iban [r1]: 404
- health actuator endpoint (check circuit breaker status) [r7]: 200
*exchange rate map fetched from DB or cache, when the rates are fetched from DB the @Cacheable method (exchageRates() from AccountService.java) is called, when the rates are fetched from cache the @Cacheable method is NOT called, this can be seen in the spring boot (container logs); also the call fetchig the record from DB takes a longer time

Rest calls (I used Postman)(mentioned in the previous paragraph "Production flow")
- [r1] GET http://localhost:5000/accounts/62knykn46
- [r2] POST http://localhost:5000/accounts
{
  "iban": "62knykn46",
  "balance": 20,
  "targetDate": "2030-11-09T10:49:23.566+0000",
  "currency": "RON"
}
- [r3] POST http://localhost:5000/accounts
{
  "iban": "n6g2igui",
  "balance": 20,
  "targetDate": "2030-11-09T10:49:23.566+0000",
  "currency": "RON"
}
- [r4] GET http://localhost:5000/accounts/evict
- [r5] GET http://localhost:5000/accounts
- [r6] DELETE http://localhost:5000/accounts
- [r7] GET http://localhost:5000/actuator/health

### Others

Unit testing
- tests: mock, H2 integration, application (context) loads
- the most important one is Service layer unit (integration) test (called ServiceTest.java) with a mocked exchange rates bean and an @Autowired repository bean (working with H2 database). This test follows the main flow: inserts one account from the service layer and gets the account (from H2) by iban field with the currency converted to EUR.

External online REST service used
- service for exchange rates
https://manage.exchangeratesapi.io/signup/free
dinu.cosmin@yahoo.com
free-plan-raiff
- get the API key of service for exchange rates
https://manage.exchangeratesapi.io/quickstart
- check api usage of free plan of service for exchange rates
https://manage.exchangeratesapi.io/usage

Other comments
- The commit messages in the history of the Git repo explain what I did step by step
- Applied circuit breaker library: @CircuitBreaker present in class RatesService.java, I tested it with a call (to the online exchange service) after I stopped the internet connection and the callback method is called (so it works). See health actuator endpoint GET http://localhost:5000/actuator/health
- Applied caching: @Cacheable present on method exchageRates() from AccountService.java
- the search is done by iban column, the find by iban endpoint returns 404 if the record with the corresponding iban is not found
- iban column is made unique in Account JPA entiy, so from the POST endpoint if you (as a REST client) try to insert accounts with the same iban it will return 400 with the corresponding message

### TODO (possible improvements or implementations)
- DTO mapping (testing)
- business (service) layer unit testing
- full code coverage with jacoco plugin
- clean code with Maven Plugins: checkstyle, pmd, findbugs