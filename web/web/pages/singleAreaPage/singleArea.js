var refreshRate = 10000; //milli seconds
var alertsRefreshRate = 5000; //milli seconds
var MENU_URL = buildUrlWithContextPath("singleAreaMenu");
var ZONE_NAME_URL = buildUrlWithContextPath("zoneName");
var CHECK_ALERTS_URL = buildUrlWithContextPath("getAlerts");
var AREAS_ITEMS_TABLE_URL = buildUrlWithContextPath("areasItemsTable");
var AREAS_STORES_TABLE_URL = buildUrlWithContextPath("areasStoresTable");

$(function() {
//init:
    ajaxCreateMenuByUserType();
    ajaxInsertZoneNameHeadLine();
    ajaxAreaInfo();

//The users list is refreshed automatically every second
    setInterval(ajaxAreaInfo, refreshRate);
    setInterval(ajaxAlertsList, alertsRefreshRate);//refreshRate); //check for alerts
});

function ajaxAlertsList() {
    $.ajax({
        url: CHECK_ALERTS_URL,
        success: function(alerts) {
            alertsHandler(alerts);
        }
    });
}

function alertsHandler(alerts){
    console.log("alert "+ alerts);

    $.each(alerts || [], function(index, alertMsg) {
        alert(alertMsg);
    });
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
