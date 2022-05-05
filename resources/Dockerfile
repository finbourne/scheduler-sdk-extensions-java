FROM maven:3.8.5-jdk-11 as maven

RUN mkdir -p /usr/src
WORKDIR /usr/src

COPY pom.xml /usr/src/
RUN mvn install

ENV FBN_CHANGEME-APPLICATION-UPPER_API_URL ${FBN_CHANGEME-APPLICATION-UPPER_API_URL}

ENTRYPOINT mvn -e -fae test