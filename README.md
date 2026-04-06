# 🚛 TT Logistics Database System

[![Database](https://img.shields.io/badge/MySQL-Database-blue?logo=mysql)](https://www.mysql.com/)
[![JavaFX](https://img.shields.io/badge/JavaFX-Desktop-007396?logo=java)](https://openjfx.io/)
[![Status](https://img.shields.io/badge/Status-Completed-brightgreen)]()
[![License](https://img.shields.io/badge/License-Academic-red)]()

> A comprehensive fleet management database solution for a logistics company — featuring EER modeling, stored procedures, triggers, views, and a full-stack application.

```
 $ whoami
 > just ranks
```
---

## 📌 Project Overview

**TT Logistics** requires a robust database system to manage:
- 👥 Personnel (Full-Time Drivers, Contract Drivers, Fleet Managers)
- 🚚 Vehicles and Depots
- 📦 Client Deliveries
- 🔧 Vehicle Maintenance
- 🧑‍🔧 Driver Supervision & Assignments

This project delivers a fully functional relational database with advanced features like **specialization**, **many-to-many relationships**, **business logic automation**, and a **front-end application** (React / JavaFX).

---

## 🎯 Objectives Achieved

| Feature | Status |
|---------|--------|
| ✅ EER Diagram | Completed |
| ✅ Mapping & Normalization | Completed |
| ✅ Table Implementation | Completed |
| ✅ Stored Procedures (2) | Completed |
| ✅ User-Defined Functions (2) | Completed |
| ✅ Triggers (2) | Completed |
| ✅ Views (2) | Completed |
| ✅ User Management (6 users) | Completed |
| ✅ Application (Login + CRUD + Reports) | Completed |

---

## 🧱 Database Schema Highlights

### 🔹 Specialization (Superclass/Subclass)
- **Person** → FullTimeDriver, ContractDriver, FleetManager
- A person can have **multiple roles** simultaneously.

### 🔹 Many-to-Many Relationships
- `Delivery` ↔ `Driver` via `Driver_Assignment` (with role & hours_worked)
- `Vehicle` ↔ `Maintenance` via `Vehicle_Maintenance`

### 🔹 Key Tables
- Person, Vehicle, Depot, Client, Delivery,
- FullTimeDriver, ContractDriver, FleetManager,
- Driver_Assignment, Vehicle_Maintenance, Delivery_Log

---

## ⚙️ Stored Procedures

### 1. `assign_driver_to_delivery`
- Accepts: `person_id`, `delivery_id`, `role`, `hours_worked`
- Prevents duplicate assignments
- Validates `hours_worked > 0`

### 2. `record_vehicle_maintenance`
- Accepts: `vehicle_id`, `maintenance_date`, `maintenance_type`, `cost`
- Prevents negative maintenance cost

---

## 📊 User-Defined Functions

### 1. `total_deliveries_by_driver(person_id)`
Returns total deliveries handled by a specific driver.

### 2. `vehicle_total_maintenance(vehicle_id)`
Returns total maintenance cost for a vehicle (or 0 if none).

---

## ⚡ Triggers

| Trigger | Table | Event | Action |
|---------|-------|-------|--------|
| `prevent_negative_salary` | `FullTimeDriver` | BEFORE INSERT/UPDATE | Raises error if salary ≤ 0 |
| `log_new_delivery` | `Delivery` | AFTER INSERT | Inserts into `Delivery_Log` with formatted description |

---

## 👁️ Views

### `Active_Deliveries_View`
Shows only deliveries where status ≠ 'Completed' — includes client name & vehicle registration.

### `Driver_Workload_View`
Summarizes each driver’s total deliveries and hours worked.

---

## 👥 User Management

| User Type | Credentials | Privileges |
|-----------|-------------|------------|
| 2 Users | `viewer1` / `viewer2` | INSERT + SELECT |
| 2 Users | `admin1` / `admin2` | ALL PRIVILEGES |
| 2 Users | `inserter1` / `inserter2` | INSERT ONLY |

All users can log in via the application.

---

## 🖥️ Application Features (JavaFX)

 ```
📱 Login Screen
└── Authenticates against database users

🏠 Main Menu
├── 🚗 Vehicle Management
├── 👤 Driver Management
├── 🧭 Trip (Delivery) Management
└── 📈 Reports Screen
```

## 🚀 Getting Started

### Prerequisites
- MySQL Server 8.0+
- Java 11+ (for JavaFX)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/tt-logistics-database.git
cd tt-logistics-database
```

```
 $ whoami
 > just ranks
```
