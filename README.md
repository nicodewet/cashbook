# cashbook

```shell
curl -d '{"type":"INVOICE_PAYMENT","parentTransactionUUID":null,"scheduledDate":"2018-08-20","completedDate":null,"amountInCents":23000,"gstInCents":0,"evidenceLink":null}' -H "Content-Type: application/json" -X POST http://localhost:8080/api/business/transaction
```

## build

Ultimately the sure-fire build process is dictated by our *build server* build steps which you can reliably run
on your local machine. 

If your only interest is in a reliable build and shipping process then skip ahead.

On your local machine, you would generally do the following from the command line or from your IDE:

```shell
$ ./gradlew build
```

## shipping

### localhost

Shipping means wrapping our spring boot application with docker engine imaging and using the gradle parts of
the [Spring Boot with Docker guide](https://spring.io/guides/gs/spring-boot-docker/) (the overarching philosophy 
being to always follow the spring guides as a starting point).

Notice that the [palantir gradle-docker plugin](https://github.com/palantir/gradle-docker) is used and that to build 
the *latest* image on your local you would execute the *docker* task from the command line or from your IDE:

```shell
$ ./gradlew docker
```

If you had pruned all your local images ($ docker system prune -a) prior to executing the docker build task your local 
images should look something like what is shown below. 

```shell
$ docker images
REPOSITORY             TAG                 IMAGE ID            CREATED             SIZE
com.thorgil/cashbook   latest              35b4a1d29319        47 seconds ago      123MB
openjdk                8-jdk-alpine        5801f7d008e5        6 weeks ago         103MB

```

### CI server

#### Two step build

We have a two step process and have deliberately chosen not to use a multistage build process. We are also accepting 
that the first version of the process will not be perfect (*we'll get there in time*). 

The first step is to build the spring boot jar using an [official openjdk build image](https://hub.docker.com/_/openjdk/).
This will be significantly slower than your local build process because the build dependencies will not be cached on the 
build image (in time this can and should be attended to).

Note what we have gained here is that the build command is our authoritative reference in the sense that as a developer
it tells you that if you are building on your local make sure you are using JDK 8.

Note it is important that we use the gradle wrapper (./gradlew) below otherwise there is no point in having the gradle
directory in version control.

```shell
$ docker run --rm -v "$PWD":/home/gradle/project -w /home/gradle/project openjdk:8-jdk-alpine ./gradlew build
$ docker build -t com.thorgil/cashbook:latest --build-arg JAR_FILE=build/libs/cashbook-0.0.1-SNAPSHOT.jar .
```

Apart from being slow, the above build process is suboptimal because we are violating the Don't-Repeat-Yourself (DRY)
principle and also the process is not as robust as it could be. The explanations for each is provided below.

* We are violating the DRY principle because the docker build step and associated configuration has already been done in
our build.gradle file.
* The process not as robust as it could be because the JAR_FILE reference is subject to change when we do a 
release.

In short, our build server process should be exactly the same as our local process ($ ./gradlew docker) - that is the 
gist of the problem we'll solve next by creating our own CI image and using it to reduce the docker image build to 
one step.

#### One step build with a CI image

Our goal with a one step CI image is to execute the exact same command, namely *./gradlew docker* that we execute on
our local machines as developers.

##### build our CI image

```shell
docker build -f Builder.Dockerfile . -t com.thorgil/cashbook-builder:latest
```

##### expose the Docker socket to our CI container

Here we expose the Docker socket to our CI container, by bind-mounting it with the -v flag, as 
[has been advised](http://jpetazzo.github.io/2015/09/03/do-not-use-docker-in-docker-for-ci/).

```shell
docker run -v /var/run/docker.sock:/var/run/docker.sock -ti com.thorgil/cashbook-builder:latest
```

Execute say #docker ps or #docker images to satisfy yourself that you are talking to Docker Engine on the host (i.e. 
this is *not* Docker in Docker).

##### build the image on the CI server in one step

```shell
docker run --rm -v "$PWD":/home/gradle/project -v /var/run/docker.sock:/var/run/docker.sock -w /home/gradle/project com.thorgil/cashbook-builder:latest ./gradlew docker
```

Once that process is done the image will reside on the host machine and can be pushed to a registry from there or as
a part of the previous step.