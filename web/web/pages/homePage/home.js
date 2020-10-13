
var refreshRate = 2000; //milli seconds
var refreshRateForAreas = 5000; //milli seconds
var refreshRateForAccount = 10000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("userslist");
var LOAD_XML_URL = buildUrlWithContextPath("loadXml");
var UPLOAD_XML_URL = buildUrlWithContextPath("uploadXmlFile");
var AREAS_TABLE_URL = buildUrlWithContextPath("areasTable");
var ACCOUNT_ACTIONS_URL = buildUrlWithContextPath("accountTable");

//activate the timer calls after the page is loaded
$(function() {
     //init:
    ajaxLoadXml();
    ajaxUsersList();
    ajaxAreasTable();
    ajaxAccountActionsTable();

    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxAreasTable, refreshRateForAreas);
    setInterval(ajaxAccountActionsTable, refreshRateForAccount);


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
        console.log("Adding user #" + index + ": " + user.name +", type: "+user.type);
        //create a new <option> tag with a value in it and
        //appeand it to the #userslist (div with id=userslist) element
        $('<li>' + user.name+", "+user.type + '</li>').appendTo($("#userslist"));
    });
}


function ajaxAreasTable() {
    $.ajax({
        url: AREAS_TABLE_URL,
        success: function(areas) {
            refreshAreasTable(areas);
        }
    });
}

function refreshAreasTable(areas) {
    $("#areasTable").empty();    //clear all current areas

    var tableHeaders = "<tr>" +
                        "<th>Owner name</th>" +
                        "<th>Zone name</th>" +
                        "<th>Total products for sell</th>" +
                        "<th>Total stores in area</th>" +
                        "<th>Total orders</th>" +
                        "<th>Avg orders price</th>" +
                    "</tr>";

    $(tableHeaders).appendTo($("#areasTable"));

    // rebuild the list of users: scan all areas and add them to the table of areas
    $.each(areas || [], function(index, area) {
        var name = area.owner.user.name;
        var zone = area.zoneName;
        var itemsNum = area.items.itemList.length;
        var storesNum = area.stores.storeList.length;
        var ordersNum = area.orders.avgOrdersPrice;
        var avgOrdersPrice = area.orders.avgOrdersPrice;

        var areaInfo = "<tr>" +
                            "<td>"+name+"</td>" +
                            "<td>"+zone+"</td>" +
                            "<td>"+itemsNum+"</td>" +
                            "<td>"+storesNum+"</td>" +
                            "<td>"+ordersNum+"</td>" +
                            "<td>"+avgOrdersPrice+"</td>" +
                        "</tr>";
        $(areaInfo).appendTo($("#areasTable"));
    });

    if(areas.length == 0 ){
        var emptyAreas = "<tr><td>There are no areas</td></tr>";
        console.log(areas);
        $(emptyAreas).appendTo($("#areasTable"));
    }
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
        "<th>Balance before action</th>" +
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
            "<td>"+sum+"</td>" +
            "<td>"+balanceBefore+"</td>" +
            "<td>"+balanceAfter+"</td>" +
            "</tr>";
        $(actionInfo).appendTo($("#accountActionsTable"));
    });

    if(accountActions.length == 0 ){
        var emptyActions = "<tr><td>There are no actions</td></tr>";
        $(emptyActions).appendTo($("#accountActionsTable"));
    }
}