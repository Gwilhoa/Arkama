FROM openjdk:20-bullseye

RUN apt-get update -y && apt-get upgrade -y
RUN apt-get install -y curl maven

WORKDIR /server

COPY server .


COPY ../../ /code/

CMD ["java", "-jar", "paper.jar"]