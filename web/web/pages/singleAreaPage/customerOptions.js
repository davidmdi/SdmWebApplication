var refreshRate = 10000; //milli seconds
var AREA_INFO_URL = buildUrlWithContextPath("areaInfo");
var MAKE_ORDER_PAGE_URL = buildUrlWithContextPath("makeOrderPage");
var SHOW_ORDER_HISTORY_PAGE_URL = buildUrlWithContextPath("showCustomersOrderHistoryPage");
var PRESENT_SELECTE_SROTE_ITEMS = buildUrlWithContextPath("showSelctedStoreItems");
var STATIC_ORDER = buildUrlWithContextPath("staticOrder");
var DYNAMIC_ORDER = buildUrlWithContextPath("dynamicOrder");
var DYNAMIC_ORDER_FIND_ITEMS = buildUrlWithContextPath("findMinItemsBasket");
var DYNAMIC_ORDER_FINAL_SUMMERY = buildUrlWithContextPath("dynamicOrderSummery");
var STATIC_ORDER_SUMMERY = buildUrlWithContextPath("staticOrderSummery");
var UPDATE_ORDER = buildUrlWithContextPath("updateOrder");
var CREATE_STORES_FEEDBACKS = buildUrlWithContextPath("createStoreFeedbacks");
var END_ORDER = buildUrlWithContextPath("endOrder");
var CUSTOMER_ORDER_ITEMS_URL = buildUrlWithContextPath("customerOrderItemsTable");
var SHOW_MAKE_ORDER_FORM_URL = buildUrlWithContextPath("showMakeOrderForm");
var CHECK_VALID_LOCATION_URL = buildUrlWithContextPath("startOrder");

var ITEMS_FOR_DYNAMIC_URL = buildUrlWithContextPath("showZoneItemsForDynamicOrder");
var STORES_FOR_STATIC_URL = buildUrlWithContextPath("storesComboBox");

var parametrs;
var ORDER_DATE;
var ORDER_TYPE;
var ORDER_X;
var ORDER_Y;
var STORE_NAME;
var saveSelecetedItemsForStaticOrder;
// var formData = new FormData();



function changeSelectedMenuOption(selectedMenuOptionID){
//find current selectedOption:
    var menuOptions = $("#menu").children("a");
//unSelect old option:
    menuOptions.removeAttr("class");
//select the new option:
    $(selectedMenuOptionID).attr({class: 'active'});
}

function ajaxShowMakeOrderForm(){
    $.ajax({
        url: SHOW_MAKE_ORDER_FORM_URL,
        success: function(response) {
            $("#content").replaceWith(response);
            $("#initDataForOrder").submit(startMakeOrderFormSubmitClicked);

        }

    });
}

function startMakeOrderFormSubmitClicked(){
    //$("input[type='submit']").attr("disabled", 'disabled');
    parametrs = $("form").serialize();
    var selectedStoreSerializedArray = $('form').serializeArray();
    ORDER_DATE = selectedStoreSerializedArray[0].value;
    ORDER_TYPE = selectedStoreSerializedArray[1].value;
    ORDER_X  = selectedStoreSerializedArray[2].value;
    ORDER_Y  = selectedStoreSerializedArray[3].value;

    try{
        $.ajax({
            data:parametrs,
            url: CHECK_VALID_LOCATION_URL,
            success: function(response) {
                $("#errorMsg").replaceWith(response);

                var msg = document.getElementById("errorMsg").textContent;
                if(msg == ""){ // == Location is valid
                    $("#initDataForOrder input[type=submit]").attr("disabled", 'disabled');
                    //start order by type
                    if(document.querySelector('input[name="typeofOrder"]:checked').value == "dynamic")
                        showZoneItemsList();
                    else
                        showZoneStoresComboBox();
                }
            }

        });
    }catch (e) {
        console.log("Error invoking the ajax !" + e);
    }
    return false;
}

function makeOrderOnClick(){
    changeSelectedMenuOption("#makeOrder");//changes to ui options
    ajaxShowMakeOrderForm();

    return false;
}


function showZoneStoresComboBox(){
    $.ajax({
        url: STORES_FOR_STATIC_URL,
        success: function(response) {
            $("#staticOrDynamicOrder").replaceWith(response); // returns comboBox of zone's stores

            $("#storeSelectForm").attr("action", PRESENT_SELECTE_SROTE_ITEMS); //replace form 'action' attribute
            $("#storeSelectForm").submit(showStoreDiscountsOffers); //override 'submit' function
            return false;
        }

    });
}

function showZoneItemsList(){
    $.ajax({
        url: ITEMS_FOR_DYNAMIC_URL,
        success: function(response) {
            $("#staticOrDynamicOrder").replaceWith(response);

            $("#dynamicOrderItems").attr("action", PRESENT_SELECTE_SROTE_ITEMS); // change name of form
            $("#dynamicOrderItems").submit(sendDynamicOrderItems);// change name of form + function
            return false;
        }

    });
}

function createDynamicOrderItemsList(){

    var checkBoxes = document.getElementsByName("itemCheckBox");
    var selectedStoreItems = [];

    //create items list
    for (var i=0; i<checkBoxes.length; i++) {
        if(checkBoxes[i].checked){
            var id = checkBoxes[i].value;
            var quantity = document.getElementsByName("itemAmount")[i].value;

            if(quantity <= 0) { //check for item with out price
                alert("Item " + id + " must have amount.");
                return null;
            }

            selectedStoreItems.push(new StoreItem("", id, quantity));
        }
    }

    if(selectedStoreItems.length == 0){
        alert("Must have at least one item in order.");
        return null;
    }

    return selectedStoreItems;
}


function sendDynamicOrderItems(){
    var discountsOffersList = [];
    discountsOffersList.push(new OfferItem(-1, -1, -1, -1));
    var selectedStoreItemsList = createDynamicOrderItemsList();

    if(selectedStoreItemsList !== null){
        var dynamicOrder = new Order(ORDER_DATE, "dynamic", ORDER_X, ORDER_Y, selectedStoreItemsList, discountsOffersList);
        console.log(dynamicOrder);

        $.ajax({
            method:'POST',
            data: JSON.stringify(dynamicOrder),
            url: DYNAMIC_ORDER_FIND_ITEMS, // CHANGE THE URL !!
            contentType: "application/json",
            timeout: 4000,
            error: function(e) { alert(e); },
            success: function(response) {
                $("#content").replaceWith(response); // show store summery
                $("#selectSpecialOffers").attr('action', DYNAMIC_ORDER_FINAL_SUMMERY);
                $("#selectSpecialOffers").submit(showDynamicOrderFinalSummeryOnSubmitClicked);

                //CALL AJAX FOR DISCOUNTS AND APPEND TO CONTENT
            }

        });
    }

    return false;
}


function showOrderHistory() {
    changeSelectedMenuOption("#orderHistory");//changes to ui options
    $.ajax({

        url: SHOW_ORDER_HISTORY_PAGE_URL,
        success: function (response) {
            $("#content").replaceWith(response);
            // add onClick to each order row
            $("#customerOrdersTable tbody tr").on("click", customerOrderClickEvent);

        }
    });
}

function customerOrderClickEvent(event){
    var orderId = event.currentTarget.attributes['orderId'].value;

    $.ajax({
        url: CUSTOMER_ORDER_ITEMS_URL,
        data: {"orderId" : orderId},
        error: function(e) { alert("error"); },
        success: function(response) {
            $("#orderItemsTable").replaceWith(response);
        }
    });
}



/* called after store was selected (static order) */

function showSelectedStoreInfo() {

    $("#initDataForOrder input[type='submit']").attr("disabled", 'disabled');
    var selectedStoreSerialized = $("form").serialize();

    $.ajax({
        data:  selectedStoreSerialized ,
        url: PRESENT_SELECTE_SROTE_ITEMS,
        error: function(e) { alert(e); console.log("in error" + e);},
        success: function (response) { // return form of store items selection.
            $("#content").replaceWith(response);
            $("#createStaticOrder").attr('action' , STATIC_ORDER)
            $("#createStaticOrder").submit(showStoreDiscountsOffers);

            console.log("in success " + response);
        }

    });
    return false;
}

function showStoreDiscountsOffers(){

    selecetedItemsForStaticOrder = createStaticOrderItemList();
    $.ajax({
        method:'POST',
        data: selecetedItemsForStaticOrder,
        url: STATIC_ORDER,
        contentType: "application/json",
        timeout: 4000,
        error: function(e) { alert(e); },
        success: function(response) {
           $("#content").append(response); // offers form

            $("#selectSpecialOffers").attr('action', STATIC_ORDER_SUMMERY);
            $("#selectSpecialOffers").submit(showStaticOrderSummeryOnSubmitClicked);
        }

    });
    return false;
}


function createStaticOrderItemList(){ // whithout discounts.
    var formItems = document.getElementsByName("item");
    var checkBoxes = document.getElementsByName("itemCheckBox");
    var selectedItems = [];

    //create items list
    for (var i=0; i<checkBoxes.length; i++) {
        if(checkBoxes[i].checked){
            var item = new Item(checkBoxes[i], i);
            if(item.quantity <= 0){ //check for item with out price
                alert("Item "+item.id+" must have amount.");
                return null;
            }else{ selectedItems.push(item); }
        }

    }

    if(selectedItems.length == 0){ //check for store with out items
        alert("Must have at least one item in order.");
        return null;
    }else{
        $("#createStaticOrder input[type='submit']").attr("disabled", 'disabled'); //disable next button
    }

    saveSelecetedItemsForStaticOrder = selectedItems;

    STORE_NAME = document.getElementById('storeNameLabel').textContent;
    var itemsListSelected = new storeSelectioItems(STORE_NAME, selectedItems); // (name of store , selecedItemsArray from user)
    console.log(itemsListSelected);

    return JSON.stringify(itemsListSelected);
}

function Item(myCheckBox, index){
    this.itemId = myCheckBox.value;
    this.quantity = document.getElementsByName("itemAmount")[index].value;
}

function storeSelectioItems(storeName,selecetdItems){
    this.storeName = storeName;
    this.selectedItemsList = selecetdItems;
}



                       /*Dynamic Orde*/
function showDynamicOrderFinalSummeryOnSubmitClicked() {
    $("input[type='submit']").attr("disabled", 'disabled');
    var selectedDiscountsOffers = createSelectedDiscountsOffersList();
    var selectedStoreItemsList = creatDummyStoreItemsList();
    var dynamicOrder = createDynamicOrder(selectedStoreItemsList, selectedDiscountsOffers);

    $.ajax({
        method:'POST',
        url: DYNAMIC_ORDER_FINAL_SUMMERY,
        contentType: "application/json",
        data: JSON.stringify(dynamicOrder),
        error: function(e) { alert(e); },
        success: function(response) { //response is order summery and approve button

            $("#content").replaceWith(response);
            //approve order button overide
            //$("#selectSpecialOffers").submit(createStaticOrderOnSubmitClicked);e:
        }
    });

    return false;

}

function creatDummyStoreItemsList() {
    var dummyList = [];
    var storeItem = new StoreItem("-1", -1, -1);
    dummyList.push(storeItem);
    return dummyList;
}
function createDynamicOrder(selectedStoreItemsList, selectedDiscountsOffers) {
    if(selectedDiscountsOffers.length == 0) //create dummy obj if array of offers empty !
        selectedDiscountsOffers.push(new OfferItem(-1, -1, -1, -1));

    return new Order(ORDER_DATE, "dynamic", ORDER_X, ORDER_Y, selectedStoreItemsList, selectedDiscountsOffers);
}

                    /* STATIC ORDER */

function showStaticOrderSummeryOnSubmitClicked(){
    $("input[type='submit']").attr("disabled", 'disabled');
    var selectedDiscountsOffers = createSelectedDiscountsOffersList();
    var selectedStoreItemsList = createSelectedStoreItemsList(STORE_NAME, saveSelecetedItemsForStaticOrder);
    console.log(selectedStoreItemsList[0]);
    var staticOrder = createStaticOrder(selectedStoreItemsList, selectedDiscountsOffers);
    console.log(staticOrder);
    console.log(staticOrder.selectedOfferItemsList[0].quantity);
    //staticOrder = JSON.stringify(staticOrder);
    console.log(staticOrder);

    $.ajax({
        method:'POST',
        url: STATIC_ORDER_SUMMERY,
        method: "POST",
        contentType: "application/json",
        data: JSON.stringify(staticOrder),
        error: function(e) { alert(e); },
        success: function(response) { //response is order summery and approve button
            $("#content").replaceWith(response);
            //approve order button overide
            // $("#selectSpecialOffers").submit(createStaticOrderOnSubmitClicked);e:
        }

    });

    return false;
}

function createAllOrNothingSelectedOffersList() {
    var checkboxes =  $("#selectSpecialOffers input[type=checkbox][value='ALL-NOTHING']");
    var arrOfferItemId = $("#selectSpecialOffers input:hidden[name='offerItemId']");
    var arrOfferItemPrice = $("#selectSpecialOffers input:hidden[name='offerItemPrice']");
    var arrOfferItemStoreId = $("#selectSpecialOffers input:hidden[name='offerItemStoreId']");
    var arrOfferItemQuantity = $("#selectSpecialOffers input:hidden[name='offerItemQuantity']");
    var selectedOffersItems = [];

    //create items list
    for (var i=0; i<checkboxes.length; i++) {
        if(checkboxes[i].checked) {
            var offerItem = new OfferItem(arrOfferItemStoreId[i].value, arrOfferItemId[i].value,
            arrOfferItemQuantity[i].value, arrOfferItemPrice[i].value);
            selectedOffersItems.push(offerItem);
        }
    }

    return selectedOffersItems;
}

function createOneOfSelectedOffersList() {

    console.log($("#selectSpecialOffers input[type=checkbox][value='ONE-OF']"));
    console.log($("#selectSpecialOffers #oneOfOfferSelect"));
    var checkboxes =  $("#selectSpecialOffers input[type=checkbox][value='ONE-OF']");
    var selections =  $("#selectSpecialOffers #oneOfOfferSelect");
    //var arrSelectedOffersOptions = $("#discount input:checked").$("#oneOfOfferSelect option:selected");
    var selectedOffersItems = [];

    //create items list
    for (var i=0; i<checkboxes.length; i++) {
        if(checkboxes[i].checked){
            var selectedOption = selections[i].options[selections[i].selectedIndex];

            var offerItemId = selectedOption.getAttribute("offerItemId");
            var offerItemPrice =selectedOption.getAttribute("offerItemPrice");
            var offerItemQuantity = selectedOption.getAttribute("offerItemQuantity");
            var offerItemStoreId = selectedOption.getAttribute("offerItemStoreId");

            var offerItem = new OfferItem(offerItemStoreId, offerItemId,
                offerItemQuantity,offerItemPrice);

            selectedOffersItems.push(offerItem);
        }
    }

    return selectedOffersItems;
}

function createSelectedDiscountsOffersList(){
    var selectedOneOfOffers = createOneOfSelectedOffersList();
    console.log("selectedOneOfOffers" + selectedOneOfOffers);
    var selectedAllOrNotOffers = createAllOrNothingSelectedOffersList();
    console.log("selectedAllOrNotOffers" + selectedAllOrNotOffers);
    var concatArray = selectedOneOfOffers.concat(selectedAllOrNotOffers);

    console.log(concatArray);

    //return JSON.stringify(concatArray);
    return concatArray;
}

function Order(date, orderKind, x, y, selectedStoreItemsList, selectedOfferItemsList){
    this.date = date;
    this.type = orderKind;
    this.customerX = x;
    this.customerY = y;
    this.selectedStoreItemsList = selectedStoreItemsList;
    this.selectedOfferItemsList = selectedOfferItemsList;
}

function StoreItem(storeName, itemId, itemQuantity){
    this.storeName = storeName;
    this.itemId = itemId;
    this.type = "store";
    this.quantity = itemQuantity;
    /*
    * store id
    * item id
    *  itemKind = "store"
    * quantity
    * */
}

function OfferItem(storeId, itemId, itemQuantity, itemPrice) {
    this.storeId = storeId;
    this.itemId = itemId;
    this.type = "offer";
    this.quantity = itemQuantity;
    this.price = itemPrice;
}

function createSelectedStoreItemsList(storeName, selecetedItemsForStaticOrder){

    var selectedItemsList = [];

    for(var i=0; i< selecetedItemsForStaticOrder.length; i++){
        var storeItem = new StoreItem(storeName, selecetedItemsForStaticOrder[i].itemId, selecetedItemsForStaticOrder[i].quantity);
        selectedItemsList.push(storeItem);
    }

    return selectedItemsList;
}

function createStaticOrder(selectedStoreItemsList, selectedDiscountsOffers){
    if(selectedDiscountsOffers.length == 0) //create dummy obj if array of offers empty !
        selectedDiscountsOffers.push(new OfferItem(-1, -1, -1, -1));

    return new Order(ORDER_DATE, "static", ORDER_X, ORDER_Y, selectedStoreItemsList, selectedDiscountsOffers);
}

function acceptOrderButton(){
    $.ajax({
        method:'POST',
        url: UPDATE_ORDER,
        error: function(e) { alert("Error!"); },
        success: function(response) {
            //$("#feedbacksForm").replaceWith(response);
            $("#content").replaceWith(response);
            $("#storesFeedBacks").attr('action', CREATE_STORES_FEEDBACKS);
            $("#storesFeedBacks").submit(ajaxSendStoresFeedbacks);
        }

    });

}

function declineOrderButton(){
    console.log("end !")
    $.ajax({
        method:'POST',
        url: END_ORDER,
    });

    makeOrderOnClick(); //return to make order page
}

function ajaxSendStoresFeedbacks(){

    var feedBacksList = createFeedBacksList(this);

    if(feedBacksList !== null){
        $.ajax({
            method:'POST',
            url: CREATE_STORES_FEEDBACKS,
            contentType: "application/json",
            data: JSON.stringify(feedBacksList),
            error: function(e) { alert(e); },
            success: function(response) {
                alert(response); // show 'feedback accepted' msg
            }
        });
    }else{
        alert("No Feedback was received.");
    }

    declineOrderButton(); // remove Order from session + goto home page
    //check sending no feedback!

    return false;
}

function createFeedBacksList(form){
   // var formItems = document.getElementsByName("item");
    var checkBoxes = document.getElementsByName("feedbackCheckBox");
    var rates = document.getElementsByName("feedbackRate");
    var comments = document.getElementsByName("feedbackComment");
    var storesNames = document.getElementsByName("storeNameFeedback");
    var selectedStoreFeedbacks = [];

    for (var i=0; i<checkBoxes.length; i++) {
        if(checkBoxes[i].checked){
            console.log(rates[i].value);
            if(rates[i].value == '' || rates[i].value == undefined){
                alert("Must fill rate for chosen store feedback.");
                return null;
            }else{
                var feedback = new Feedback(storesNames[i].textContent, ORDER_DATE,
                    rates[i].value, comments[i].value);
                selectedStoreFeedbacks.push(feedback);
            }
        }
    }

    console.log(selectedStoreFeedbacks);
    if(selectedStoreFeedbacks.length == 0)
        return null;
    else
        var feedbacksList = new FeedbackList(selectedStoreFeedbacks);

    return feedbacksList;
}

function FeedbackList(feedbacksList){
    this.feedbacksList = feedbacksList;
}

function Feedback(storeName, orderDate, rate, comments){
    this.storeName = storeName;
    this.orderDate = orderDate; // = String od date value!
    this.rate = rate;
    this.comments = comments;
}