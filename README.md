![logo_hd](https://github.com/spe-uob/2023-OnlineDeliveryPlatform/assets/123550614/fb0e871f-75ca-479e-91f9-f0560a2a5793)


<div align="center">
  <h1 align="center">Online Delivery Platform</h1>
  <h3>A green approach to food delivery</h3>
</div>

<div align="center">
  <a><img alt="Backend" src="https://img.shields.io/badge/Backend-Java-%23ff0000"></a>
  <a><img alt="License" src="https://img.shields.io/badge/License-MIT-%23800080"></a>
</div>

## Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Build instructions](#run-the-app-locally)
- [Tech Stack](#tech-stack)
- [User Stories](#user-stories)
- [Stakeholders](#stake-holders)
- [Product Map](#product-map)
- [Licence](#licence)
- [Acknowledgements](#acknowledgements)
- [Team](#team)
---
## Project Overview
### Sustainable Delivery Options
Meals are delivered using bicycles and electric cars, significantly reducing CO2 emissions associated with traditional delivery methods.

### Reusable Containers
To combat the issue of single-use plastics, the idea is to deliver food in reusable containers, encouraging a circular economy and reducing waste.
Support for Local Chefs and Producers: Offer a platform that highlights local talent, allowing chefs and food producers to reach a wider audience while ensuring that customers enjoy the freshest ingredients.

### Customizable Subscriptions
Users have the flexibility to choose from various subscription plans that fit their lifestyle. Whether it's a meal a day or a special weekend treat.

## Features
Our delivery app aims to create a seamless experience for customers, chefs, and delivery drivers alike. 

Customers can browse through an array of restaurants, view menus, and place orders effortlessly. They can input their location via GPS or manual entry and receive real-time updates to monitor their order progress. Each customer is provided with a unique order number to present to their designated driver. The app also features optional functionalities like a review system, temporary private messaging with the driver, and even a potential refund mechanism backed by photo verification. If time permits, the app will offer personalized food recommendations based on previous order history. Customers can also set filters to match their specific dining needs such as allergies, vegan options, or Halal food.

For chefs, the platform offers the ability to upload and manage menus, update food item availability via a stock management system, and communicate estimated preparation times to drivers. Chefs also receive orders complete with customer preferences and dietary needs.

Delivery drivers are assigned orders based on their proximity to the pick-up location, ensuring efficiency. They receive unique pick-up and drop-off numbers for verification purposes and can use a temporary messaging system for coordinating with chefs and customers. This holistic approach aims to streamline the food ordering and delivery process for all parties involved.

## Run the app locally
#### 1. Clone the repository
Navigate to the desired directory, open a terminal, and paste the following command
```console
git clone git@github.com:spe-uob/2023-OnlineDeliveryPlatform.git
```

#### 2. Change into the new project directory 
Navigate to the project directory using
```console
cd 2023-OnlineDeliveryPlatform/online-delivery-system
```

#### 3. Ensure you have the correct version of Java installed
The app runs on Java 17. Ensure you have the correct version of Java installed by running
```console
java --version
```
If you don't have the correct version, you can download it directly from the [Oracle website](https://www.oracle.com/java/technologies/downloads/#java17).

#### 4. Build the project
You will need to use Maven to build the project. You can check if Maven is already installed in your computer by running the following command
###### MacOS and Windows
```console
mvn -v
```
###### Linux
```console
mvn -version
```

If Maven is installed, you will see the version information. Otherwise, you can follow the installation instructions below for your respective OS.
##### MacOS
First, ensure either [Homebrew](https://brew.sh/) or [Macports](https://www.macports.org/install.php) are installed by following the instructions on their respective websites.
###### Using Homebrew
```console
brew install maven
```
###### Using MacPorts
```console
sudo port install maven
```

##### Linux
###### For Debian/Ubuntu distributions run
```console
sudo apt install maven
```
###### For Fedora distributions use
```console
sudo dnf install maven
```
For other distributions, use the package manager accordingly or download the binary from the [Apache Maven Project Website](https://maven.apache.org/download.cgi).

##### Windows
- Download the Maven zip file from the [Apache Maven Project Website](https://maven.apache.org/download.cgi).
- Unzip the distribution archive to the directory you wish to install Maven. The subdirectory apache-maven-x.x.x will be created from the archive.
- Add the bin directory of the created directory (apache-maven-x.x.x\bin) to the PATH environment variable.


Once Maven is installed, navigate to the project directory and run the followning command on the terminal to install all the dependencies
```console
mvn clean install
```
or 
```console
./mvnw clean install
```
Once finished, you can run the application with
```console
mvn spring-boot:run
```
or 
```console
./mvnw spring-boot:run
```
You should see a message signaling that the app has been sucessfully built by Maven.

#### 5. Access the application
Once the application is running, you can access it by opening a web browser and navigating to:
```url
http://localhost:8080
```

## Tech Stack
#### Backend
- [SpringBoot](https://spring.io/) - Framework
- [PostgreSQL](https://www.postgresql.org/) - Database
#### Frontend
- HTML
- CSS
- JavaScript
#### Cloud
- [Microsoft Azure](https://azure.microsoft.com/en-gb/) - App Cloud Services
- [Neon](https://neon.tech/) - Database Server
#### DevOps
- GitHub Actions - CI/CD

## User Stories

As a **customer**, I seek a way to browse available options for meals which can be delivered to me in subsequent days; the food delivery will be cooked ready for reheating, and will arrive in a reuseable container which I can then return to a driver at a later time.

As a **chef**, I wish to receive orders promptly after they are processed, so that I can quickly prepare food for delivery within the following days.

As a **delivery driver**, I would like to receive a daily quota of deliveries to be made, complete with a pick-up and drop-off location so that I can deliver food quickly and efficiently. Ideally my drop-off locations would be as close to each other as possible.

## Stake Holders

- **Chefs/restaurants** have the option to sign up to the service so that they can sell and distribute their meals to customers in reusable containers which will be returned to them. 
- **Customers** can place orders for meals to be delivered to them on following days in reusable containers; at a later time or during their next order the container can be collected by a driver to be returned to the chef/restaurant.
- **Delivery Drivers** will sign up to the service and receive notifications of nearby orders to pick up/customers to deliver to/containers to collect. They are able to verify deliveries by a customer's username and unique order code. They should have some way of storing the reusable containers during journeys, and ideally would be either travelling by bicycle or electric vehicle.
- **The client** will have created a sustainable, eco-friendly delivery service in which food containers are reused and orders are delivered by green vehicles.

## Product Map
<p align="center">
  <img src="product.map.png" width="1000" title="hover text">
</p>

## Licence

This project is licensed under MIT - see the [LICENSE](https://github.com/spe-uob/2023-OnlineDeliveryPlatform/blob/dev/LICENSE) file for details.

## Acknowledgements

Mention any external libraries, frameworks, or tools used in the project.   
Credit any sources of inspiration or references that contributed to the project.

## Team
- Luke Trott (zq22843)
- Hugh Kiggell (bz22499)
- Anthony Price (ek21604)
- Jiahao Li (ob21095)
- JP Ruiz (vt22223)
#### Mentor
Leo Lai

