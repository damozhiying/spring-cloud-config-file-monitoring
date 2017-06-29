#FROM openjdk:jre
#FROM gliderlabs/alpine:3.4
FROM debian:latest
MAINTAINER Jacek Kowalski <Jacek@jacekk.info>

ENV GITBLIT_VERSION 1.8.0

RUN apt-get update \
	&& apt-get dist-upgrade -y \
	&& apt-get install -y git-core sudo wget \
	&& apt-get clean

# Install Gitblit

WORKDIR /opt
RUN wget -O /tmp/gitblit.tar.gz http://dl.bintray.com/gitblit/releases/gitblit-${GITBLIT_VERSION}.tar.gz \
	&& tar xzf /tmp/gitblit.tar.gz \
	&& rm -f /tmp/gitblit.tar.gz \
	&& ln -s gitblit-${GITBLIT_VERSION} gitblit \
	&& mv gitblit/data gitblit-data-initial \
	&& mkdir gitblit-data
RUN groupadd -r -g 500 gitblit \
	&& useradd -r -d /opt/gitblit-data -u 500 -g 500 gitblit \
	&& chown -Rf gitblit:gitblit /opt/gitblit-*

# Adjust the default Gitblit settings to bind to 8080, 8443, 9418, 29418, and allow RPC administration.

RUN echo "server.httpPort=8090" >> gitblit-data-initial/gitblit.properties \
	&& echo "server.httpsPort=8453" >> gitblit-data-initial/gitblit.properties \
	&& echo "web.enableRpcManagement=true" >> gitblit-data-initial/gitblit.properties \
	&& echo "web.enableRpcAdministration=true" >> gitblit-data-initial/gitblit.properties

# Setup the Docker container environment and run Gitblit

VOLUME /opt/gitblit-data
EXPOSE 8090 8453 9418 29418

ADD ./spring-cloud-config.git /opt/gitblit-data/git/spring-cloud-config.git
ADD ./notify-commit.groovy /opt/gitblit-data/groovy/notify-commit.groovy

WORKDIR /opt/gitblit
COPY run.sh /run.sh
CMD /run.sh