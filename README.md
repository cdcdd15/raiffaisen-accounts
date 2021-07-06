# raiffaisen-accounts (Spring boot project to manage accounts)

Package locally with mvn clean package does the following
- all unit tests are run
- the Docker image is created locally

Start Docker containers locally
- the docker-compose.yml located in project root starts 2 containers: MySQL (from official image) and the Spring Boot project container (from the image created with the mvn clean package on the current Spring Boot project)
- containers start inside --network=spring-mysql-net so they need this Docker network to be created locally
- the env.sh script (located in scripts folder) does the following: stops deletes all docker containers locally (if the third line is uncommneted it will delete all your images in the local Docker too), creates the project image by running mvn clean package on the current Spring Boot project, creates the Docker network locally, starts the 2 containers mentioned previously (DB and app)

Other comments
- The commit messages in the history of the Git repo explain what I did step by step
- Applied circuit breaker library: @CircuitBreaker present in class RatesService.java, I tested it with a call (to the online exchange service) after I stopped the internet connection and the callback method is called (so it works)
- Applied caching: @Cacheable present on method exchageRates() from AccountService.java
- the search is done by iban column, the find by iban endpoint returns 404 if the record with the corresponding iban is not found
- iban column is made unique in Account JPA entiy, so from the POST endpoint if you (as a REST client) try to insert accounts with the same iban it will return 400 with the corresponding message

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

External online REST service used
- service for exchange rates
https://manage.exchangeratesapi.io/signup/free
dinu.cosmin@yahoo.com
free-plan-raiff
- get the API key of service for exchange rates
https://manage.exchangeratesapi.io/quickstart
- check api usage of free plan of service for exchange rates
https://manage.exchangeratesapi.io/usage

TODO
- DTO mapping (testing)
- flow integration testing with H2
- business (service) layer unit testing