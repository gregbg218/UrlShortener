<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>URL Shortener</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
</head>
<body>
    <header class="header">
        <h1>URL Shortener</h1>
    </header>
    <div class="container">
        <form action="shorten" method="POST" class="url-form">
            <label for="url">Enter URL:</label>
            <input type="text" id="url" name="url" class="url-input" required>
            <button type="submit" class="shorten-button">Shorten</button>
        </form>
    </div>
</body>
</html>