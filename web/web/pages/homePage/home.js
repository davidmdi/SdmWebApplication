
var refreshRate = 2000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("userslist");
var LOAD_XML_URL = buildUrlWithContextPath("loadXml");
var UPLOAD_XML_URL = buildUrlWithContextPath("uploadXmlFile");

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

                            $("#uploadForm").attr('action', UPLOAD_XML_URL);
                            $("#uploadForm").submit(function() {

                                var file = this[0].files[0];

                                var formData = new FormData();
                                formData.append("fake-key-1", file);
                                formData.append("name", this[2].value);

                                $.ajax({
                                    method:'POST',
                                    data: formData,
                                    url: this.action,
                                    processData: false, // Don't process the files
                                    contentType: false, // Set content type to false as jQuery will tell the server its a query string request
                                    timeout: 4000,
                                    error: function(e) {
                                        console.error("Failed to submit");
                                        //$("#result").text("Failed to get result from server " + e);
                                    },
                                    success: function(r) {
                                    console.error("xml was loaded");
                                        //$("#result").text(r);
                                    }
                                });

                                // return value of the submit operation
                                // by default - we'll always return false so it doesn't redirect the user.
                                return false;
                            })

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
/*
$(function() { // onload...do

                $("#uploadForm").attr('action', UPLOAD_XML_URL);
                $("#uploadForm").submit(function() {

                    var file = this[0].files[0];

                    var formData = new FormData();
                    formData.append("fake-key-1", file);
                    formData.append("name", this[2].value);

                    $.ajax({
                        method:'POST',
                        data: formData,
                        url: this.action,
                        processData: false, // Don't process the files
                        contentType: false, // Set content type to false as jQuery will tell the server its a query string request
                        timeout: 4000,
                        error: function(e) {
                            console.error("Failed to submit");
                            //$("#result").text("Failed to get result from server " + e);
                        },
                        success: function(r) {
                        console.error("xml was loaded");
                            //$("#result").text(r);
                        }
                    });

                    // return value of the submit operation
                    // by default - we'll always return false so it doesn't redirect the user.
                    return false;
                })
            })
            */