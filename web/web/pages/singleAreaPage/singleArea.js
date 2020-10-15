
var refreshRate = 10000; //milli seconds
var AREA_INFO_URL = buildUrlWithContextPath("areaInfo");
var MENU_URL = buildUrlWithContextPath("singleAreaMenu");
var ZONE_NAME_URL = buildUrlWithContextPath("zoneName");

$(function() {
//init:
    ajaxCreateMenuByUserType();
    ajaxInsertZoneNameHeadLine();
    ajaxAreaInfo();

//The users list is refreshed automatically every second
    setInterval(ajaxAreaInfo, refreshRate);
});

function ajaxAreaInfo() {
    $.ajax({
        url: AREA_INFO_URL,
        success: function(areas) {
            var arrItems = [];
            var arrStores = [];

                for (var i = 0 ; i < areas.length ; i++){

                    var superItems = areas[i].items.itemList;
                    var stores = areas[i].stores.storeList;

                    for (var j = 0 ; j < superItems.length ; j++){
                        var item = superItems[j];
                        arrItems.push(item);
                    }

                    for (var j = 0 ; j < stores.length ; j++){
                        var store = stores[j];
                        arrStores.push(store);
                    }
                }

            //show area info in tables:
            refreshItemsTable(arrItems);
            refreshStoresTable(arrStores);
        }
    });
}

function refreshItemsTable(items) {
    $("#itemsTable").empty();    //clear all current areas

    var tableHeaders = "<tr>" +
                            "<th>Id</th>" +
                            "<th>Name</th>" +
                            "<th>Purchase method</th>" +
                            "<th>Sold by X stores</th>" +
                            "<th>Average price</th>" +
                            "<th>Purchases amount</th>" +
                        "</tr>";

    $(tableHeaders).appendTo($("#itemsTable"));

    $.each(items || [], function(index, item) {
        var itemInfo = "<tr name='item'>" +
                            "<td>"+item.itemId+"</td>" +
                            "<td>"+item.name+"</td>" +
                            "<td>"+item.purchaseCategory+"</td>" +
                            "<td>"+item.howManyStoresSellsThisItem+"</td>" +
                            "<td>"+item.averageItemPrice+"</td>" +
                            "<td>"+item.howManyTimesItemSold+"</td>" +
                        "</tr>";
        $(itemInfo).appendTo($("#itemsTable"));
    });
}

function refreshStoresTable(stores) {
    $("#storesTable").empty();    //clear all current areas

    var tableHeaders = "<tr>" +
                            "<th>Id</th>" +
                            "<th>Name</th>" +
                            "<th>Owner's name</th>" +
                            "<th>Coordinates</th>" +
                            "<th>Items</th>" +
                            "<th>Total orders</th>" +
                            "<th>Total items cost</th>" +
                            "<th>PPK</th>" +
                            "<th>Total delivery cost</th>" +
                        "</tr>";

    $(tableHeaders).appendTo($("#storesTable"));

    $.each(stores || [], function(index, store) {
        var itemInfo = "<tr name='item'>" +
                            "<td>"+store.sdmStore.id+"</td>" +
                            "<td>"+store.sdmStore.name+"</td>" +
                            "<td>"+store.ownerName+"</td>" +
                            "<td>("+store.myLocation.X+","+store.myLocation.Y+")</td>" +
                            "<td>"+createStoreItemsTable(store.storeItems.itemsList)+"</td>" +
                            "<td>"+store.storeSingleOrderItemsList.length+"</td>" +
                            "<td>**Need to change the code**</td>" +            //Need to add parameter to MyStore
                            "<td>"+store.sdmStore.deliveryPpk+"</td>" +
                            "<td>**Need to change the code**</td>" +            //Need to add parameter to MyStore
                        "</tr>";
        $(itemInfo).appendTo($("#storesTable"));
    });
}

function createStoreItemsTable(storeItems){
    var storeItemsTable = "<table id='storeItemsTable'>" +
                            "<tr>" +
                                "<th>Id</th>" +
                                "<th>Name</th>" +
                                "<th>Purchase method</th>" +
                                "<th>Price</th>" +
                                "<th>Total purchases</th>" +
                            "</tr>";

    $.each(storeItems || [], function(index, item) {
        var itemInfo = "<tr name='item'>" +
                            "<td>"+item.myItem.itemId+"</td>" +
                            "<td>"+item.myItem.name+"</td>" +
                            "<td>"+item.myItem.purchaseCategory+"</td>" +
                            "<td>"+item.price+"</td>" +
                            "<td>"+item.howManyTimeSold+"</td>" +
                        "</tr>";
        storeItemsTable += itemInfo;
    });

    storeItemsTable += "</table>";

    return storeItemsTable;
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