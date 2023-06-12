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
- Register a user<br>
POST /users/register?username=name&password=pass
- User authorization<br>
GET /auth/login?username=name&password=pass
- User logout<br>
POST /auth/logout<br>
Authorization: Bearer your-access-token
## Requests that require a token (Authorization: Bearer your-access-token):
- Get all users<br>
GET /users/all
- Update a user<br>
PUT /users/{id}?username=name&password=pass
- Delete a user<br>
DELETE /users/{id}
- Get a user by id<br>
GET /users/{id}
- Get a user by username<br>
GET /users?username=name
- Follow a user<br>
POST /followers/follow?followerId=id1&followeeId=id2
- Unfollow a user<br>
DELETE /followers/unfollow?followerId=id1&followeeId=id2
- User's followers<br>
GET /followers/followersByUser/{userId}
- User's followees<br>
GET /followers/followeesByUser/{userId}
- Create a post<br>
POST /posts<br>
Content-Type: application/json<br>
{<br>
&nbsp;&nbsp;&nbsp;"content": "This is my new post!",<br>
&nbsp;&nbsp;&nbsp;"authorId": "author-id"<br>
}
- Update post<br>
PUT /posts/{id}<br>
Content-Type: application/json<br>
{<br>
&nbsp;&nbsp;&nbsp;"content": "This is my CHANGED post!"<br>
}
- Delete post (with all likes and comments)<br>
DELETE /posts/{id}
- Get posts by author<br>
GET /posts/author/{authorId}
- Get feed by user (All posts by those to whom the user is followed)<br>
GET /posts/feed/{userId}
- Like the post<br>
POST /posts?userId=id1&postId=id2
- Unlike the post<br>
DELETE /posts?userId=id1&postId=id2
- Get likes by post<br>
GET /posts/post/{postId}
- Comment the post<br>
POST /comments<br>
Content-Type: application/json<br>
{<br>
&nbsp;&nbsp;&nbsp;"content": "This is a good post"<br>
&nbsp;&nbsp;&nbsp;"authorId": "author-id"<br>
&nbsp;&nbsp;&nbsp;"postId": "post-id"<br>
}
- Delete comment<br>
DELETE /comments/{commentId}
- Get comments by post<br>
GET /comments/post/{postId}
# Future plans:
- Create roles
- Counting likes and comments
## Developer
Baranetskyi Vitalii

Email: vitalik.baranetskiy@gmail.com
