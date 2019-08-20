FROM openjdk:8u171-jdk-stretch as builder
WORKDIR /opt
ENV kotlin_native_ver "1.3.41"
ENV kotlin_native_home "/opt/kotlin-native-linux-$kotlin_native_ver"
ENV cjson_ver "1.7.12"
RUN wget "https://github.com/JetBrains/kotlin/releases/download/v$kotlin_native_ver/kotlin-native-linux-$kotlin_native_ver.tar.gz" \
	&& tar -xzf kotlin-native-linux-$kotlin_native_ver.tar.gz && rm kotlin-native-linux-$kotlin_native_ver.tar.gz \
	&& apt-get update && apt-get install -qy libcurl4-openssl-dev \
	&& mkdir -p /app/src/main/kotlin/org/example/weather_func \
	&& mkdir -p /app/lib/cJSON-$cjson_ver/include/cjson && mkdir -p /app/lib/cJSON-$cjson_ver/lib/x86_64-linux-gnu \
	&& mkdir -p /app/gradle/wrapper
WORKDIR /app

COPY gradle/wrapper/gradle-wrapper.properties gradle/wrapper/gradle-wrapper.jar /app/gradle/wrapper/
COPY gradlew openweathermap_key.txt /app/
# TODO: Figure out how to generate the Konan cache and copy it to the Docker image.
#COPY .konan/ /root/.konan
COPY *.def *.kts /app/
COPY src/ /app/src
COPY lib/cJSON-$cjson_ver/include/cjson/cJSON.h /app/lib/cJSON-$cjson_ver/include/cjson/cJSON.h
COPY lib/cJSON-$cjson_ver/lib/libcjson.so /usr/lib/libcjson.so
COPY lib/cJSON-$cjson_ver/lib/libcjson.so.1 /usr/lib/libcjson.so.1
COPY lib/cJSON-$cjson_ver/lib/libcjson.so.$cjson_ver /usr/lib/libcjson.so.$cjson_ver
RUN ./gradlew build && cp build/bin/linux/weatherReleaseExecutable/weather.kexe weather

FROM debian:stretch
ADD https://github.com/openfaas/faas/releases/download/0.16.0/fwatchdog /usr/bin
RUN apt-get update && apt-get -qy install libcurl4-openssl-dev \
	&& chmod +x /usr/bin/fwatchdog && mkdir -p /app
WORKDIR /app
COPY --from=builder /app/openweathermap_key.txt .
COPY --from=builder /app/weather .
RUN chmod +x weather
ENV fprocess "./weather"
HEALTHCHECK --interval=2s CMD [ -e /tmp/.lock ] || exit 1
CMD ["fwatchdog"]
