# Order Service API

This document provides details for the Order Service endpoints. These endpoints are used to manage customer orders, including creation, status updates, and shipment linking.

**Base URL**: `/api/v1/orders`

## Endpoints

### 1. Create New Order
- **Method**: `POST`
- **Path**: `/api/v1/orders`
- **Description**: Creates one or more order records for a customer at a specific store.
- **Request Body**: [OrderRequest](#orderrequest)
- **Sample Request**:
  ```json
  {
    "customerId": 1,
    "storeId": 1,
    "deliveryAddress": "123 Main St, New York, NY",
    "items": [
      {
        "productId": 1,
        "quantity": 2,
        "unitPrice": 19.99
      }
    ]
  }
  ```
- **Response Body**: Array of [OrderResponse](#orderresponse)
- **Status Code**: `201 Created`

### 2. Get All Orders
- **Method**: `GET`
- **Path**: `/api/v1/orders`
- **Description**: Retrieves a list of all orders in the system.
- **Response Body**: Array of [OrderResponse](#orderresponse)

### 3. Get Order by ID
- **Method**: `GET`
- **Path**: `/api/v1/orders/{id}`
- **Description**: Retrieves detailed information for a specific order.
- **Path Parameters**:
  - `id` (Integer): The unique ID of the order.
- **Response Body**: [OrderResponse](#orderresponse)

### 4. Get Orders by Store
- **Method**: `GET`
- **Path**: `/api/v1/orders/store/{storeId}`
- **Description**: Retrieves all orders associated with a specific store.
- **Path Parameters**:
  - `storeId` (Integer): The unique ID of the store.
- **Response Body**: Array of [OrderResponse](#orderresponse)

### 5. Get Orders by Customer
- **Method**: `GET`
- **Path**: `/api/v1/orders/customer/{customerId}`
- **Description**: Retrieves all orders placed by a specific customer.
- **Path Parameters**:
  - `customerId` (Integer): The unique ID of the customer.
- **Response Body**: Array of [OrderResponse](#orderresponse)

### 6. Update Order Status
- **Method**: `PATCH`
- **Path**: `/api/v1/orders/{id}/status`
- **Description**: Updates the status of an existing order.
- **Path Parameters**:
  - `id` (Integer): The ID of the order to update.
- **Request Body**: [OrderStatusUpdateDTO](#orderstatusupdatedto)
- **Sample Request**:
  ```json
  {
    "orderStatus": "SHIPPED"
  }
  ```
- **Response Body**: [OrderResponse](#orderresponse)

### 7. Link Shipment to Order
- **Method**: `PATCH`
- **Path**: `/api/v1/orders/{id}/shipment`
- **Description**: Links a shipment tracking ID to an order.
- **Path Parameters**:
  - `id` (Integer): The ID of the order.
- **Request Body**: [OrderShipmentUpdateDTO](#ordershipmentupdatedto)
- **Sample Request**:
  ```json
  {
    "shipmentId": 101
  }
  ```
- **Response Body**: [OrderResponse](#orderresponse)

### 8. Delete Order
- **Method**: `DELETE`
- **Path**: `/api/v1/orders/{id}`
- **Description**: Cancels and removes an order from the system.
- **Path Parameters**:
  - `id` (Integer): The ID of the order to delete.
- **Response Body**: None
- **Status Code**: `204 No Content`

---

## Data Models

### OrderRequest
| Field | Type | Description |
|---|---|---|
| `customerId` | Integer | ID of the customer placing the order |
| `storeId` | Integer | ID of the store where the order is placed |
| `deliveryAddress` | String | Shipping address for the order |
| `items` | Array[[OrderItemRequest](#orderitemrequest)] | List of items included in the order |

### OrderItemRequest
| Field | Type | Description |
|---|---|---|
| `productId` | Integer | ID of the product being ordered |
| `unitPrice` | Decimal | Price per unit of the product |
| `quantity` | Integer | Quantity of the product |

### OrderResponse
| Field | Type | Description |
|---|---|---|
| `orderId` | Integer | Unique identifier for the order |
| `orderTms` | DateTime | Timestamp when the order was placed |
| `customerId` | Integer | ID of the customer |
| `orderStatus` | String | Current status (e.g., CREATED, SHIPPED, DELIVERED) |
| `storeId` | Integer | ID of the store |
| `item` | [ItemDetail](#itemdetail) | Detailed information about the order item |

### ItemDetail
| Field | Type | Description |
|---|---|---|
| `orderId` | Integer | Reference to the order ID |
| `lineItemId` | Integer | Unique ID for this line item |
| `productId` | Integer | ID of the product |
| `unitPrice` | Decimal | Price at time of order |
| `quantity` | Integer | Quantity ordered |
| `shipmentId` | Integer | Linked shipment ID (if any) |

### OrderStatusUpdateDTO
| Field | Type | Description |
|---|---|---|
| `orderStatus` | String | The new status to set for the order |

### OrderShipmentUpdateDTO
| Field | Type | Description |
|---|---|---|
| `shipmentId` | Integer | The shipment ID to link to the order |
