# Shipping Service API

This document provides details for the Shipping Service endpoints. These endpoints are used to track and manage shipments for customer orders.

**Base URL**: `/api/v1/shipments`

## Endpoints

### 1. Create a Shipment
- **Method**: `POST`
- **Path**: `/api/v1/shipments`
- **Description**: Creates a new shipment record for an order.
- **Request Body**: [ShipmentRequestDTO](#shipmentrequestdto)
- **Sample Request**:
  ```json
  {
    "storeId": 1,
    "customerId": 1,
    "deliveryAddress": "123 Main St, New York, NY",
    "shipmentStatus": "CREATED"
  }
  ```
- **Response Body**: [ShipmentResponseDTO](#shipmentresponsedto)
- **Status Code**: `201 Created`

### 2. Get Shipment by ID
- **Method**: `GET`
- **Path**: `/api/v1/shipments/{id}`
- **Description**: Retrieves detailed information for a specific shipment.
- **Path Parameters**:
  - `id` (Integer): The unique ID of the shipment.
- **Response Body**: [ShipmentResponseDTO](#shipmentresponsedto)

### 3. Update Shipment Status
- **Method**: `PATCH`
- **Path**: `/api/v1/shipments/{id}/status`
- **Description**: Updates the current status of a shipment.
- **Path Parameters**:
  - `id` (Integer): The ID of the shipment.
- **Request Body**: `String` (The new status, e.g., "SHIPPED", "DELIVERED")
- **Sample Request**: `"SHIPPED"`
- **Response Body**: [ShipmentResponseDTO](#shipmentresponsedto)

### 4. Get Shipments by Customer
- **Method**: `GET`
- **Path**: `/api/v1/shipments/customer/{customerId}`
- **Description**: Retrieves all shipments associated with a specific customer.
- **Path Parameters**:
  - `customerId` (Integer): The unique ID of the customer.
- **Response Body**: Array of [ShipmentResponseDTO](#shipmentresponsedto)

### 5. Get All Shipments
- **Method**: `GET`
- **Path**: `/api/v1/shipments`
- **Description**: Retrieves a list of all shipments in the system.
- **Response Body**: Array of [ShipmentResponseDTO](#shipmentresponsedto)

### 6. Get Shipments by Store
- **Method**: `GET`
- **Path**: `/api/v1/shipments/store/{storeId}`
- **Description**: Retrieves all shipments originating from a specific store.
- **Path Parameters**:
  - `storeId` (Integer): The unique ID of the store.
- **Response Body**: Array of [ShipmentResponseDTO](#shipmentresponsedto)

### 7. Delete Shipment
- **Method**: `DELETE`
- **Path**: `/api/v1/shipments/{id}`
- **Description**: Removes a shipment record from the system.
- **Path Parameters**:
  - `id` (Integer): The ID of the shipment to delete.
- **Response Body**: None
- **Status Code**: `204 No Content`

---

## Data Models

### ShipmentRequestDTO
| Field | Type | Description |
|---|---|---|
| `storeId` | Integer | ID of the originating store |
| `customerId` | Integer | ID of the recipient customer |
| `deliveryAddress` | String | Shipping destination address |
| `shipmentStatus` | String | Initial status of the shipment |

### ShipmentResponseDTO
| Field | Type | Description |
|---|---|---|
| `shipmentId` | Integer | Unique identifier for the shipment |
| `storeId` | Integer | ID of the originating store |
| `customerId` | Integer | ID of the recipient customer |
| `deliveryAddress` | String | Shipping destination address |
| `shipmentStatus` | String | Current status of the shipment |
