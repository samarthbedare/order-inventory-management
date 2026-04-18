# Store Service API

This document provides details for the Store Service endpoints. These endpoints are used to manage physical store locations and search for stores by address.

**Base URL**: `/api/v1/stores`

## Endpoints

### 1. Get All Stores
- **Method**: `GET`
- **Path**: `/api/v1/stores`
- **Description**: Retrieves a list of all physical store locations.
- **Response Body**: Array of [StoreDTO](#storedto)

### 2. Get Store by ID
- **Method**: `GET`
- **Path**: `/api/v1/stores/{id}`
- **Description**: Retrieves detailed information for a specific store.
- **Path Parameters**:
  - `id` (Integer): The unique ID of the store.
- **Response Body**: [StoreDTO](#storedto)

### 3. Search Stores by Address
- **Method**: `GET`
- **Path**: `/api/v1/stores/search/address`
- **Description**: Searches for stores based on a physical address query.
- **Query Parameters**:
  - `q` (String): The address or city name to search for.
- **Response Body**: Array of [StoreDTO](#storedto)
- **Sample Request**: `GET /api/v1/stores/search/address?q=New%20York`

### 4. Add New Store
- **Method**: `POST`
- **Path**: `/api/v1/stores`
- **Description**: Registers a new store location.
- **Request Body**: [StoreDTO](#storedto) (Omit `storeId`)
- **Sample Request**:
  ```json
  {
    "storeName": "Main St Store",
    "webAddress": "https://www.mainstore.com",
    "physicalAddress": "123 Main St, New York, NY",
    "latitude": 40.7128,
    "longitude": -74.0060
  }
  ```
- **Response Body**: [StoreDTO](#storedto)
- **Status Code**: `201 Created`

### 5. Update Store
- **Method**: `PUT`
- **Path**: `/api/v1/stores/{id}`
- **Description**: Updates an existing store's information.
- **Path Parameters**:
  - `id` (Integer): The ID of the store to update.
- **Request Body**: [StoreDTO](#storedto)
- **Sample Request**:
  ```json
  {
    "storeName": "Updated Store Name",
    "webAddress": "https://www.updatedstore.com"
  }
  ```
- **Response Body**: [StoreDTO](#storedto)

### 6. Delete Store
- **Method**: `DELETE`
- **Path**: `/api/v1/stores/{id}`
- **Description**: Removes a store location from the system.
- **Path Parameters**:
  - `id` (Integer): The ID of the store to delete.
- **Response Body**: None
- **Status Code**: `204 No Content`

---

## Data Models

### StoreDTO
| Field | Type | Description |
|---|---|---|
| `storeId` | Integer | Unique identifier for the store |
| `storeName` | String | Name of the store |
| `webAddress` | String | Website URL of the store |
| `physicalAddress` | String | Physical location address |
| `latitude` | Decimal | Geographic latitude coordinate |
| `longitude` | Decimal | Geographic longitude coordinate |
