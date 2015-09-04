<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hubbub&trade; ${applicationScope.version} -- ${subject}'s Wall</title>
        <link rel="stylesheet" type="text/css" href="styles/main.css"/>
    </head>
    <body>
        <img src="images/hubbub.png"/><br/>
        <h1>${subject}'s Wall</h1>
        <h2 class="flash">${flash}</h2>
        <p><a href="main?action=profile&for=${subject}">View ${subject}'s Profile<a> |
            <a href="main">Back to the Timeline</a> | 
            <a href="main?action=post">Post Something</a> | 
            <a href="main?action=logout">Log Out</a></p>
        <c:forEach var="post" items="${posts}">
            <div class="postdiv">
                <div class="contentdiv">
                    ${post.content}
                </div>
                    <span class="datespan">Posted <fmt:formatDate type="DATE" value="${post.postDate}"/></span>
            </div>
        </c:forEach>
        <h3 class="footer">
            Copyright 2015 www.austincc.edu and www.bytecaffeine.com
        </h3>
    </body>
</html>
