#!/bin/bash

# Weekly Meal Planner - Setup Verification Script
# This script verifies that all required files and configurations are in place

set -e

echo "=========================================="
echo "Weekly Meal Planner - Setup Verification"
echo "=========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check counter
CHECKS_PASSED=0
CHECKS_FAILED=0

# Function to check if a file exists
check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓${NC} $1 exists"
        ((CHECKS_PASSED++))
        return 0
    else
        echo -e "${RED}✗${NC} $1 is missing"
        ((CHECKS_FAILED++))
        return 1
    fi
}

# Function to check if a directory exists
check_dir() {
    if [ -d "$1" ]; then
        echo -e "${GREEN}✓${NC} $1 exists"
        ((CHECKS_PASSED++))
        return 0
    else
        echo -e "${RED}✗${NC} $1 is missing"
        ((CHECKS_FAILED++))
        return 1
    fi
}

# Function to check if a command exists
check_command() {
    if command -v "$1" &> /dev/null; then
        VERSION=$($1 --version 2>&1 | head -n 1)
        echo -e "${GREEN}✓${NC} $1 is installed ($VERSION)"
        ((CHECKS_PASSED++))
        return 0
    else
        echo -e "${RED}✗${NC} $1 is not installed"
        ((CHECKS_FAILED++))
        return 1
    fi
}

echo "1. Checking Required Tools"
echo "----------------------------"
check_command docker
check_command docker-compose
echo ""

echo "2. Checking Docker Configuration Files"
echo "---------------------------------------"
check_file "Dockerfile"
check_file "docker-compose.yml"
check_file "docker-compose.dev.yml"
check_file "docker-compose.prod.yml"
check_file ".dockerignore"
check_file "frontend/Dockerfile"
check_file "frontend/.dockerignore"
check_file "frontend/nginx.conf"
echo ""

echo "3. Checking Environment Configuration"
echo "--------------------------------------"
check_file ".env.example"
if [ -f ".env" ]; then
    echo -e "${GREEN}✓${NC} .env exists"
    ((CHECKS_PASSED++))
else
    echo -e "${YELLOW}⚠${NC} .env does not exist (will be created from .env.example)"
    echo -e "${YELLOW}→${NC} Run: cp .env.example .env"
fi
echo ""

echo "4. Checking Application Configuration"
echo "--------------------------------------"
check_file "src/main/resources/application.yml"
check_file "src/main/resources/application-dev.yml"
check_file "src/main/resources/application-prod.yml"
check_file "pom.xml"
echo ""

echo "5. Checking Frontend Files"
echo "---------------------------"
check_file "frontend/package.json"
check_file "frontend/vite.config.js"
check_dir "frontend/src"
echo ""

echo "6. Checking Documentation"
echo "-------------------------"
check_file "README.md"
check_file "DEPLOYMENT.md"
check_file "PROJECT_SETUP.md"
check_file "DATABASE_SCHEMA.md"
check_file "Makefile"
echo ""

echo "7. Checking Database Migrations"
echo "--------------------------------"
check_dir "src/main/resources/db/migration"
if ls src/main/resources/db/migration/*.sql 1> /dev/null 2>&1; then
    MIGRATION_COUNT=$(ls -1 src/main/resources/db/migration/*.sql | wc -l)
    echo -e "${GREEN}✓${NC} Found $MIGRATION_COUNT migration files"
    ((CHECKS_PASSED++))
else
    echo -e "${RED}✗${NC} No migration files found"
    ((CHECKS_FAILED++))
fi
echo ""

echo "8. Checking CI/CD Configuration"
echo "--------------------------------"
check_file ".github/workflows/docker-build.yml"
echo ""

echo "=========================================="
echo "Verification Summary"
echo "=========================================="
echo -e "${GREEN}Passed: $CHECKS_PASSED${NC}"
if [ $CHECKS_FAILED -gt 0 ]; then
    echo -e "${RED}Failed: $CHECKS_FAILED${NC}"
else
    echo -e "${GREEN}Failed: 0${NC}"
fi
echo ""

if [ $CHECKS_FAILED -eq 0 ]; then
    echo -e "${GREEN}✓ All checks passed! Your setup is ready.${NC}"
    echo ""
    echo "Next steps:"
    echo "  1. Create .env file: cp .env.example .env"
    echo "  2. Edit .env with your configuration"
    echo "  3. Start the application: docker-compose up -d"
    echo "  4. View logs: docker-compose logs -f"
    echo "  5. Access frontend: http://localhost:3000"
    echo "  6. Access backend: http://localhost:8080"
    echo ""
    exit 0
else
    echo -e "${RED}✗ Some checks failed. Please fix the issues above.${NC}"
    echo ""
    exit 1
fi
