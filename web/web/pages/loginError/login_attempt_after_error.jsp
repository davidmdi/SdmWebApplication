<%--&lt;%&ndash;--%>
<%--    Document   : index--%>
<%--    Created on : Jan 24, 2012, 6:01:31 AM--%>
<%--    Author     : blecherl--%>
<%--    This is the login JSP for the online chat application--%>
<%--&ndash;%&gt;--%>

<%--<%@page contentType="text/html" pageEncoding="UTF-8"%>--%>
<%--<!DOCTYPE html>--%>
<%--<html>--%>
<%--    <%@page import="utils.*" %>--%>
<%--    <%@ page import="constants.Constants" %>--%>
<%--    <head>--%>
<%--        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">--%>
<%--        <title>Online Chat</title>--%>
<%--<!--        Link the Bootstrap (from twitter) CSS framework in order to use its classes-->--%>
<%--        <!--      <link rel="stylesheet" href="../../common/bootstrap.min.css"/> -->--%>
<%--      <!--        Link jQuery JavaScript library in order to use the $ (jQuery) method-->--%>
<%--<!--        <script src="script/jquery-2.0.3.min.js"></script>-->--%>
<%--<!--        and\or any other scripts you might need to operate the JSP file behind the scene once it arrives to the client-->--%>
<%--    </head>--%>
<%--    <body>--%>
<%--        <div class="container">--%>
<%--            <% String usernameFromSession = SessionUtils.getUsername(request);%>--%>
<%--            <% String usernameFromParameter = request.getParameter(Constants.USERNAME) != null ? request.getParameter(Constants.USERNAME) : "";%>--%>
<%--            <% if (usernameFromSession == null) {%>--%>
<%--            <h1>Welcome to the Online Chat</h1>--%>
<%--            <br/>--%>
<%--            <h2>Please enter a unique user name:</h2>--%>
<%--            <form method="GET" action="login">--%>
<%--                <input type="text" name="<%=Constants.USERNAME%>" value="<%=usernameFromParameter%>"/>--%>
<%--                <input type="submit" value="Login"/>--%>
<%--            </form>--%>
<%--            <% Object errorMessage = request.getAttribute(Constants.USER_NAME_ERROR);%>--%>
<%--            <% if (errorMessage != null) {%>--%>
<%--            <span class="bg-danger" style="color:red;"><%=errorMessage%></span>--%>
<%--            <% } %>--%>
<%--            <% } else {%>--%>
<%--            <h1>Welcome back, <%=usernameFromSession%></h1>--%>
<%--            <a href="../loginPage/login.html">Click here to enter the chat room</a>--%>
<%--            <br/>--%>
<%--            <a href="login?logout=true" id="logout">logout</a>--%>
<%--            <% }%>--%>
<%--        </div>--%>
<%--    </body>--%>
<%--</html>--%>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>SdmMarket</title>

    <!-- Link the Bootstrap (from twitter) CSS framework in order to use its classes-->
    <link rel="stylesheet" href="login.css">

    <!-- Link jQuery JavaScript library in order to use the $ (jQuery) method-->
    <!-- <script src="common/jquery-2.0.3.min.js"></script>-->
    <!-- and\or any other scripts you might need to operate the JSP file behind the scene once it arrives to the client-->
</head>
<body>
<div class="container">
    <form method="GET" action="login">
        <div>
            <img src="../../resources/sdm_logo.png" alt="Super Duper Market" class="logo">
        </div>
        <hr>
        <div id ="user">
            <label for="username">User name: </label>
            <input type="text" id="username" name="username" class="" placeholder="Enter user name" required/>
        </div>
        <input type="radio" id="customer" name="typeofuser" value="customer" checked=true>
        <label for="customer">Customer </label>
        <input type="radio" id="owner" name="typeofuser" value="owner">
        <label for="owner">Owner</label><br><br>
        <input type="submit" value="Login" class = "login-button"/>
        <label style="color:red">Username already exists. Please enter a different username.</label>
    </form>

</div>
</body>
</html>