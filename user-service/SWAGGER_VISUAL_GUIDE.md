# 🎥 Step-by-Step Visual Guide for Swagger Testing

## Prerequisites Checklist
- [ ] Java 21 installed
- [ ] Maven installed
- [ ] PostgreSQL running on localhost:5432
- [ ] Database `flashkart_users` created
- [ ] Port 8081 is available

---

## Step 1: Start the Application

### Terminal Commands:
```bash
# Navigate to user-service directory
cd user-service

# Run the application
./mvnw spring-boot:run

# Expected output:
# ...
# 2024-XX-XX XX:XX:XX.XXX  INFO ... Started UserServiceApplication in X.XXX seconds
# Tomcat started on port(s): 8081
```

### Check Application is Running:
- Open browser
- Go to: `http://localhost:8081/actuator/health`
- You should see: `{"status":"UP"}`

---

## Step 2: Open Swagger UI

### In Your Browser:
1. Type URL: `http://localhost:8081/swagger-ui.html`
2. Press Enter
3. You should see the Swagger UI with two sections:
   - **Authentication** (blue)
   - **User Management** (green)

### Screenshot Description:
```
┌─────────────────────────────────────────────────────────┐
│  Swagger UI - Flashkart User Service API                │
├─────────────────────────────────────────────────────────┤
│  [Authorize] [Explore] 🔍                               │
├─────────────────────────────────────────────────────────┤
│ ▼ Authentication                                        │
│   POST    /api/v1/auth/register                         │
│   POST    /api/v1/auth/login                            │
│   GET     /api/v1/auth/validate                         │
├─────────────────────────────────────────────────────────┤
│ ▼ User Management                                       │
│   GET     /api/v1/users                                 │
│   POST    /api/v1/users                                 │
│   ...and more                                           │
└─────────────────────────────────────────────────────────┘
```

---

## Step 3: Register a New User

### Instructions:
1. **Find the endpoint:**
   - Scroll to "Authentication" section
   - Click on `POST /api/v1/auth/register` (blue button)

2. **Click "Try it out":**
   - On the right side, find the "Try it out" button
   - Click it

3. **Enter Request Body:**
   - You'll see a text area with placeholder JSON
   - Clear it and paste:
   ```json
   {
     "firstName": "John",
     "lastName": "Doe",
     "email": "john@example.com",
     "password": "SecurePassword123!",
     "phoneNumber": "9876543210"
   }
   ```

4. **Execute:**
   - Scroll down and click the blue "Execute" button
   - Wait for response

5. **Check Response:**
   - You should see "201" status code
   - Response body shows success message
   - Note the `user.id` (usually 1 for first user)

### Example Response:
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

---

## Step 4: Login and Get JWT Token

### Instructions:
1. **Find the endpoint:**
   - In "Authentication" section
   - Click on `POST /api/v1/auth/login`

2. **Click "Try it out"**

3. **Enter Login Credentials:**
   ```json
   {
     "email": "john@example.com",
     "password": "SecurePassword123!"
   }
   ```

4. **Execute and Get Token:**
   - Click "Execute"
   - Wait for response
   - You'll see "200" status code

5. **Copy JWT Token:**
   - Look for `"accessToken"` field
   - It's a long string starting with "eyJ..."
   - Copy the entire token (without quotes)

### Example Response:
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
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNzA5MDAwMDAwfQ.1a2b3c4d5e6f7g8h9i0j1k2l3m4n5o6p7q8r9s0t"
}
```

### 🔐 Important:
- This token is your authentication credential
- Valid for 1 hour
- Keep it secret
- Use it for all protected endpoints

---

## Step 5: Authorize in Swagger

### This is CRITICAL for testing protected endpoints!

### Instructions:
1. **Find Authorize Button:**
   - Look at the top-right of Swagger UI
   - Find the green `Authorize` button
   - Click it

2. **Enter JWT Token:**
   - A dialog box appears with a field for "bearerAuth"
   - Paste your JWT token from Step 4
   - Example: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ...`
   - Do NOT include "Bearer " prefix

3. **Click Authorize:**
   - Click the blue "Authorize" button
   - You should see a success message

4. **Close Dialog:**
   - Click the "Close" button
   - Now return to the main Swagger view

### Visual Flow:
```
[Click Authorize] → [Enter Token] → [Click Authorize] → [Close] → Ready!
```

### Verification:
- Look for a lock icon 🔒 next to protected endpoints
- This indicates authorization is configured

---

## Step 6: Test Protected Endpoints

### Get Your Profile

**Endpoint:** `GET /api/v1/users/me`

**Steps:**
1. Scroll to "User Management" section
2. Click on `GET /api/v1/users/me` (should have 🔒 lock icon)
3. Click "Try it out"
4. Click "Execute"
5. You should get 200 status with your profile data

**Expected Response:**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "9876543210"
}
```

### Update Your Profile

**Endpoint:** `PUT /api/v1/users/me`

**Steps:**
1. Find `PUT /api/v1/users/me`
2. Click "Try it out"
3. Enter request body:
   ```json
   {
     "firstName": "Jane",
     "lastName": "Smith",
     "phoneNumber": "1111111111"
   }
   ```
4. Click "Execute"
5. Verify response shows updated data

---

## Step 7: Validate Token

### Endpoint: `GET /api/v1/auth/validate`

**Steps:**
1. Scroll back to "Authentication" section
2. Click on `GET /api/v1/auth/validate`
3. Click "Try it out"
4. Enter your JWT token in the `token` parameter field
5. Click "Execute"

**Success Response (200):**
```
Token is valid
```

**Failure Response (401):**
```
Invalid token
```

---

## Step 8: Test Error Scenarios

### Scenario 1: Try to Register with Duplicate Email

**Steps:**
1. Repeat Step 3 with same email
2. Expected: 400 Bad Request
3. Response: `"success": false, "message": "Email already registered"`

### Scenario 2: Wrong Password Login

**Steps:**
1. In Step 4, use wrong password
2. Expected: 401 Unauthorized
3. Response: `"success": false, "message": "Invalid credentials"`

### Scenario 3: Access Protected Endpoint Without Token

**Steps:**
1. Clear authorization (click Authorize → logout)
2. Try `GET /api/v1/users/me`
3. Expected: 401 Unauthorized
4. Message: "JWT token required" or similar

### Scenario 4: Invalid Token

**Steps:**
1. In validate endpoint, paste invalid/random token
2. Click "Execute"
3. Expected: 401 Unauthorized

---

## Step 9: Advanced Testing

### Test All User Endpoints

#### Get User by ID
- Endpoint: `GET /api/v1/users/{userId}`
- Parameter: Enter `1`
- Execute: Should return user with id=1

#### Get User by Email
- Endpoint: `GET /api/v1/users/email/{email}`
- Parameter: Enter `john@example.com`
- Execute: Should return matching user

#### Check Email Exists
- Endpoint: `GET /api/v1/users/check/{email}` *(No auth needed)*
- Parameter: Enter `john@example.com`
- Execute: Should return `true`

#### Update User Profile
- Endpoint: `PUT /api/v1/users/{userId}`
- Parameter: Enter `1`
- Body: Same as Step 6
- Execute: Should return updated user

#### Delete User
- Endpoint: `DELETE /api/v1/users/{userId}`
- Parameter: Enter `1`
- Execute: Should get 204 No Content

#### Get All Users
- Endpoint: `GET /api/v1/users` *(Requires ADMIN role)*
- Execute: Should return list of all users

---

## Troubleshooting Guide

### Problem: Swagger UI shows "No servers available"
**Solution:**
1. Check if application is running
2. Verify port 8081 is correct
3. Restart application

### Problem: "Unauthorized" error on protected endpoints
**Solution:**
1. Click Authorize button again
2. Verify JWT token is pasted correctly
3. Token might be expired - get a new one via login

### Problem: "Database connection error"
**Solution:**
1. Start PostgreSQL service
2. Create database: 
   ```sql
   CREATE DATABASE flashkart_users;
   ```
3. Check `application.yml` settings
4. Restart application

### Problem: "Port 8081 already in use"
**Solution:**
```bash
# Kill process using port 8081
# On macOS/Linux:
lsof -i :8081
kill -9 <PID>

# Then restart application
./mvnw spring-boot:run
```

### Problem: "Duplicate email" on first registration
**Solution:**
1. Check if user already exists in database
2. Use a different email address
3. Or delete existing user and try again

---

## Complete Testing Checklist

- [ ] Application starts successfully
- [ ] Swagger UI loads
- [ ] Can register new user
- [ ] Can login and get JWT token
- [ ] Can authorize in Swagger
- [ ] Can get profile with token
- [ ] Can update profile
- [ ] Can get user by ID
- [ ] Can get user by email
- [ ] Can check email exists
- [ ] Can validate token
- [ ] Cannot access protected endpoint without token
- [ ] Cannot login with wrong password
- [ ] Cannot register duplicate email
- [ ] Can delete user

---

## Next Steps

1. ✅ Test all endpoints using Swagger
2. 📊 Monitor application logs
3. 🔍 Check database directly
4. 📋 Document your test results
5. 🚀 Move to automated testing (Postman, Cucumber, etc.)


