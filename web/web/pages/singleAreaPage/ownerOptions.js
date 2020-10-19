var refreshRate = 10000; //milli seconds
var AREA_INFO_URL = buildUrlWithContextPath("areaInfo");
var STORE_ORDERS_PAGE_URL = buildUrlWithContextPath("storeOrdersPage");
var STORES_LIST_URL = buildUrlWithContextPath("zoneStoresList");
var NEW_STORE_CONTENT_URL = buildUrlWithContextPath("newStorePage");
var ADD_NEW_STORE_URL = buildUrlWithContextPath("addNewStore");
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
                    setInterval(ajaxStoresList, refreshRate); //stores list will update every few seconds
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

    var orderRow = 	"<tr name='order_tr' selectedOrderID='"+orderId+"'>" +
                        "<th>"+order.orderId+"</th>" +
                        "<th>"+order.date+"</th>" +
                        "<th>"+order.customer.user.name+"</th>" +
                        "<th>Customer location ?WHERE?</th>" +
                        "<th>"+order.thisStoreQuantityMapFromOrder.length+"</th>" +
                        "<th>"+order.orderCost+"</th>" +
                        "<th>"+order.deliveryCost+"</th>" +
                    "</tr>";
        $(orderRow).appendTo($("#ordersHistoryTable tbody"));
    });

    if(selectedStore.storeSingleOrderItemsList.length == 0 ){ //msg if there are no orders
        var emptyOrders = "<tr name='order_tr' selectedOrderID='1'><td>There are no areas</td></tr>";
        console.log(selectedStore.storeSingleOrderItemsList);
        $(emptyOrders).appendTo($("#ordersHistoryTable tbody"));
        //toggle display for orderItems table:
            $("#orderItems").toggle(false);
    }else
        $("#orderItems").toggle(true);

    //add all the orders rows click event:
    addOrdersClickEvent();

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

function refreshOrderItemsTable(selectedStore) {
console.log("refreshOrderItemsTable");
    //const selectedOrder = arrStores.find( ( store ) => store.sdmStore.id == storeId );
    //store.storeSingleOrderItemsList[].thisStoreQuantityMapFromOrder[].
    /*
        private MyItem myItem;
        private int price;
        private int storeId;
        private String itemKind; // store offer.
        private int howManyTimeSold = 0 ;
    */

    /*
    //div id="orderItems" style="display:none;" /block>
    out.println("<form id=\"storeOrderItems\" method=\"POST\" action=\"\">");
                out.println("<input id=\"selected_order\" type='hidden' name=\"selectedOrder\"/>");
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
    ajaxOpenStore();

    return false;
}

function ajaxOpenStore(){
     $.ajax({
        url: NEW_STORE_CONTENT_URL,
        success: function(response) {
            $("#content").replaceWith(response);
            //override submit of '#createNewOrderForm' form
            $("#createNewOrderForm").submit(createNewOrderOnSubmit);
        }
    });
}

function createNewOrderOnSubmit(){
    var formItems = document.getElementsByName("item");
    var checkBoxes = document.getElementsByName("itemCheckBox");
    var storeItems = [];
    var formData = new FormData();

    formData.append("storeName", this[0].value);
    formData.append("storeLocationX", this[1].value);
    formData.append("storeLocationY", this[2].value);
    formData.append("ppk", this[3].value);

    for (var i=0; i<checkBoxes.length; i++) {
        if(checkBoxes[i].checked){
            var item = new Item(checkBoxes[i], i);
            if(item.price <= 0){
                alert("Item "+item.id+" must have price.");
                return;
            }else{ storeItems.push(item); }
        }
    }

    formData.append("items", storeItems);

     $.ajax({
        method:'POST',
        data: formData, //{"storeName" : this[0].value, "items" : storeItems},
        url: ADD_NEW_STORE_URL,
        processData: false, // Don't process the files
        contentType: false, // Set content type to false as jQuery will tell the server its a query string request
        timeout: 4000,
        error: function(e) { alert(e); },
        success: function(response) {
                    //ajaxOpenStore(); //refresh open store page
                    alert(response);
        }
    });

    console.log("New store clicked");
    return false;
}

function Item(myCheckBox, index){
    this.id = myCheckBox.value;
    console.log("this.itemId= "+this.id);
    this.price = document.getElementsByName("itemPrice")[index].value;
    console.log("this.price= "+this.price);
}