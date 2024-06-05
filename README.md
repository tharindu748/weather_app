# Weather App

A simple Android application that displays the current weather, location, and system time using data from the OpenWeatherMap API.

## Table of Contents
- [Features](#features)
- [Setup](#setup)
- [Usage](#usage)
- [Permissions](#permissions)
- [Libraries Used](#libraries-used)
- [Contributing](#contributing)
- [License](#license)

## Features
- Displays current latitude and longitude in decimal degrees format.
- Reverse geo-coded address of the current location.
- Displays the current system time.
- Fetches and displays weather information including temperature, humidity, and weather description.

## Setup

### Prerequisites
- Android Studio installed on your system.
- A device or emulator running at least Android 5.0 (Lollipop).
- Internet connection.

### Installation
1. **Clone the repository:**
    ```sh
    git clone https://github.com/yourusername/yourrepository.git
    ```
    Replace `yourusername` and `yourrepository` with your GitHub username and repository name.

2. **Open the project in Android Studio:**
    - Open Android Studio.
    - Click on `File` -> `Open` and navigate to the directory where you cloned the repository.
    - Select the project and click `OK`.

3. **Sync the project with Gradle:**
    - Click on `File` -> `Sync Project with Gradle Files`.

4. **Obtain an API key from OpenWeatherMap:**
    - Sign up at [OpenWeatherMap](https://openweathermap.org/) and obtain an API key.

5. **Add the API key to your project:**
    - Open `MainActivity.java` and replace the `API_KEY` value with your API key.

## Usage
1. **Run the application:**
    - Connect an Android device via USB or start an emulator.
    - Click the `Run` button in Android Studio or use `Shift+F10` to build and run the project.

2. **Grant necessary permissions:**
    - The app requires location permissions to fetch weather data based on the current location.
    - Ensure you grant these permissions when prompted.

## Permissions
The app requires the following permissions:
- `ACCESS_FINE_LOCATION` - To get the precise location of the user.
- `INTERNET` - To fetch weather data from the OpenWeatherMap API.

## Libraries Used
- [Android Async HTTP](https://loopj.com/android-async-http/) - For making asynchronous HTTP requests.
- [Gson](https://github.com/google/gson) - For parsing JSON responses.

## Contributing
Contributions are welcome! Please follow these steps to contribute:
1. Fork the repository.
2. Create a new branch: `git checkout -b feature-branch`.
3. Make your changes and commit them: `git commit -m 'Add some feature'`.
4. Push to the branch: `git push origin feature-branch`.
5. Open a pull request on GitHub.
