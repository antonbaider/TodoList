<%@ page import="com.softserve.itacademy.model.Priority" %>
<%@ page import="com.softserve.itacademy.model.Task" %>
<%@ page import="com.softserve.itacademy.repository.TaskRepository" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Create new Task</title>

    <style>
        <%@include file="../styles/main.css"%>
    </style>
    
</head>
<body>
    <%@include file="header.html"%>
    <h2>Create a New Task</h2>
    <c:if test="${param.error eq 'duplicate'}">
        <p class="error">Task with a given name already exists!</p>
    </c:if>
    <c:if test="${param.error eq 'invalid'}">
        <p class="error">Invalid input</p>
    </c:if>
    <c:if test="${not empty requestScope.errorMessage}">
        <p class="error">${requestScope.errorMessage}</p>
    </c:if>
    <form action="/create-task" method="post">
        <label for="title">Name:</label>
        <input type="text" id="title" name="title" value="${not empty param.title ? param.title : ''}" required><br>

        <label for="priority">Priority:</label>
        <select id="priority" name="priority" required>
            <c:forEach var="priority" items="${requestScope.priorities}">
                <option value="${priority}" ${param.priority eq priority ? 'selected' : ''}>${priority}</option>
            </c:forEach>
        </select><br>

        <button type="submit">Create Task</button>
    </form>

<c:if test="${param.ok eq 'success'}">
    <table><p class="success">Task added successfully!</p>
        <thead><div class="success">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Priority</th>
        </tr></div>
        </thead>
        <tbody>
        <% Task lastTask = TaskRepository.getTaskRepository().all().stream().reduce((first, second) -> second).orElse(null); %>
        <% if (lastTask != null) { %>

        <tr><div class="success">
            <td><%= lastTask.getId() %></td>
            <td><%= lastTask.getTitle() %></td>
            <td><%= lastTask.getPriority() %></td>
        </div></tr>

        <% } else { %>
        <tr>
            <td colspan="3">No tasks available.</td>
        </tr>
        <% } %>
        </tbody>
    </table>
</c:if>
</body>
</html>
