<%--
  Created by IntelliJ IDEA.
  User: Laptop
  Date: 26.08.2025
  Time: 19:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
<head>
    <title></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js" integrity="sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI" crossorigin="anonymous"></script>
    <style>
        .avatar {
            width: 32px;
            height: 32px;
            border-radius: 50%;
            object-fit: cover;
            margin-right: 8px;
        }
        .navbar-nav .nav-link {
            display: flex;
            align-items: center;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">Twitter-CLONE</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarNav">
            <div class="navbar-nav me-auto">
                <a class="nav-link active" aria-current="page" href="/">Home</a>
                <c:choose>
                    <c:when test="${not empty sessionScope.account}">
                        <a class="nav-link" href="#" onclick="document.getElementById('logoutForm').submit(); return false;">Log Out</a>
                        <form id="logoutForm" action="${pageContext.request.contextPath}/logout" method="post" style="display:none;">
                        </form>
                        <a class="nav-link" href="${pageContext.request.contextPath}/create">Create Post</a>
                    </c:when>
                    <c:otherwise>
                        <a class="nav-link" href="${pageContext.request.contextPath}/login">Log In</a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/signup">Sign Up</a>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- аватарка и имя пользователя -->
            <div class="navbar-nav ms-auto">
                <c:choose>
                    <c:when test="${not empty sessionScope.account}">
                        <a class="nav-link" href="${pageContext.request.contextPath}/profile">
                            <img src="https://ui-avatars.com/api/?name=${account.username()}&background=random&size=32&rounded=true&format=svg"
                                 alt="${account.username()}" class="avatar">
                                ${account.username()}
                        </a>
                    </c:when>
                    <c:otherwise>
                        <span class="nav-link disabled">
                            <img src="https://ui-avatars.com/api/?name=Guest&background=cccccc&size=32&rounded=true&format=svg"
                                 alt="Guest" class="avatar">
                            Guest
                        </span>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</nav>
</body>
</html>