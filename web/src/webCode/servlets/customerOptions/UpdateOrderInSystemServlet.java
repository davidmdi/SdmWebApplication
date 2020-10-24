package webCode.servlets.customerOptions;

import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyOrder;
import logic.Logic.My_CLASS.MyStoreSingleOrderItems;
import logic.Logic.My_CLASS.MySuperMarket;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class UpdateOrderInSystemServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processes(req, resp);
    }

    private void processes(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //init
        MyOrder order = SessionUtils.getOrder(req);
        Map<Integer, MyStoreSingleOrderItems> storeSingleOrderItemsMap =
                SessionUtils.getStoreSingleOrderItemsMap(req);

        Engine engine = ServletUtils.getEngine(getServletContext());
        String areaName = SessionUtils.getAreaName(req);
        MySuperMarket superMarket = engine.getMySupermarkets()
                .getAreaSuperMarketByName(areaName);

        superMarket.updateOrder(order,storeSingleOrderItemsMap);


        // send notification to store owners...
        // handle money transfer...
        //throw alert -> update success...

        try (PrintWriter out = resp.getWriter()) {
            out.println("Order sent successfully , thank you for buying :) ");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processes(req, resp);
    }
}
