<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Home</title>
</head>

<body>
    <h1>KDT Spring App</h1>

    <p>테스트 스트링 : ${testString} </p>

    <table>
        <tr>
            <th>customerId</th>
            <th>name</th>
            <th>email</th>
            <th>createdAt</th>
            <th>lastLoginAt</th>
        </tr>
        <c:forEach var="customer" items="${customers}">
            <tr>
                <td>${customer.customerId}</td>
                <td>${customer.name}</td>
                <td>${customer.email}</td>
                <td>${customer.createdAt}</td>
                <td>${customer.lastLoginAt}</td>
            </tr>
        </c:forEach>
    </table>

    <image src="<c:url value="/resources/1.png" />"  />


</body>

</html>