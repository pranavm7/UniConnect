![UniConnect Logo](https://github.com/pranavm7/UniConnect/blob/23664bbb8b06340709c1fec4d7a658b51937d87f/readmeAssets/UniConnect.png)
# UniConnect
> CircleCI Status: [![CircleCI](https://circleci.com/gh/pranavm7/UniConnect/tree/main.svg?style=svg&circle-token=2897cd210ec47ef1c90338f0f97f77e7730112e5)](https://circleci.com/gh/pranavm7/UniConnect/tree/main)

## Introduction

Do you want to know what is trending at your university? Do you want to know more about the latest events, sports, clubs, academics at your university? UniConnect can help you with that: 
+ Join your universities community and see all the groups for you
+ Create a post, share pictures, participate in discussions
+ Keep up with the university plans and news
+ Anonmynous profiles so you can be you

Use your android device to join your universities community. Download the UniConnect app today!

## Storyboard
---
### Main View:
![alt text](https://github.com/pranavm7/UniConnect/blob/main/readmeAssets/mainView.svg)
### Make a Post:
![alt text](https://github.com/pranavm7/UniConnect/blob/main/readmeAssets/makePost.png)
### Read a Post:
![alt text](https://github.com/pranavm7/UniConnect/blob/main/readmeAssets/postDetails.svg)


## Class Diagram

<img src="https://github.com/pranavm7/UniConnect/blob/main/readmeAssets/Class_Diagram_New.jpg" height=950>

### Description

**MainActivity**: This is the first screen that the user sees, it displays a feed of posts.

**MakePostActivity**: This screen allows a logged in user to create a post

**PostDetailsActivity**: This screen shows the details of a post, including post title, description, photos, comments, and replies

**RetrofitClientInstance**: Bootstrap class required for Retrofit

**Community**: Noun class that represents a community

**Post**: Noun class that represents a post

**Comment**: Noun class that represents a comment

**IPostDAO**: interface to work with Post data

**ICommentDAO**: interface to work with Comment data

**ICommunityDAO**: interface to work with Community data

## Functional Requirments

### Examples

#### Given
The user is not logged in

#### When
the user opens the app

#### When
the user clicks on "create account"

#### When
the user fills in "eternallydoomed" as username, "abc@xyz.com" as email, and "Pa$$w0rd" as password

#### Then
an account is created for the user through firebase authentication

---

#### Given

#### When
the user opens the app

#### Then
The user views a feed of posts that are ordered by popularity

---

#### Given
The user is in the app

#### When
the user clicks on one of the post

#### Then
the user is able to see the post in detail, including comments and pictures

---

#### Given
The user is in the app

#### When
the user searchs in the search bar

#### Then
The user views a feed of posts that match the search text

---

#### Given
The user is in the app

#### When
the user is not logged in

#### Then
the user can log in or sign up

---

#### Given
The user has an account

#### When
the user logs in

#### Then
the user sees a personalized feed of posts

2. Creating posts

---

#### Given
The user has an account and is logged in

#### When
the user click on the "Add Post" button

#### When
The user fills in Title, description, photo (optional), community

#### Then
the user's post is saved in the database

---

#### Given
Bipal has an account and is logged in

#### When
Bipal clicks on the "Add Post" button

#### When
Bipal fills in "Mobile App development" as title

#### When
Bipal fills in "" as description

#### When
Bipal fills in "School of IT" as community

#### Then
Bipal's post is saved in the database with "Mobile App development" as title and "School of IT" as community

---

#### Given
Bipal has an account and is logged in

#### When
Bipal clicks on the "Add Post" button

#### When
Bipal fills in "Mobile App development" as title

#### When
Bipal fills in "" as description

#### Then
An error occurs and the post is not saved in the database

---

#### Given
The user is logged in

#### When
The user is on a post

#### When
the user clicks on comment under the main title

#### When
the user fills out a comment 

#### Then
The comment is saved under the post

---

#### Given
The user is logged in

#### When
The user is on a post

#### When
the user clicks on comment under a sub comment

#### When
the user fills out a comment 

#### Then
The comment is saved under the post

---

#### Given
The user is logged in

#### When
The user is on a post

#### When
the user tries commenting under a sub comment

#### Then
The user is not allowed to do that

## Scrum Roles

- **Product Owner/Scrum Master**: Pranav Mahajan
- **Front-End Developvers**: Bill Appiagyei & Edward Bender
- **Back-End Developers**: Bipal Goyal, Anand Pandey & Pranav Mahajan

## Standup

Tuesdays and Thursdays 11:00 in class.
