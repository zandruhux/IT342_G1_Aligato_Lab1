# Auth System - Backend & Frontend

A complete authentication system with User Registration, Login, Dashboard, and Logout functionality.

## Architecture Overview

### Backend (Spring Boot)
- **Controller Layer**: Handles HTTP requests/responses (AuthController)
- **Service Layer**: Business logic & validation (AuthService)
- **Repository Layer**: Database access (UserRepository, TokenRepository)
- **Models**: User & AuthToken entities

### Frontend (React + Vite)
- **Pages**: Register, Login, Dashboard
- **Components**: Navbar with logout functionality
- **Services**: API client with axios interceptors
- **Routing**: Protected routes with React Router

### Database
- **Users Table**: Stores user information (email, password, name, role, created_at)
- **Auth_Tokens Table**: Stores authentication tokens with expiration

## Backend Setup

### Prerequisites
- Java 17 (or higher - can upgrade to Java 21 using the upgrade plan)
- Maven 3.6+
- MySQL 8.0+

### Step 1: Database Setup
Create the database:
```sql
CREATE DATABASE it342_aligato_g1_lab;
USE it342_aligato_g1_lab;
```

No need to create tables manually - Hibernate will auto-create them with `ddl-auto=update`.

### Step 2: Configure Database Connection
Edit `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/it342_aligato_g1_lab
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### Step 3: Build and Run Backend
```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

The backend will run on `http://localhost:8080`

## Frontend Setup

### Prerequisites
- Node.js 18+
- npm or yarn

### Step 1: Install Dependencies
```bash
cd web
npm install
```

### Step 2: Configure API URL
Create `.env` file (already created):
```
VITE_API_URL=http://localhost:8080/api
```

### Step 3: Run Development Server
```bash
npm run dev
```

The frontend will run on `http://localhost:5173`

## Usage

### 1. Register a New User
- Navigate to `http://localhost:5173/register`
- Fill in First Name, Last Name, Email, Password
- Click Register
- Redirected to login page

### 2. Login
- Navigate to `http://localhost:5173/login`
- Enter Email and Password
- Token is stored in localStorage
- Redirected to Dashboard

### 3. View Dashboard
- Protected route - requires login
- Displays user profile information
- Shows account status

### 4. Logout
- Click "Logout" button in navbar
- Token is removed from localStorage and database
- Redirected to login page

## API Endpoints

### Authentication Endpoints

**POST** `/api/auth/register`
- Request: `{ "firstName": "John", "lastName": "Doe", "email": "john@example.com", "password": "..." }`
- Response: `UserDTO` with user info

**POST** `/api/auth/login`
- Request: `{ "email": "john@example.com", "password": "..." }`
- Response: `{ "token": "...", "user": UserDTO }`

**GET** `/api/auth/profile`
- Header: `Authorization: Bearer <token>`
- Response: `UserDTO` with user info

**POST** `/api/auth/logout`
- Header: `Authorization: Bearer <token>`
- Response: `{ "message": "Logged out successfully" }`

## Project Structure

### Backend
```
backend/
├── src/main/java/com/example/demo/
│   ├── BackendApplication.java
│   ├── controller/
│   │   └── AuthController.java           (HTTP handlers)
│   ├── service/
│   │   └── AuthService.java              (Business logic)
│   ├── repository/
│   │   ├── UserRepository.java           (User CRUD)
│   │   └── TokenRepository.java          (Token management)
│   ├── model/
│   │   ├── User.java                     (User entity)
│   │   └── AuthToken.java                (Token entity)
│   └── dto/
│       ├── RegisterRequest.java
│       ├── LoginRequest.java
│       ├── UserDTO.java
│       ├── AuthResponse.java
├── application.properties
└── pom.xml

```

### Frontend
```
web/
├── src/
│   ├── App.jsx                           (Main app with routing)
│   ├── Navbar.jsx                        (Navigation & logout)
│   ├── ProtectedRoute.jsx                (Route protection)
│   ├── api.js                            (Axios client)
│   ├── pages/
│   │   ├── Register.jsx
│   │   ├── Login.jsx
│   │   └── Dashboard.jsx
│   ├── main.jsx
│   ├── App.css
│   └── index.css
├── .env                                  (API configuration)
├── vite.config.js
└── package.json
```

## Key Features

### Security
- Passwords are bcrypt-encoded (in backend)
- Tokens stored in localStorage (frontend) and database (backend)
- Protected routes prevent unauthorized access
- CORS configured for localhost:5173

### User Experience
- Clean, responsive UI with Tailwind CSS
- Form validation
- Error messages on failed operations
- Loading states during API calls
- Auto-redirect on token expiration

### Code Organization
- **Separation of Concerns**: Controller → Service → Repository
- **Clean DTOs**: No exposed entity passwords
- **Reusable Components**: Navbar, ProtectedRoute

## Troubleshooting

### Backend won't start
1. Check MySQL is running: `mysql -u root -p`
2. Verify database exists
3. Check port 8080 is available

### Frontend can't connect to backend
1. Ensure backend is running on port 8080
2. Check `.env` has correct API URL
3. Check browser console for CORS errors

### Login fails
1. Verify user was registered successfully
2. Check email is correct (case-sensitive)
3. Verify password matches

### Token expiration
1. Tokens expire after 24 hours
2. User needs to login again after expiration
3. Expired tokens are automatically deleted

## Next Steps

### To Upgrade Java to 21 (LTS)
Run the Java upgrade plan:
```bash
# From backend directory
./mvnw clean install   # After upgrading to Java 21
```

### To Add More Features
- User profile editing
- Role-based access control (admin/user)
- Multiple authentication methods (OAuth, SSO)
- Password reset functionality
- Token refresh mechanism

## Notes
- This is a simple, clean implementation suitable for learning
- Use appropriate security practices in production (HTTPS, secure cookies, etc.)
- Add rate limiting for login attempts
- Implement CSRF protection
- Use environment variables for sensitive data

