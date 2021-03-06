var refreshRate = 10000; //milli seconds
var STORE_ORDERS_PAGE_URL = buildUrlWithContextPath("storeOrdersPage");
var NEW_STORE_CONTENT_URL = buildUrlWithContextPath("newStorePage");
var ADD_NEW_STORE_URL = buildUrlWithContextPath("addNewStore");
var STORE_FEEDBACKS_URL = buildUrlWithContextPath("showStoreFeedbacks");
var STORE_ORDERS_URL = buildUrlWithContextPath("loadStoreOrdersInfo");
var STORE_ORDER_ITEMS_URL = buildUrlWithContextPath("loadStoreOrderItemsInfo");
var OWNER_STORES_LIST_URL = buildUrlWithContextPath("ownerStoresList");
var feedbacks_interval;
var storeList_interval;

function stopIntervalFunction(interval) {
    clearInterval(interval);
}

function changeSelectedMenuOption(selectedMenuOptionID){
//find current selectedOption:
    var menuOptions = $("#menu").children("a");
//unSelect old option:
    menuOptions.removeAttr("class");
//select the new option:
    $(selectedMenuOptionID).attr({class: 'active'});
}

function homeClicked(){
    changeSelectedMenuOption("#homePage");//changes to ui options

    if (feedbacks_interval) { //stop feedbacks interval if running
        stopIntervalFunction(feedbacks_interval);
        feedbacks_interval = undefined;
    }

    if (storeList_interval) { //stop feedbacks interval if running
        stopIntervalFunction(storeList_interval);
        storeList_interval = undefined;
    }
}

function storeOrdersClicked(){
    changeSelectedMenuOption("#storeOrders");//changes to ui options

    if (feedbacks_interval) { //stop feedbacks interval if running
        stopIntervalFunction(feedbacks_interval);
        feedbacks_interval = undefined;
    }

    $.ajax({
        url: STORE_ORDERS_PAGE_URL,
        success: function(response) {
            $("#content").replaceWith(response);
            ajaxStoresList();
            if (!storeList_interval) {
                storeList_interval = setInterval(ajaxStoresList, 6000);
            }
        }
    });

    return false;
}

function ajaxStoresList() {
    console.log("inside ajaxStoresList");

    $.ajax({
        url: OWNER_STORES_LIST_URL,
        error: function(e) { console.log("ajaxStoresList success"); alert(e); },
        success: function(stores) {
            console.log("ajaxStoresList success");
            refreshStoresList(stores);
        }
    });
}

function refreshStoresList(stores) {
    console.log("stores= "+ stores);
    //clear all current stores
    $("#storesList").empty();

    // rebuild the list of stores:
    $.each(stores || [], function(index, store) {
    var listItem = '<li class="store" storeId="'+store.id+'" storeName="'+store.name+'">' + store.name + '</li>';
        //listItem.onclick = storeClickEvent;
    $(listItem).appendTo($("#storesList"));
    });

    $("#storesList li").on("click", storeClickEvent);
}

//declaration of a function that will be called on mouse clicks
function storeClickEvent(event) {
    //var storeId = event.currentTarget.attributes['storeId'].value;
    var storeName = event.currentTarget.attributes['storeName'].value;
    //const selectedStore = arrStores.find( ( store ) => store.id == storeId );

    //change UI for the selected store
    var listItems = $("#storesList").children("li");
    listItems.removeAttr("class");
    var clickedListItem = event.currentTarget;
    clickedListItem.setAttribute("class", "store-active");

    $.ajax({
        url: STORE_ORDERS_URL,
        data: {"storeName" : storeName},
        error: function(e) { alert("error"); },
        success: function(response) {
            $("#ordersHistoryTable tbody").empty();
            $("#ordersHistoryTable tbody").replaceWith(response);

            $("#ordersHistoryTable tbody tr").on("click", orderClickEvent);// add onClick to each order row
        }
    });
}

function orderClickEvent(event){
    var storeName = event.currentTarget.attributes['storeName'].value;
    var singleOrderIndex = event.currentTarget.attributes['singleOrderIndex'].value;
    var orderId = event.currentTarget.attributes['orderId'].value;

    $.ajax({
        url: STORE_ORDER_ITEMS_URL,
        data: {"storeName" : storeName, "singleOrderIndex" : singleOrderIndex},
        error: function(e) { alert("error"); },
        success: function(response) {
            $("#orderItemsTable tbody").empty();
            $("#orderItemsTable tbody").replaceWith(response);
            $("#pInfo").replaceWith("<p id='pInfo'>items purchase from order(ID): "+orderId+"</p>");
        }
    });
}

/* add onClick event for 'order row' in ordersHistoryTable
    to show the order items */
function addOrdersClickEvent(){
    var ordersRows = document.getElementsByName("order_tr");
    for (var i = 0 ; i < ordersRows.length ; i++){
        var orderRow = ordersRows[i];
        orderRow.onclick = myOrderRowClickEvent;
    }
};

/*  Function that will be called on mouse clicks
    on order row
*/
function myOrderRowClickEvent (event) {
    var displayOrderID = $("#orderItems").attr("orderID");
    var selectedOrderID = event.currentTarget.attributes['selectedOrderID'].value;
    var display = (displayOrderID === selectedOrderID)? false : true;

    //toggle display for orderItems table:
    $("#orderItems").toggle(display);
    if(display === true){
    //show items of selected order:
        $("#orderItems").attr("orderID", selectedOrderID);
            //refreshOrderItemsTable(); refresh items table    <------
    }else
        $("#orderItems").attr("orderID", '');

}

////////////////////////// FEEDBACKS /////////////////////

function feedbacksClicked(){
    changeSelectedMenuOption("#feedbacks");//changes to ui options

    if (storeList_interval) { //stop store list interval if running
        stopIntervalFunction(storeList_interval);
        storeList_interval = undefined;
    }

    ajaxRefreshStoresFeedbacks();

    if (!feedbacks_interval) {
        feedbacks_interval = setInterval(ajaxRefreshStoresFeedbacks, 2000);
    }

    return false;
}

function ajaxRefreshStoresFeedbacks(){

    $.ajax({
        url: STORE_FEEDBACKS_URL,
        success: function(response) {
            $("#content").replaceWith(response);

        }
    });
}


////////////////////////// FEEDBACKS /////////////////////


function openStoreClicked(){
    changeSelectedMenuOption("#openStore");

    if (feedbacks_interval) { //stop feedbacks interval if running
        stopIntervalFunction(feedbacks_interval);
        feedbacks_interval = undefined;
    }

    if (storeList_interval) { //stop feedbacks interval if running
        stopIntervalFunction(storeList_interval);
        storeList_interval = undefined;
    }

    ajaxOpenStore();

    return false;
}

function ajaxOpenStore(){
     $.ajax({
        url: NEW_STORE_CONTENT_URL,
        success: function(response) {
            $("#content").replaceWith(response);
            //override submit of '#createNewOrderForm' form
            $("#createNewOrderForm").submit(createNewStoreOnSubmit);
        }
    });
}

function createNewStoreOnSubmit(){
    var store = createStoreToAdd(this); //send the form

    if(store === null){ // Exit submit function
        return false;
    }else{
         $.ajax({
            method:'POST',
            data: store,
            url: ADD_NEW_STORE_URL,
            contentType: "application/json",
            success: function(response) {
                        alert(response);
                        ajaxOpenStore(); //refresh open store page
            }
        });
    }

    return false;
}

function ItemJson(itemId, itemName, itemPurchaseMethod){
    this.id = itemId;
    this.name = itemName;
    this.purchaseMethod = itemPurchaseMethod;
}

function StoreItemJson(itemPrice, itemJson){
    this.price = itemPrice;//document.getElementsByName("itemPrice")[index].value;
    this.jsonItem = itemJson;
    this.storeId = 1; // NEED TO CHANGE
    //public int storeId;

}

function StoreJson(storeName, X, Y, PPK, itemsList){
    this.name = storeName;
    this.x = X;
    this.y = Y;
    this.ppk = PPK;
    this.storeItems = itemsList;
}

/*
    Get: form.
    Do: create json store from the form values.
    Return: if the store valid return it, else return null.
*/
function createStoreToAdd(form){
    //var formItems = document.getElementsByName("item");
    var checkBoxes = document.getElementsByName("itemCheckBox");
    var itemsIds = document.getElementsByName('itemId'); //.textContent
    var itemsNames = document.getElementsByName('itemName');
    var itemsPurchaseCategory = document.getElementsByName('itemPurchaseCategory');

    var arrStoreItems = [];

    //create items list
    for (var i=0; i<checkBoxes.length; i++) {
        if(checkBoxes[i].checked){
            var itemPrice = document.getElementsByName("itemPrice")[i].value;

            if(itemPrice <= 0){ //check for item with out price
                alert("Item "+itemsIds[i].textContent+" must have price.");
                return null;
            }else{
                var item = new ItemJson(itemsIds[i].textContent, itemsNames[i].textContent,
                    itemsPurchaseCategory[i].textContent);
                var storeItem = new StoreItemJson(itemPrice, item);
                arrStoreItems.push(storeItem);
            }
        }
    }

    if(arrStoreItems.length == 0){ //check for store with out items
        alert("Must have at least one item in store.");
        return null;
    }

    var storeToAdd = new StoreJson(form[0].value, form[1].value, form[2].value, form[3].value, arrStoreItems);

    return JSON.stringify(storeToAdd);
}
