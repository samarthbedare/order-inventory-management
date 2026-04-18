# Customer Service API

This document provides details for the Customer Service endpoints. These endpoints are used to manage customer information in the system.

**Base URL**: `/api/v1/customers`

## Endpoints

### 1. Get All Customers
- **Method**: `GET`
- **Path**: `/api/v1/customers`
- **Description**: Retrieves a list of all customers.
- **Response Body**: Array of [CustomerDTO](#customerdto)
- **Sample Response**:
  ```json
  [
    {
      "customerId": 1,
      "fullName": "John Doe",
      "emailAddress": "john.doe@example.com"
    },
    {
      "customerId": 2,
      "fullName": "Jane Smith",
      "emailAddress": "jane.smith@example.com"
    }
  ]
  ```

### 2. Get Customer by ID
- **Method**: `GET`
- **Path**: `/api/v1/customers/{id}`
- **Description**: Retrieves a single customer by their ID.
- **Path Parameters**:
  - `id` (Integer): The unique ID of the customer.
- **Response Body**: [CustomerDTO](#customerdto)
- **Sample Response**:
  ```json
  {
    "customerId": 1,
    "fullName": "John Doe",
    "emailAddress": "john.doe@example.com"
  }
  ```

### 3. Get Customer by Email
- **Method**: `GET`
- **Path**: `/api/v1/customers/email/{email}`
- **Description**: Retrieves a customer by their email address.
- **Path Parameters**:
  - `email` (String): The email address of the customer.
- **Response Body**: [CustomerDTO](#customerdto)

### 4. Create New Customer
- **Method**: `POST`
- **Path**: `/api/v1/customers`
- **Description**: Creates a new customer record.
- **Request Body**: [CustomerDTO](#customerdto) (Omit `customerId` as it is auto-generated)
- **Sample Request**:
  ```json
  {
    "fullName": "New Customer",
    "emailAddress": "new.customer@example.com"
  }
  ```
- **Response Body**: [CustomerDTO](#customerdto) (Includes generated ID)
- **Status Code**: `201 Created`

### 5. Update Customer
- **Method**: `PUT`
- **Path**: `/api/v1/customers/{id}`
- **Description**: Updates an existing customer's information.
- **Path Parameters**:
  - `id` (Integer): The ID of the customer to update.
- **Request Body**: [CustomerDTO](#customerdto)
- **Sample Request**:
  ```json
  {
    "fullName": "John Doe Updated",
    "emailAddress": "updated.email@example.com"
  }
  ```
- **Response Body**: [CustomerDTO](#customerdto)

### 6. Delete Customer
- **Method**: `DELETE`
- **Path**: `/api/v1/customers/{id}`
- **Description**: Removes a customer record from the system.
- **Path Parameters**:
  - `id` (Integer): The ID of the customer to delete.
- **Response Body**: None
- **Status Code**: `204 No Content`

### 7. Validate Customer
- **Method**: `GET`
- **Path**: `/api/v1/customers/validate/{id}`
- **Description**: Checks if a customer exists and is valid.
- **Path Parameters**:
  - `id` (Integer): The ID of the customer to validate.
- **Response Body**: `Boolean` (true/false)

---

## Data Models

### CustomerDTO
| Field | Type | Description |
|---|---|---|
| `customerId` | Integer | Unique identifier for the customer |
| `fullName` | String | Full name of the customer |
| `emailAddress` | String | Email address of the customer |
