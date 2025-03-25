FROM bellsoft/liberica-openjre-debian:23.0.2 AS layers
WORKDIR /application
COPY target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM bellsoft/liberica-openjre-debian:23.0.2
VOLUME /tmp
RUN useradd -ms /bin/bash spring-user
USER spring-user
COPY --from=layers /application/dependencies/ ./
COPY --from=layers /application/spring-boot-loader/ ./
COPY --from=layers /application/snapshot-dependencies/ ./
COPY --from=layers /application/application/ ./
COPY src/main/resources/application.yml /application/config/application.yml

ENTRYPOINT ["java", "-Dspring.config.location=/application/config/application.yml", "org.springframework.boot.loader.launch.JarLauncher"]
