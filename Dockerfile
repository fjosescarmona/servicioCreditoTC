FROM openjdk:8
VOLUME /tmp
ADD ./target/servicioCreditoTC-0.0.1-SNAPSHOT.jar servicioCreditoTC.jar
ENTRYPOINT ["java","-jar","/servicioCreditoTC.jar"]