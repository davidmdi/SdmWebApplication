
var refreshRate = 2000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("userslist");
var LOAD_XML_URL = buildUrlWithContextPath("loadXml");

//activate the timer calls after the page is loaded
$(function() {

    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);

    ajaxLoadXml();
    //The chat content is refreshed only once (using a timeout) but
    //on each call it triggers another execution of itself later (1 second later)
            // triggerAjaxChatContent();
});

function ajaxUsersList() {
    $.ajax({
        url: USER_LIST_URL,
        success: function(users) {
            refreshUsersList(users);
        }
    });
}

function ajaxLoadXml() {
    $("#loadXml").empty();
     $.ajax({
        url: LOAD_XML_URL,
        success: function(response) {
            //refreshUsersList(users);
            $("#loadXml").replaceWith(response);
        }
    });
}

//users = a list of usernames, essentially an array of javascript strings:
// ["moshe","nachum","nachche"...]
function refreshUsersList(users) {
    //clear all current users
    $("#userslist").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(users || [], function(index, user) {
        console.log("Adding user #" + index + ": " + user.name +", type: "+user.type);
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
        $('<li>' + user.name+", "+user.type + '</li>').appendTo($("#userslist"));
    });
}