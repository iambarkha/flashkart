# Swagger Testing Guide - User Service API

## 📋 Table of Contents
1. [Prerequisites](#prerequisites)
2. [Starting the Application](#starting-the-application)
3. [Accessing Swagger UI](#accessing-swagger-ui)
4. [Testing Authentication Endpoints](#testing-authentication-endpoints)
5. [Testing User Management Endpoints](#testing-user-management-endpoints)
6. [JWT Token Usage in Swagger](#jwt-token-usage-in-swagger)
7. [Common Test Scenarios](#common-test-scenarios)

---

## Prerequisites

Ensure you have:
- Java 21 installed
- PostgreSQL running on `localhost:5432`
- Database `flashkart_users` created
- Maven installed

### Required Dependency
The project already includes `springdoc-openapi-starter-webmvc-ui` for Swagger integration.

---

## Starting the Application

### Option 1: Using Maven
```bash
cd user-service
./mvnw spring-boot:run
```

### Option 2: Building and Running JAR
```bash
cd user-service
./mvnw clean install
java -jar target/user-service-0.0.1-SNAPSHOT.jar
```

### Option 3: Using IDE
Run the `UserServiceApplication.java` main class directly.

**Expected Output:**
```
2024-XX-XX XX:XX:XX.XXX  INFO ... UserServiceApplication : Started UserServiceApplication
...
Tomcat started on port(s): 8081
```

---

## Accessing Swagger UI

Once the application is running, open your browser and navigate to:

### **Swagger UI:**
```
http://localhost:8081/swagger-ui.html
```

### **OpenAPI JSON Definition:**
```
http://localhost:8081/v3/api-docs
```

### **Alternative OpenAPI YAML:**
```
http://localhost:8081/v3/api-docs.yaml
```

---

## Testing Authentication Endpoints

### 1. Register a New User

**Endpoint:** `POST /api/v1/auth/register`

**Steps:**
1. In Swagger UI, find the **Authentication** section
2. Click on `POST /api/v1/auth/register`
3. Click **"Try it out"**
4. Enter the request body:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "SecurePassword123!",
  "phoneNumber": "9876543210"
}
```
5. Click **"Execute"**

**Expected Response (201 Created):**
```json
{
  "success": true,
  "message": "User registered successfully",
  "user": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phoneNumber": "9876543210"
  },
  "accessToken": null
}
```

### 2. User Login

**Endpoint:** `POST /api/v1/auth/login`

**Steps:**
1. Find the **Authentication** section
2. Click on `POST /api/v1/auth/login`
3. Click **"Try it out"**
4. Enter the request body:
```json
{
  "email": "john@example.com",
  "password": "SecurePassword123!"
}
```
5. Click **"Execute"**

**Expected Response (200 OK):**
```json
{
  "success": true,
  "message": "Login successful",
  "user": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phoneNumber": "9876543210"
  },
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**⚠️ Important:** Copy the `accessToken` value - you'll need it for authenticated requests.

### 3. Validate JWT Token

**Endpoint:** `GET /api/v1/auth/validate`

**Steps:**
1. Find the **Authentication** section
2. Click on `GET /api/v1/auth/validate`
3. Click **"Try it out"**
4. Enter the token in the `token` parameter:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
5. Click **"Execute"**

**Expected Response (200 OK):**
```
Token is valid
```

---

## JWT Token Usage in Swagger

### Using Bearer Token for Protected Endpoints

**Method 1: Swagger Authorize Button (Recommended)**
1. Look for the green **"Authorize"** button at the top of the Swagger UI
2. Click it
3. In the "bearerAuth" field, paste your JWT token:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```
4. Click **"Authorize"**
5. Click **"Close"**

Now all protected endpoints will automatically include the Authorization header.

**Method 2: Manual Header Entry**
1. Click on a protected endpoint (those with 🔒 lock icon)
2. Click **"Try it out"**
3. You'll see headers section - add:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## Testing User Management Endpoints

### 1. Get Current User Profile

**Endpoint:** `GET /api/v1/users/me`

**Steps:**
1. Ensure you've authorized with JWT token (see section above)
2. Find the **User Management** section
3. Click on `GET /api/v1/users/me`
4. Click **"Try it out"**
5. Click **"Execute"**

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "9876543210"
}
```

### 2. Update Current User Profile

**Endpoint:** `PUT /api/v1/users/me`

**Steps:**
1. Find the **User Management** section
2. Click on `PUT /api/v1/users/me`
3. Click **"Try it out"**
4. Enter the request body:
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "phoneNumber": "1234567890"
}
```
5. Click **"Execute"**

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "john@example.com",
  "phoneNumber": "1234567890"
}
```

### 3. Get User by ID

**Endpoint:** `GET /api/v1/users/{userId}`

**Steps:**
1. Click on `GET /api/v1/users/{userId}`
2. Click **"Try it out"**
3. Enter `userId` parameter: `1`
4. Click **"Execute"**

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "john@example.com",
  "phoneNumber": "1234567890"
}
```

### 4. Get User by Email

**Endpoint:** `GET /api/v1/users/email/{email}`

**Steps:**
1. Click on `GET /api/v1/users/email/{email}`
2. Click **"Try it out"**
3. Enter `email` parameter: `john@example.com`
4. Click **"Execute"**

### 5. Check Email Existence

**Endpoint:** `GET /api/v1/users/check/{email}`

**Note:** This endpoint does NOT require authentication

**Steps:**
1. Click on `GET /api/v1/users/check/{email}`
2. Click **"Try it out"**
3. Enter `email` parameter: `john@example.com`
4. Click **"Execute"**

**Expected Response (200 OK):**
```
true
```

### 6. Update User Profile by ID

**Endpoint:** `PUT /api/v1/users/{userId}`

**Steps:**
1. Click on `PUT /api/v1/users/{userId}`
2. Click **"Try it out"**
3. Enter `userId`: `1`
4. Enter request body:
```json
{
  "firstName": "Updated",
  "lastName": "Name",
  "phoneNumber": "5555555555"
}
```
5. Click **"Execute"**

### 7. Delete User

**Endpoint:** `DELETE /api/v1/users/{userId}`

**Steps:**
1. Click on `DELETE /api/v1/users/{userId}`
2. Click **"Try it out"**
3. Enter `userId`: `1`
4. Click **"Execute"**

**Expected Response (204 No Content):**
```
User deleted successfully
```

### 8. Get All Users (Admin Only)

**Endpoint:** `GET /api/v1/users`

**Steps:**
1. Ensure JWT token is authorized and user has ADMIN role
2. Click on `GET /api/v1/users`
3. Click **"Try it out"**
4. Click **"Execute"**

**Expected Response (200 OK):**
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phoneNumber": "9876543210"
  },
  {
    "id": 2,
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane@example.com",
    "phoneNumber": "9876543211"
  }
]
```

---

## Common Test Scenarios

### Scenario 1: Complete User Workflow
1. **Register** a new user
2. **Login** to get JWT token
3. **Authorize** in Swagger with the token
4. **Retrieve** current user profile
5. **Update** user profile
6. **View** updated profile

### Scenario 2: User Lookup
1. **Verify** email doesn't exist (before registration)
2. **Register** user
3. **Check** email now exists
4. **Get** user by email
5. **Get** user by ID

### Scenario 3: Error Handling
1. **Register** with duplicate email (should fail)
2. **Login** with wrong password (should fail)
3. **Validate** with invalid token (should fail)
4. **Get** non-existent user (should return 404)

### Scenario 4: Token Management
1. **Login** to get fresh token
2. **Validate** token (should succeed)
3. **Wait** until token expires (1 hour by default)
4. **Validate** expired token (should fail)
5. **Login again** to get new token

---

## Response Codes Reference

| Code | Meaning |
|------|---------|
| 200 | Success (GET, PUT) |
| 201 | Created (POST) |
| 204 | No Content (DELETE) |
| 400 | Bad Request (invalid input) |
| 401 | Unauthorized (missing/invalid JWT) |
| 403 | Forbidden (insufficient permissions) |
| 404 | Not Found (resource doesn't exist) |
| 409 | Conflict (duplicate email) |
| 500 | Internal Server Error |

---

## Troubleshooting

### Issue: "No Schema Could Be Generated"
**Solution:** Ensure all DTO classes have proper Lombok `@Data` annotation or getters/setters.

### Issue: "Unauthorized - JWT token required"
**Solution:** 
1. Click the **Authorize** button
2. Paste your JWT token from login response
3. Include "Bearer " prefix if not already present

### Issue: "Database connection error"
**Solution:**
1. Ensure PostgreSQL is running
2. Create database: `CREATE DATABASE flashkart_users;`
3. Check `application.yml` for correct connection settings

### Issue: Swagger UI not loading
**Solution:**
1. Verify application is running on port 8081
2. Check logs for startup errors
3. Try accessing: `http://localhost:8081/swagger-ui.html`

---

## Next Steps

- Explore the API documentation in Swagger UI
- Test all endpoints with different scenarios
- Monitor application logs for debugging
- Implement error handling in your client application
- Set up automated API tests using tools like Postman or RestAssured


