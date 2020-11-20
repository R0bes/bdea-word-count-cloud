
## status quo
* für jedes Textdokument das hochgeladen wird, wied eine wordcloud erzeugt
* für jedes Build wird ein neuer map-reduce job ausgelöst über den gesammten korpus

## build
mvn clean package

## exec
java -jar target/*.jar

## api

##### upload image 
curl -X POST localhost:8080/api/text/upload -F "documents=@example_data/elefant.txt"

##### download wordcloud
curl -X GET localhost:8080/api/image/elefant >> elefant.png

### im browser: 
http://localhost:8080/home