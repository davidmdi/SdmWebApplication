var refreshRate = 10000; //milli seconds
var AREA_INFO_URL = buildUrlWithContextPath("areaInfo");
var STORE_ORDERS_PAGE_URL = buildUrlWithContextPath("storeOrdersPage");

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
                }
            });
    return false;
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
