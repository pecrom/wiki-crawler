# Wiki Crawler

## Description

This program is used to download name and logo from the wiki page.

## How to run
For testing purposes I have prepared docker compose with postgres database. This compose has preset user credentials, so that 
it is not necessary to set anything.

First it is necessary to run the docker compose:
```
cd docker
docker-compose up 
```

So far we have successfully started docker service with postgres database. The database is listening on port 5434, 
username is **admin** and the password is **admin** as well. Next let's create necessary database tables.

```
go to the location where the pom.xml is
mvn compile
mvn flyway:migrate
``` 

Now we have prepared everything needed by our appilcation, so it's time to create **jar** and test it.

```
mvn package
java -jar target/wiki-crawler-1.0-SNAPSHOT.jar https://en.wikipedia.org/wiki/Google
```

The images are then saved to **images** folder. Name and path to the image is save in database **wiki** in to table named
**article_image**.