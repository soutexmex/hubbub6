<%@page contentType="text/html" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Hubbub&trade; ${applicationScope.version} -- Profile for ${subject}</title>
        <link rel="stylesheet" type="text/css" href="styles/main.css"/>
    </head>
    <body>
        <img src="images/hubbub.png"/><br/>
        <h1>Hubbub Profile for <a href="main?action=wall&for=${subject}">${subject}</a></h1>
        <h2 class="flash">${flash}</h2>
        <p><a href="main">Back to the Timeline</a> |
            <a href="main?action=wall&for=${subject}">Back to ${subject}'s Wall</a> |
            <a href="main?action=post">Post Something</a> |
            <a href="main?action=logout">Log me out</a>
        <p>
        <div class="postdiv">
            <form method="POST" action="main">
                <input type="hidden" name="action" value="profile"/>
                <input type="hidden" name="for" value="${subject}"/>
                <table id="formtable">
                    <tr><td>Change your Password:</td><td><input type="password" name="pass1" placeholder="6 to 15 characters."></td></tr>
                    <tr><td>Re-enter the new Password:</td><td><input type="password" name="pass2"/></td></tr>
                    <tr><td>Your Email Address:</td><td><input value="${profile.email}" type="email" name="email"/></td></tr>
                    <tr><td>Your Zip Code:</td><td><input value="${profile.zip}" type="text" name="zip"/></td></tr>
                    <tr><td>Update your Story:</td><td><textarea rows="10" cols="100" name="biography">${profile.biography}</textarea></td></tr>
                    <tr><td></td><td><input type="reset" value="Never mind."/>&nbsp;&nbsp;&nbsp;<input type="submit" value="Change me up!"/></td></tr>
                </table>
            </form>
        </div>
        </p>
        <h3 class="footer">
            Copyright 2015 www.austincc.edu and www.bytecaffeine.com
        </h3>
    </body>
</html>
