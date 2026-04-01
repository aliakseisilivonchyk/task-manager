# Task Manager

RESTful service to manage tasks based on Java and Spring Boot.

## Features

- tasks creation,
- tasks modification,
- tasks assigning,
- tasks filtering.

## Getting started

To get a local copy up and running you need [Git](https://git-scm.com/install/) to clone the project (or download a zip version of the project)
and [Docker](https://www.docker.com/products/docker-desktop/) to run the project.

## Installation

1) Clone the repo
```
git clone https://github.com/aliakseisilivonchyk/task-manager.git
```
2) Start the project using Docker
```
docker compose up -d
```

## Request examples

1) Register new user
```
curl --location 'localhost:8080/api/auth/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "admin",
    "email": "admin@test.com",
    "password": "secret",
    "role": "ADMIN"
}'
```
2) Login to get a JWT token
```
curl --location 'localhost:8080/api/auth/login' \
--header 'Content-Type: application/json' \
--data '{
    "username": "admin",
    "password": "secret"
}'
```
Copy a JWT token from the **token** field of the response.
3) Create a task (Replace **YOUR_TOKEN** with the copied token, **USER_ID** with an id of a user)
```
curl --location 'localhost:8080/api/tasks' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <YOUR_TOKEN>' \
--data '{
    "title": "testTitle",
    "description": "testDescription",
    "status": "TODO",
    "priority": "LOW",
    "assignee": <USER_ID>
}'
```
4) Get all tasks (Replace **YOUR_TOKEN** with the copied token). Optionally filter by **status**, **authorId**, **assigneeId** (Replace **USER_ID** with an id of a user)
```
curl --location 'localhost:8080/api/tasks?status=TODO&authorId=<USER_ID>&assigneeId=<USER_ID>' \
--header 'Authorization: Bearer <YOUR_TOKEN>'
```
5) Get a task by id (Replace **TASK_ID** with an id of a task, **YOUR_TOKEN** with the copied token)
```
curl --location 'localhost:8080/api/tasks/<TASK_ID>' \
--header 'Authorization: Bearer <YOUR_TOKEN>'
```
6) Update a task by id (Replace **TASK_ID** with an id of a task, **YOUR_TOKEN** with the copied token, **USER_ID** with an id of a user)
```
curl --location --request PUT 'localhost:8080/api/tasks/<TASK_ID>' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <YOUR_TOKEN>' \
--data '{
    "title": "testTitleUpdated",
    "description": "testDescriptionUpdated",
    "status": "DONE",
    "priority": "MEDIUM",
    "assignee": <USER_ID>
}'
```
7) Delete a task (Replace **TASK_ID** with an id of a task, **YOUR_TOKEN** with the copied token)
```
curl --location --request DELETE 'localhost:8080/api/tasks/<TASK_ID>' \
--header 'Authorization: Bearer <YOUR_TOKEN>'
```