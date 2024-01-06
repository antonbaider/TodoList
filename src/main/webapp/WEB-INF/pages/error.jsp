<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Error Page</title>

    <style>
        <%@include file="../styles/main.css"%>
    </style>

</head>
<body><div class="container">
    <%@include file="header.html" %>
    <div class="text">Error</div>
    <p>${error}</p>
</div>
</body>
</html>
