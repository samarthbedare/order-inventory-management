# Product Service API

This document provides details for the Product Service endpoints. These endpoints are used to manage the product catalog, including adding, updating, and filtering products.

**Base URL**: `/api/v1/products`

## Endpoints

### 1. Get All Products (with filters)
- **Method**: `GET`
- **Path**: `/api/v1/products`
- **Description**: Retrieves a list of products, optionally filtered by name, brand, colour, or size.
- **Query Parameters**:
  - `name` (String, optional): Filter by product name.
  - `brand` (String, optional): Filter by brand name.
  - `colour` (String, optional): Filter by product colour.
  - `size` (String, optional): Filter by product size.
- **Response Body**: Array of [ProductDTO](#productdto)
- **Sample Request**: `GET /api/v1/products?brand=Nike&colour=Red`

### 2. Get Product by ID
- **Method**: `GET`
- **Path**: `/api/v1/products/{id}`
- **Description**: Retrieves detailed information for a specific product.
- **Path Parameters**:
  - `id` (Integer): The unique ID of the product.
- **Response Body**: [ProductDTO](#productdto)

### 3. Add New Product
- **Method**: `POST`
- **Path**: `/api/v1/products`
- **Description**: Adds a new product to the catalog.
- **Request Body**: [ProductDTO](#productdto) (Omit `productId`)
- **Sample Request**:
  ```json
  {
    "productName": "T-Shirt",
    "brand": "Nike",
    "colour": "Red",
    "size": "M",
    "unitPrice": 19.99,
    "rating": 5
  }
  ```
- **Response Body**: [ProductDTO](#productdto)
- **Status Code**: `201 Created`

### 4. Update Product
- **Method**: `PATCH`
- **Path**: `/api/v1/products/{id}`
- **Description**: Partially updates a product's price or rating.
- **Path Parameters**:
  - `id` (Integer): The ID of the product to update.
- **Request Body**: [ProductUpdateDTO](#productupdatedto)
- **Sample Request**:
  ```json
  {
    "unitPrice": 24.99,
    "rating": 4
  }
  ```
- **Response Body**: [ProductDTO](#productdto)

### 5. Delete Product
- **Method**: `DELETE`
- **Path**: `/api/v1/products/{id}`
- **Description**: Removes a product from the catalog.
- **Path Parameters**:
  - `id` (Integer): The ID of the product to delete.
- **Response Body**: None
- **Status Code**: `204 No Content`

---

## Data Models

### ProductDTO
| Field | Type | Description |
|---|---|---|
| `productId` | Integer | Unique identifier for the product |
| `productName` | String | Name of the product |
| `unitPrice` | Decimal | price per unit |
| `colour` | String | Colour of the product |
| `brand` | String | Brand of the product |
| `size` | String | Size of the product (e.g., S, M, L, XL) |
| `rating` | Integer | Product rating (1-5) |

### ProductUpdateDTO
| Field | Type | Description |
|---|---|---|
| `unitPrice` | Decimal | Updated price per unit (optional) |
| `rating` | Integer | Updated product rating (optional) |
