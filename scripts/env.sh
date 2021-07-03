#clean my local docker environment
docker stop $(docker ps -a -q)
docker rm $(docker ps -a -q)
docker rmi -f $(docker images -a -q)

cd ..

#create project image
mvn clean package

#start containers
docker run -d --network=spring-mysql-net -p 3306:3306 --name localmysql -e MYSQL_ROOT_PASSWORD=rootpass -e MYSQL_DATABASE=todos -e MYSQL_USER=cosmin -e MYSQL_PASSWORD=pass mysql
docker run -d -p 5000:5000 --network=spring-mysql-net -e RDS_HOSTNAME=localmysql cosdin/raiffeisen-account-service:0.0.1-SNAPSHOT