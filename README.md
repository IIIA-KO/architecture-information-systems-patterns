# Movies Application - Jakarta EE 10 Project

A comprehensive **Jakarta EE 10** web application demonstrating modern enterprise Java development with:

- **JSF (Jakarta Server Faces)** for web UI
- **JAX-RS (RESTful Web Services)** for REST API endpoints
- - **EJB (Enterprise JavaBeans)** for business logic
- - **JPA/Hibernate** for ORM and database management
- - **JMS (Java Message Service)** for asynchronous messaging
- - **Docker** containerization with PostgreSQL

## Requirements

- **Java 25** (JDK 25)
- **Maven 3.9+**
- **Docker** & **Docker Compose**
- **PostgreSQL 17.6+**

## Assignment Implementations (1-6)

### Assignment 1: JSF Web Interface

- **Files**: `src/main/webapp/` & `src/main/java/si/um/feri/jee/movies/jsf/`
- Movie CRUD operations via web UI (Create, Read, Update, Delete)
- Form validation with JSF validators
- Dynamic page rendering with XHTML templates
- Session-scoped managed beans for state management
- Navigation rules for page transitions

### Assignment 2: EJB Business Logic

- **File**: `src/main/java/si/um/feri/jee/movies/ejb/`
- `@Stateless MovieServiceBean` delegates to DAO layer
- Container-managed transactions (CMT) via JTA
- Local & Remote business interfaces
- Logging with Lombok `@Log` annotation

### Assignment 3: JPA Entity Mapping

- **Files**: `src/main/java/si/um/feri/jee/movies/vao/` & `src/main/resources/META-INF/persistence.xml`
- `Movie` and `Review` entities with proper relationships
- Hibernate ORM configuration (auto-DDL via `hbm2ddl.auto=update`)
- PostgreSQL dialect configuration
- Cascading operations for related entities

### Assignment 4: JMS Asynchronous Messaging

- **File**: `src/main/java/si/um/feri/jee/movies/jms/`
- `ReviewProducer` sends review messages to JMS queue
- `ReviewProcessorMDB` processes reviews asynchronously
- Queue: `java:/jms/queue/ReviewQueue`
- Asynchronous processing with logging

### Assignment 5: DAO Layer & Data Seeder
- **File**: `src/main/java/si/um/feri/jee/movies/dao/`
- `MovieDataStore` - JPA-based data access with CRUD operations
- `ReviewDataStore` - review persistence
- `DataSeeder` - initializes database with sample movies on first run
- Explicit find-then-update pattern prevents duplicate entity creation
- Transaction-safe operations with JPA EntityManager

### Assignment 6: REST API (JAX-RS)

- **File**: `src/main/java/si/um/feri/jee/movies/rest/`
- `MovieController` (@Stateless @Path("/movies")) - REST endpoints:
- `GET /api/movies` - retrieve all movies (HTTP 200)
- `GET /api/movies/{id}` - retrieve movie by ID (HTTP 200 or 404)
- `POST /api/movies` - create new movie (HTTP 201)
- `PUT /api/movies/{id}` - update movie (HTTP 200 or 404)
- `DELETE /api/movies/{id}` - delete movie (HTTP 204 or 404)
- `RestApplication` (@ApplicationPath("/api")) - JAX-RS bootstrap with auto-discovery

- **Client**: `client/src/main/java/si/um/feri/jee/movies/client/RestMovieClient.java`
- Idempotent HTTP REST client using Java 11+ `HttpClient`
- Demonstrates all CRUD operations with proper HTTP methods
- Prevents duplicate movie creation on restart
- Jackson ObjectMapper for JSON serialization/deserialization

## Author

Student project for Jakarta EE 10 course at University of Maribor (FERI)

## License

Educational Use
