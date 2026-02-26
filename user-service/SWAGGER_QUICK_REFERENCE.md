# Swagger Testing Quick Reference Card

## 🚀 Quick Start

1. **Start Application:**
   ```bash
   cd user-service
   ./mvnw spring-boot:run
   ```

2. **Open Swagger UI:**
   ```
   http://localhost:8081/swagger-ui.html
   ```

3. **Register & Login:**
   - POST `/api/v1/auth/register` - Create account
   - POST `/api/v1/auth/login` - Get JWT token
   - Copy `accessToken` from response

4. **Authorize in Swagger:**
   - Click green **"Authorize"** button
   - Paste JWT token (without "Bearer " prefix)
   - Click "Authorize" → "Close"

---

## 📌 Essential Endpoints

### Authentication (No JWT Required ❌)
| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/api/v1/auth/register` | Create new account |
| POST | `/api/v1/auth/login` | Get JWT token |
| GET | `/api/v1/auth/validate` | Check token validity |

### User Management (JWT Required ✅)
| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/v1/users/me` | Get your profile |
| PUT | `/api/v1/users/me` | Update your profile |
| GET | `/api/v1/users/{id}` | Get user by ID |
| GET | `/api/v1/users/email/{email}` | Get user by email |
| PUT | `/api/v1/users/{id}` | Update user profile |
| DELETE | `/api/v1/users/{id}` | Delete user |
| GET | `/api/v1/users` | Get all users (Admin) |
| GET | `/api/v1/users/check/{email}` | Check if email exists |

---

## 🧪 Sample Test Data

### Register Request
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "phoneNumber": "9876543210"
}
```

### Login Request
```json
{
  "email": "john@example.com",
  "password": "SecurePass123!"
}
```

### Update Profile Request
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "phoneNumber": "1234567890"
}
```

---

## 🔐 JWT Token Management

**Token Structure:**
```
Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0...
```

**Expiration:** 1 hour (3600000 ms)

**How to Use:**
1. Login and copy `accessToken`
2. Click **Authorize** → paste token
3. Token automatically added to all requests

---

## 📊 HTTP Status Codes

```
✅ 200 - OK (GET, PUT successful)
✅ 201 - Created (POST successful)
✅ 204 - No Content (DELETE successful)
❌ 400 - Bad Request (invalid input)
❌ 401 - Unauthorized (missing/invalid JWT)
❌ 403 - Forbidden (insufficient permissions)
❌ 404 - Not Found (resource missing)
❌ 409 - Conflict (duplicate email)
❌ 500 - Server Error
```

---

## 🎯 Test Workflow

### Complete Flow:
1. **Register** → Get user ID
2. **Login** → Copy JWT token
3. **Authorize** → Paste token in Swagger
4. **Get Profile** → View your data
5. **Update Profile** → Modify info
6. **Logout** → Clear token (optional)

### Error Testing:
- Register with **duplicate email** → 400/409
- Login with **wrong password** → 401
- Access protected endpoint **without token** → 401
- Use **expired token** → 401
- Get **non-existent user** → 404

---

## 🛠️ Troubleshooting

| Problem | Solution |
|---------|----------|
| Swagger not loading | Check `http://localhost:8081` |
| "Unauthorized" error | Click Authorize, paste JWT token |
| Database connection failed | Start PostgreSQL, create `flashkart_users` DB |
| Duplicate email error | Use unique email for registration |
| Invalid JWT token | Get new token by logging in again |

---

## 📖 Documentation Files

- **Full Guide:** `SWAGGER_TESTING_GUIDE.md`
- **API Docs:** `API_DOCUMENTATION.md`
- **Implementation:** `IMPLEMENTATION_SUMMARY.md`

---

## 🔗 Useful URLs

| Resource | URL |
|----------|-----|
| Swagger UI | `http://localhost:8081/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8081/v3/api-docs` |
| OpenAPI YAML | `http://localhost:8081/v3/api-docs.yaml` |
| Health Check | `http://localhost:8081/actuator/health` |

---

## ⚙️ Configuration

**Server Port:** 8081
**JWT Secret:** Set via `JWT_SECRET` environment variable
**JWT Expiration:** 3600000 ms (1 hour)
**Database:** PostgreSQL on localhost:5432
**DB Name:** flashkart_users

---

## 📝 Notes

- All user endpoints require valid JWT token except `/check/{email}`
- Passwords are encrypted using BCrypt
- Admin endpoints require ADMIN role
- CORS enabled for all origins
- Use fresh tokens for each test session


