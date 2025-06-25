# Spring Batch Application

A robust Spring Batch application designed to process CSV data and import it into a PostgreSQL database. Built with clean architecture principles, comprehensive error handling, and RESTful APIs for job management.

## 🏗️ Architecture

This application follows **Clean Architecture** principles with clear separation of concerns:

```
src/main/java/com/example/spring_batch/
├── application/                    # Application layer (use cases & services)
├── domain/                        # Domain layer (entities & business logic)
├── infrastructure/                # Infrastructure layer (config, adapters)
├── interfaces/                    # Interface layer (controllers, DTOs)
└── shared/                       # Shared components (exceptions, utilities)
```

## 🚀 Features

- **CSV Processing**: Read and process CSV files with Spring Batch
- **Database Integration**: PostgreSQL with JPA/Hibernate
- **RESTful APIs**: Complete CRUD operations for persons
- **Batch Job Management**: Start, monitor, and manage batch jobs via REST
- **Pagination**: Full pagination support for all list operations
- **Error Handling**: Comprehensive exception handling with proper HTTP status codes
- **Logging**: Structured logging throughout the application
- **Fault Tolerance**: Skip limit and error recovery in batch processing
- **Audit Trail**: Automatic timestamp management for created/updated records

## 📋 Prerequisites

- **Java 24** or higher
- **PostgreSQL 14** or higher
- **Gradle 8.x** or higher

## 🛠️ Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd spring-batch
```

### 2. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE person_batch_db;
```

### 3. Environment Configuration

Set up your environment variables or update `application.properties`:

```properties
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=person_batch_db
DB_USERNAME=postgres
DB_PASSWORD=your_password
```

### 4. Build the Application

```bash
./gradlew clean build
```

### 5. Run the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## 📊 Database Schema

The application automatically creates the following tables:

### Business Tables
- `persons` - Main entity for person data

### Spring Batch Metadata Tables
- `BATCH_JOB_INSTANCE`
- `BATCH_JOB_EXECUTION`
- `BATCH_JOB_EXECUTION_PARAMS`
- `BATCH_JOB_EXECUTION_CONTEXT`
- `BATCH_STEP_EXECUTION`
- `BATCH_STEP_EXECUTION_CONTEXT`

## 🔌 API Endpoints

### Batch Job Management

#### Health Check
```http
GET /api/jobs/health
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Spring Batch Application is running!",
  "data": null,
  "timestamp": "2025-06-25T18:00:00"
}
```

#### Get Job Information
```http
GET /api/jobs/info
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Job information retrieved",
  "data": {
    "jobName": "importPersonsJob",
    "jobDescription": "Import persons from CSV to database"
  },
  "timestamp": "2025-06-25T18:00:00"
}
```

#### Start Import Job
```http
POST /api/jobs/import-persons
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Job started successfully",
  "data": {
    "jobExecutionId": 1,
    "jobInstanceId": 1,
    "status": "STARTING",
    "startTime": "2025-06-25T18:00:00",
    "endTime": null
  },
  "timestamp": "2025-06-25T18:00:00"
}
```

### Person Management

#### Get All Persons (Paginated)
```http
GET /api/persons?page=0&size=20&sortBy=createdAt&sortDir=desc
```

**Query Parameters:**
- `page` (default: 0) - Page number
- `size` (default: 20) - Page size
- `sortBy` (default: createdAt) - Sort field
- `sortDir` (default: desc) - Sort direction (asc/desc)

#### Get Person by ID
```http
GET /api/persons/{id}
```

#### Get Person by Email
```http
GET /api/persons/email/{email}
```

#### Search Persons by Name
```http
GET /api/persons/search?name=john&page=0&size=20
```

#### Get Statistics
```http
GET /api/persons/statistics
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Statistics retrieved",
  "data": {
    "totalPersons": 1000,
    "personsWithExampleDomain": 1000
  },
  "timestamp": "2025-06-25T18:00:00"
}
```

## 📁 File Structure

### Input Data
Place your CSV file in: `src/main/resources/input/persons.csv`

**Expected CSV Format:**
```csv
firstName,lastName
John,Doe
Jane,Smith
```

## 🔧 Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/person_batch_db
spring.datasource.username=postgres
spring.datasource.password=admin

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Batch
spring.batch.job.enabled=false
spring.batch.jdbc.initialize-schema=never

# Server
server.port=8080

# Logging
logging.level.com.example.spring_batch=DEBUG
logging.level.org.springframework.batch=DEBUG
```

## 🧪 Testing

### Run Tests
```bash
./gradlew test
```

### Integration Tests
```bash
./gradlew integrationTest
```

## 📝 Usage Examples

### Using cURL

#### Start Batch Job
```bash
curl -X POST http://localhost:8080/api/jobs/import-persons
```

#### Get All Persons
```bash
curl -X GET "http://localhost:8080/api/persons?page=0&size=10"
```

#### Search Persons
```bash
curl -X GET "http://localhost:8080/api/persons/search?name=john&page=0&size=10"
```

#### Get Statistics
```bash
curl -X GET http://localhost:8080/api/persons/statistics
```

### Using Postman

1. Import the provided Postman collection
2. Set the base URL to `http://localhost:8080`
3. Use the available endpoints for testing

## 🐛 Troubleshooting

### Common Issues

#### Database Connection
- Ensure PostgreSQL is running
- Verify database credentials in `application.properties`
- Check if database `person_batch_db` exists

#### Batch Job Issues
- Verify CSV file exists in `src/main/resources/input/`
- Check CSV format matches expected structure
- Review application logs for detailed error messages

#### Port Conflicts
- Change `server.port` in `application.properties` if 8080 is occupied

### Logs

Application logs provide detailed information about:
- Batch job execution progress
- Database operations
- API requests and responses
- Error details and stack traces

## 🔒 Security Considerations

- Database credentials should be externalized (use environment variables)
- Consider implementing authentication for production use
- Validate and sanitize all input data
- Use HTTPS in production environments

## 📈 Performance

### Batch Processing
- Chunk size: 10 records per transaction
- Skip limit: 10 failed records
- Fault-tolerant processing with error recovery

### Database
- Optimized queries with proper indexing
- Connection pooling with HikariCP
- Pagination for large datasets

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the application logs for error details
- Review the troubleshooting section above

## 🔄 Version History

- **v1.0.0** - Initial release with basic batch processing
- **v2.0.0** - Refactored with clean architecture and comprehensive APIs

---

**Built with ❤️ using Spring Boot and Spring Batch** 