<%--
  Created by IntelliJ IDEA.
  User: Laptop
  Date: 28.08.2025
  Time: 12:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${account.username()} - Profile</title>
</head>
<body>
<jsp:include page="_header.jsp"/>
<div class="container">
    <div class="col-2">
        ${account.username()}
    </div>
</div>

</body>
</html>
