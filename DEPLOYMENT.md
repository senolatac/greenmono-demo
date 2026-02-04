# Weekly Meal Planner - Deployment Guide

This guide covers Docker-based deployment for the Weekly Meal Planner application.

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Quick Start](#quick-start)
3. [Environment Configuration](#environment-configuration)
4. [Production Deployment](#production-deployment)
5. [Development Setup](#development-setup)
6. [Service Architecture](#service-architecture)
7. [Health Checks](#health-checks)
8. [Troubleshooting](#troubleshooting)
9. [Monitoring](#monitoring)

## Prerequisites

Before deploying the application, ensure you have the following installed:

- **Docker**: Version 20.10 or higher
- **Docker Compose**: Version 2.0 or higher
- **Java**: Version 17 (for local development only)
- **Node.js**: Version 20 (for local development only)
- **Git**: For cloning the repository

### Verify Installation

```bash
docker --version
docker-compose --version
```

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/senolatac/greenmono-demo.git
cd greenmono-demo
```

### 2. Configure Environment Variables

Copy the example environment file and customize it:

```bash
cp .env.example .env
```

Edit `.env` file with your preferred settings:

```bash
# Minimal configuration for quick start
DB_PASSWORD=your_secure_password
SPRING_PROFILES_ACTIVE=prod
```

### 3. Start the Application

```bash
# Build and start all services
docker-compose up -d

# View logs
docker-compose logs -f
```

### 4. Access the Application

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health

### 5. Stop the Application

```bash
# Stop all services
docker-compose down

# Stop and remove volumes (WARNING: This will delete all data)
docker-compose down -v
```

## Environment Configuration

### Environment Variables

The application uses environment variables for configuration. All available variables are documented in `.env.example`.

#### Required Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_PASSWORD` | Database password | `postgres` |
| `SPRING_PROFILES_ACTIVE` | Spring profile (dev/test/prod) | `prod` |

#### Optional Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_NAME` | Database name | `meal_planner_db` |
| `DB_USERNAME` | Database username | `postgres` |
| `DB_PORT` | Database port | `5432` |
| `BACKEND_PORT` | Backend API port | `8080` |
| `FRONTEND_PORT` | Frontend port | `3000` |
| `CORS_ALLOWED_ORIGINS` | Allowed CORS origins | `http://localhost:3000,http://localhost:80,http://localhost` |
| `JAVA_OPTS` | JVM options | `-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0` |
| `LOG_LEVEL` | Logging level | `INFO` |

### Creating Environment File

```bash
# Copy example file
cp .env.example .env

# Edit with your favorite editor
nano .env  # or vim, code, etc.
```

Example `.env` for production:

```bash
# Database Configuration
DB_NAME=meal_planner_db
DB_USERNAME=mealplanner
DB_PASSWORD=your_very_secure_password_here
DB_PORT=5432

# Spring Boot Configuration
SPRING_PROFILES_ACTIVE=prod

# Application Ports
BACKEND_PORT=8080
FRONTEND_PORT=80

# CORS Configuration
CORS_ALLOWED_ORIGINS=https://yourdomain.com,https://www.yourdomain.com

# JVM Configuration
JAVA_OPTS=-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=50.0

# Logging
LOG_LEVEL=INFO
```

## Production Deployment

### 1. Prepare Production Environment

```bash
# Create production environment file
cp .env.example .env.production

# Edit production settings
vim .env.production
```

### 2. Build Production Images

```bash
# Build all services
docker-compose build

# Or build specific service
docker-compose build backend
docker-compose build frontend
```

### 3. Start Production Services

```bash
# Start with production environment file
docker-compose --env-file .env.production up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f database
```

### 4. Verify Deployment

```bash
# Check backend health
curl http://localhost:8080/actuator/health

# Check frontend
curl http://localhost:3000

# Check database connection
docker-compose exec database psql -U postgres -d meal_planner_db -c "SELECT version();"
```

### 5. Production Best Practices

#### Security

1. **Change Default Passwords**: Always use strong, unique passwords
2. **Use HTTPS**: Deploy behind a reverse proxy (nginx, Caddy) with SSL
3. **Limit Port Exposure**: Only expose necessary ports to the internet
4. **Regular Updates**: Keep Docker images and dependencies updated

#### Database Backups

```bash
# Create backup
docker-compose exec database pg_dump -U postgres meal_planner_db > backup_$(date +%Y%m%d_%H%M%S).sql

# Restore backup
cat backup_20260204_120000.sql | docker-compose exec -T database psql -U postgres meal_planner_db
```

#### Volume Management

```bash
# List volumes
docker volume ls | grep meal-planner

# Backup volume data
docker run --rm -v meal-planner-postgres-data:/data -v $(pwd):/backup alpine tar czf /backup/postgres-backup.tar.gz -C /data .

# Restore volume data
docker run --rm -v meal-planner-postgres-data:/data -v $(pwd):/backup alpine tar xzf /backup/postgres-backup.tar.gz -C /data
```

## Development Setup

For development with hot-reload and debugging tools:

### 1. Start Development Database

```bash
# Start only database with pgAdmin
docker-compose -f docker-compose.dev.yml up -d

# Access pgAdmin at http://localhost:5050
# Default credentials: admin@mealplanner.com / admin
```

### 2. Run Backend Locally

```bash
# Set development profile
export SPRING_PROFILES_ACTIVE=dev

# Run with Maven
mvn spring-boot:run

# Or run with your IDE
```

### 3. Run Frontend Locally

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev

# Frontend available at http://localhost:3000
```

### 4. Development Workflow

```bash
# Database only
docker-compose -f docker-compose.dev.yml up -d

# Backend: local Maven/IDE
# Frontend: local npm dev server

# Benefits:
# - Hot reload for both frontend and backend
# - Easy debugging
# - Faster iteration
```

## Service Architecture

### Services Overview

The application consists of three main services:

```
┌─────────────────┐
│    Frontend     │ :3000 (nginx + React)
│   (Container)   │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    Backend      │ :8080 (Spring Boot)
│   (Container)   │
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    Database     │ :5432 (PostgreSQL)
│   (Container)   │
└─────────────────┘
```

### Container Details

#### Backend Container
- **Base Image**: `eclipse-temurin:17-jre-alpine`
- **Build**: Multi-stage build with Maven
- **Port**: 8080
- **Health Check**: `/actuator/health` endpoint
- **Dependencies**: Database service

#### Frontend Container
- **Base Image**: `nginx:1.25-alpine`
- **Build**: Multi-stage build with Node.js
- **Port**: 80 (mapped to 3000 on host)
- **Reverse Proxy**: Nginx proxies `/api` to backend
- **Health Check**: HTTP GET on port 80

#### Database Container
- **Image**: `postgres:15-alpine`
- **Port**: 5432
- **Volume**: Persistent data storage
- **Health Check**: `pg_isready` command

### Network

All services communicate through a custom bridge network: `meal-planner-network`

### Volumes

- `meal-planner-postgres-data`: PostgreSQL data persistence

## Health Checks

### Automated Health Checks

All services include Docker health checks:

```bash
# View health status
docker-compose ps

# Check backend health endpoint
curl http://localhost:8080/actuator/health

# Check frontend
curl http://localhost:3000

# Check database
docker-compose exec database pg_isready -U postgres
```

### Health Check Endpoints

#### Backend Endpoints

- **Liveness**: `GET /actuator/health/liveness`
- **Readiness**: `GET /actuator/health/readiness`
- **General Health**: `GET /actuator/health`
- **Metrics**: `GET /actuator/metrics`
- **Info**: `GET /actuator/info`

#### Frontend

- **Health**: `GET /` (returns HTTP 200 if healthy)

#### Database

- Internal health check via `pg_isready`

## Troubleshooting

### Common Issues

#### 1. Port Already in Use

```bash
# Check what's using the port
lsof -i :8080  # Backend
lsof -i :3000  # Frontend
lsof -i :5432  # Database

# Change port in .env file
BACKEND_PORT=8081
FRONTEND_PORT=3001
DB_PORT=5433
```

#### 2. Database Connection Failed

```bash
# Check database logs
docker-compose logs database

# Verify database is healthy
docker-compose ps database

# Check connection from backend
docker-compose exec backend ping database
```

#### 3. Backend Fails to Start

```bash
# Check backend logs
docker-compose logs backend

# Common causes:
# - Database not ready: Wait for health check
# - Flyway migration error: Check database schema
# - Port conflict: Change BACKEND_PORT
```

#### 4. Frontend Can't Reach Backend

```bash
# Check nginx configuration
docker-compose exec frontend cat /etc/nginx/conf.d/default.conf

# Test backend from frontend container
docker-compose exec frontend wget -O- http://backend:8080/actuator/health

# Check CORS settings
curl -H "Origin: http://localhost:3000" -I http://localhost:8080/api/menu-plans
```

#### 5. Build Failures

```bash
# Clear build cache
docker-compose build --no-cache

# Remove old images
docker-compose down --rmi all

# Rebuild from scratch
docker-compose build
```

### Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f database

# Last 100 lines
docker-compose logs --tail=100 backend

# Since timestamp
docker-compose logs --since 2026-02-04T12:00:00
```

### Debugging

```bash
# Execute commands in running container
docker-compose exec backend sh
docker-compose exec frontend sh
docker-compose exec database psql -U postgres meal_planner_db

# Check environment variables
docker-compose exec backend env

# Check network connectivity
docker-compose exec backend ping database
docker-compose exec frontend ping backend
```

## Monitoring

### Container Metrics

```bash
# Real-time resource usage
docker stats

# Container details
docker-compose ps
docker-compose top
```

### Application Metrics

The backend exposes Prometheus-compatible metrics:

```bash
# View all metrics
curl http://localhost:8080/actuator/metrics

# Specific metric
curl http://localhost:8080/actuator/metrics/jvm.memory.used
curl http://localhost:8080/actuator/metrics/http.server.requests
```

### Log Aggregation

For production, consider using:
- **ELK Stack** (Elasticsearch, Logstash, Kibana)
- **Loki + Grafana**
- **Splunk**
- **CloudWatch Logs** (AWS)

### Example: Prometheus + Grafana

```yaml
# Add to docker-compose.yml
  prometheus:
    image: prom/prometheus
    volumes:
      - ./prometheus.yml:/etc/prometheus/prometheus.yml
    ports:
      - "9090:9090"

  grafana:
    image: grafana/grafana
    ports:
      - "3001:3000"
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin
```

## Scaling

### Horizontal Scaling

```bash
# Scale backend service
docker-compose up -d --scale backend=3

# Requires load balancer (nginx, HAProxy, Traefik)
```

### Vertical Scaling

Adjust container resources in docker-compose.yml:

```yaml
services:
  backend:
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 2G
        reservations:
          cpus: '1'
          memory: 1G
```

## Maintenance

### Update Application

```bash
# Pull latest changes
git pull origin main

# Rebuild and restart
docker-compose down
docker-compose build
docker-compose up -d
```

### Database Migrations

Flyway migrations run automatically on startup. To run manually:

```bash
# Inside backend container
docker-compose exec backend sh

# Run Flyway
mvn flyway:migrate
```

### Cleanup

```bash
# Remove stopped containers
docker-compose down

# Remove volumes (WARNING: deletes data)
docker-compose down -v

# Remove images
docker-compose down --rmi all

# Full cleanup
docker system prune -a --volumes
```

## Security Checklist

- [ ] Change default database password
- [ ] Use environment-specific `.env` files
- [ ] Enable HTTPS with reverse proxy
- [ ] Restrict database port exposure
- [ ] Regular security updates
- [ ] Implement backup strategy
- [ ] Monitor application logs
- [ ] Use secrets management (Docker secrets, Vault)
- [ ] Implement rate limiting
- [ ] Enable firewall rules

## Support

For issues and questions:
- **GitHub Issues**: https://github.com/senolatac/greenmono-demo/issues
- **Documentation**: See README.md and PROJECT_SETUP.md

## License

[Add your license information]
