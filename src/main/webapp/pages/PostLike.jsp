<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Список постов</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
        }
        .post {
            border: 1px solid #ccc;
            padding: 20px;
            margin-bottom: 20px;
            border-radius: 8px;
        }
        .like-button {
            background-color: #ff4d4d;
            color: white;
            border: none;
            padding: 8px 12px;
            border-radius: 4px;
            cursor: pointer;
        }
        .like-button:hover {
            background-color: #e60000;
        }
    </style>
</head>
<body>
<h1>Посты</h1>

<c:forEach var="post" items="${posts}">
    <div class="post">
        <h2>${post.title}</h2>
        <p>${post.content}</p>
        <p><strong>Лайков:</strong> ${post.likes}</p>
        <form action="like" method="post">
            <input type="hidden" name="id" value="${post.id}" />
            <button type="submit" class="like-button">❤️ Лайк</button>
        </form>
    </div>
</c:forEach>

</body>
</html>