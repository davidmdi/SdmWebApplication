var refreshRate = 10000; //milli seconds
var AREA_INFO_URL = buildUrlWithContextPath("areaInfo");
var MAKE_ORDER_PAGE_URL = buildUrlWithContextPath("makeOrderPage");
var SHOW_ORDER_HISTORY_PAGE_URL = buildUrlWithContextPath("showCustomersOrderHistoryPage");

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

    $.ajax({
        url: MAKE_ORDER_PAGE_URL, // need to change to correct url
        success: function(response) {
            $("#content").replaceWith(buildMakeOrderHtml(response));
        }
    });
    return false;
}

function showOrderHistory() {
    changeSelectedMenuOption("#orderHistory");//changes to ui options
    $.ajax({
        url: SHOW_ORDER_HISTORY_PAGE_URL,
        success: function (response) {
            $("#content").replaceWith(response);
            //jqury -> push the wanted data...
        }
    });
    return false;
}

   function buildMakeOrderHtml(response){
    var htmlbuilder;
    htmlbuilder = "<div>" + "<form action=\"/getInputForOrder\">\n" +
        "  <label for=\"datePicker\">Date:</label>\n" +
        "  <input type=\"date\" id=\"datePicker\" name=\"dateInput\">\n" +
        "  <input type=\"submit\">\n" +
        "</form>"
       + "</div>";

    return htmlbuilder;




   }

