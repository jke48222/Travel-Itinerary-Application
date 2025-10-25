# Travel Itinerary API App

A JavaFX application that generates personalized travel itineraries using multiple RESTful JSON APIs.  
Users select a country, and the app automatically recommends a city, retrieves nearby hotels, attractions, activities, and restaurants, and displays them with details such as names, addresses, and ratings.

---

## Features

- Dynamic itinerary creation using **country**, **city**, and **location** APIs  
- Real-time integration with **Google Maps** services  
- Automatic selection of a popular city within a user-chosen country  
- Displays hotels, attractions, activities, and restaurants with:
  - Names  
  - Addresses  
  - Ratings  
  - Location photos  
- Options to regenerate the itinerary or choose a new country

---

## Technologies Used

| Component | Purpose |
|------------|----------|
| **Java 17 + JavaFX** | Core GUI framework |
| **HTTP Client (Java 11+)** | Fetches API data |
| **Gson** | Parses and formats JSON responses |
| **Maven** | Dependency and build management |
| **RESTful APIs** | Integrates country, city, and location services |

---

## Setup and Run

### Prerequisites
- Java 17 or later  
- Maven 3.8+  
- Internet connection for API calls  

### Installation
```bash
git clone https://github.com/jke48222/cs1302-api-app.git
cd cs1302-api-app
mvn clean compile
```

### Run
```bash
mvn javafx:run
```
or use the helper script:
```bash
./run.sh
```

---

## Project Structure

```
src/
 └── main/java/cs1302/api/
     ├── ApiApp.java               # Main application class (JavaFX)
     ├── ApiDriver.java            # Launches the JavaFX app
     ├── CityResponse.java         # Model for parsed city data (JSON)
     ├── CountryResponse.java      # Model for parsed country data (JSON)
     ├── LocationPhotos.java       # Model for parsed location photo data
     ├── LocationResponse.java     # Model for parsed location details (JSON)
     └── LocationResults.java      # Model for grouped location results
pom.xml                            # Maven build configuration
README.md                          # Project documentation
```

---

## How It Works

1. The user selects a **country** from a dropdown.  
2. The app queries a **country API** to get its ISO code.  
3. A **city API** finds a popular city within that country.  
4. The **location API** retrieves hotels, restaurants, and attractions near that city.  
5. All results are displayed with photo previews and ratings.

---

## 🧑‍💻 Author

**Jalen Edusei**  
University of Georgia  
GitHub: [@jke48222](https://github.com/jke48222)

---

## 📄 License

This project is licensed under the **MIT License** — see the `LICENSE` file for details.

---

> Originally developed as part of a university JavaFX project; this version has been cleaned and refactored for public demonstration.
# Travel Itinerary API App

A JavaFX application that generates personalized travel itineraries using multiple RESTful JSON APIs.  
Users select a country, and the app automatically recommends a city, retrieves nearby hotels, attractions, activities, and restaurants, and displays them with details such as names, addresses, and ratings.

---

## Features

- Dynamic itinerary creation using **country**, **city**, and **location** APIs  
- Real-time integration with **Google Maps** services  
- Automatic selection of a popular city within a user-chosen country  
- Displays hotels, attractions, activities, and restaurants with:
  - Names  
  - Addresses  
  - Ratings  
  - Location photos  
- Options to regenerate the itinerary or choose a new country

---

## Technologies Used

| Component | Purpose |
|------------|----------|
| **Java 17 + JavaFX** | Core GUI framework |
| **HTTP Client (Java 11+)** | Fetches API data |
| **Gson** | Parses and formats JSON responses |
| **Maven** | Dependency and build management |
| **RESTful APIs** | Integrates country, city, and location services |

---

## Setup and Run

### Prerequisites
- Java 17 or later  
- Maven 3.8+  
- Internet connection for API calls  

### Installation
```bash
git clone https://github.com/jke48222/cs1302-api-app.git
cd cs1302-api-app
mvn clean compile
```

### Run
```bash
mvn javafx:run
```
or use the helper script:
```bash
./run.sh
```

---

## Project Structure

```
src/
 └── main/java/cs1302/api/
     ├── ApiApp.java               # Main application class (JavaFX)
     ├── ApiDriver.java            # Launches the JavaFX app
     ├── CityResponse.java         # Model for parsed City data JSON
     ├── CountryResponse.java      # Model for parsed Country data JSON
     ├── LocationPhotos.java       # Model for parsed Location Photos data JSON
     ├── LocationResponse.java     # Model for parsed Location JSON
     └── LocationResults.java      # Model for parsed Location Results JSON
pom.xml                            # Maven build configuration
README.md                          # Project overview
```

---

## How It Works

1. The user selects a **country** from a dropdown.  
2. The app queries a **country API** to get its ISO code.  
3. A **city API** finds a popular city within that country.  
4. The **location API** retrieves hotels, restaurants, and attractions near that city.  
5. All results are displayed with photo previews and ratings.

---

## 🧑‍💻 Author

**Jalen Edusei**  
University of Georgia  
GitHub: [@jke48222](https://github.com/jke48222)

---

## 📄 License

This project is licensed under the **MIT License** — see the `LICENSE` file for details.

---

> Originally developed as part of a university JavaFX project; this version has been cleaned and refactored for public demonstration.
