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
    $("#content").replaceWith(buildFormForOrder());
    $("#initDataForOrder").submit(function () {
        var parametrs = $(this).serialize();
        try{
            $.ajax({
                data:parametrs,
                url: MAKE_ORDER_PAGE_URL,
                    success: function(response) {
                       // $("#content").replaceWith(response);
                        $("#content").append(response);
                        console.log(response);
                    }

            });

        }catch (e) {
            console.log("Error invoking the ajax !" + e);
        }
        return false;

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
        " <input type=\"text\" id=\"xCord\" name=\"xCord\" class=\"\" placeholder=\" x cord-> int range of 1-50\" required/>"+
        " <label for=\"yCord\">Y location: </label>\n" +
        " <input type=\"text\" id=\"yCord\" name=\"yCord\" class=\"\" placeholder=\"y cord-> int range of 1-50\" required/>"+
        "<br><br>" +
        " <input type=\"submit\" value=\"start Shopping\" class = \"login-button\"/>"+
        "</form>"  + "</div>";

    return htmlBuilder ;
   }

