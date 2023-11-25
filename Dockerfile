FROM openjdk:17-jdk-alpine

# Create a non-root user
RUN addgroup -S task-api-group && adduser -S task-api-user -G task-api-group

# Create a directory for configuration
RUN mkdir /configs

# Copy the keystore file into the /configs directory
COPY ./keystore.jks /configs/keystore.jks

# Adjust the ownership and permissions of the /configs directory and keystore file
RUN chown -R task-api-user:task-api-group /configs
RUN chmod 750 /configs
RUN chmod 400 /configs/keystore.jks

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Change the ownership of the application directory to the non-root user
RUN chown task-api-user:task-api-group /app.jar


ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Change the ownership of the application directory to the non-root user
RUN chown task-api-user:task-api-group /app.jar

USER task-api-user

ENTRYPOINT ["java","-jar","/app.jar"]
