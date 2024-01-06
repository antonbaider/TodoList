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
    <%@include file="header.html"%>
    <br>
    <h2>Edit existing Task</h2>
    <% Task task = TaskRepository.getTaskRepository().read(Integer.parseInt(request.getParameter("id"))); %>
    <% if (task != null) { %>
    <form action="/edit-task" method="post">
        <label for="id">Id:</label>
        <input type="text" id="id" name="id" value="<%= task.getId() %>" disabled><br>

        <label for="name">Name:</label>
        <input type="text" id="name" name="name" value="<%= task.getTitle() %>" required><br>

        <label for="priority">Priority:</label>
        <select id="priority" name="priority" required>
            <c:forEach var="priority" items="${requestScope.priorities}">
                <option value="${priority}" ${priority eq task.getPriority() ? 'selected' : ''}>${priority}</option>
            </c:forEach>
        </select><br>

        <button type="submit">Update Task</button>
    </form>
    <% } %>

</body>
</html>
