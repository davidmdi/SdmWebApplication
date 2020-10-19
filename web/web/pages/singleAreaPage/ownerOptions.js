var refreshRate = 10000; //milli seconds
var AREA_INFO_URL = buildUrlWithContextPath("areaInfo");
var STORE_ORDERS_PAGE_URL = buildUrlWithContextPath("storeOrdersPage");
var STORES_LIST_URL = buildUrlWithContextPath("zoneStoresList");
var arrStores;
// OnLoad function
$(function() {
    arrStores = [];
    //The users list is refreshed automatically every second
    //setInterval(ajaxUsersList, refreshRate);

});


function changeSelectedMenuOption(selectedMenuOptionID){
//find current selectedOption:
    var menuOptions = $("#menu").children("a");
//unSelect old option:
    menuOptions.removeAttr("class");
//select the new option:
    $(selectedMenuOptionID).attr({class: 'active'});
}

function storeOrdersClicked(){
            changeSelectedMenuOption("#storeOrders");//changes to ui options
            //$("#content").empty(); //clear old content

             $.ajax({
                url: STORE_ORDERS_PAGE_URL,
                success: function(response) {
                    $("#content").replaceWith(response);
                    ajaxStoresList();
                    setInterval(ajaxStoresList, refreshRate); //stores list will update every few seconds.
                }
            });
    return false;
}

function ajaxStoresList() {
    $.ajax({
        url: STORES_LIST_URL,
        success: function(stores) {
            arrStores = stores; //save stores in array
            refreshStoresList(stores);
        }
    });
}

function refreshStoresList(stores) {
    //clear all current stores
    $("#storesList").empty();

    // rebuild the list of stores:
    $.each(stores || [], function(index, store) {
    var listItem = '<li class="store" storeId="'+store.sdmStore.id+'" onclick="storeClickEvent(event)">' + store.sdmStore.name + '</li>';
        $(listItem).appendTo($("#storesList"));
    });
}

//declaration of a function that will be called on mouse clicks
function storeClickEvent (event) {
    var storeId = event.currentTarget.attributes['storeId'].value;
    const selectedStore = arrStores.find( ( store ) => store.sdmStore.id == storeId );
    refreshStoreOrdersHistory(selectedStore); //refresh store orders table

    //change UI for the selected store
    var listItems = $("#storesList").children("li");
    listItems.removeAttr("class");
    var clickedListItem = event.currentTarget;
    clickedListItem.setAttribute("class", "store-active");
}


function refreshStoreOrdersHistory(selectedStore) {
    console.log("inside refreshStoreOrdersHistory");
    //clear all current orders
    $("#ordersHistoryTable tbody").empty();

    // rebuild the list of users: scan all users and add them to the list of users
    $.each(selectedStore.storeSingleOrderItemsList || [], function(index, order) {

    console.log("order.thisStoreQuantityMapFromOrder= "+order.thisStoreQuantityMapFromOrder);

    var orderRow = 	"<tr>" +
                        "<th>"+order.storeId+"</th>" +
                        "<th>"+order.date+"</th>" +
                        "<th>"+order.customer.user.name+"</th>" +
                        "<th>Customer location ?WHERE?</th>" +
                        "<th>"+order.thisStoreQuantityMapFromOrder.length+"</th>" +
                        "<th>"+order.orderCost+"</th>" +
                        "<th>"+order.deliveryCost+"</th>" +
                    "</tr>";
        $(orderRow).appendTo($("#ordersHistoryTable tbody"));
    });
        /*
        var testRow = 	"<tr>" +
                            "<th>123</th>" +
                            "<th>03/04</th>" +
                            "<th>Oryom</th>" +
                            "<th>(2,2)</th>" +
                            "<th>3</th>" +
                            "<th>20</th>" +
                            "<th>20</th>" +
                        "</tr>";
            $(testRow).appendTo($("#ordersHistoryTable tbody"));
        */
}


function feedbacksClicked(){
            changeSelectedMenuOption("#feedbacks");
            /*
             $.ajax({
                url: STORE_ORDERS_PAGE_URL,
                success: function(response) {
                    $("#content").replaceWith(response);
                }
            });
            */
    return false;
}

function openStoreClicked(){
            changeSelectedMenuOption("#openStore");
            /*
             $.ajax({
                url: STORE_ORDERS_PAGE_URL,
                success: function(response) {
                    $("#content").replaceWith(response);
                }
            });
            */
    return false;
}