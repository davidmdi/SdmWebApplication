<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>SdmMarket</title>
    <link rel="stylesheet" href="login.css">
</head>
<body>
<div class="container">
    <form method="GET" action="login">
        <div class="imgcontainer">
            <img src="../../resources/sdm_logo.png" alt="Super Duper Market" class="logo">
        </div>
        <hr>
        <div class="container">
            <div id ="user">
                <label for="username">User name: </label>
                <input type="text" id="username" name="username" class="" placeholder="Enter user name" required/>
            </div>
            <div class='radiobuttons-container'>
                <label for="username">User Type: </label>

                <label for="customer">
                    <input type="radio" id="customer" name="typeofuser" value="customer" checked=true>
                    Customer
                </label>

                <label for="owner">
                    <input type="radio" id="owner" name="typeofuser" value="owner">
                    Owner
                </label>
            </div>
            <br>
            <br>
            <input type="submit" value="Login" class = "login-button"/>
        </div>
    </form>
    <label style="color:red">User name already exists. Please enter a different username.</label>
</div>
</body>
</html>

