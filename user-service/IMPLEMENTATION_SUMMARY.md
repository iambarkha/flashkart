# User Profile APIs - Implementation Summary

## What Has Been Created

### 1. **Data Transfer Objects (DTOs)** - Located in `src/main/java/dto/`

#### a. RegisterRequest.java
- `firstName` - User's first name (2-50 characters)
- `lastName` - User's last name (2-50 characters)
- `email` - Email address with validation
- `password` - Password with minimum 8 characters
- `phoneNumber` - Phone number (10-15 characters)

#### b. LoginRequest.java
- `email` - User's email
- `password` - User's password

#### c. UserResponse.java
- `userId` - Unique user identifier
- `firstName` - User's first name
- `lastName` - User's last name
- `email` - User's email
- `phoneNumber` - User's phone number
- `role` - User role (USER, ADMIN, VENDOR)
- `active` - Account status

#### d. AuthResponse.java
- `accessToken` - JWT token (when implemented)
- `tokenType` - Type of token (Bearer)
- `expiresIn` - Token expiration time
- `user` - UserResponse object
- `message` - Response message
- `success` - Operation status

---

### 2. **Entity** - Located in `src/main/java/entity/`

#### User.java (JPA Entity)
- Maps to `users` table in PostgreSQL
- Fields: userId, firstName, lastName, email, password, phoneNumber, role, active
- Auto-timestamps: createdAt, updatedAt
- Password encryption ready
- Default role: USER
- Default active status: true

---

### 3. **Repository Layer** - Located in `src/main/java/repository/`

#### UserRepository.java
- Extends `JpaRepository<User, Long>`
- Custom methods:
  - `findByEmail()` - Find user by email
  - `existsByEmail()` - Check if email exists
  - `findByPhoneNumber()` - Find user by phone number

---

### 4. **Service Layer** - Located in `src/main/java/service/`

#### AuthService.java (Interface)
Methods:
- `register(RegisterRequest)` - Register new user
- `login(LoginRequest)` - Login user
- `getUserProfile(Long userId)` - Get user profile
- `updateUserProfile(Long userId, UserResponse)` - Update profile
- `deleteUser(Long userId)` - Delete user

#### AuthServiceImpl.java (Implementation)
- Handles user registration with email validation
- Handles login with password verification
- Password encryption using BCrypt
- JWT token placeholder (ready for implementation)

#### UserService.java (Interface)
Methods:
- `getUserById(Long userId)`
- `getUserByEmail(String email)`
- `updateUser(Long userId, UserResponse)`
- `deleteUser(Long userId)`
- `existsByEmail(String email)`

#### UserServiceImpl.java (Implementation)
- Implements all user management operations

---

### 5. **Mapper** - Located in `src/main/java/mapper/`

#### UserMapper.java
- MapStruct mapper for DTO-Entity conversion
- Methods:
  - `toUserResponse(User)` - Convert User entity to UserResponse DTO
  - `toUser(RegisterRequest)` - Convert RegisterRequest to User entity

---

### 6. **Controllers** - Located in `src/main/java/controller/`

#### AuthController.java
- Base URL: `/api/v1/auth`
- Endpoints:
  - `POST /register` - Register new user
  - `POST /login` - User login
  - `GET /validate` - Validate JWT token

#### UserController.java
- Base URL: `/api/v1/users`
- Endpoints:
  - `GET /{userId}` - Get user profile by ID
  - `GET /email/{email}` - Get user profile by email
  - `PUT /{userId}` - Update user profile
  - `DELETE /{userId}` - Delete user account
  - `GET /check/{email}` - Check email existence

---

### 7. **Configuration** - Located in `src/main/java/config/`

#### SecurityConfig.java
- Defines `PasswordEncoder` bean using BCryptPasswordEncoder
- Ready for Spring Security configuration

---

### 8. **Exception Handling** - Located in `src/main/java/exception/`

#### ResourceNotFoundException.java
- Custom exception for resource not found scenarios

#### GlobalExceptionHandler.java
- `@RestControllerAdvice` for centralized exception handling
- Handles:
  - ResourceNotFoundException (404)
  - MethodArgumentNotValidException (400)
  - Generic exceptions (500)

---

### 9. **Configuration File** - `src/main/resources/application.yml`
```yaml
Database Configuration:
- URL: jdbc:postgresql://localhost:5432/flashkart_users
- Username: postgres
- Password: postgres

JPA Configuration:
- DDL Auto: update
- Dialect: PostgreSQL
- Server Port: 8081
```

---

## API Endpoints Overview

### Authentication APIs
| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/v1/auth/register` | Register new user |
| POST | `/api/v1/auth/login` | Login user |
| GET | `/api/v1/auth/validate` | Validate token |

### User Profile APIs
| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/v1/users/{userId}` | Get user by ID |
| GET | `/api/v1/users/email/{email}` | Get user by email |
| PUT | `/api/v1/users/{userId}` | Update user profile |
| DELETE | `/api/v1/users/{userId}` | Delete user |
| GET | `/api/v1/users/check/{email}` | Check email existence |

---

## Key Features

✅ **User Registration**
- Email validation
- Password encryption with BCrypt
- Phone number validation
- Duplicate email prevention

✅ **User Authentication**
- Login with email and password
- JWT token structure ready
- Password verification

✅ **User Profile Management**
- Get user profile by ID or email
- Update user information
- Delete user account
- Check email availability

✅ **Data Validation**
- Input validation with annotations
- Field-level error messages
- Custom exception handling

✅ **Database Integration**
- PostgreSQL integration
- JPA/Hibernate ORM
- Auto-timestamp management
- Unique email constraint

---

## Next Steps (Optional Enhancements)

1. **JWT Implementation**: Implement token generation and validation
2. **Email Verification**: Add email verification step during registration
3. **Password Reset**: Implement password reset functionality
4. **Role-Based Access Control**: Implement role-based endpoint authorization
5. **Audit Logging**: Add audit trail for user actions
6. **Two-Factor Authentication**: Implement 2FA
7. **User Profile Picture**: Add profile image upload
8. **Search Users**: Add user search functionality
9. **Rate Limiting**: Add API rate limiting
10. **API Documentation**: Add Swagger/OpenAPI documentation

---

## Testing the APIs

You can test these APIs using:
- **Postman** - Create requests to each endpoint
- **cURL** - Command-line testing
- **REST Client** - VSCode extension
- **Thunder Client** - VSCode extension

Refer to `API_DOCUMENTATION.md` for detailed request/response examples.

---

## Notes
- All passwords are encrypted using BCrypt before storage
- Email addresses are case-sensitive and must be unique
- User role defaults to "USER" during registration
- Timestamps are automatically managed
- CORS is enabled for cross-origin requests

