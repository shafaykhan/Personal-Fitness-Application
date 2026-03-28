# Personal Fitness Application

A comprehensive fitness and health tracking application built with Spring Boot and a responsive vanilla HTML/JS/Bootstrap frontend.

## Features

* **User Management & Authentication**: Secure login and registration system. Role-based access control (Admin/User).
* **Dashboard**: Overview of user's fitness journey and summary of records.
* **Workout Tracking**: Log daily workouts including exercise name, type, duration, intensity, and calories burned.
* **Diet Records**: Keep track of food intake, categorizing by meal type, and tracking macronutrients (Calories, Protein, Carbs, Fat).
* **Health Records**: Monitor vital health metrics such as weight, sleep hours, water intake, BMI, heart rate, and step count.
* **Goal Setting**: Set and track personalized fitness goals with target values and deadlines.
* **AI Recommendations**: Get personalized fitness and health recommendations based on logged data.
* **Master Data Management**: Admins can manage lookup values (e.g., UOMs, Workout Types, Meal Types) via the Lookup management UI.

## Tech Stack

### Backend
* **Java 17+**
* **Spring Boot** (Web, Data JPA, Security)
* **Database**: Relational Database (e.g., MySQL / PostgreSQL / H2)
* **ModelMapper**: For DTO mapping
* **Maven / Gradle**: Build tool

### Frontend
* **HTML5, CSS3, JavaScript (ES6+)**
* **Bootstrap 5**: For responsive layout and UI components
* **AdminLTE 3 / 4**: For dashboard layout and styling
* **Bootstrap Icons**: For scalable vector icons
* **Fetch API**: For RESTful communication with the backend

## Getting Started

### Prerequisites
* Java Development Kit (JDK) 17 or higher installed.
* An IDE like IntelliJ IDEA, Eclipse, or VS Code.
* Node.js and npm (optional, if using frontend build tools in the future).

### Running the Application

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd "Personal Fitness Application"
   ```

2. **Configure the Database**:
   Update your `src/main/resources/application.properties` or `application.yml` file with your database credentials.

3. **Build and Run**:
   You can run the application using your IDE or via command line using Maven/Gradle wrapper:
   
   *Using Maven:*
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the Application**:
   Once the application is running, open your web browser and navigate to:
   ```
   http://localhost:9000/fitness-app/page/login.html
   ```
   *(Note: Adjust the port and context path if configured differently in `application.properties`)*

## Project Structure

* `src/main/java/.../fitness_application`: Contains all Java backend code organized by domain (user, workout, diet_record, etc.).
* `src/main/resources/static`: Contains the frontend assets.
  * `/css`: Custom stylesheets and AdminLTE styles.
  * `/js`: JavaScript logic handling API calls, form validation, and UI interactivity.
  * `/page`: HTML pages for different modules (Dashboard, Workout, Diet, etc.).
  * `/img`: Image assets.

## Contributing
Contributions are welcome. Please open an issue first to discuss what you would like to change before submitting a pull request.

## License
[MIT License](LICENSE)
