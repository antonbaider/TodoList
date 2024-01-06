<%@ page import="com.softserve.itacademy.model.Task" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Read existing Task</title>

    <style>
        <%@include file="../styles/main.css"%>
    </style>

</head>
<body><div class="container">
    <%@include file="header.html"%>
    <div class="text">Read existing Task</div>



    <table class="table">

            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Priority</th>
            </tr>


            <tr>
                <td>${task.id}</td>
                <td>${task.title}</td>
                <td>${task.priority}</td>
            </tr>

        </table>
</div>
</body>
</html>
