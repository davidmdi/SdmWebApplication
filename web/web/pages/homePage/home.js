
var refreshRate = 2000; //milli seconds
var alertsRefreshRate = 5000; //milli seconds
var refreshRateForAreas = 5000; //milli seconds
var refreshRateForAccount = 5000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("userslist");
var LOAD_XML_URL = buildUrlWithContextPath("loadXml");
var UPLOAD_XML_URL = buildUrlWithContextPath("uploadXmlFile");
var AREAS_TABLE_URL = buildUrlWithContextPath("areasTable");
var ACCOUNT_ACTIONS_URL = buildUrlWithContextPath("accountTable");
var SELECTED_ZONE_URL = buildUrlWithContextPath("zoneSelected");
var USER_NAME_URL = buildUrlWithContextPath("userName");
var LOAD_MONEY_FORM = buildUrlWithContextPath("loadMoneyForm");
var CHECK_ALERTS_URL = buildUrlWithContextPath("getAlerts");
var LOAD_MONY_ON_ACTION = buildUrlWithContextPath("loadMoneyOnAction")
var CREATE_NEW_ALERT = buildUrlWithContextPath("createNewAlert")

//activate the timer calls after the page is loaded
// The onLoad function...
$(function() {
     //init:
    ajaxInsertUserNameGreeting();
    ajaxLoadXml();
    ajaxShowLoadMoneyForm();
    ajaxUsersList();
    ajaxAreasTable();
    ajaxAccountActionsTable();

    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxAreasTable, refreshRateForAreas);
    setInterval(ajaxAccountActionsTable, refreshRateForAccount);
    setInterval(ajaxAlerts, alertsRefreshRate); //check for alerts
});

function ajaxAlerts() {
    $.ajax({
        url: CREATE_NEW_ALERT,
        success: function(alert) {
            $("#userAlerts").append(alert);
            addAlertsClickHandler();
        }
    });
}

function addAlertsClickHandler(){
    var okButtons = document.getElementsByName("okButton");

    for (var i = 0 ; i < okButtons.length ; i++){
        var btn = okButtons[i];
        btn.onclick = removeAlert;
    }
}

function removeAlert(event){
    $(this.parentElement).remove();
}

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

                $("#uploadForm").submit(function() {

                    var file = this[0].files[0];

                    var formData = new FormData();
                    formData.append("xmlFile", file);

                    $.ajax({
                        method:'POST',
                        data: formData,
                        url: UPLOAD_XML_URL,
                        processData: false, // Don't process the files
                        contentType: false, // Set content type to false as jQuery will tell the server its a query string request
                        timeout: 4000,
                        error: function(e) {
                            console.error("Failed to submit");
                            $("#errorMsg").text("Failed to get XML file " + e);//.text("Failed to get XML file " + e);
                        },
                        success: function(errorMsg) {
                            //console.error("xml was loaded");
                            $("#errorMsg").text(errorMsg);//.text(errorMsg);
                            ajaxAreasTable();
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
        //console.log("Adding user #" + index + ": " + user.name +", type: "+user.type);
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
        $('<li>' + user.name+", "+user.type + '</li>').appendTo($("#userslist"));
    });
}


function ajaxAreasTable() {
    $.ajax({
        url: AREAS_TABLE_URL,
        error: function(e){alert(e);},
        success: function(areasTableResp) {
            $('#areasTable').replaceWith(areasTableResp);
            addZoneClickEvent();
        }
    });
}

function ajaxAccountActionsTable() {
    $.ajax({
        url: ACCOUNT_ACTIONS_URL,
        success: function(accountActions) {
            refreshAccountTable(accountActions);
        }
    });
}

function refreshAccountTable(accountActions) {
    $("#accountActionsTable").empty();    //clear all current areas

    var tableHeaders = "<tr>" +
        "<th>Action type</th>" +
        "<th>Date</th>" +
        "<th>Sum</th>" +
        "<th>Balance before action</th>" +
        "<th>Balance after action</th>" +
        "</tr>";

    $(tableHeaders).appendTo($("#accountActionsTable"));

    // rebuild the list of users: scan all areas and add them to the table of areas
    $.each(accountActions || [], function(index, action) {
        var type = action.type;
        var date = action.date;
        var sum = action.sumOfAction;
        var balanceBefore = action.balanceBefore
        var balanceAfter = action.balanceAfter;

        var actionInfo = "<tr>" +
            "<td>"+type+"</td>" +
            "<td>"+date+"</td>" +
            "<td>"+sum.toFixed(2)+"</td>" +
            "<td>"+balanceBefore.toFixed(2)+"</td>" +
            "<td>"+balanceAfter.toFixed(2)+"</td>" +
            "</tr>";
        $(actionInfo).appendTo($("#accountActionsTable"));
    });

    if(accountActions.length == 0 ){
        var emptyActions = "<tr><td>There are no actions</td></tr>";
        $(emptyActions).appendTo($("#accountActionsTable"));
    }
}


function addZoneClickEvent(){
    var cells = document.getElementsByName("area");//document.getElementById("trArea");
    for (var i = 0 ; i < cells.length ; i++){
        var cell = cells[i];
        cell.onmouseup = myZoneClickEvent;
    }
};

//declaration of a function that will be called on mouse clicks
function myZoneClickEvent (event) {
    console.log("clicked");
    var area = event.currentTarget.attributes['selectedArea'].value;
    document.getElementById('selected_area').value = area;
    var myform = document.forms['areaSelect'];
    myform.setAttribute("action", SELECTED_ZONE_URL);
    myform.submit();
}

function ajaxInsertUserNameGreeting() {
    $("#userName").empty(); //clear old zone name
     $.ajax({
        url: USER_NAME_URL,
        success: function(response) {
            $("#userName").replaceWith(response);
        }
    });
}

function ajaxShowLoadMoneyForm(){
    $("#loadingMoneyToAccount").empty();

    $.ajax({
        url: LOAD_MONEY_FORM,
        success: function(response) {//if customer: load money form. else: empty div
            //refreshUsersList(users);
            $("#loadingMoneyToAccount").replaceWith(response);
        }
    });
}

function ajaxLoadMoney(){
    var moneyData = $("#moneyToLoad").val();
    $.ajax({
        data: moneyData,
        contentType: false,//"multipart/form-data",
        url: LOAD_MONY_ON_ACTION ,
        method: 'POST',
        success: function(response) {
            //refreshUsersList(users);
           alert("Load Money Successfully");
        }
    });
}