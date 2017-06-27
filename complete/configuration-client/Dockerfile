FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
#RUN echo nameserver 8.8.8.8 > /etc/resolv.conf
#COPY resolv.conf /etc/resolv.conf
ADD ./target/configuration-client-0.0.1-SNAPSHOT.jar configuration-client-0.0.1-SNAPSHOT.jar
RUN sh -c 'touch /configuration-client-0.0.1-SNAPSHOT.jar'
#ARG client_active_profiles
#ENV SPRING_PROFILES_ACTIVE $client_active_profiles
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /configuration-client-0.0.1-SNAPSHOT.jar" ]
