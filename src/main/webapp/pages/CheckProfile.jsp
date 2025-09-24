<%--
  Created by IntelliJ IDEA.
  User: Максим
  Date: 03.09.2025
  Time: 20:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
<head>
  <html>
  <head>
    <title>${account.username} - Profile</title>
  </head>
  <body>
  <jsp:include page="_header.jsp"/>
  <p><strong>ID:</strong> ${id}</p>
  <p><strong>Username:</strong> ${username}</p>
</body>
</html>
