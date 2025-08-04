#!/bin/bash

# Monolithic Bank Application Deployment Script

echo "=== Monolithic Bank Application Deployment ==="

# Configuration
TOMCAT_HOME=${TOMCAT_HOME:-"/opt/tomcat"}
DB_HOST=${DB_HOST:-"localhost"}
DB_PORT=${DB_PORT:-"5432"}
DB_NAME=${DB_NAME:-"monolithicbank"}
DB_USER=${DB_USER:-"postgres"}

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check prerequisites
check_prerequisites() {
    print_status "Checking prerequisites..."
    
    # Check Java
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed or not in PATH"
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        print_error "Java 17 or higher is required. Current version: $JAVA_VERSION"
        exit 1
    fi
    print_status "Java version: $(java -version 2>&1 | head -n 1)"
    
    # Check PostgreSQL connection
    if command -v psql &> /dev/null; then
        print_status "PostgreSQL client found"
    else
        print_warning "PostgreSQL client not found. Database setup will be skipped."
    fi
    
    # Check Tomcat
    if [ ! -d "$TOMCAT_HOME" ]; then
        print_error "Tomcat not found at $TOMCAT_HOME"
        print_error "Please set TOMCAT_HOME environment variable or install Tomcat"
        exit 1
    fi
    print_status "Tomcat found at: $TOMCAT_HOME"
}

# Setup database
setup_database() {
    print_status "Setting up database..."
    
    if command -v psql &> /dev/null; then
        # Check if database exists
        DB_EXISTS=$(psql -h $DB_HOST -p $DB_PORT -U $DB_USER -lqt | cut -d \| -f 1 | grep -qw $DB_NAME; echo $?)
        
        if [ $DB_EXISTS -ne 0 ]; then
            print_status "Creating database: $DB_NAME"
            createdb -h $DB_HOST -p $DB_PORT -U $DB_USER $DB_NAME
        else
            print_status "Database $DB_NAME already exists"
        fi
        
        # Run DDL script
        if [ -f "src/main/resources/sql/MONOLITHIC_BANK_DDL.sql" ]; then
            print_status "Running DDL script..."
            psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f src/main/resources/sql/MONOLITHIC_BANK_DDL.sql
        fi
        
        # Run DML script
        if [ -f "src/main/resources/sql/MONOLITHIC_BANK_DML.sql" ]; then
            print_status "Running DML script..."
            psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -f src/main/resources/sql/MONOLITHIC_BANK_DML.sql
        fi
    else
        print_warning "Skipping database setup - PostgreSQL client not available"
    fi
}

# Build application
build_application() {
    print_status "Building application..."
    
    # Clean previous build
    ./gradlew clean
    
    # Build WAR file
    ./gradlew war
    
    if [ $? -eq 0 ]; then
        print_status "Build successful"
    else
        print_error "Build failed"
        exit 1
    fi
}

# Deploy to Tomcat
deploy_to_tomcat() {
    print_status "Deploying to Tomcat..."
    
    # Stop Tomcat if running
    if [ -f "$TOMCAT_HOME/bin/shutdown.sh" ]; then
        print_status "Stopping Tomcat..."
        $TOMCAT_HOME/bin/shutdown.sh
        sleep 5
    fi
    
    # Remove old deployment
    if [ -d "$TOMCAT_HOME/webapps/monolithic-bank" ]; then
        print_status "Removing old deployment..."
        rm -rf $TOMCAT_HOME/webapps/monolithic-bank
    fi
    
    if [ -f "$TOMCAT_HOME/webapps/monolithic-bank.war" ]; then
        rm -f $TOMCAT_HOME/webapps/monolithic-bank.war
    fi
    
    # Copy new WAR file
    if [ -f "build/libs/monolithic-bank.war" ]; then
        print_status "Copying WAR file to Tomcat..."
        cp build/libs/monolithic-bank.war $TOMCAT_HOME/webapps/
    else
        print_error "WAR file not found: build/libs/monolithic-bank.war"
        exit 1
    fi
    
    # Start Tomcat
    if [ -f "$TOMCAT_HOME/bin/startup.sh" ]; then
        print_status "Starting Tomcat..."
        $TOMCAT_HOME/bin/startup.sh
    fi
}

# Main deployment process
main() {
    print_status "Starting deployment process..."
    
    check_prerequisites
    setup_database
    build_application
    deploy_to_tomcat
    
    print_status "Deployment completed successfully!"
    print_status "Application will be available at: http://localhost:8080/monolithic-bank"
    print_status "Please wait a few moments for Tomcat to fully start the application."
}

# Run main function
main "$@"