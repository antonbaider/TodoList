<%@ page import="com.softserve.itacademy.model.Task" %>
<%@ page import="java.util.List" %>
<%@ page import="com.softserve.itacademy.repository.TaskRepository" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>List of Tasks</title>

    <style>
        <%@include file="../styles/main.css"%>
    </style>

</head>
<body>
<div class="container">
    <%@include file="header.html" %>
    <div class="text">List of Tasks</div>

<table class="table">
    <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Priority</th>
        <th COLSPAN="3">Operations</th>
    </tr>

    <%
        for(Task task : (List<Task>)request.getAttribute("tasks")){
    %>

    <tr class="priority-<%=task.getPriority()%>">
        <td><%= task.getId()%></td>
        <td><%= task.getTitle()%></td>
        <td><%= task.getPriority()%></td>
        <td>
            <a href="/read-task?id=<%=task.getId()%>">Read</a>
        </td>
        <td>
            <a href="/edit-task?id=<%=task.getId()%>">Update</a>
        </td>
        <td>
            <a href="/delete-task?id=<%=task.getId()%>">Delete</a>
        </td>
    </tr>
    <%
        }
    %>
</table>
</div>
</body>
</html>
