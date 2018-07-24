# Jenkins Build Env :: Gatling
#
# Example:
#   $ docker run -it onsdigital/jenkins-slave-gatling:2.3.0 ????
#
FROM onsdigital/jenkins-slave-sbt:1.1.6
LABEL maintainer="sion.williams@ext.ons.gov.uk" \
      build-tool="gatling"

RUN mkdir -p /usr/perftest-workdir \
    && cd /usr/perftest-workdir \
    && sbt sbtVersion

WORKDIR /usr/perftest-workdir

ADD . /usr/perftest-workdir

ENTRYPOINT ["sbt"]
CMD ["gatling:test"]
