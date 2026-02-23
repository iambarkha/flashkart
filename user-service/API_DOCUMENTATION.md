# User Profile APIs Documentation

## Overview
This document describes the User Profile APIs available in the flashkart user-service microservice.

## Base URL
```
http://localhost:8081
```

## Authentication Endpoints (`/api/v1/auth`)

### 1. User Registration
**Endpoint:** `POST /api/v1/auth/register`

**Description:** Register a new user account

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "SecurePass123",
  "phoneNumber": "+1234567890"
}
```

**Response (Success - 201 Created):**
```json
{
  "accessToken": null,
  "tokenType": null,
  "expiresIn": null,
  "user": {
    "userId": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phoneNumber": "+1234567890",
    "role": "USER",
    "active": true
  },
  "message": "User registered successfully",
  "success": true
}
```

**Response (Error - 400 Bad Request):**
```json
{
  "accessToken": null,
  "tokenType": null,
  "expiresIn": null,
  "user": null,
  "message": "Email already registered",
  "success": false
}
```

---

### 2. User Login
**Endpoint:** `POST /api/v1/auth/login`

**Description:** Login with email and password

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "SecurePass123"
}
```

**Response (Success - 200 OK):**
```json
{
  "accessToken": "jwt-token-here",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "userId": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phoneNumber": "+1234567890",
    "role": "USER",
    "active": true
  },
  "message": "Login successful",
  "success": true
}
```

**Response (Error - 401 Unauthorized):**
```json
{
  "accessToken": null,
  "tokenType": null,
  "expiresIn": null,
  "user": null,
  "message": "Invalid email or password",
  "success": false
}
```

---

### 3. Validate Token
**Endpoint:** `GET /api/v1/auth/validate`

**Description:** Validate JWT token

**Query Parameters:**
- `token` (required): JWT token to validate

**Response (Success - 200 OK):**
```
Token validated
```

---

## User Profile Endpoints (`/api/v1/users`)

### 1. Get User Profile by ID
**Endpoint:** `GET /api/v1/users/{userId}`

**Description:** Retrieve user profile by user ID

**Path Parameters:**
- `userId` (required): User ID (Long)

**Response (Success - 200 OK):**
```json
{
  "userId": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "+1234567890",
  "role": "USER",
  "active": true
}
```

**Response (Error - 404 Not Found):**
```json
{
  "error": "User not found",
  "status": "NOT_FOUND"
}
```

---

### 2. Get User Profile by Email
**Endpoint:** `GET /api/v1/users/email/{email}`

**Description:** Retrieve user profile by email address

**Path Parameters:**
- `email` (required): User email address

**Response (Success - 200 OK):**
```json
{
  "userId": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "+1234567890",
  "role": "USER",
  "active": true
}
```

---

### 3. Update User Profile
**Endpoint:** `PUT /api/v1/users/{userId}`

**Description:** Update user profile information

**Path Parameters:**
- `userId` (required): User ID

**Request Body:**
```json
{
  "firstName": "Johnny",
  "lastName": "Doe",
  "phoneNumber": "+9876543210",
  "email": "john@example.com",
  "role": "USER",
  "active": true
}
```

**Response (Success - 200 OK):**
```json
{
  "userId": 1,
  "firstName": "Johnny",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "+9876543210",
  "role": "USER",
  "active": true
}
```

---

### 4. Delete User Account
**Endpoint:** `DELETE /api/v1/users/{userId}`

**Description:** Delete a user account

**Path Parameters:**
- `userId` (required): User ID

**Response (Success - 204 No Content):**
```
User deleted successfully
```

---

### 5. Check Email Existence
**Endpoint:** `GET /api/v1/users/check/{email}`

**Description:** Check if an email address is already registered

**Path Parameters:**
- `email` (required): Email address to check

**Response (Success - 200 OK):**
```
true
```

---

## Data Models

### RegisterRequest
```json
{
  "firstName": "string (2-50 characters)",
  "lastName": "string (2-50 characters)",
  "email": "string (valid email format)",
  "password": "string (minimum 8 characters)",
  "phoneNumber": "string (10-15 characters)"
}
```

### LoginRequest
```json
{
  "email": "string (valid email format)",
  "password": "string"
}
```

### UserResponse
```json
{
  "userId": "long",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "phoneNumber": "string",
  "role": "USER | ADMIN | VENDOR",
  "active": "boolean"
}
```

### AuthResponse
```json
{
  "accessToken": "string (JWT token)",
  "tokenType": "string (e.g., Bearer)",
  "expiresIn": "long (milliseconds)",
  "user": "UserResponse object",
  "message": "string",
  "success": "boolean"
}
```

---

## Error Handling

### Validation Errors (400 Bad Request)
```json
{
  "email": "Email is required",
  "password": "Password must be at least 8 characters",
  "firstName": "First name must be between 2 and 50 characters"
}
```

### Not Found Errors (404)
```json
{
  "error": "User not found",
  "status": "NOT_FOUND"
}
```

### Server Errors (500 Internal Server Error)
```json
{
  "error": "Error message",
  "status": "INTERNAL_SERVER_ERROR"
}
```

---

## Security Features
- Password encryption using BCrypt
- CORS enabled for cross-origin requests
- Input validation on all endpoints
- JWT token support (to be implemented)
- Email uniqueness validation

---

## Database Schema

### users table
| Column | Type | Constraints |
|--------|------|-------------|
| user_id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| first_name | VARCHAR(50) | NOT NULL |
| last_name | VARCHAR(50) | NOT NULL |
| email | VARCHAR(255) | NOT NULL, UNIQUE |
| password | VARCHAR(255) | NOT NULL |
| phone_number | VARCHAR(15) | NOT NULL |
| role | VARCHAR(20) | NOT NULL, DEFAULT 'USER' |
| active | BOOLEAN | NOT NULL, DEFAULT true |
| created_at | TIMESTAMP | NOT NULL, DEFAULT NOW() |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT NOW() |

---

## Notes
- All endpoints (except /auth/register and /auth/login) require authentication (JWT token)
- Passwords are automatically encrypted using BCrypt
- User role is set to "USER" by default during registration
- User account is marked as active by default
- Timestamps are automatically managed by the system

