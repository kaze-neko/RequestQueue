docker compose stop
container_id=$(docker ps -aqf "ancestor=requestqueue-application")
docker cp $container_id:/saved.json /home/kazeneko/SSTU/6_OCC/RequestQueue/saved.json
docker cp $container_id:/logFile.txt /home/kazeneko/SSTU/6_OCC/RequestQueue/logFile.txt
cat logFile.txt
cat saved.json