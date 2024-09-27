# Weather App

A simple weather application that fetches and displays weather data for a specified city. Built using modern Android technologies and best practices, this app utilizes dependency injection, networking, and location services.

## Table of Contents
- [Technologies Used](#technologies-used)
- [Features](#features)
- [Getting Started](#getting-started)
- [Architecture](#architecture)
- [License](#license)

## Technologies Used

### 1. **Kotlin**
   - **Description**: Kotlin is the primary programming language used for Android development. It is concise, expressive, and designed to interoperate fully with Java, which helps in maintaining and expanding existing applications.
   - **Usage**: Used throughout the application to define data classes, interfaces, and the overall application logic.

### 2. **Android Jetpack**
   - **Description**: A set of libraries, tools, and architectural guidance for Android app development. Jetpack simplifies complex tasks and helps developers adhere to best practices.
   - **Usage**: Components like ViewModel, LiveData, and Navigation are used for managing UI-related data and navigation between different screens.

### 3. **Hilt**
   - **Description**: Hilt is a dependency injection library for Android that reduces boilerplate code and helps manage dependencies more effectively.
   - **Usage**: Used to provide dependencies like `AuthService` and `WeatherRepo`, making the application modular and easier to test.

### 4. **Retrofit**
   - **Description**: A type-safe HTTP client for Android and Java, Retrofit simplifies the process of making network requests and handling API responses.
   - **Usage**: Used to interact with the weather API, retrieving weather data based on the city name provided by the user.

### 5. **OkHttp**
   - **Description**: OkHttp is an efficient HTTP client that handles network requests and responses. It supports features like connection pooling, GZIP compression, and caching.
   - **Usage**: Integrated with Retrofit to manage HTTP requests and responses, including logging of network calls for debugging.

### 6. **Coroutines and Flow**
   - **Description**: Kotlin Coroutines provide a way to write asynchronous code, allowing tasks to run concurrently without blocking the main thread. Flow is a cold asynchronous data stream that can emit multiple values sequentially.
   - **Usage**: Used in the repository layer to fetch weather data asynchronously, allowing the UI to reactively update as new data arrives.

### 7. **Fused Location Provider**
   - **Description**: A part of Google Play Services that provides location services for Android devices. It simplifies the process of obtaining location data with various sources like GPS, Wi-Fi, and cell networks.
   - **Usage**: Used to get the user's current location to suggest the relevant weather data without requiring the user to input the city name manually.

### 8. **Geocoder**
   - **Description**: A class that provides a way to convert latitude and longitude coordinates into a human-readable address.
   - **Usage**: Used to determine the city name based on the user's current location.

### 9. **Compose**
   - **Description**: Jetpack Compose is a modern toolkit for building native Android UI. It simplifies UI development with a declarative approach.
   - **Usage**: Used for creating the app's user interface, providing a more intuitive way to build and manage UI components.

## Features
- Fetch and display current weather data for a specified city.
- Use of GPS to get the user's current location and suggest the relevant weather data.
- Clean architecture with well-structured code using dependency injection.
- Responsive UI using Jetpack Compose.

## Getting Started

### Prerequisites
- Android Studio
- Kotlin SDK
- An API key for accessing the weather API
