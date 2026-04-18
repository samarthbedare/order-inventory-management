# Authentication Service API

This document provides details for the Authentication Service endpoints. these endpoints are used for user signup and login to obtain access tokens.

**Base URL**: `/api/v1/auth`

## Endpoints

### 1. Signup
- **Method**: `POST`
- **Path**: `/api/v1/auth/signup`
- **Description**: Registers a new administrator user.
- **Request Body**: [AdminUser](#adminuser)
- **Sample Request**:
  ```json
  {
    "username": "admin_user",
    "password": "securepassword123"
  }
  ```
- **Response Body**: [AdminUser](#adminuser) (password will be encoded)

### 2. Login
- **Method**: `POST`
- **Path**: `/api/v1/auth/login`
- **Description**: Authenticates a user and returns a JWT token.
- **Request Body**:
  ```json
  {
    "username": "admin_user",
    "password": "securepassword123"
  }
  ```
- **Response Body**:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "admin_user",
    "role": "ADMIN"
  }
  ```

---

## Data Models

### AdminUser
| Field | Type | Description |
|---|---|---|
| `id` | Integer | Unique identifier for the admin user |
| `username` | String | Unique username for login |
| `password` | String | Password (will be stored encoded) |
