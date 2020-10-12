
var refreshRate = 2000; //milli seconds
var refreshRateForAreas = 5000; //milli seconds
var USER_LIST_URL = buildUrlWithContextPath("userslist");
var LOAD_XML_URL = buildUrlWithContextPath("loadXml");
var UPLOAD_XML_URL = buildUrlWithContextPath("uploadXmlFile");
var AREAS_TABLE_URL = buildUrlWithContextPath("areasTable");

//activate the timer calls after the page is loaded
$(function() {
     //init:
     ajaxUsersList();
     ajaxAreasTable();
    //The users list is refreshed automatically every second
    setInterval(ajaxUsersList, refreshRate);
    setInterval(ajaxAreasTable, refreshRateForAreas);

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
/*
[
{"sdmSuper":{
                        "sdmZone":{"name":"Galil Maarvi"},
                        "sdmItems":{"sdmItem":[{"name":"Ketshop","purchaseCategory":"Quantity","id":1},{"name":"Banana","purchaseCategory":"Weight","id":2},{"name":"Water","purchaseCategory":"Quantity","id":3},{"name":"Pasta","purchaseCategory":"Quantity","id":4},{"name":"Tomato","purchaseCategory":"Weight","id":5}]},
                        "sdmStores":{"sdmStore":[{"name":"super baba","deliveryPpk":30,"location":{"y":4,"x":3},"sdmPrices":{"sdmSell":[{"price":20,"itemId":1},{"price":10,"itemId":2},{"price":50,"itemId":5}]},"sdmDiscounts":{"sdmDiscount":[{"name":"Balabait ishtagea !","ifYouBuy":{"quantity":1.0,"itemId":1},"thenYouGet":{"sdmOffer":[{"quantity":1.0,"itemId":2,"forAdditional":0},{"quantity":2.0,"itemId":5,"forAdditional":20}],"operator":"ONE-OF"}},{"name":"1 + 1","ifYouBuy":{"quantity":1.0,"itemId":2},"thenYouGet":{"sdmOffer":[{"quantity":1.0,"itemId":1,"forAdditional":0}]}}]},"id":1},{"name":"Givataim Shivataim","deliveryPpk":20,"location":{"y":5,"x":1},"sdmPrices":{"sdmSell":[{"price":20,"itemId":2},{"price":10,"itemId":3},{"price":50,"itemId":4}]},"id":2}]}},
                        "owner":{"user":{"userId":1,"name":"admin","type":"owner"},
                        "zonesNames":["Galil Maarvi"]},
                        "items":{"sdmItems":{"sdmItem":[{"name":"Ketshop","purchaseCategory":"Quantity","id":1},{"name":"Banana","purchaseCategory":"Weight","id":2},{"name":"Water","purchaseCategory":"Quantity","id":3},{"name":"Pasta","purchaseCategory":"Quantity","id":4},{"name":"Tomato","purchaseCategory":"Weight","id":5}]},
                        "itemList":[{"sdmItem":{"name":"Ketshop","purchaseCategory":"Quantity","id":1},"itemId":1,"name":"Ketshop","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":20.0,"averageItemPriceString":"20.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},{"sdmItem":{"name":"Banana","purchaseCategory":"Weight","id":2},"itemId":2,"name":"Banana","purchaseCategory":"Weight","howManyStoresSellsThisItem":2,"averageItemPrice":15.0,"averageItemPriceString":"15.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},{"sdmItem":{"name":"Water","purchaseCategory":"Quantity","id":3},"itemId":3,"name":"Water","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":10.0,"averageItemPriceString":"10.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},{"sdmItem":{"name":"Pasta","purchaseCategory":"Quantity","id":4},"itemId":4,"name":"Pasta","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":50.0,"averageItemPriceString":"50.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},{"sdmItem":{"name":"Tomato","purchaseCategory":"Weight","id":5},"itemId":5,"name":"Tomato","purchaseCategory":"Weight","howManyStoresSellsThisItem":1,"averageItemPrice":50.0,"averageItemPriceString":"50.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"}],"itemsMap":{"1":{"sdmItem":{"name":"Ketshop","purchaseCategory":"Quantity","id":1},"itemId":1,"name":"Ketshop","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":20.0,"averageItemPriceString":"20.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"2":{"sdmItem":{"name":"Banana","purchaseCategory":"Weight","id":2},"itemId":2,"name":"Banana","purchaseCategory":"Weight","howManyStoresSellsThisItem":2,"averageItemPrice":15.0,"averageItemPriceString":"15.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"3":{"sdmItem":{"name":"Water","purchaseCategory":"Quantity","id":3},"itemId":3,"name":"Water","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":10.0,"averageItemPriceString":"10.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"4":{"sdmItem":{"name":"Pasta","purchaseCategory":"Quantity","id":4},"itemId":4,"name":"Pasta","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":50.0,"averageItemPriceString":"50.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"5":{"sdmItem":{"name":"Tomato","purchaseCategory":"Weight","id":5},"itemId":5,"name":"Tomato","purchaseCategory":"Weight","howManyStoresSellsThisItem":1,"averageItemPrice":50.0,"averageItemPriceString":"50.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"}}},"stores":{"sdmStores":{"sdmStore":[{"name":"super baba","deliveryPpk":30,"location":{"y":4,"x":3},"sdmPrices":{"sdmSell":[{"price":20,"itemId":1},{"price":10,"itemId":2},{"price":50,"itemId":5}]},"sdmDiscounts":{"sdmDiscount":[{"name":"Balabait ishtagea !","ifYouBuy":{"quantity":1.0,"itemId":1},"thenYouGet":{"sdmOffer":[{"quantity":1.0,"itemId":2,"forAdditional":0},{"quantity":2.0,"itemId":5,"forAdditional":20}],"operator":"ONE-OF"}},{"name":"1 + 1","ifYouBuy":{"quantity":1.0,"itemId":2},"thenYouGet":{"sdmOffer":[{"quantity":1.0,"itemId":1,"forAdditional":0}]}}]},"id":1},{"name":"Givataim Shivataim","deliveryPpk":20,"location":{"y":5,"x":1},"sdmPrices":{"sdmSell":[{"price":20,"itemId":2},{"price":10,"itemId":3},{"price":50,"itemId":4}]},"id":2}]},"storeList":[{"sdmStore":{"name":"super baba","deliveryPpk":30,"location":{"y":4,"x":3},"sdmPrices":{"sdmSell":[{"price":20,"itemId":1},{"price":10,"itemId":2},{"price":50,"itemId":5}]},"sdmDiscounts":{"sdmDiscount":[{"name":"Balabait ishtagea !","ifYouBuy":{"quantity":1.0,"itemId":1},"thenYouGet":{"sdmOffer":[{"quantity":1.0,"itemId":2,"forAdditional":0},{"quantity":2.0,"itemId":5,"forAdditional":20}],"operator":"ONE-OF"}},{"name":"1 + 1","ifYouBuy":{"quantity":1.0,"itemId":2},"thenYouGet":{"sdmOffer":[{"quantity":1.0,"itemId":1,"forAdditional":0}]}}]},"id":1},"storeItems":{"itemsList":[{"myItem":{"sdmItem":{"name":"Ketshop","purchaseCategory":"Quantity","id":1},"itemId":1,"name":"Ketshop","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":20.0,"averageItemPriceString":"20.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":20,"storeId":1,"itemKind":"store","howManyTimeSold":0},{"myItem":{"sdmItem":{"name":"Banana","purchaseCategory":"Weight","id":2},"itemId":2,"name":"Banana","purchaseCategory":"Weight","howManyStoresSellsThisItem":2,"averageItemPrice":15.0,"averageItemPriceString":"15.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":10,"storeId":1,"itemKind":"store","howManyTimeSold":0},{"myItem":{"sdmItem":{"name":"Tomato","purchaseCategory":"Weight","id":5},"itemId":5,"name":"Tomato","purchaseCategory":"Weight","howManyStoresSellsThisItem":1,"averageItemPrice":50.0,"averageItemPriceString":"50.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":50,"storeId":1,"itemKind":"store","howManyTimeSold":0}],"itemsMap":{"1":{"myItem":{"sdmItem":{"name":"Ketshop","purchaseCategory":"Quantity","id":1},"itemId":1,"name":"Ketshop","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":20.0,"averageItemPriceString":"20.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":20,"storeId":1,"itemKind":"store","howManyTimeSold":0},"2":{"myItem":{"sdmItem":{"name":"Banana","purchaseCategory":"Weight","id":2},"itemId":2,"name":"Banana","purchaseCategory":"Weight","howManyStoresSellsThisItem":2,"averageItemPrice":15.0,"averageItemPriceString":"15.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":10,"storeId":1,"itemKind":"store","howManyTimeSold":0},"5":{"myItem":{"sdmItem":{"name":"Tomato","purchaseCategory":"Weight","id":5},"itemId":5,"name":"Tomato","purchaseCategory":"Weight","howManyStoresSellsThisItem":1,"averageItemPrice":50.0,"averageItemPriceString":"50.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":50,"storeId":1,"itemKind":"store","howManyTimeSold":0}}},"storeOrderMap":{},"myLocation":{"sdmLocation":{"y":4,"x":3},"X":3,"Y":4},"storeSingleOrderItemsList":[]},{"sdmStore":{"name":"Givataim Shivataim","deliveryPpk":20,"location":{"y":5,"x":1},"sdmPrices":{"sdmSell":[{"price":20,"itemId":2},{"price":10,"itemId":3},{"price":50,"itemId":4}]},"id":2},"storeItems":{"itemsList":[{"myItem":{"sdmItem":{"name":"Banana","purchaseCategory":"Weight","id":2},"itemId":2,"name":"Banana","purchaseCategory":"Weight","howManyStoresSellsThisItem":2,"averageItemPrice":15.0,"averageItemPriceString":"15.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":20,"storeId":2,"itemKind":"store","howManyTimeSold":0},{"myItem":{"sdmItem":{"name":"Water","purchaseCategory":"Quantity","id":3},"itemId":3,"name":"Water","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":10.0,"averageItemPriceString":"10.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":10,"storeId":2,"itemKind":"store","howManyTimeSold":0},{"myItem":{"sdmItem":{"name":"Pasta","purchaseCategory":"Quantity","id":4},"itemId":4,"name":"Pasta","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":50.0,"averageItemPriceString":"50.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":50,"storeId":2,"itemKind":"store","howManyTimeSold":0}],"itemsMap":{"2":{"myItem":{"sdmItem":{"name":"Banana","purchaseCategory":"Weight","id":2},"itemId":2,"name":"Banana","purchaseCategory":"Weight","howManyStoresSellsThisItem":2,"averageItemPrice":15.0,"averageItemPriceString":"15.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":20,"storeId":2,"itemKind":"store","howManyTimeSold":0},"3":{"myItem":{"sdmItem":{"name":"Water","purchaseCategory":"Quantity","id":3},"itemId":3,"name":"Water","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":10.0,"averageItemPriceString":"10.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":10,"storeId":2,"itemKind":"store","howManyTimeSold":0},"4":{"myItem":{"sdmItem":{"name":"Pasta","purchaseCategory":"Quantity","id":4},"itemId":4,"name":"Pasta","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":50.0,"averageItemPriceString":"50.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":50,"storeId":2,"itemKind":"store","howManyTimeSold":0}}},"storeOrderMap":{},"myLocation":{"sdmLocation":{"y":5,"x":1},"X":1,"Y":5},"storeSingleOrderItemsList":[]}],"storeMap":{"1":{"sdmStore":{"name":"super baba","deliveryPpk":30,"location":{"y":4,"x":3},"sdmPrices":{"sdmSell":[{"price":20,"itemId":1},{"price":10,"itemId":2},{"price":50,"itemId":5}]},"sdmDiscounts":{"sdmDiscount":[{"name":"Balabait ishtagea !","ifYouBuy":{"quantity":1.0,"itemId":1},"thenYouGet":{"sdmOffer":[{"quantity":1.0,"itemId":2,"forAdditional":0},{"quantity":2.0,"itemId":5,"forAdditional":20}],"operator":"ONE-OF"}},{"name":"1 + 1","ifYouBuy":{"quantity":1.0,"itemId":2},"thenYouGet":{"sdmOffer":[{"quantity":1.0,"itemId":1,"forAdditional":0}]}}]},"id":1},"storeItems":{"itemsList":[{"myItem":{"sdmItem":{"name":"Ketshop","purchaseCategory":"Quantity","id":1},"itemId":1,"name":"Ketshop","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":20.0,"averageItemPriceString":"20.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":20,"storeId":1,"itemKind":"store","howManyTimeSold":0},{"myItem":{"sdmItem":{"name":"Banana","purchaseCategory":"Weight","id":2},"itemId":2,"name":"Banana","purchaseCategory":"Weight","howManyStoresSellsThisItem":2,"averageItemPrice":15.0,"averageItemPriceString":"15.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":10,"storeId":1,"itemKind":"store","howManyTimeSold":0},{"myItem":{"sdmItem":{"name":"Tomato","purchaseCategory":"Weight","id":5},"itemId":5,"name":"Tomato","purchaseCategory":"Weight","howManyStoresSellsThisItem":1,"averageItemPrice":50.0,"averageItemPriceString":"50.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":50,"storeId":1,"itemKind":"store","howManyTimeSold":0}],"itemsMap":{"1":{"myItem":{"sdmItem":{"name":"Ketshop","purchaseCategory":"Quantity","id":1},"itemId":1,"name":"Ketshop","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":20.0,"averageItemPriceString":"20.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":20,"storeId":1,"itemKind":"store","howManyTimeSold":0},"2":{"myItem":{"sdmItem":{"name":"Banana","purchaseCategory":"Weight","id":2},"itemId":2,"name":"Banana","purchaseCategory":"Weight","howManyStoresSellsThisItem":2,"averageItemPrice":15.0,"averageItemPriceString":"15.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":10,"storeId":1,"itemKind":"store","howManyTimeSold":0},"5":{"myItem":{"sdmItem":{"name":"Tomato","purchaseCategory":"Weight","id":5},"itemId":5,"name":"Tomato","purchaseCategory":"Weight","howManyStoresSellsThisItem":1,"averageItemPrice":50.0,"averageItemPriceString":"50.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":50,"storeId":1,"itemKind":"store","howManyTimeSold":0}}},"storeOrderMap":{},"myLocation":{"sdmLocation":{"y":4,"x":3},"X":3,"Y":4},"storeSingleOrderItemsList":[]},"2":{"sdmStore":{"name":"Givataim Shivataim","deliveryPpk":20,"location":{"y":5,"x":1},"sdmPrices":{"sdmSell":[{"price":20,"itemId":2},{"price":10,"itemId":3},{"price":50,"itemId":4}]},"id":2},"storeItems":{"itemsList":[{"myItem":{"sdmItem":{"name":"Banana","purchaseCategory":"Weight","id":2},"itemId":2,"name":"Banana","purchaseCategory":"Weight","howManyStoresSellsThisItem":2,"averageItemPrice":15.0,"averageItemPriceString":"15.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":20,"storeId":2,"itemKind":"store","howManyTimeSold":0},{"myItem":{"sdmItem":{"name":"Water","purchaseCategory":"Quantity","id":3},"itemId":3,"name":"Water","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":10.0,"averageItemPriceString":"10.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":10,"storeId":2,"itemKind":"store","howManyTimeSold":0},{"myItem":{"sdmItem":{"name":"Pasta","purchaseCategory":"Quantity","id":4},"itemId":4,"name":"Pasta","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":50.0,"averageItemPriceString":"50.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":50,"storeId":2,"itemKind":"store","howManyTimeSold":0}],"itemsMap":{"2":{"myItem":{"sdmItem":{"name":"Banana","purchaseCategory":"Weight","id":2},"itemId":2,"name":"Banana","purchaseCategory":"Weight","howManyStoresSellsThisItem":2,"averageItemPrice":15.0,"averageItemPriceString":"15.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":20,"storeId":2,"itemKind":"store","howManyTimeSold":0},"3":{"myItem":{"sdmItem":{"name":"Water","purchaseCategory":"Quantity","id":3},"itemId":3,"name":"Water","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":10.0,"averageItemPriceString":"10.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":10,"storeId":2,"itemKind":"store","howManyTimeSold":0},"4":{"myItem":{"sdmItem":{"name":"Pasta","purchaseCategory":"Quantity","id":4},"itemId":4,"name":"Pasta","purchaseCategory":"Quantity","howManyStoresSellsThisItem":1,"averageItemPrice":50.0,"averageItemPriceString":"50.00","howManyTimesItemSold":0.0,"howManyTimesItemSoldString":"0.0"},"price":50,"storeId":2,"itemKind":"store","howManyTimeSold":0}}},"storeOrderMap":{},"myLocation":{"sdmLocation":{"y":5,"x":1},"X":1,"Y":5},"storeSingleOrderItemsList":[]}}},
                        "orders":{"orderList":[],"orderMap":{},"avgOrdersPrice":0.0,"sumOfOrdersPrice":0.0},"zoneName":"Galil Maarvi"}]
*/
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
}