<%@ page import="com.softserve.itacademy.model.Task" %>
<%@ page import="com.softserve.itacademy.model.Priority" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.softserve.itacademy.repository.TaskRepository" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Edit existing Task</title>

    <style>
        <%@include file="../styles/main.css"%>
    </style>

</head>
<body>
<div class="container">
    <%@include file="header.html" %>
    <div class="text">Edit existing Task</div>
    <% Task task = TaskRepository.getTaskRepository().read(Integer.parseInt(request.getParameter("id"))); %>
    <% if (task != null) { %>
    <form action="/edit-task" method="post">
        <div class="form-row">
            <div class="input-data">
                <div class="underline"></div>
                <input type="text" id="id" name="id" value="<%= task.getId() %>" disabled><br>
            </div>
            <div class="input-data">
                <div class="underline"></div>
        <input type="text" id="name" name="name" value="<%= task.getTitle() %>" required><br>
                <label for="name">Name:</label>
            </div>
            <div class="input-data">
                <br> <label for="priority">Priority:</label>
        <select id="priority" name="priority" required>
            <c:forEach var="priority" items="${requestScope.priorities}">
                <option value="${priority}" ${priority eq task.getPriority() ? 'selected' : ''}>${priority}</option>
            </c:forEach>
        </select>
                <div class="underline"></div>
            </div>
        </div>

        <br>
        <div class="form-row submit-btn">
            <div class="input-data">
                <div class="inner"></div>
                <input type="submit" value="update">
            </div>
        </div>
    </form>
    <% } %>
    <div class="underline"></div>
</div>
</body>
</html>
