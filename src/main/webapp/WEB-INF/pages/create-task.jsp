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

<div class="container">
    <%@include file="header.html" %>
    <div class="text">Create a New Task</div>
    <c:if test="${param.error eq 'invalid'}">
        <p class="error">Invalid input</p>
    </c:if>
    <c:if test="${not empty requestScope.errorMessage}">
        <p class="error">${requestScope.errorMessage}</p>
    </c:if>
    <form action="/create-task" method="post">
        <div class="form-row">
            <div class="input-data">
                <input type="text" id="title" maxlength="128" name="title" value="${not empty param.title ? param.title : ''}" required><br>
                <div class="underline"></div>
                <label for="title">Name:</label>
            </div>
            <div class="input-data">
                <br> <label for="priority">Priority:</label>
        <select id="priority" name="priority" required>
            <c:forEach var="priority" items="${requestScope.priorities}">
                <option value="${priority}" ${param.priority eq priority ? 'selected' : ''}>${priority}</option>
            </c:forEach>
        </select>
                <div class="underline"></div>

            </div>
        </div>

        <div class="form-row submit-btn">
            <div class="input-data">
                <div class="inner"></div>
                <input type="submit" value="create">
            </div>
        </div>
    </form>

<c:if test="${param.ok eq 'success'}">
    <table class="table"><p class="success">Task added successfully!</p>
        <div class="success">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Priority</th>
        </tr></div>

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

    </table>
</c:if>
</div>
</body>
</html>
