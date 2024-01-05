<%@ page import="com.softserve.itacademy.model.Task" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Read existing Task</title>

    <style>
        <%@include file="../styles/main.css"%>
    </style>

</head>
<body><h2>Read existing Task</h2>
<table>
    <%@include file="header.html"%>

        <table>
            <thead>
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Priority</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>${task.id}</td>
                <td>${task.title}</td>
                <td>${task.priority}</td>
            </tr>
            </tbody>
        </table>

</body>
</html>
