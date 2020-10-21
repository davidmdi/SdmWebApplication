var refreshRate = 10000; //milli seconds
var AREA_INFO_URL = buildUrlWithContextPath("areaInfo");
var MAKE_ORDER_PAGE_URL = buildUrlWithContextPath("makeOrderPage");
var SHOW_ORDER_HISTORY_PAGE_URL = buildUrlWithContextPath("showCustomersOrderHistoryPage");
var PRESENT_SELECTE_SROTE_ITEMS = buildUrlWithContextPath("showSelctedStoreItems");
var STATIC_ORDER = buildUrlWithContextPath("staticOrder");
var DYNAMIC_ORDER = buildUrlWithContextPath("dynamicOrder");
var parametrs;
// var formData = new FormData();

function changeSelectedMenuOption(selectedMenuOptionID){
//find current selectedOption:
    var menuOptions = $("#menu").children("a");
//unSelect old option:
    menuOptions.removeAttr("class");
//select the new option:
    $(selectedMenuOptionID).attr({class: 'active'});
}

function makeOrderOnClick(){

    changeSelectedMenuOption("#makeOrder");//changes to ui options
    //$("#content").empty(); //clear old content
    $("#content").replaceWith(buildFormForOrder());
    $("#initDataForOrder").submit(function () {
            parametrs = $("form").serialize();
            try{
                if(document.querySelector('input[name="typeofOrder"]:checked').value == dynamic)
                    showZoneItemsList(parametrs);
                else
                    showZoneStoresComboBox(parametrs);
            }catch (e) {
                console.log("Error invoking the ajax !" + e);
            }
        return false;
    });

    return false;
}

function showZoneStoresComboBox(parametrs){
    $.ajax({
        data:parametrs,
        //  data:formData,
        //  processData: false,
        url: MAKE_ORDER_PAGE_URL,
        success: function(response) {
            // $("#content").replaceWith(response);
            $("h6").hide()
            $("#content").append(response); // returns comboBox of zone's stores

            //$(id).submit(function y{creating ajax  param.append(name, value) });
            $("#storeSelectForm").attr("action", PRESENT_SELECTE_SROTE_ITEMS); //replace form 'action' attribute
            $("#storeSelectForm").submit(showStoreDiscountsOffers); //override 'submit' function
            return false;
        }

    });
}


function showZoneItemsList(parametrs){
    $.ajax({
        data:parametrs,
        //  data:formData,
        //  processData: false,
        url: MAKE_ORDER_PAGE_URL,
        success: function(response) {
            // $("#content").replaceWith(response);
            $("h6").hide()
            $("#content").append(response);
            //$(id).submit(function y{creating ajax  param.append(name, value) });
            $("#storeSelectForm").attr("action", PRESENT_SELECTE_SROTE_ITEMS);
            $("#storeSelectForm").submit(showSelectedStoreInfo);
            return false;
        }

    });
}

function showOrderHistory() {
    changeSelectedMenuOption("#orderHistory");//changes to ui options


    $.ajax({

        url: SHOW_ORDER_HISTORY_PAGE_URL,
        success: function (response) {
            $("#content").replaceWith(response)}


    });
}

/* called after store was selected (static order) */
function showSelectedStoreInfo() {

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
    var selecetedItemsForStaticOrder = createStaticOrderItemList();
    $.ajax({
        method:'POST',
        data: selecetedItemsForStaticOrder,
        url: STATIC_ORDER,
        contentType: "application/json",
        timeout: 4000,
        error: function(e) { alert(e); },
        success: function(response) {
           $("#content").append(response);
           //on submit function...
        }

    });
    return false;
}

//action= "+ MAKE_ORDER_PAGE_URL + "
    function buildFormForOrder(){
     var htmlBuilder = "<div id='content'>" +
         "<form id='initDataForOrder' action= " + MAKE_ORDER_PAGE_URL + "method='GET' >" +
         "<input type=\"date\" id=\"dateId\" name=\"dateFromUser\">" + "<br><br> " +
        "<input type=\"radio\" id=\"static\" name=\"typeofOrder\" value=\"static\" checked = true>" +
        "<label for=\"static\">Static </label>\n" +
        "<input type=\"radio\" id=\"dynamic\" name=\"typeofOrder\" value=\"dynamic\">\n" +
        "<label for=\"dynamic\">Dynamic</label><br><br>\n" +
        " <label for=\"xCord\">X location: </label>\n" +
        " <input type=\"number\" id=\"xCord\" name=\"xCord\" class=\"\" placeholder=\" x cord-> int range of 1-50\" min=\"1\" max=\"50\" required/>"+
        " <label for=\"yCord\">Y location: </label>\n" +
        " <input type=\"number\" id=\"yCord\" name=\"yCord\" class=\"\" placeholder=\"y cord-> int range of 1-50\" min=\"1\" max=\"50\" required/>"+
        "<br><br>" +
        " <input type=\"submit\" value=\"start Shopping\" class = \"login-button\"/>"+
        "</form>"  + "</div>";

    return htmlBuilder ;
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
    }
    var itemsListSelected = new storeSelectioItems(document.getElementById('storeNameLabel').textContent
        ,selectedItems); // (name of store , selecedItemsArray from user)
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

