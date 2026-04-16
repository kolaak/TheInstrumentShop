FROM eclipse-temurin:11-jre

ADD target/javashop.tester-latest.jar ./app.jar

EXPOSE 8010

#ADD https://github.com/signalfx/splunk-otel-java/releases/latest/download/splunk-otel-javaagent-all.jar .
#ENTRYPOINT java -javaagent:splunk-otel-javaagent-all.jar   -Dotel.resource.attributes=service.name=shoptester,deployment.environment=${USERNAME}22_Apm_Instrumentation_Shop -cp  ./app.jar GenerateTraffic

ENTRYPOINT java -cp  ./app.jar GenerateTraffic

