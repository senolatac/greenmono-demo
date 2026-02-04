# Weekly Meal Planner - Makefile
# Convenient commands for Docker deployment

.PHONY: help setup build up down logs clean test restart status health backup restore

# Default target
help:
	@echo "Weekly Meal Planner - Available Commands:"
	@echo ""
	@echo "  make setup        - Initial setup (copy .env.example to .env)"
	@echo "  make build        - Build all Docker images"
	@echo "  make up           - Start all services"
	@echo "  make down         - Stop all services"
	@echo "  make restart      - Restart all services"
	@echo "  make logs         - View logs from all services"
	@echo "  make logs-backend - View backend logs"
	@echo "  make logs-frontend- View frontend logs"
	@echo "  make logs-db      - View database logs"
	@echo "  make status       - Show service status"
	@echo "  make health       - Check application health"
	@echo "  make clean        - Stop services and remove volumes (WARNING: deletes data)"
	@echo "  make test         - Run tests"
	@echo "  make backup       - Backup database"
	@echo "  make dev          - Start development environment"
	@echo ""

# Initial setup
setup:
	@echo "Setting up environment..."
	@if [ ! -f .env ]; then \
		cp .env.example .env; \
		echo ".env file created. Please edit it with your configuration."; \
	else \
		echo ".env file already exists."; \
	fi

# Build Docker images
build:
	@echo "Building Docker images..."
	docker-compose build

# Build without cache
build-no-cache:
	@echo "Building Docker images without cache..."
	docker-compose build --no-cache

# Start all services
up: setup
	@echo "Starting all services..."
	docker-compose up -d
	@echo "Services started. Access the application at:"
	@echo "  Frontend: http://localhost:3000"
	@echo "  Backend:  http://localhost:8080"
	@echo "  API Docs: http://localhost:8080/swagger-ui.html"

# Stop all services
down:
	@echo "Stopping all services..."
	docker-compose down

# Restart all services
restart: down up

# View logs from all services
logs:
	docker-compose logs -f

# View backend logs
logs-backend:
	docker-compose logs -f backend

# View frontend logs
logs-frontend:
	docker-compose logs -f frontend

# View database logs
logs-db:
	docker-compose logs -f database

# Show service status
status:
	docker-compose ps

# Health checks
health:
	@echo "Checking application health..."
	@echo "\nBackend Health:"
	@curl -s http://localhost:8080/actuator/health | json_pp || echo "Backend not responding"
	@echo "\nFrontend Health:"
	@curl -s -o /dev/null -w "%{http_code}" http://localhost:3000
	@echo "\n"

# Clean up (removes volumes - WARNING: deletes data)
clean:
	@echo "WARNING: This will delete all data!"
	@read -p "Are you sure? [y/N] " -n 1 -r; \
	echo; \
	if [[ $$REPLY =~ ^[Yy]$$ ]]; then \
		docker-compose down -v; \
		echo "Cleanup complete."; \
	else \
		echo "Cleanup cancelled."; \
	fi

# Run tests
test:
	@echo "Running tests..."
	docker-compose run --rm backend mvn test

# Development environment
dev:
	@echo "Starting development environment..."
	docker-compose -f docker-compose.dev.yml up -d
	@echo "Development database started."
	@echo "  PostgreSQL: localhost:5432"
	@echo "  pgAdmin:    http://localhost:5050"

# Stop development environment
dev-down:
	docker-compose -f docker-compose.dev.yml down

# Backup database
backup:
	@echo "Creating database backup..."
	@mkdir -p backups
	@docker-compose exec -T database pg_dump -U postgres meal_planner_db > backups/backup_$$(date +%Y%m%d_%H%M%S).sql
	@echo "Backup created in backups/ directory"

# Restore database (use: make restore FILE=backups/backup_20260204_120000.sql)
restore:
	@if [ -z "$(FILE)" ]; then \
		echo "Error: Please specify backup file. Usage: make restore FILE=backups/backup_20260204_120000.sql"; \
		exit 1; \
	fi
	@echo "Restoring database from $(FILE)..."
	@cat $(FILE) | docker-compose exec -T database psql -U postgres meal_planner_db
	@echo "Restore complete."

# Database shell
db-shell:
	docker-compose exec database psql -U postgres meal_planner_db

# Backend shell
backend-shell:
	docker-compose exec backend sh

# Frontend shell
frontend-shell:
	docker-compose exec frontend sh

# Pull latest images
pull:
	docker-compose pull

# Update application
update: pull build restart
	@echo "Application updated successfully!"

# Production deployment
prod-up:
	@echo "Starting production deployment..."
	docker-compose --env-file .env.production up -d

# Show resource usage
stats:
	docker stats --no-stream
