FROM openjdk:24
MAINTAINER dbocini
COPY target/*.jar florenceConsultingTest-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/florenceConsultingTest-1.0-SNAPSHOT.jar"]