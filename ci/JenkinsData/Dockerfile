FROM debian:jessie

RUN useradd -d "/var/jenkins_home" -u 1000 -m -s /bin/bash jenkins
RUN mkdir -p /var/log/jenkins
RUN mkdir -p /usr/share/jenkins/ref/plugins
RUN chown -R jenkins:jenkins /var/log/jenkins

# Copy Jenkins Jobs
COPY ./ci-build/config.xml /var/jenkins_home/jobs/ci-build/

RUN chown -R jenkins:jenkins /var/jenkins_home/

VOLUME ["/var/log/jenkins", "/var/jenkins_home"]

USER jenkins

CMD ["echo", "Data container for Jenkins"]
