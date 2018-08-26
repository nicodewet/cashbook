FROM openjdk:8-jdk
RUN apt-get update && apt-get -y install apt-transport-https dirmngr
RUN echo 'deb https://apt.dockerproject.org/repo debian-stretch main' >> /etc/apt/sources.list
RUN apt-key adv --keyserver hkp://p80.pool.sks-keyservers.net:80 --recv-keys F76221572C52609D
RUN apt-get update
RUN apt-get -y install docker-engine