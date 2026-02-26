# Postman Collection - User Service API

## 📦 How to Import This Collection

### Option 1: Manual Import
1. Open Postman
2. Click "Import" button (top-left)
3. Select "Raw text" tab
4. Copy the JSON below
5. Paste it in the text area
6. Click "Import"

### Option 2: From File
1. Save this as `user-service-api.postman_collection.json`
2. Open Postman
3. Click "Import"
4. Select the file
5. Click "Import"

---

## Postman Collection JSON

```json
{
  "info": {
    "name": "Flashkart User Service API",
    "description": "Complete API collection for User Service with JWT authentication",
    "version": "1.0.0"
  },
  "auth": {
    "type": "bearer",
    "bearer": [
      {
        "key": "token",
        "value": "{{jwt_token}}",
        "type": "string"
      }
    ]
  },
  "item": [
    {
      "name": "Authentication",
      "description": "Authentication endpoints (no JWT required)",
      "item": [
        {
          "name": "Register User",
          "event": [
            {
              "listen": "test",
              "script": {
                "exec": [
                  "if (pm.response.code === 201) {",
                  "  pm.environment.set('user_id', pm.response.json().user.id);",
                  "  pm.test('Registration successful', () => {",
                  "    pm.expect(pm.response.json().success).to.be.true;",
                  "  });",
                  "}"
                ]
              }
            }
          ],
          "request": {
            "method": "POST",
            "url": "http://localhost:8081/api/v1/auth/register",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@example.com\",\"password\":\"SecurePassword123!\",\"phoneNumber\":\"9876543210\"}"
            }
          }
        },
        {
          "name": "Login User",
          "event": [
            {
              "listen": "test",
              "script": {
                "exec": [
                  "if (pm.response.code === 200) {",
                  "  const token = pm.response.json().accessToken;",
                  "  pm.environment.set('jwt_token', token);",
                  "  pm.test('Login successful and token saved', () => {",
                  "    pm.expect(pm.response.json().success).to.be.true;",
                  "    pm.expect(token).to.be.truthy;",
                  "  });",
                  "}"
                ]
              }
            }
          ],
          "request": {
            "method": "POST",
            "url": "http://localhost:8081/api/v1/auth/login",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\"email\":\"john@example.com\",\"password\":\"SecurePassword123!\"}"
            }
          }
        },
        {
          "name": "Validate Token",
          "request": {
            "method": "GET",
            "url": {
              "raw": "http://localhost:8081/api/v1/auth/validate?token={{jwt_token}}",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8081",
              "path": ["api", "v1", "auth", "validate"],
              "query": [
                {
                  "key": "token",
                  "value": "{{jwt_token}}"
                }
              ]
            }
          }
        }
      ]
    },
    {
      "name": "User Management",
      "description": "User profile management endpoints (JWT required)",
      "item": [
        {
          "name": "Get Current User Profile",
          "request": {
            "method": "GET",
            "url": "http://localhost:8081/api/v1/users/me",
            "header": []
          }
        },
        {
          "name": "Update Current User Profile",
          "request": {
            "method": "PUT",
            "url": "http://localhost:8081/api/v1/users/me",
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"phoneNumber\":\"1111111111\"}"
            }
          }
        },
        {
          "name": "Get User by ID",
          "request": {
            "method": "GET",
            "url": {
              "raw": "http://localhost:8081/api/v1/users/{{user_id}}",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8081",
              "path": ["api", "v1", "users", "{{user_id}}"]
            }
          }
        },
        {
          "name": "Get User by Email",
          "request": {
            "method": "GET",
            "url": {
              "raw": "http://localhost:8081/api/v1/users/email/john@example.com",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8081",
              "path": ["api", "v1", "users", "email", "john@example.com"]
            }
          }
        },
        {
          "name": "Check Email Exists",
          "description": "No JWT required",
          "request": {
            "method": "GET",
            "url": {
              "raw": "http://localhost:8081/api/v1/users/check/john@example.com",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8081",
              "path": ["api", "v1", "users", "check", "john@example.com"]
            }
          }
        },
        {
          "name": "Update User by ID",
          "request": {
            "method": "PUT",
            "url": {
              "raw": "http://localhost:8081/api/v1/users/{{user_id}}",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8081",
              "path": ["api", "v1", "users", "{{user_id}}"]
            },
            "header": [
              {
                "key": "Content-Type",
                "value": "application/json"
              }
            ],
            "body": {
              "mode": "raw",
              "raw": "{\"firstName\":\"Updated\",\"lastName\":\"Name\",\"phoneNumber\":\"5555555555\"}"
            }
          }
        },
        {
          "name": "Get All Users (Admin)",
          "request": {
            "method": "GET",
            "url": "http://localhost:8081/api/v1/users"
          }
        },
        {
          "name": "Delete User",
          "request": {
            "method": "DELETE",
            "url": {
              "raw": "http://localhost:8081/api/v1/users/{{user_id}}",
              "protocol": "http",
              "host": ["localhost"],
              "port": "8081",
              "path": ["api", "v1", "users", "{{user_id}}"]
            }
          }
        }
      ]
    }
  ]
}
```

---

## 📋 Environment Variables for Postman

Create a new environment with these variables:

```json
{
  "name": "Flashkart Development",
  "values": [
    {
      "key": "base_url",
      "value": "http://localhost:8081",
      "enabled": true
    },
    {
      "key": "jwt_token",
      "value": "",
      "enabled": true
    },
    {
      "key": "user_id",
      "value": "",
      "enabled": true
    }
  ]
}
```

### How to Set Up Environment:
1. Open Postman
2. Click "Environments" on the left
3. Click "+"
4. Add the values above
5. Save with name "Flashkart Development"
6. Select this environment from dropdown (top-right)

---

## 🧪 Testing Workflow in Postman

### 1. Register New User
```
1. Click "Register User" request
2. Change email if needed (must be unique)
3. Click "Send"
4. User ID should be saved automatically
```

### 2. Login User
```
1. Click "Login User" request
2. Update email if you changed it in step 1
3. Click "Send"
4. JWT token should be saved automatically
```

### 3. Test Protected Endpoints
```
1. Any request now will use your JWT token automatically
2. Click on any protected endpoint
3. Click "Send"
4. Should get 200/OK responses
```

---

## 🔐 How Postman Handles JWT

### Automatic Token Setting:
After login, this script runs:
```javascript
pm.environment.set('jwt_token', pm.response.json().accessToken);
```

This saves the token to your environment variable.

### Automatic Token Inclusion:
The collection is set to use Bearer auth:
- **Type:** Bearer Token
- **Token:** `{{jwt_token}}` (uses environment variable)

So every request automatically includes:
```
Authorization: Bearer [your_token_here]
```

---

## 📊 Collection Structure

```
Flashkart User Service API
├── Authentication (No JWT)
│   ├── Register User
│   ├── Login User
│   └── Validate Token
└── User Management (JWT Required)
    ├── Get Current User Profile
    ├── Update Current User Profile
    ├── Get User by ID
    ├── Get User by Email
    ├── Check Email Exists (No JWT)
    ├── Update User by ID
    ├── Get All Users
    └── Delete User
```

---

## ✅ Testing Checklist for Postman

### Initial Setup:
- [ ] Import collection
- [ ] Create environment
- [ ] Select environment from dropdown

### Test Registration:
- [ ] Register user - should get 201
- [ ] User ID saved in environment

### Test Login:
- [ ] Login with correct credentials - should get 200
- [ ] JWT token saved in environment

### Test Protected Endpoints:
- [ ] Get Current Profile - should get 200
- [ ] Update Profile - should get 200
- [ ] Get All Users - should get 200/403

### Test Error Scenarios:
- [ ] Register duplicate email - should get 400
- [ ] Login wrong password - should get 401
- [ ] Delete user - should get 204

---

## 🎯 Tips for Using Postman

### 1. View Request/Response
- Click on request → Click "Send"
- Check response in "Body" tab
- Check headers in "Headers" tab

### 2. Save Response as Example
- Right-click response
- "Save as example"
- Later, click "Examples" to see saved responses

### 3. Create Test Scripts
- Go to "Tests" tab
- Add assertions
- Run collection to validate all tests

### 4. Use Postman Console
- Click "Console" at bottom-left
- See request/response details
- Useful for debugging

### 5. Generate Code
- Click "Code" icon on right
- Select your language (cURL, Python, JavaScript, etc.)
- Use in your application

---

## 🚀 Running Collection as Test Suite

### Automated Testing:
1. Click collection → "Run"
2. Select requests to run
3. Click "Run Flashkart User Service API"
4. View results in Test Results tab

### CI/CD Integration:
Use Newman (Postman CLI):
```bash
# Install Newman
npm install -g newman

# Run collection
newman run user-service-api.postman_collection.json \
  -e environment.json \
  --reporters cli,json

# View results
cat newman/user-service-api.postman_collection.json.newman-results.json
```

---

## 📝 Notes

- All URLs use `localhost:8081`
- Change `john@example.com` to unique emails for testing
- JWT expires in 1 hour - need to re-login after that
- Admin endpoints require ADMIN role (setup needed)
- Keep API running while testing

---

## 🔗 Quick Links

- **Postman Download:** https://www.postman.com/downloads/
- **Newman (CLI):** https://www.npmjs.com/package/newman
- **Postman Learning:** https://learning.postman.com/

---

**Last Updated:** February 26, 2026
**Collection Version:** 1.0.0


