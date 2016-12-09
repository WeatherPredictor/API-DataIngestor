echo 'Installing the Data Ingestor API...'
cd '/home/ec2-user/ingestor-microservice/api-ingestor'

mvn clean install >> /var/log/tomcat.log
mvn compile war:war

cd '/home/ec2-user/docker'
sudo docker login -e="sneha.tilak26@gmail.com" -u="tilaks" -p="teamAviato"
sudo docker pull tilaks/dataingestor
sudo docker images | grep '<none>' | awk '{print $3}' | xargs --no-run-if-empty docker rmi -f
sudo docker run -d -p 9000:8080 --name api-di $(docker images | grep -w "tilaks/dataingestor" | awk '{print $3}') >> /var/log/dataingestor.log 2>&1 &
