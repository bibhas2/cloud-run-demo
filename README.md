## Installation
Install Docker and Google Cloud CLI following their documentations.

## One Time Setup
Docker must be run as a non-root user without the ``sudo`` command. Otherwise, ``docker push`` will fail.

Add the logged in user to the ``docker`` group. This group is created when Docker is installed.

```
sudo usermod -a -G docker ${USER}
```

Log out and log back in. Make sure that you can run ``docker`` without ``sudo``.

```
docker images
```

Log into Google Cloud. Here we always use ``us-central1`` region for the rest of the tutorial.


```
gcloud init
```

Choose the default project. In this tutorial, we will assume the project ID to be ``strategic-team-458516-f8``.

```
gcloud auth configure-docker \
    us-central1-docker.pkg.dev
```

## Deploy Application to Google Cloud Run

Create an image repository in Google Cloud Artifact Registry. We will call the repository ``my-docker-repo``.

```
gcloud artifacts repositories create my-docker-repo \
    --location=us-central1 \
    --repository-format=docker 
```

Build the application.

```
./gradlew build
```

Build the docker image and test it out.

```
docker build -t account-svc:1.0.0 .

docker run -p 8080:8080 -d --rm  account-svc:1.0.0

curl -v http://localhost:8080/account/hello

docker stop account-svc
```

Tag the image with the Google Artifact Registry repository location.

```
docker tag account-svc:1.0.0 \
us-central1-docker.pkg.dev/strategic-team-458516-f8/my-docker-repo/account-svc:1.0.0
```

Push the image to the Google Artifact Registry repository.


```
docker push us-central1-docker.pkg.dev/strategic-team-458516-f8/my-docker-repo/account-svc:1.0.0
```

Create a new service called ``account-svc`` in Google Cloud run.

```
gcloud run deploy account-svc \
--image=us-central1-docker.pkg.dev/strategic-team-458516-f8/my-docker-repo/account-svc:1.0.0 \
--no-invoker-iam-check \
--port=8080 \
--region=us-central1 \
--project=strategic-team-458516-f8
```

This will print out the URL for the service. You can also get that any time using:

```
gcloud run services describe account-svc --region=us-central1
```

The URL is also deterministic. Meaning, you can deduce the URL even before you deploy the service by following this format.

```
https://<service name>-<project number>.<region>.run.app
```

The project number is different from project ID and can be seen in the project settings page or the GCP welcome page.

## Deploying an Update
Rebuild the docker image and push it. You can use the same tag or create a new tag.


Re-deploy the service and send all traffic to the new version.

```
gcloud run deploy account-svc \
--image=us-central1-docker.pkg.dev/strategic-team-458516-f8/my-docker-repo/account-svc:1.0.0 \
--region=us-central1 \
--project=strategic-team-458516-f8 \
 && gcloud run services update-traffic account-svc --to-latest --region=us-central1
 ```

 ## Cloud Logging
 Take these steps to send application logs to Google Cloud Logging.

 Add a dependency to ``build.gradle``.

 ```
 implementation 'com.google.cloud:google-cloud-logging-logback:0.131.11-alpha'
 ```

 Do all your application logging using the ``slf4j`` API. Spring sets up ``Logback`` as the logging provider. And, above we have registered Google Cloud Logging as the appender for ``Logback``. 

 ```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyClass {
    private static final Logger logger = LoggerFactory.getLogger(MyClass.class);

    public void syHello() {
        logger.info("Logging INFO with Logback");
        logger.error("Logging ERROR with Logback");
        logger.debug("Logging DEBUG with Logback");
        logger.warn("Logging WARN with Logback");
        logger.trace("Logging TRACE with Logback");
    }

}
```

The default log level is ``INFO``. Which means, we will only see the ``INFO``, ``ERROR`` and ``WARN`` messages. To increase the log level for a package, add this line in ``src/main/resources/application.properties``.

```
logging.level.com.example.demo.svc=DEBUG
```

You can go to ``https://console.cloud.google.com/logs`` to view the logs.