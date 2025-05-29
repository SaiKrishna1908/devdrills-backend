Build Docker Image

```
docker build -t devdrills-backend .
```

Run Docker Container

```
docker run -p 8080:8080 --env-file .env devdrills-backend
```