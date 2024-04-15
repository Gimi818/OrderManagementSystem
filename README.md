# Project in progress
This project is a microservice-based e-commerce system designed to enhance shopping experience through efficient and scalable service architecture. Utilizing Apache Kafka for inter-service communication, it ensures real-time data synchronization and event-driven processing. The system comprises several key services:
  * User Service: Manages user registration and account details, providing functionalities such as account creation and user authentication.
  * Product Service: Handles the product catalog, allowing for the addition, browsing, and searching of products within the store.
  * Cart Service: Creates shopping carts for users and supports adding products to carts. It leverages Kafka to maintain consistent cart states across the system.
  * Order Service: Manages order placement and verification of product availability. It coordinates with the Delivery Service for shipping arrangements and uses the EmailSender Service for sending order confirmations to customers.
  * Delivery Service: Oversees the logistics of order deliveries, including tracking and delivery method selection.
  * EmailSender Service: Automates customer communication via email for registration confirmations, order updates, and other notifications.

Built on Spring Boot for ease of deployment and PostgreSQL for reliable data storage.
