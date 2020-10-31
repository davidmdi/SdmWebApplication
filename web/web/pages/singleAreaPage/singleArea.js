var refreshRate = 10000; //milli seconds
var alertsRefreshRate = 5000; //milli seconds
var MENU_URL = buildUrlWithContextPath("singleAreaMenu");
var ZONE_NAME_URL = buildUrlWithContextPath("zoneName");
var AREAS_ITEMS_TABLE_URL = buildUrlWithContextPath("areasItemsTable");
var AREAS_STORES_TABLE_URL = buildUrlWithContextPath("areasStoresTable");

var CREATE_NEW_ALERT = buildUrlWithContextPath("createNewAlert")

$(function() {
//init:
    ajaxCreateMenuByUserType();
    ajaxInsertZoneNameHeadLine();
    ajaxAreaInfo();

//The users list is refreshed automatically every second
    setInterval(ajaxAreaInfo, refreshRate);
    setInterval(ajaxCheckAlerts, alertsRefreshRate); //check for alerts
});

function ajaxCheckAlerts(){
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

function ajaxAreaItemsTable(){
    $.ajax({
        url: AREAS_ITEMS_TABLE_URL,
        error: function(e){alert(e);},
        success: function(areasItemsTableResp) {
            $('#itemsTable').replaceWith(areasItemsTableResp);
        }
    });
}

function ajaxAreaStoresTable(){
    $.ajax({
        url: AREAS_STORES_TABLE_URL,
        error: function(e){alert(e);},
        success: function(areasStoresTableResp) {
            $('#storesTable').replaceWith(areasStoresTableResp);
        }
    });
}

function ajaxAreaInfo() {
    ajaxAreaItemsTable();
    ajaxAreaStoresTable();
}

function ajaxInsertZoneNameHeadLine() {
    $("#zone").empty(); //clear old zone name
     $.ajax({
        url: ZONE_NAME_URL,
        success: function(response) {
            $("#zone").replaceWith(response);
        }
    });
}

function ajaxCreateMenuByUserType() {
    $("#menu").empty(); //clear old menu
     $.ajax({
        url: MENU_URL,
        success: function(response) {
            $("#menu").replaceWith(response);
        }
    });
}
