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
<body class="container mt-4">
<p class="text-center h4">Создать пост</p>
<form action="create" method="post">
  <div class="mb-3">
    <label class="form-label">Заголовок</label>
    <input type="text" name="title" class="form-control" required/>
  </div>
  <div class="mb-3">
    <label class="form-label">Контент</label>
    <textarea name="content" class="form-control" rows="5" required></textarea>
  </div>
  <div class="col-3 mx-auto">
    <a class="btn btn-primary" href="#" role="button">Опубликовать</a>
    <a class="btn btn-primary" href="/" role="button">На главную</a>
  </div>


</form>
</body>
</html>
