# Stage 1: Build with JDK 25
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
ENV MAVEN_VERSION=3.9.9
RUN apt-get update && apt-get install -y curl \
    && curl -fsSL https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz | tar -xz -C /opt \
    && ln -s /opt/apache-maven-${MAVEN_VERSION}/bin/mvn /usr/bin/mvn
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Run on WildFly with JDK 25
FROM eclipse-temurin:25-jdk
ENV WILDFLY_VERSION=35.0.1.Final
ENV POSTGRESQL_DRIVER_VERSION=42.7.5
RUN apt-get update && apt-get install -y --no-install-recommends curl && rm -rf /var/lib/apt/lists/* \
    && groupadd -r jboss && useradd -r -g jboss -m jboss \
    && curl -fsSL https://github.com/wildfly/wildfly/releases/download/${WILDFLY_VERSION}/wildfly-${WILDFLY_VERSION}.tar.gz | tar -xz -C /opt \
    && mv /opt/wildfly-${WILDFLY_VERSION} /opt/wildfly \
    && chown -R jboss:jboss /opt/wildfly

# Download PostgreSQL JDBC driver
RUN curl -fsSL -o /tmp/postgresql.jar \
    https://jdbc.postgresql.org/download/postgresql-${POSTGRESQL_DRIVER_VERSION}.jar

# Install PostgreSQL module and datasource via WildFly CLI
RUN mkdir -p /opt/wildfly/modules/org/postgresql/main && \
    cp /tmp/postgresql.jar /opt/wildfly/modules/org/postgresql/main/ && \
    echo '<?xml version="1.0" ?>\n\
<module xmlns="urn:jboss:module:1.1" name="org.postgresql">\n\
    <resources>\n\
        <resource-root path="postgresql.jar"/>\n\
    </resources>\n\
    <dependencies>\n\
        <module name="javax.api"/>\n\
        <module name="javax.transaction.api"/>\n\
    </dependencies>\n\
</module>' > /opt/wildfly/modules/org/postgresql/main/module.xml && \
    rm /tmp/postgresql.jar

# Configure datasource using CLI script
RUN echo 'embed-server --server-config=standalone-full.xml --std-out=echo' > /tmp/cli.txt && \
    echo '/subsystem=datasources/jdbc-driver=postgresql:add(driver-name=postgresql,driver-module-name=org.postgresql,driver-xa-datasource-class-name=org.postgresql.xa.PGXADataSource)' >> /tmp/cli.txt && \
    echo 'data-source add --name=MoviesDS --jndi-name=java:jboss/datasources/MoviesDS --driver-name=postgresql --connection-url=jdbc:postgresql://postgres:5432/moviesdb --user-name=movies --password=movies --use-java-context=true --enabled=true' >> /tmp/cli.txt && \
    echo 'jms-queue add --queue-address=ReviewQueue --entries=java:/jms/queue/ReviewQueue java:jboss/exported/jms/queue/ReviewQueue' >> /tmp/cli.txt && \
    echo 'stop-embedded-server' >> /tmp/cli.txt && \
    /opt/wildfly/bin/jboss-cli.sh --file=/tmp/cli.txt && \
    rm /tmp/cli.txt

RUN chown -R jboss:jboss /opt/wildfly
USER jboss
# Create an application user for remote EJB access
RUN /opt/wildfly/bin/add-user.sh -a -u ejbuser -p 'ejbpass1!' -g guest --silent
COPY --from=build /app/target/movies.war /opt/wildfly/standalone/deployments/
EXPOSE 8080
CMD ["/opt/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-c", "standalone-full.xml"]
