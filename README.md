# Mandatory Practice - CPD

Practice of **Distributed Application Development**  
This project simulates the automated provisioning of virtual instances and disks in a distributed cloud-like architecture using Spring Boot microservices, RabbitMQ for asynchronous communication, and MySQL for data persistence.

---
## Integrantes: 
| Name | Email | Github |
|-----------|-----------|-----------|
| Nerea González Mogeda | n.gonzalezm.2021@alumnos.urjc.es |[Nerea-González-Mogeda](https://github.com/Nereaaaa) |             |
| Noah Joseph Mateo Almagro | nj.mateo.2021@alumnos.urjc.es |[Noah-Joseph-Mateo-Almagro](https://github.com/NoahJosephMateoAlmagro)

## Architecture Overview

The system is composed of the following microservices:

- `api-service`: Main REST API. Handles user requests and controls the system.
- `disk-service`: Simulates the creation and state transitions of disks.
- `instance-service`: Simulates the creation and state transitions of instances.
- `mysql`: Official MySQL image for data persistence.
- `rabbitmq`: Official RabbitMQ broker.
- `haproxy`: Balances HTTP traffic across two replicas of the API service.

---

## System Flow

1. The user sends a request to create an instance via the API.
2. The `api-service` saves the data and sends a disk creation request to RabbitMQ (`disk-requests`).
3. The `disk-service` simulates the disk creation process and sends status updates via `disk-statuses`.
4. Once the disk is `ASSIGNED`, `api-service` sends an instance creation request to `instance-service`.
5. The `instance-service` simulates the instance state lifecycle and assigns an IP once it reaches `RUNNING`.
6. The final state and IP are sent back via `instance-statuses` and persisted by the `api-service`.

---
## Entity Relationship Diagram
  <img src="Images\EntityDiagram.png" width="1000" /> 
  <img src="Images\EntityDiagram2.png" width="500" /> 

## Class Relationship Diagram
<img src="Images\ClassesDiagram.png" width="1000" /> 

---

## Contributor: Nerea González Mogeda

### Tasks performed

Tasks:
- Developed the `api-service` microservice and integrated it with RabbitMQ and MySQL
- Creted the DockerFiles in each microservice
- Handled state management for disk creation flow between `api-service` and `disk-service`
- Implemented status listeners and message consumers in `api-service`  
- Create and publish the three images on Docker

---

### Top 5 commits

1. [`Add conexion to MySQL on API service`](https://github.com/Nereaaaa/cpd-dad/commit/c4b8d5bf3ab8446b9030fe0b2dc64b7552dfc11d)
2. [`Add connection to RabbitMQ on API service`](https://github.com/Nereaaaa/cpd-dad/commit/3d4d37fc49c05de20b8ea4f2ab325a5e006ed50a)
3. [`Create DockerFiles for each service`](https://github.com/Nereaaaa/cpd-dad/commit/cc80c7f76f807637a029e6efe606e778d90e441b)
4. [`Fix RabbitMQ communication between API and disk`](https://github.com/Nereaaaa/cpd-dad/commit/a415f251acdede731f527d5bdf2c6ebcf5507095)
5. [`Publish Docker images for all services`](https://github.com/Nereaaaa/cpd-dad/commit/32c418d15cc3c053a126f07f676b6b1ccaed14b5)


---

### Top 5 files edited

1. [`InstanceController.java`](https://github.com/Nereaaaa/cpd-dad/blob/main/api/src/main/java/es/codeurjc/api/Controller/InstanceController.java)
2. [`DiskRequestListener.java`](https://github.com/Nereaaaa/cpd-dad/blob/main/disk/src/main/java/es/codeurjc/disk/Listener/DiskRequestListener.java)
3. [`DiskStatusListener`](https://github.com/Nereaaaa/cpd-dad/blob/main/api/src/main/java/es/codeurjc/api/Messaging/DiskStatusListener.java)
4. [`application.properties (api)`](https://github.com/Nereaaaa/cpd-dad/blob/main/api/src/main/resources/application.properties)
5. [`DiskController.java`](https://github.com/Nereaaaa/cpd-dad/blob/main/api/src/main/java/es/codeurjc/api/Controller/DiskController.java)

---

## Contributor: Noah Joseph Mateo Almagro

### Tasks performed
  
- Developed the instance-service and disk-service microservices and integrated both with RabbitMQ.
- Implemented the hierarchical logic for instances and disks, enabling structured resource representation.
- Configured and wrote the docker-compose.yml to orchestrate MySQL, RabbitMQ, API, Disk, and Instance services.
- Integrated Jackson JSON conversion for message handling in RabbitMQ.
- Enhanced API behavior by using environment variables, fixing REST operations, and improving resource deletion logic.

---

### Top 5 commits

1. [`Disk hierarchy implemented (not tested) (With RabbitMQ)`](https://github.com/Nereaaaa/cpd-dad/commit/2ba5a526e307d0c5a4dd8dd127db88d5b07d743a)
2. [`Instance hierarchy implemented (not tested) (With RabbitMQ)`](https://github.com/Nereaaaa/cpd-dad/commit/fc5a7d0dd1218dd68bfb875a29faa99d68996bfd)
3. [`Created docker-compose.yml`](https://github.com/Nereaaaa/cpd-dad/commit/2b8ace576ce80bf71f54b0d10273fd45ee5d1e98)
4. [`Jackson2Json message conversion initiated`](https://github.com/Nereaaaa/cpd-dad/commit/64a3a1ae0da85f2b9c07c334f4d0363bc991fc3c)
5. [`Use env vars, fix REST API, disable disk creation, fix instance delet`](https://github.com/your-user/cpd-dad/commit/ad76db55c2111568120a2dbb339c3ab788146aec)

---

### Top 5 files edited

1. [`InstanceRequestListener.java`](https://github.com/Nereaaaa/cpd-dad/blob/main/instance/src/main/java/es/codeurjc/instance/Listener/InstanceRequestListener.java)
2. [`Instance.java`]((https://github.com/Nereaaaa/cpd-dad/blob/main/instance/src/main/java/es/codeurjc/instance/Model/Instance.java)
3. [`docker-compose.yml`]((https://github.com/Nereaaaa/cpd-dad/blob/main/docker-compose.yml)
4. [`Disk.java`]((https://github.com/Nereaaaa/cpd-dad/blob/main/disk/src/main/java/es/codeurjc/disk/Model/Disk.java)
5. [`DiskRequestListener.java`]((https://github.com/Nereaaaa/cpd-dad/blob/main/disk/src/main/java/es/codeurjc/disk/Listener/DiskRequestListener.java)

---

## Instructions for Building and Running the Application

### Prerequisites

To run the application, ensure you have:
1. [Docker](https://www.docker.com/products/docker-desktop) installed on your system.
2. Docker Compose (comes built-in with Docker Desktop on Windows and macOS).

### Running the application (Dockerized version)

1. Clone the repository:
```shell
git clone https://github.com/nnrea/cpd-dad.git
cd cpd-dad
```

2. Run the system using Docker Compose:
```shell
docker-compose up
```
3. Access the application: API: http://localhost
 
### Building and Publishing Docker Images

Each microservice (api, disk, instance) is built and published as a Docker image using Spring Boot Buildpacks.

1. Building the image locally (with Buildpacks):
  - Navigate to each service directory and run:
```shell
  mvnw.cmd spring-boot:build-image -Dspring-boot.build-image.imageName=user/<image_name> -DskipTests
```

2. Publishing the image to Docker Hub:
- Log into Docker: docker login
```shell
  docker login
```
- Push each image:
```shell
  docker push user/api
  docker push user/disk
  docker push user/instance
```
3. Run the system using Docker Compose:
```shell
  docker-compose up
```