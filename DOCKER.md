# Docker Configuration Guide

This document provides detailed information about the Docker configuration for the Weekly Meal Planner application.

## Architecture Overview

The application uses a microservices architecture with three main containers:

```
┌─────────────────────────────────────────────────────┐
│                   Docker Host                        │
│                                                      │
│  ┌────────────────────────────────────────────┐    │
│  │         meal-planner-network (bridge)       │    │
│  │                                              │    │
│  │  ┌──────────────┐  ┌──────────────┐        │    │
│  │  │   Frontend   │  │   Backend    │        │    │
│  │  │   (nginx)    │  │  (Spring)    │        │    │
│  │  │   Port 80    │  │  Port 8080   │        │    │
│  │  └──────┬───────┘  └──────┬───────┘        │    │
│  │         │                  │                │    │
│  │         │                  │                │    │
│  │         │         ┌────────▼───────┐        │    │
│  │         │         │   Database     │        │    │
│  │         │         │  (PostgreSQL)  │        │    │
│  │         │         │   Port 5432    │        │    │
│  │         │         └────────────────┘        │    │
│  │         │                                    │    │
│  └─────────┼────────────────────────────────────┘    │
│            │                                         │
│    ┌───────▼──────────┐                             │
│    │  Host Network    │                             │
│    │  3000, 8080      │                             │
│    └──────────────────┘                             │
└─────────────────────────────────────────────────────┘
```

## Container Details

### 1. Frontend Container

**Image:** `nginx:1.25-alpine`

**Purpose:** Serves the React application and acts as a reverse proxy for API requests.

**Build Process:**
```dockerfile
Stage 1: Build React app with Node.js 20
Stage 2: Serve static files with nginx
```

**Key Features:**
- Multi-stage build for smaller image size
- Gzip compression enabled
- Security headers configured
- Proxies `/api` requests to backend
- Health check on port 80

**Configuration Files:**
- `frontend/Dockerfile` - Container build configuration
- `frontend/nginx.conf` - Nginx web server configuration
- `frontend/.dockerignore` - Files excluded from build context

### 2. Backend Container

**Image:** `eclipse-temurin:17-jre-alpine`

**Purpose:** Runs the Spring Boot application.

**Build Process:**
```dockerfile
Stage 1: Build JAR with Maven
Stage 2: Run application with JRE
```

**Key Features:**
- Multi-stage build for smaller image size
- Non-root user for security
- JVM optimized for containers
- Health check via Spring Boot Actuator
- Automatic database migration with Flyway

**Configuration Files:**
- `Dockerfile` - Container build configuration
- `.dockerignore` - Files excluded from build context
- `src/main/resources/application-prod.yml` - Production configuration

**Environment Variables:**
- `SPRING_PROFILES_ACTIVE` - Active Spring profile (prod)
- `SPRING_DATASOURCE_URL` - Database connection URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `JAVA_OPTS` - JVM options

### 3. Database Container

**Image:** `postgres:15-alpine`

**Purpose:** Stores application data.

**Key Features:**
- Persistent volume for data storage
- Health check with `pg_isready`
- Automatic initialization
- Isolated network access (not exposed to internet in production)

**Environment Variables:**
- `POSTGRES_DB` - Database name
- `POSTGRES_USER` - Database username
- `POSTGRES_PASSWORD` - Database password

**Volume:**
- `meal-planner-postgres-data` - Persistent data storage

## Docker Compose Files

### docker-compose.yml (Base Configuration)

Main configuration file for development and production deployment.

**Services:**
- database (PostgreSQL)
- backend (Spring Boot)
- frontend (React + nginx)

**Networks:**
- `meal-planner-network` - Bridge network for inter-service communication

**Volumes:**
- `meal-planner-postgres-data` - PostgreSQL data persistence

**Usage:**
```bash
docker-compose up -d
```

### docker-compose.dev.yml (Development)

Development-specific configuration with additional tools.

**Additional Services:**
- pgAdmin - Database management UI (port 5050)

**Usage:**
```bash
docker-compose -f docker-compose.dev.yml up -d
```

### docker-compose.prod.yml (Production Override)

Production-specific overrides for enhanced security and performance.

**Features:**
- Resource limits (CPU, memory)
- Enhanced JVM settings
- Removed external database port exposure
- Structured logging with rotation

**Usage:**
```bash
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d
```

## Build Process

### Backend Build

1. **Dependency Resolution**
   ```bash
   mvn dependency:go-offline
   ```

2. **Compilation and Packaging**
   ```bash
   mvn clean package -DskipTests
   ```

3. **Image Creation**
   - Copy JAR to runtime image
   - Set up non-root user
   - Configure health checks

### Frontend Build

1. **Dependency Installation**
   ```bash
   npm ci
   ```

2. **Production Build**
   ```bash
   npm run build
   ```

3. **Image Creation**
   - Copy nginx configuration
   - Copy built static files
   - Configure reverse proxy

## Networking

### Bridge Network

All services communicate through a custom bridge network: `meal-planner-network`

**Advantages:**
- Automatic DNS resolution between services
- Network isolation from other containers
- Internal communication doesn't use host network

### Service Communication

```
Frontend → Backend: http://backend:8080/api/*
Backend → Database: jdbc:postgresql://database:5432/meal_planner_db
```

### Port Mapping

| Service  | Internal Port | External Port | Configurable |
|----------|---------------|---------------|--------------|
| Frontend | 80            | 3000          | Yes (FRONTEND_PORT) |
| Backend  | 8080          | 8080          | Yes (BACKEND_PORT) |
| Database | 5432          | 5432          | Yes (DB_PORT) |

## Volume Management

### PostgreSQL Data Volume

**Name:** `meal-planner-postgres-data`

**Mount Point:** `/var/lib/postgresql/data`

**Purpose:** Persists database data across container restarts

**Backup:**
```bash
docker run --rm \
  -v meal-planner-postgres-data:/data \
  -v $(pwd):/backup \
  alpine tar czf /backup/postgres-backup.tar.gz -C /data .
```

**Restore:**
```bash
docker run --rm \
  -v meal-planner-postgres-data:/data \
  -v $(pwd):/backup \
  alpine tar xzf /backup/postgres-backup.tar.gz -C /data
```

## Health Checks

### Frontend Health Check

```yaml
test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:80/"]
interval: 30s
timeout: 5s
retries: 3
start_period: 20s
```

### Backend Health Check

```yaml
test: ["CMD", "wget", "--no-verbose", "--tries=1", "--spider", "http://localhost:8080/actuator/health"]
interval: 30s
timeout: 10s
retries: 5
start_period: 90s
```

### Database Health Check

```yaml
test: ["CMD-SHELL", "pg_isready -U postgres -d meal_planner_db"]
interval: 10s
timeout: 5s
retries: 5
start_period: 30s
```

## Security Best Practices

### 1. Non-Root User

Backend container runs as non-root user `spring:spring` for improved security.

### 2. Resource Limits

Production configuration includes CPU and memory limits to prevent resource exhaustion.

### 3. Network Isolation

Database is not exposed to external network in production configuration.

### 4. Secrets Management

Sensitive data (passwords) are passed via environment variables, never hardcoded.

### 5. Image Security

- Using official base images from trusted sources
- Alpine variants for smaller attack surface
- Regular updates recommended

## Performance Optimization

### 1. Multi-Stage Builds

Both frontend and backend use multi-stage builds:
- Smaller final image size
- Only runtime dependencies included
- Faster deployment

### 2. Layer Caching

Dockerfile layers are ordered to maximize cache hits:
- Dependencies downloaded first
- Source code copied last
- Reduces rebuild time

### 3. JVM Tuning

Backend container uses container-aware JVM settings:
```bash
-XX:+UseContainerSupport
-XX:MaxRAMPercentage=75.0
-XX:InitialRAMPercentage=50.0
```

### 4. Nginx Caching

Frontend nginx is configured with:
- Gzip compression
- Static asset caching
- Browser cache headers

## Troubleshooting

### Build Issues

**Problem:** Build fails with "Cannot resolve dependency"

**Solution:**
```bash
# Clear Docker build cache
docker-compose build --no-cache

# Or remove all cached layers
docker builder prune -a
```

**Problem:** Frontend build fails with memory error

**Solution:**
```bash
# Increase Node.js memory
docker-compose build --build-arg NODE_OPTIONS="--max-old-space-size=4096" frontend
```

### Runtime Issues

**Problem:** Backend cannot connect to database

**Solution:**
```bash
# Check database is healthy
docker-compose ps database

# Check network connectivity
docker-compose exec backend ping database

# Verify environment variables
docker-compose exec backend env | grep SPRING_DATASOURCE
```

**Problem:** Frontend returns 502 Bad Gateway

**Solution:**
```bash
# Check backend is healthy
curl http://localhost:8080/actuator/health

# Check nginx configuration
docker-compose exec frontend nginx -t

# View nginx logs
docker-compose logs frontend
```

### Resource Issues

**Problem:** Container OOM (Out of Memory)

**Solution:**
```bash
# Check memory usage
docker stats

# Increase memory limits in docker-compose.prod.yml
deploy:
  resources:
    limits:
      memory: 4G
```

## Development Workflow

### Local Development with Docker

```bash
# 1. Start only database
docker-compose -f docker-compose.dev.yml up -d

# 2. Run backend locally
mvn spring-boot:run

# 3. Run frontend locally
cd frontend && npm run dev
```

### Full Docker Development

```bash
# Start all services
docker-compose up -d

# Watch logs
docker-compose logs -f backend frontend

# Make code changes
# Rebuild and restart
docker-compose up -d --build
```

### Testing in Docker

```bash
# Run backend tests
docker-compose run --rm backend mvn test

# Run with coverage
docker-compose run --rm backend mvn clean test jacoco:report
```

## CI/CD Integration

### GitHub Actions

The project includes a GitHub Actions workflow (`.github/workflows/docker-build.yml`) that:

1. Runs backend tests
2. Builds Docker images
3. Runs integration tests
4. Performs security scans

### Deployment Pipeline

```
Code Push → Tests → Build Images → Integration Tests → Security Scan → Deploy
```

## Monitoring and Logging

### Viewing Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend

# Last 100 lines
docker-compose logs --tail=100 backend
```

### Metrics

Backend exposes Prometheus metrics at `/actuator/prometheus`:

```bash
curl http://localhost:8080/actuator/prometheus
```

### Log Rotation

Production configuration includes log rotation:
```yaml
logging:
  driver: "json-file"
  options:
    max-size: "10m"
    max-file: "3"
```

## Scaling

### Horizontal Scaling

```bash
# Scale backend service to 3 instances
docker-compose up -d --scale backend=3

# Note: Requires load balancer configuration
```

### Vertical Scaling

Adjust resource limits in `docker-compose.prod.yml`:

```yaml
deploy:
  resources:
    limits:
      cpus: '4'
      memory: 4G
```

## Cleanup

### Remove Containers

```bash
# Stop and remove containers
docker-compose down

# Remove containers and volumes
docker-compose down -v

# Remove containers, volumes, and images
docker-compose down -v --rmi all
```

### System Cleanup

```bash
# Remove unused images
docker image prune -a

# Remove unused volumes
docker volume prune

# Complete cleanup
docker system prune -a --volumes
```

## References

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/topicals/spring-boot-docker)
- [Nginx Configuration](https://nginx.org/en/docs/)

## Support

For issues related to Docker configuration:
- Check logs: `docker-compose logs`
- Verify setup: `./verify-setup.sh`
- Review [DEPLOYMENT.md](DEPLOYMENT.md) for detailed deployment guide
