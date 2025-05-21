FROM gradle:8.14.0-jdk21 AS builder
COPY --chown=gradle:gradle . /home/gradle/app
WORKDIR /home/gradle/app
EXPOSE 8080
CMD ["gradle", "bootRun", "--no-daemon"]