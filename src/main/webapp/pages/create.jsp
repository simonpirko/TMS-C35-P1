<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 31.08.2025
  Time: 20:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Создать пост</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body >
<jsp:include page="_header.jsp"/>
<div class="d-flex vh-100">
<div class="container d-flex justify-content-center align-items-center">
  <div class="col-md-6">
    <p class="text-center h4 mb-4">Создать пост</p>
    <form action="create" method="post" class="card p-4 shadow">
      <div class="mb-3">
        <label class="form-label">Заголовок</label>
        <input type="text" name="title" class="form-control" required/>
      </div>
      <div class="mb-3">
        <label class="form-label">Контент</label>
        <textarea name="content" class="form-control" rows="5" required style="resize: none" height="150px"></textarea>
      </div>
      <div class="d-flex justify-content-center gap-3">
        <button type="submit" class="btn btn-primary">Опубликовать</button>
        <a class="btn btn-secondary" href="/" role="button">На главную</a>
      </div>
    </form>
  </div>
</div>
</div>
</body>
</html>

<%
  Object user = session.getAttribute("account");
  if (user == null) {
    response.sendRedirect("/login?error=auth");
    return;
  }
%>
</form>
</body>
</html>
