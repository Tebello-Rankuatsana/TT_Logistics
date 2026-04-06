# 🚛 TT Logistics Database System

[![Database](https://img.shields.io/badge/MySQL-Database-blue?logo=mysql)](https://www.mysql.com/)
[![JavaFX](https://img.shields.io/badge/JavaFX-Desktop-007396?logo=java)](https://openjfx.io/)
[![Status](https://img.shields.io/badge/Status-Completed-brightgreen)]()
[![License](https://img.shields.io/badge/License-Academic-red)]()

> A comprehensive fleet management database solution for a logistics company — featuring EER modeling, stored procedures, triggers, views, and a full-stack application.

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


