FROM eclipse-temurin:17-jre
ADD target/javashop.instruments-latest.jar app.jar
EXPOSE 8040

# ADD 'https://dtdg.co/latest-java-tracer' dd-java-agent.jar

# ENV DD_SERVICE instruments
# ENV DD_VERSION 1.4

# LABEL com.datadoghq.tags.env=${USERNAME}_xApm_Instrumentation_Shop
# LABEL com.datadoghq.tags.service="instruments"
# LABEL com.datadoghq.tags.version="1.4"

# ARG DD_GIT_REPOSITORY_URL
# ARG DD_GIT_COMMIT_SHA
# ENV DD_GIT_REPOSITORY_URL=${DD_GIT_REPOSITORY_URL} 
# ENV DD_GIT_COMMIT_SHA=${DD_GIT_COMMIT_SHA}


ENV JAVA_OPTS="-Xms2048M -Xmx2048M"

# ENTRYPOINT java -javaagent:dd-java-agent.jar \
#   -Ddd.profiling.enabled=true \
#   -XX:FlightRecorderOptions=stackdepth=256 \
#   -Ddd.agent.host=datadog-agent \
#   -Ddd.data.streams.enabled=true \
#  # -Ddd.logs.injection=true \
#   -Ddd.trace.sample.rate=1 \
# # -Ddd.appsec.enabled=true \
#  # -Ddd.iast.enabled=true  \
#   -Ddd.service=instruments \
#   -Ddd.env=${USERNAME}_xApm_Instrumentation_Shop -jar app.jar

ENTRYPOINT java -jar app.jar