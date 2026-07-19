# 🚗 Parking Spot Optimizer

A comprehensive, Java-based Parking Management System built to simulate real-world vehicle routing, billing, and administrative tracking. This system features a custom two-pass allocation algorithm, persistent data storage, and an interactive Swing GUI with a dark-mode visual map.

This project was developed as a comprehensive Object-Oriented Programming (OOP) application, demonstrating advanced Java concepts and desktop UI design.

## ✨ Core Features

* **Smart Two-Pass Routing Algorithm:** Automatically prioritizes Electric Vehicles (EVs) for charging spots, with graceful fallback to standard spots if chargers are full.
* **Interactive Graphical User Interface (GUI):** A fully customized Java Swing interface featuring a dark-mode theme and a live, color-coded "Spot Map" to monitor lot capacity in real-time.
* **Persistent Storage:** Utilizes Java Object Serialization (`Parking_Data.txt`) to automatically save and load the state of the parking lot between sessions.
* **Admin Dashboard:** A secured dashboard for administrators to track total system revenue, view available capacity, and force-checkout vehicles.
* **Dynamic Billing Structure:** Calculates checkout fees based on vehicle size (Motorcycle, Compact Car, Large SUV) and exact time parked using `java.time.LocalDateTime`.
* **Dual Interfaces:** Fully functional through both the visual GUI (`ParkingLotGUI.java`) and a classic Command-Line Interface (`Main.java`).

## 🛠️ Tech Stack & OOP Concepts

* **Language:** Java 25
* **UI Framework:** Java Swing & AWT
* **Core OOP Principles Applied:**
  * **Inheritance & Polymorphism:** Abstract `Vehicle` and `ParkingSpot` classes extended by specific types (e.g., `SUV`, `MotorcycleSpot`).
  * **Interfaces:** `Billable` interface implemented for standardized fee calculation.
  * **Composition:** The `ParkingLot` object acts as a manager that owns and routes an `ArrayList` of `ParkingSpot` objects.
  * **File I/O:** `Serializable` implementation for robust local data storage.

## 🚀 How to Run the Project

### Prerequisites
Make sure you have the Java Development Kit (JDK) installed on your system.

### 1. Clone the Repository
```bash
git clone [https://github.com/YourUsername/Parking-Spot-Optimizer.git](https://github.com/YourUsername/Parking-Spot-Optimizer.git)
cd Parking-Spot-Optimizer/src
```

### 2. Compile the Code
Compile all the Java files in the source directory:
```bash
javac *.java
```

### 3. Launch the Application
You can choose to run either the Graphical User Interface or the Terminal Interface:

**To run the visual GUI (Recommended):**
```bash
java ParkingLotGUI
```

**To run the Command-Line Interface (CLI):**
```bash
java Main
```

## 📸 System Usage

1. **Park a Vehicle:** Navigate to the "Park Vehicle" tab, enter a license plate, select the vehicle size and power source. The system will automatically route it to the optimal spot.
2. **Checkout:** Navigate to the "Checkout" tab and enter the license plate. The system will free the spot and print a detailed receipt to the console.
3. **Admin Access:** Use the password `123` to access the Admin Dashboard to view live revenue and override spot occupancies.

## 👤 Author
**Muhammad Awais** 
Software Engineering Student
