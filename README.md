# Order-Inventory Management System

A comprehensive, modularized Spring Boot application designed for professional E-commerce inventory and order orchestration. The system provides a centralized backbone for managing multi-store inventories, complex order lifecycles, and secure administrative access.

## 🏗️ System Architecture

The application follows a **Monolithic Modular** design, ensuring that while the codebase is unified, each business concern is decoupled into its own service layer:

- **Security Module**: Handles stateless authentication using JWT and bcrypt-encrypted administrative credentials.
- **Customer Service**: Manages customer profiles, address books, and identity validation.
- **Product Service**: A robust catalog management system with filtering by brand, category, and physical attributes.
- **Store & Inventory Service**: Orchestrates stock levels across multiple physical locations, supporting atomic stock reduction during checkout.
- **Order Service**: Coordinates the transition of items from inventory to customer ownership, managing various statuses (OPEN, PAID, SHIPPED).
- **Shipment Service**: Tracks the logistics component of orders, providing real-time status updates from creation to delivery.

---

## 🛡️ Security Model

The system utilizes a **High-Security Stateless Configuration**:
- **Authentication**: JWT (JSON Web Tokens) with a 256-bit HS256 signature.
- **Access Control**: Role-based access (ADMIN) enforced via a standard Spring Security Filter Chain.
- **Session Management**: Completely stateless; no server-side sessions are maintained, facilitating horizontal scalability.
- **Token Policy**: Short-lived access tokens (5-minute expiration) to minimize the attack window.

---

## 🛣️ API Catalog (by Service)

### 1. security
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/v1/auth/signup` | Register a new Admin User |
| `POST` | `/api/v1/auth/login` | Login to receive JWT Token |

### 2. customerservice
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/customers` | List all registered customers |
| `GET` | `/api/v1/customers/{id}` | Get specific customer by ID |
| `GET` | `/api/v1/customers/email/{email}` | Find customer by email address |
| `POST` | `/api/v1/customers` | Create a new customer profile |
| `PUT` | `/api/v1/customers/{id}` | Update existing customer details |
| `DELETE` | `/api/v1/customers/{id}` | Remove a customer record |
| `GET` | `/api/v1/customers/validate/{id}` | Check if a customer exists |

### 3. productservice
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/products` | List all products (supports filters: `name`, `brand`, `colour`, `size`) |
| `GET` | `/api/v1/products/{id}` | Get detailed product specifications |
| `POST` | `/api/v1/products` | Add a new product to the catalog |
| `PATCH` | `/api/v1/products/{id}` | Update product attributes |
| `DELETE` | `/api/v1/products/{id}` | Remove a product from the catalog |

### 4. storeservice
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/stores` | List all physical store locations |
| `GET` | `/api/v1/stores/{id}` | Get specific store details |
| `GET` | `/api/v1/stores/search/address` | Search stores by physical address |
| `POST` | `/api/v1/stores` | Create a new store location |
| `PUT` | `/api/v1/stores/{id}` | Update store information |
| `DELETE` | `/api/v1/stores/{id}` | Remove a store |
| `GET` | `/api/v1/inventory/product/{pid}` | Get stock availability across all stores |
| `GET` | `/api/v1/inventory/store/{sid}` | Get all product stock within a specific store |
| `POST` | `/api/v1/inventory` | Initialize or update a stock record |
| `PATCH` | `/api/v1/inventory/add` | Replenish stock quantity |
| `PATCH` | `/api/v1/inventory/reduce` | Subtract stock quantity |
| `DELETE` | `/api/v1/inventory/store/{sid}/product/{pid}` | Remove a product from a store's inventory |

### 5. orderservice
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/orders` | List all system orders |
| `GET` | `/api/v1/orders/{id}` | Get specific order and line items |
| `GET` | `/api/v1/orders/customer/{cid}` | Retrieve order history for a customer |
| `GET` | `/api/v1/orders/store/{sid}` | Retrieve orders processed by a specific store |
| `POST` | `/api/v1/orders` | Place a new order (Atomic Checkout) |
| `PATCH` | `/api/v1/orders/{id}/status` | Update order status |
| `PATCH` | `/api/v1/orders/{id}/shipment` | Link a shipment record to an order |
| `DELETE` | `/api/v1/orders/{id}` | Cancel/Delete an order record |

### 6. shippingservice
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/v1/shipments` | List all active shipments |
| `GET` | `/api/v1/shipments/{id}` | Get shipment tracking and status |
| `GET` | `/api/v1/shipments/customer/{cid}` | Get all shipments for a specific customer |
| `GET` | `/api/v1/shipments/store/{sid}` | Get all shipments dispatched from a specific store |
| `POST` | `/api/v1/shipments` | Manually initialize a shipment |
| `PATCH` | `/api/v1/shipments/{id}/status` | Update delivery tracking status |
| `DELETE` | `/api/v1/shipments/{id}` | Remove a shipment record |

---

## 🧪 Quality Assurance

The application is validated by a comprehensive **32-test JUnit suite**:
- **Unit Tests**: Ensure individual controller logic is sound.
- **Integration Tests**: Verify service-to-service communication.
- **System Flow Tests**: Validate a complete "Signup-to-Shipment" lifecycle under full security constraints.

---

## 👥 Team Contributions

The development of this system was a collaborative effort with specific ownership of core services:

- **Product Service**: Narayani Gupta
- **Store & Inventory Service**: Priya Chavan
- **Customer Service**: Vishal Gavali
- **Order Service**: Samarth Bedare
- **Shipping Service**: Rohan Kumbhar

All other core infrastructure (Security, Global Exception Handling, API Gateway patterns, and Cross-Cutting Concerns) were developed collaboratively by the **Entire Team**.
