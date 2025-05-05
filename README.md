```
sudo usermod -a -G docker ${USER}
```

```
./gradlew build
```

```
docker build -t account-svc:1.0.0 .

docker run -p 8080:8080 -d --rm  account-svc:1.0.0
```

```
gcloud init

gcloud auth configure-docker \
    us-central1-docker.pkg.dev
```

```
docker tag account-svc:1.0.0 \
us-central1-docker.pkg.dev/strategic-team-458516-f8/my-docker-repo/account-svc:1.0.0
```

```
docker push us-central1-docker.pkg.dev/strategic-team-458516-f8/my-docker-repo/account-svc:1.0.0
```

```
gcloud run deploy account-svc \
--image=us-central1-docker.pkg.dev/strategic-team-458516-f8/my-docker-repo/account-svc:1.0.0 \
--no-invoker-iam-check \
--port=8080 \
--region=us-central1 \
--project=strategic-team-458516-f8
```