# Text-Based Social Media API

## Overview

This is a text-based social media API that allows users to perform various actions, including user registration, login, posting text content, commenting on posts, following other users, and viewing different types of posts and comments. The API is designed to be used by a front-end application, such as a single-page app.

## Technology Stack

- Java 17
- Maven
- Javalin
- PostgreSQL Data Storage

## Endpoints

### User Management

- **POST /users:** Create a new user
- **POST /users/verify/{email}:** Verify an existing user
- **POST /users/login:** User login (returns JWT token)

### Content Management

- **POST /posts:** Create a new post
- **POST /comments:** Create a new comment on a post

### Follow System

- **POST /follows:** Follow another user
- **DELETE /follows:** Unfollow a followed user

### Content Viewing

- **GET /followingPosts:** View posts of users following
- **GET /ownPosts:** View own posts
- **GET /commentsOnOwnPosts:** View comments on own posts
- **GET /commentsOnOwnAndFollowingPosts:** View comments on own and following posts

### User Information

- **GET /ownFollows:** View followers and following
- **GET /users/{searchTerm}:** Search for users' emails based on the search term

## Contact

- Github: https://github.com/AntonisPlatis92
- Email: antonispl92@gmail.com