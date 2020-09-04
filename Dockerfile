FROM navikt/java:12
COPY target/pam-ad-*.jar /app/app.jar
EXPOSE 8080
COPY scripts/init-env.sh /init-scripts/init-env.sh
ENV JAVA_OPTS="-Xms768m -Xmx1024m"
