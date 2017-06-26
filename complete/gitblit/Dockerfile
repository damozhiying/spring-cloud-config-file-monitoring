FROM jmoger/gitblit


#RUN apk update && apk upgrade && \
#	apk add --no-cache bash git openssh
# Clone the conf files into the docker container
#RUN cd /opt/gitblit-data
VOLUME /tmp
WORKDIR /tmp
RUN mkdir -p spring-cloud-config-poc-repo
ADD ./spring-cloud-config-poc-repo-docker /tmp/spring-cloud-config-poc-repo
#WORKDIR /tmp/spring-cloud-config-poc-repo-docker
#RUN cd /tmp/spring-cloud-config-poc-repo
#RUN git init --bare
#RUN cd /tmp/spring-cloud-config-poc-repo
#RUN git remote add origin http://localhost/springcloudpoc.git
VOLUME /opt/gitblit-data
ADD start.sh /tmp/start.sh
#git clone http://gitblit:8080/springcloudpoc.git
#RUN cp /tmp/backup/spring-cloud-config-poc-repo /tmp/spring-cloud-config-poc-repo
#RUN cd /tmp/spring-cloud-config-poc-repo
#RUN git push origin master
ENTRYPOINT ["sh", "/tmp/start.sh"] 
