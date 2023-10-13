FROM openjdk:20-bullseye

RUN apt-get update -y && apt-get upgrade -y
RUN apt-get install -y curl maven

RUN curl -L -o /usr/local/bin/rcon-cli https://example.com/rcon-cli
RUN chmod +x /usr/local/bin/rcon-cli

WORKDIR /server

COPY server .


COPY ../../ /code/

CMD ["java", "-jar", "paper.jar"]