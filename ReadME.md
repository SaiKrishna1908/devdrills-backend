Build Docker Image

```
docker build -t devdrills-backend .
```

Run Docker Container

```
docker run -d -p 443:443 --env-file .env devdrills-backend
```

List Docker Containers

```
docker ps -a
```

Stop Docker Container

```
docker stop <container_id>
```

Delete Docker Container

```
docker rm <container_id>
```

List Docker Images

```
docker images
```

Delete Docker Image

```
docker rmi <image_id>
```