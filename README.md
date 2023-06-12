# Social Bird
Social networking service
## Idea
The main idea is to create an API for a social networking service, similar to Twitter.
# Technological stack
- SpringBoot
- MongoDB database
- Spring Web
- Spring Security
- Spring Data MongoDB
- JWT
- Spock
# Description:
## Requests that do not require a token:
- Register a user
POST /users/register?username=name&password=pass
- User authorization
GET /auth/login?username=name&password=pass
- User logout
POST /auth/logout
Authorization: Bearer your-access-token
## Requests that require a token (Authorization: Bearer your-access-token):
- Get all users
GET /users/all
- Update a user
PUT /users/{id}?username=name&password=pass
- Delete a user
DELETE /users/{id}
- Get a user by id
GET /users/{id}
- Get a user by username
GET /users?username=name
- Follow a user
POST /followers/follow?followerId=id1&followeeId=id2
- Unfollow a user
DELETE /followers/unfollow?followerId=id1&followeeId=id2
- User's followers
GET /followers/followersByUser/{userId}
- User's followees
GET /followers/followeesByUser/{userId}
- Create a post
POST /posts
Content-Type: application/json
{
  "content": "This is my new post!",
  "authorId": "author-id"
}
- Update post
PUT /posts/{id}
Content-Type: application/json
{
  "content": "This is my CHANGED post!"
}
- Delete post (with all likes and comments)
DELETE /posts/{id}
- Get posts by author
GET /posts/author/{authorId}
- Get feed by user (all posts by followees)
GET /posts/feed/{userId}
- Like the post
POST /posts?userId=id1&postId=id2
- Unlike the post
DELETE /posts?userId=id1&postId=id2
- Get likes by post
GET /posts/post/{postId}
- Comment the post
POST /comments
Content-Type: application/json
{
  "content": "This is a good post"
  "authorId": "This is a good post"
  "postId": "This is a good post"
}
- Delete comment
DELETE /comments/{commentId}
- Get comments by post
GET /comments/post/{postId}
## Developer
Baranetskyi Vitalii

Email: vitalik.baranetskiy@gmail.com
