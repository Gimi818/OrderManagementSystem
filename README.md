# Project in progress
This project is a microservice-based e-commerce system designed to enhance shopping experience through efficient and scalable service architecture. Utilizing Apache Kafka for inter-service communication, it ensures real-time data synchronization and event-driven processing. The system comprises several key services:
  * User Service: Manages user registration and account details, providing functionalities such as account creation and user authentication.
  * Product Service: Handles the product catalog, allowing for the addition, browsing, and searching of products within the store.
  * Cart Service: Creates shopping carts for users and supports adding products to carts. It leverages Kafka to maintain consistent cart states across the system.
  * Order Service: Manages order placement and verification of product availability. It coordinates with the Delivery Service for shipping arrangements and uses the EmailSender Service for sending order confirmations to customers.
  * Delivery Service: Oversees the logistics of order deliveries, including tracking and delivery method selection.
  * EmailSender Service: Automates customer communication via email for registration confirmations, order updates, and other notifications.

Built on Spring Boot for ease of deployment and PostgreSQL for reliable data storage.


 ## Application is developed using following technologies:
 Core:
<p align="left"><a href="https://www.java.com" target="_blank" rel="noreferrer"> 
<img src="https://ultimateqa.com/wp-content/uploads/2020/12/Java-logo-icon-1.png" alt="java" width="80" height="50"/> 
</a> <a href="https://spring.io/" target="_blank" rel="noreferrer"> <img src="https://e4developer.com/wp-content/uploads/2018/01/spring-boot.png" alt="spring" width="90" height="50"/> 
<a href="https://www.mongodb.com/" target="_blank" rel="noreferrer"> <a href="https://www.docker.com/" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/docker/docker-original-wordmark.svg" alt="docker" width="50" height="50"/>
  <a href="https://www.java.com" target="_blank" rel="noreferrer"> <img src="https://blog.min.io/content/images/2021/09/1_kqpVTzo8b0e2oKdOjWQxZA.png" alt="java" width="99" height="50"/></a>
 <a href="https://git-scm.com/" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/git-scm/git-scm-icon.svg" alt="git" width="50" height="50"/> </a> 
 <a href="https://www.mysql.com/" target="_blank" rel="noreferrer"> <img src="https://www.zdnet.com/a/img/resize/e7aff3398e12f0fa70fd66238d743054c4c8b95e/2018/04/19/092cbf81-acac-4f3a-91a1-5a26abc1721f/postgresql-logo.png?auto=webp&fit=crop&height=900&width=1200" alt="mysql" width="80" height="50"/> </a>
 <a href="https://www.docker.com/" target="_blank" rel="noreferrer"> <img src="https://mapstruct.org/images/mapstruct.png" alt="docker" width="80" height="50"/></a>
 </a> <a href="https://www.java.com" target="_blank" rel="noreferrer"> <img src="https://junit.org/junit4/images/junit5-banner.png" alt="java" width="90" height="50"/>
 <a href="https://www.java.com" target="_blank" rel="noreferrer"> <img src="https://javadoc.io/static/org.mockito/mockito-core/1.9.5/org/mockito/logo.jpg" alt="java" width="90" height="50"/></a>
 <a href="https://www.java.com" target="_blank" rel="noreferrer"> <img src="https://static.javatpoint.com/tutorial/spring-cloud/images/spring-cloud.png" alt="java" width="70" height="50"/></a>
  <a href="https://www.java.com" target="_blank" rel="noreferrer"> <img src="https://user-images.githubusercontent.com/27962005/35682934-68b84abe-0730-11e8-926d-66ae93aa4b1d.png" alt="java" width="90" height="55"/></a>
   <a href="https://www.java.com" target="_blank" rel="noreferrer"> <img src="https://static.wixstatic.com/media/fb5029_919b4bcf47a346b091751ccfc87a2d08~mv2.png" alt="java" width="90" height="50"/></a>
 </p>

  ## Microservice Architecture
  
  <img src="https://github.com/Gimi818/OrderManagementSystem/blob/master/steps/microservice-architecture/MicroserviceArchitectureShop.PNG" width="1000" heigt="700"/>

   ## To run the application, follow these steps :
- Install IntelliJ IDEA and Docker Desktop on your computer.
- Run Docker Desktop.
- Clone the repository in IntelliJ IDEA using the link https://github.com/Gimi818/OrderManagementSystem
- Run docker-compose up in the terminal. 
- Run the ConfigServer, DiscoveryServer, and Gateway  in IntelliJ IDEA.
- Run the other services: user, product, cart, order, and emailSender  in IntelliJ IDEA.
- Try the applications in Postaman, the steps on how to do it are below.
 
 ## How to use the application in Postman:
 
    Step 1 :
    POST localhost:8222/api/v1/users/registration
    Enter your data.
    JSON:
    {
     "email":"wojtekapachekafka@gmail.com",
     "fullName":"Wojciech Gmiterek",
     "password":"password",
     "repeatedPassword" :"password"
    }
  
  <img src="https://github.com/Gimi818/OrderManagementSystem/blob/master/steps/steps/1.PNG" width="500" heigt="700"/>
  
    Step 2 :
    Get all available products
    GET localhost:8222/api/v1/products/all
    
    You can use other queries to sort the products
    localhost:8222/api/v1/products/sorted/price/desc
    localhost:8222/api/v1/products/sorted/price/asc
    localhost:8222/api/v1/products/category?category=PHONE
    localhost:8222/api/v1/products/search/range/by-price?priceMin=3000&priceMax=3555
    
   <img src="https://github.com/Gimi818/OrderManagementSystem/blob/master/steps/steps/2.PNG" width="600" heigt="800"/>

    Step 3 :
    POST localhost:8222/api/v1/products/1/addToCart/5/?stock=1
    Enter the user ID, product id, and product stock into the URL to add the product to the cart
    
   <img src="https://github.com/Gimi818/OrderManagementSystem/blob/master/steps/steps/3.PNG" width="500" heigt="700"/>

    Step 4 :
    GET localhost:8222/api/v1/carts/1/contents
    Enter the user ID to get the contents of your cart.
    
   <img src="https://github.com/Gimi818/OrderManagementSystem/blob/master/steps/steps/4.PNG" width="500" heigt="700"/>

    Step 5 :
    POST localhost:8222/api/v1/carts/order/1
    Enter the user ID to place an order. 
    JSON:
    {
     "city": "Kraków",
     "postcode": "30-001",
     "street": "Rynek Główny",
     "houseNumber": "12"
    }
  
  <img src="https://github.com/Gimi818/OrderManagementSystem/blob/master/steps/steps/5.PNG" width="500" heigt="700"/>

    Step 6 :
    Check the email to which the order confirmation email was sent
    
   <img src="https://github.com/Gimi818/OrderManagementSystem/blob/master/steps/steps/6.PNG"  width="500" heigt="700"/>
