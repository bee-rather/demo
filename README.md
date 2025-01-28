# Weather Forecast Application

A modern full-stack weather application that provides real-time weather forecasts using OpenWeatherMap API. The application consists of a Spring Boot backend and a React frontend.

## 🌟 Features

- Real-time weather data fetching
- City-based weather search with country code support
- Caching for improved performance
- Error handling and resilience patterns
- Reactive programming with Spring WebFlux
- Modern UI with React
- Swagger API documentation

## 🏗️ Architecture

### Backend (Spring Boot)
- Spring WebFlux for reactive programming
- Caffeine caching
- Circuit breaker pattern
- Structured logging
- OpenAPI/Swagger documentation

### Frontend (React)
- Modern React with hooks
- Responsive design
- Error handling
- Loading states
- TypeScript support

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Node.js 16+
- Maven 3.6+
- OpenWeatherMap API key

### Backend Setup

1. Clone the repository:
```bash
git clone https://github.com/yourusername/weather-app.git
cd weather-app/backend
```

2. Configure environment variables:
Create `application.properties`:
```properties
openweathermap.api.key=your_api_key
openweathermap.base.url=https://api.openweathermap.org/data/2.5/weather
```

3. Build and run:
```bash
mvn clean install
mvn spring-boot:run
```

The backend will start at `http://localhost:8080`

### Frontend Setup

1. Navigate to frontend directory:
```bash
cd ../frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the application:
```bash
ng serve
```

The frontend will start at `http://localhost:4200`

## 📚 API Documentation

Access the Swagger UI at: `http://localhost:8080/swagger-ui.html`

### Sample API Request
```bash
curl -X POST http://localhost:8080/api/weather/current \
  -H "Content-Type: application/json" \
  -d '{"city":"London"}'
```

## 🔧 Configuration

### Backend Configuration Options
- `spring.cache.type`: Cache provider (default: caffeine)
- `spring.cache.caffeine.spec`: Cache specification
- `logging.level.root`: Logging level

### Frontend Environment Variables
- apiUrl: Backend API URL (http://localhost:8080/api/weather/forecast)

## 🧪 Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## 📝 Project Structure

```
demo/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   ├── pom.xml
│   └── README.md
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   ├── environments/
│   ├── package.json
└── README.md
```

## 🔐 Security

- CSRF protection
- API key validation
- Request validation
- Error handling
- Rate limiting

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## 👥 Authors

- Ganesh - [YourGithub](https://github.com/bee-rather)

## 🙏 Acknowledgments

- OpenWeatherMap API for weather data
- Spring Boot team for the amazing framework
- Angular team for the frontend framework

## 📧 Contact

Ganesh - iamganeshbiradar@gmail.com

Project Link: [https://github.com/bee-rather/demo](https://github.com/bee-rather/demo)
