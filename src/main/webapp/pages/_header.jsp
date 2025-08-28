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
</head>
<body>
<nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container-fluid">
        <a class="navbar-brand" href="/">Twitter-CLONE</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
            <div class="navbar-nav">
                <a class="nav-link active" aria-current="page" href="/">Home</a>
                <c:choose>
                    <c:when test="${not empty sessionScope.account}">
                        <form action="${pageContext.request.contextPath}/logout" method="post" style="display:inline;">
                            <button type="submit" class="nav-link btn btn-link">Log Out</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <a class="nav-link" href="${pageContext.request.contextPath}/login">Log In</a>
                        <a class="nav-link" href="${pageContext.request.contextPath}/signup">Sign Up</a>
                    </c:otherwise>
                </c:choose>
                <a class="nav-link disabled " aria-disabled="true">
                    <c:choose>
                        <c:when test="${not empty sessionScope.account}">
                            <a class = "nav-link" href="${pageContext.request.contextPath}/profile">
                                    ${account.username()}
                            </a>
                        </c:when>
                        <c:otherwise>
                            Guest
                        </c:otherwise>
                </c:choose></a>
            </div>
        </div>
    </div>
</nav>
</body>
</html>
