<%--
  Created by IntelliJ IDEA.
  User: myan
  Date: 2017/8/9
  Time: 10:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="BASE" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Customer List</title>
</head>
<body>
<h1>List table</h1>
<table>
    <tr>
        <th>Name</th>
        <th>Contact</th>
        <th>Telephone</th>
        <th>Email</th>
        <th>Operation</th>
    </tr>
    <c:forEach var="c" items="${customers}">
        <tr>
            <td>${c.name}</td>
            <td>${c.contact}</td>
            <td>${c.telephone}</td>
            <td>${c.email}</td>
            <td>
                <a href="${BASE}/customer/edit?id=${c.id}">Edit</a>
                <a href="${BASE}/customer/delete?id=${c.id}">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
