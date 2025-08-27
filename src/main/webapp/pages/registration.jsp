<%--
  Created by IntelliJ IDEA.
  User: Laptop
  Date: 27.08.2025
  Time: 12:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
  <jsp:include page="_header.jsp"/>
  <div class="container">
      <div class="row align-items-center justify-content-center">
          <div class="col-3">
            <form>
                <div class="mb-3">
                    <label for="exampleInputUsername" class="form-label">Username</label>
                    <input type="text" class="form-control" id="exampleInputUsername" aria-describedby="usernameHelp">
                    <div id="usernameHelp" class="form-text">Username should be unique.</div>
               </div>
               <div class="mb-3">
                   <label for="exampleInputPassword1" class="form-label">Password</label>
                   <input type="password" class="form-control" id="exampleInputPassword1">
               </div>
              <div class="mb-3">
                     <label for="exampleInputPassword2" class="form-label">Repeat password</label>
                   <input type="password" class="form-control" id="exampleInputPassword2">
              </div>
      <button type="submit" class="btn btn-primary w-100">Register</button>

  </form>
  </div>
  </div>
  </div>
</body>
</html>
