ISS Tracker – Real-Time International Space Station Tracking
A Java-based desktop application that tracks the real-time location of the International Space Station (ISS) using a public API and visualizes its position on an interactive world map.
The project is built with clean layered (N-tier) architecture, supports both console and JavaFX UI, and follows industry-standard Maven structure.


Features

Real-time ISS latitude & longitude tracking
Interactive world map using JavaFX WebView + Leaflet.js
Clean Layered (N-Tier) Architecture
Periodic updates using scheduled background tasks
Maven-based build and dependency management
Easily extensible for future features (history, speed, orbit path)


Architecture Overview

This project follows a Layered (N-Tier) Architecture to keep responsibilities cleanly separated.


UI Layer        → JavaFX / Console Apps
Service Layer   → Business logic
Network Layer   → API communication
Model Layer     → Data models
