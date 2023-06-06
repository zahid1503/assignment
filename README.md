# quiz-challenge

The Quiz Challenge is a Spring Boot-based RESTful web service that provides an interface to manage and play quizzes. It allows users to fetch random questions, store them in a database, and participate in a quiz by answering questions.

## Features

- Fetch 5 random questions from an external API and store them in the database.
- Create a REST API endpoint to retrieve a question along with its ID.
- Create a REST API endpoint to submit an answer for a given question ID and retrieve the correct answer along with the next question.
- Support for PostgreSQL database.
- Error handling and appropriate HTTP response codes.

## Technologies Used

- Java
- Spring Boot
- MySQL
- Maven

## Getting Started

To run the Quiz API locally, follow these steps:

1. Clone the repository.
2. Set up a MySQL database and update the database configuration in `application.properties`.
3. Build the project using Maven: `mvn clean install`.
4. Run the application: `mvn spring-boot:run`.
5. The API will be accessible at `http://localhost:8080`.

## API Endpoints

- `GET /quiz/getQuestions`: Fetch 5 random questions from the external API and store them in the database.
- `GET /quiz/play`: Retrieve a question along with its ID.
- `POST /quiz/next`: Submit a question ID and retrieve the correct answer for given question ID along with the next question.

