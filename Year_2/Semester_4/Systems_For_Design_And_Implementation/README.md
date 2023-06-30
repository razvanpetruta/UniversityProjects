
# Library Management System

This is a full-stack web application built using Java Spring Boot and Angular that serves as a library management system. It allows users to manage libraries, books in libraries, readers, and reader memberships. The application also incorporates JWT authentication to ensure secure access for users.

## Tech Stack

- **Front-end**: Angular, TypeScript, Bootstrap 5, Material UI, HTML, CSS.
- **Back-end**: Java Spring Boot, Spring Data JPA, Spring Security.
- **Database**: PostgreSQL.
- **Deployment**: The app was deployed on an AWS EC2 instance (using nginx as a proxy server and securing the backend with let's encrypt) during the semester, but I deleted the account due to the free trial expiration.


## Features

- **User Authentication**: Secure user authentication is implemented using JSON Web Tokens (JWT). Users can register, log in, and receive a token for subsequent API requests.
- **Library Management**: Create, update, and delete libraries. Each library can have multiple books and multiple memberships associated with it.
- **Book Management**: Add, update, and delete books within libraries.
- **Reader Management**: Maintain reader details and membership information, create new memberships.
- **Membership Management**: Assign memberships for readers in specific libraries. Keep track of membership start and end dates.
- **Search, filtering and statistics**: Search for libraries, users. Filter books by price. See top libraries based on statistics.

## Preview
Please download the [app_preview.mp4](https://github.com/razvanpetruta/UniversityProjects/blob/main/Year_2/Semester_4/Systems_For_Design_And_Implementation/app_preview.mp4).