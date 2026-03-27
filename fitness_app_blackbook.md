# RESEARCH REPORT ON PERSONAL FITNESS APPLICATION

**Radhai Mahavidyalaya, Chhatrapati Sambhajinagar**  
*ISO Certified and U.G.C. 2(f) 12(B) Recognized*  
*NAAC Accredited with 'B' Grade*

---

## 1. TITLE PAGE

**Research Report on**  
**"PERSONAL FITNESS APPLICATION"**

**In Partial fulfillment of Master of Computer Science**  
**Year 2024-25**

**Submitted by**  
**[Your Full Name]**  
**M.Sc. (CS) III-Sem**

**Teacher Co-Ordinator**  
**Kanchan R. Darbe**

---

## 2. CERTIFICATE

**CERTIFICATE**

This is to certify that **[Your Full Name]** of M.Sc(CS) III-Semester, has successfully completed his/her **Research Report** on **"PERSONAL FITNESS APPLICATION"**. And has submitted a satisfactory report as per the requirement of **Dr. Babasaheb Ambedkar Marathwada University, Aurangabad** in partial fulfillment of Master of Computer Science courses.

**For the academic year 2024-2025**

| **Name of Teacher** | **Dr. N.S. Sonawane** | **Dr. S.S. Lomte** |
| :---: | :---: | :---: |
| **Kanchan R. Darbe** | **HOD** | **Principal** |

---

## 3. ACKNOWLEDGMENT

**ACKNOWLEDGMENT**

On the very outset of this report, I would like to extend my sincere & heartfelt obligation towards all the personages who have helped me in this endeavor. Without their active guidance, help, cooperation & encouragement, I would not have made headway in the project.

I am grateful to my project Guide **Asst. Prof. Kanchan R. Darbe** for the guidance, inspiration and constructive suggestions that helped me in the preparation of this project.

I express my thanks to the director of **Radhai Mahavidyalaya** for extending his support.

I also acknowledge with a deep sense of reverence, my gratitude towards my parents and member of my family, who has always supported me morally as well as economically. At last, but not least gratitude goes to all of my friends who directly or indirectly helped me to complete this project report. Any omission in this brief acknowledgement does not mean lack of gratitude.

**Thanking You**  
**[Your Full Name]**

---

## 4. DECLARATION

**DECLARATION**

I hereby declare that this Project report of **"PERSONAL FITNESS APPLICATION"** submitted to **Radhai Mahavidyalaya, Chhatrapati Sambhajinagar** for the partial fulfillment of Bachelor of Computer Science course under the guidance of **Asst. Prof. Kanchan R. Darbe**. The information and data given in the report is authentic to the best of my knowledge.

This research report is not submitted to any other university or institution for the award of any degree, diploma or fellowship or published any time before.

**Name of Student:** [Your Full Name]

---

## CHAPTER 1: INTRODUCTION

### 1.1 Introduction to the Project
The rapid advancement of digital technology has transformed almost every aspect of human life, including health and fitness. In the post-pandemic world, individuals have become increasingly aware of the importance of maintaining a healthy lifestyle. However, the modern sedentary lifestyle, characterized by long hours of desk work and minimal physical activity, has led to a rise in chronic conditions like obesity, hypertension, and diabetes. 

The **Personal Fitness Application** is a specialized web platform designed to bridge the gap between busy schedules and fitness goals. It provides a centralized ecosystem where users can track their physical activities, dietary intake, and physiological metrics. By leveraging the power of data visualization and secure cloud-based storage, the application empowers users to take control of their health through informed decision-making.

### 1.2 Motivation and Need of the System
The primary motivation behind this project is the observation that while many people start fitness routines with high enthusiasm, a significant percentage drop out due to a lack of visible progress or an organized tracking mechanism. 

**The need for this system is defined by:**
*   **Information Silos**: Most users use separate tools for diet, steps, and weight tracking. This fragmentation prevents a holistic view of health.
*   **Lack of Accountability**: Without a historical record of efforts, users find it difficult to stay motivated during plateaus.
*   **Data Security Concerns**: Many free applications sell user health data to third-party advertisers. Our system prioritizes private hosting and token-based security.
*   **Accessibility**: A web-based solution ensures that users can access their dashboard from any device (Desktop/Mobile) without needing to install heavy native applications.

### 1.3 Objectives of the Project
The core objectives of the Personal Fitness Application are:
1.  **Centralization**: To provide a single source of truth for all fitness-related data point including Workouts, Health Records, Diet, and Goals.
2.  **Security**: To implement a robust **JWT-based stateless authentication** system to protect sensitive personal health data.
3.  **Visualization**: To develop a dynamic dashboard with progress bars and color-coded badges to provide immediate feedback on fitness status.
4.  **Usability**: To create an intuitive interface based on the **AdminLTE** design system, ensuring a high-end user experience.
5.  **Extensibility**: To build a modular backend using **Spring Boot** that can easily accommodate future features like AI-based recommendations.

### 1.4 Scope of the Project
**In-Scope:**
*   User registration and secure login via JWT.
*   Dashboard displaying the 5 most recent activities across all modules.
*   Full CRUD (Create, Read, Update, Delete) for Workout logs, Diet records, and Health vitals.
*   Goal setting and percentage-based progress tracking.
*   Dynamic lookup management (Exercise types, intensities).

**Out-of-Scope:**
*   Direct integration with wearable sensors (Fitbit/Garmin) in the initial version.
*   Community/Social workout sharing features.
*   AI/Machine Learning for personalized diet plans (planned for future scope).

### 1.5 Problem Statement
"The current fitness landscape suffers from fragmented data tracking and a lack of integrated platforms that combine workout management with dietary analysis. Users require a secure, unified, and visually engaging web application that simplifies the logging process and provides real-time progress monitoring to ensure long-term adherence to fitness routines."

---

## CHAPTER 2: LITERATURE REVIEW

### 2.1 Analysis of Existing Systems
Current fitness tracking solutions fall into three main categories:
1.  **Manual Logs**: Notebooks or generic spreadsheets. While flexible, they lack calculation capabilities and real-time visualization.
2.  **Commercial Apps (e.g., MyFitnessPal, Strava)**: Feature-rich but often cluttered with ads or locked behind premium subscriptions. They also pose privacy risks as they are hosted on public vendor clouds.
3.  **Static Web Forms**: Basic websites that allow logging but do not provide a "Dashboard" experience or secure session management.

### 2.2 Limitations of Existing Systems
*   **Complexity**: Many apps have steep learning curves, discouraging casual users.
*   **Cost**: Advanced features like "Goal Progression" are often paid attributes.
*   **Interoperability**: Data from a weight-tracking app rarely talks to a workout-tracking app.
*   **UI/UX Quality**: Traditional academic projects often lack a "premium" feel, leading to low user engagement.

### 2.3 Proposed System Characteristics
Our system addresses these limitations by:
*   **Minimalist Design**: A clean, sidebar-driven navigation that stays out of the user's way.
*   **Modern Auth**: Using **Spring Security** to handle tokens instead of traditional session cookies, making the API more scalable.
*   **Real-time Logic**: Frontend JavaScript (Vanilla JS) that handles data sorting and table population without full-page reloads.

---

## CHAPTER 3: SYSTEM ANALYSIS

### 3.1 Software Requirements
*   **Operating System**: Windows 10/11 or Ubuntu Linux for hosting.
*   **JDK**: Java 17 or higher (for modern Spring Boot features).
*   **Framework**: Spring Boot 3.x (Modular, Lightweight, REST-ready).
*   **Build Tool**: Maven (Dependency management).
*   **Database**: MySQL 8.0 (Structured relational storage).
*   **Frontend**: HTML5, Vanilla JavaScript (ES6), Bootstrap 5.
*   **UI Theme**: AdminLTE (Professional dashboard layout).

### 3.2 Functional Requirements (Module Wise)
1.  **Identity Module**:
    *   `POST /auth/login`: Authenticates user and returns JWT.
    *   `POST /auth/register`: Creates a new user profile with encrypted passwords.
2.  **Workout Module**:
    *   Ability to log Exercise Name, Type (Cardio/Strength), Duration, and Calories Burned.
3.  **Health Metric Module**:
    *   Tracking of Weight, Sleep duration, Steps taken, and Heart Rate.
4.  **Goal Module**:
    *   Setting "Target Values" vs "Current Values" and calculating the completion percentage.

### 3.3 Non-Functional Requirements
*   **Security**: Password hashing using **BCrypt**. JWT tokens for every API request header.
*   **Responsiveness**: The UI must adapt to various screen sizes using Bootstrap's grid system.
*   **Maintainability**: Separate layers for API Controller, Logic (Service), and Data (Repository).
*   **Performance**: Dashboard tables must load under 500ms using optimized database queries.

### 3.4 Feasibility Study
*   **Technical Feasibility**: The chosen stack (Spring Boot + MySQL) is the industry standard for enterprise-grade web applications.
*   **Economic Feasibility**: Use of open-source frameworks ensures zero licensing costs.
*   **Behavioral Feasibility**: The dashboard-centric design caters to the human psychology of "streaks" and "progress," ensuring user retention.

---

## CHAPTER 4: SYSTEM DESIGN

### 4.1 System Architecture
The application utilizes a **N-Tier Architecture**:
1.  **Client Layer (View)**: HTML/CSS/JS files running in the user's browser.
2.  **Web API Layer (Controller)**: REST controllers that define the endpoints.
3.  **Business Logic Layer (Service)**: Handles calculations, validations, and data transformations.
4.  **Persistence Layer (Repository)**: Uses **Spring Data JPA** to communicate with the MySQL database.

### 4.2 Security Architecture (JWT Flow)
The security flow is designed to be stateless:
1.  User sends credentials to `/api/auth/login`.
2.  Server verifies and generates a **JSON Web Token (JWT)** signed with a secret key.
3.  Browser stores the token in `localStorage`.
4.  Every subsequent request includes the token in the `Authorization` header.
5.  A **JwtFilter** on the server intercepts requests, validates the token, and sets the security context.

### 4.3 Database Schema Design
*   **User Entity**: ID, Username, Email, Password (hashed), CreatedAt.
*   **Workout Entity**: ID, UserID (FK), ExerciseName, Type, Duration, Calories, Date.
*   **Goal Entity**: ID, UserID (FK), Description, TargetValue, CurrentValue, Progress_Percentage, Status.
*   **Lookup Entity**: A master-data table that stores Exercise Types and Intensities to populate dropdowns dynamically.

---

## CHAPTER 5: IMPLEMENTATION

### 5.1 Technology Detail: Spring Boot
Spring Boot was chosen for its "opinionated" configuration, which allows for rapid development. The backend uses:
*   **Spring Data JPA**: Automates the generation of SQL queries for CRUD operations.
*   **Spring Security**: Provides the architecture for Authentication and Authorization.
*   **Lombok**: Reduces boilerplate code in Java entities.

### 5.2 Technology Detail: Frontend JavaScript
Instead of using heavy frameworks, we used **Vanilla JS** to maintain high speed:
*   **api.js**: A central utility that handles the global context path (`/fitness-app`) and adds the JWT token to every request automatically.
*   **dashboard.js**: Implements asynchronous data fetching using `Promise.all` to ensure the dashboard feels "live."
*   **common.js**: Handles shared UI components like the Sidebar and Header across all pages.

### 5.3 Module Description (Sample: Dashboard)
The dashboard logic is critical. It fetches data for Workout, Health, Diet, and Goals in parallel. Before rendering, it **sorts the data by `recordDateTime` (Descending)** and slices the top 5 records. If a table has fewer than 5 records, the [renderList](file:///d:/project/workspace/Personal%20Fitness%20Application/src/main/resources/static/js/dashboard.js#103-116) function automatically injects empty rows to maintain a consistent UI height across all cards.

---

## CHAPTER 6: TESTING

### 6.1 Testing Methodology
The system followed an **Iterative Testing Approach**:
1.  **Unit Testing**: Individual API endpoints were tested using **Postman**.
2.  **Integration Testing**: Frontend-to-Backend data flow was verified, specifically focusing on the JWT token persistence.
3.  **UI Testing**: Cross-browser testing (Chrome, Edge, Firefox) to ensure AdminLTE components render correctly.

### 6.2 Test Scenarios & Results
| ID | Module | Test Scenario | Expected Result | Result |
| :--- | :--- | :--- | :--- | :--- |
| 1 | Auth | Login with incorrect password | "Invalid Credentials" error | PASS |
| 2 | Auth | Access dashboard without login | Redirect to login.html | PASS |
| 3 | Dashboard | Fetching workout data | Top 5 recent records displayed | PASS |
| 4 | Goals | Updating "Current Value" | Progress bar updates instantly | PASS |
| 5 | Security | Expired token usage | 401 Unauthorized response | PASS |

---

## CHAPTER 7: CONCLUSION & FUTURE SCOPE

### 7.1 Conclusion
The **Personal Fitness Application** successfully demonstrates how modern full-stack technologies can be combined to solve real-world health problems. By focusing on a secure backend and a data-rich dashboard, the project provides a professional-grade tool for personal fitness management. The integration of Spring Security ensures data privacy, while the AdminLTE UI provides a desktop-like experience in a web browser.

### 7.2 SWOT Analysis
*   **Strengths**: Secure JWT auth, responsive UI, fixed-height consistent dashboard.
*   **Weaknesses**: Relies on manual data entry for steps/heart rate.
*   **Opportunities**: Integration with Google Fit/Apple Health APIs.
*   **Threats**: High competition from mobile-native fitness applications.

### 7.3 Future Scope
*   **Progressive Web App (PWA)**: Enabling "Add to Home Screen" functionality and offline logging.
*   **Social Analytics**: Comparison charts to see how a user's fitness levels compare to average users.
*   **Notification System**: Browser notifications for goal reminders and workout schedules.
*   **Dietary AI**: Integration with OpenAI API to calculate calories from a photo of a meal.

---

## REFERENCES
1.  **Spring Documentation**: [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)
2.  **MDN Web Docs**: JavaScript Async/Await and Fetch API guides.
3.  **Bootstrap Framework**: Documentation for responsive design patterns.
4.  **AdminLTE Open Source**: Dashboard template documentation and components.
5.  *Clean Code: A Handbook of Agile Software Craftsmanship* by Robert C. Martin.
6.  *Design Patterns: Elements of Reusable Object-Oriented Software* by GoF.
