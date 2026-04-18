# Inventory Service API

This document provides details for the Inventory Service endpoints. These endpoints are used to manage product stock levels across different store locations.

**Base URL**: `/api/v1/inventory`

## Endpoints

### 1. Get Stock by Product
- **Method**: `GET`
- **Path**: `/api/v1/inventory/product/{productId}`
- **Description**: Retrieves stock levels for a specific product across all stores.
- **Path Parameters**:
  - `productId` (Integer): The unique ID of the product.
- **Response Body**: Array of [InventoryDTO](#inventorydto)

### 2. Get Products by Store
- **Method**: `GET`
- **Path**: `/api/v1/inventory/store/{storeId}`
- **Description**: Retrieves all product stock levels for a specific store.
- **Path Parameters**:
  - `storeId` (Integer): The unique ID of the store.
- **Response Body**: Array of [InventoryDTO](#inventorydto)

### 3. Create Inventory Record
- **Method**: `POST`
- **Path**: `/api/v1/inventory`
- **Description**: Creates a new inventory record (initializes stock for a product at a store).
- **Request Body**: [InventoryDTO](#inventorydto)
- **Sample Request**:
  ```json
  {
    "storeId": 1,
    "productId": 1,
    "quantity": 100
  }
  ```
- **Response Body**: [InventoryDTO](#inventorydto)
- **Status Code**: `201 Created`

### 4. Reduce Stock
- **Method**: `PATCH`
- **Path**: `/api/v1/inventory/reduce`
- **Description**: Reduces the stock quantity for a product at a store (usually after an order).
- **Request Body**: [InventoryDTO](#inventorydto)
- **Sample Request**:
  ```json
  {
    "storeId": 1,
    "productId": 1,
    "quantity": 10
  }
  ```
- **Response Body**: [InventoryDTO](#inventorydto)

### 5. Add Stock
- **Method**: `PATCH`
- **Path**: `/api/v1/inventory/add`
- **Description**: Increases the stock quantity for a product at a store (restock).
- **Request Body**: [InventoryDTO](#inventorydto)
- **Sample Request**:
  ```json
  {
    "storeId": 1,
    "productId": 1,
    "quantity": 50
  }
  ```
- **Response Body**: [InventoryDTO](#inventorydto)

### 6. Delete Inventory Record
- **Method**: `DELETE`
- **Path**: `/api/v1/inventory/store/{storeId}/product/{productId}`
- **Description**: Removes an inventory record from the system.
- **Path Parameters**:
  - `storeId` (Integer): The ID of the store.
  - `productId` (Integer): The ID of the product.
- **Response Body**: None
- **Status Code**: `204 No Content`

---

## Data Models

### InventoryDTO
| Field | Type | Description |
|---|---|---|
| `inventoryId` | Integer | Unique identifier for the inventory record (Internal) |
| `storeId` | Integer | ID of the store where the stock is located |
| `productId` | Integer | ID of the product in stock |
| `quantity` | Integer | Current stock level |
