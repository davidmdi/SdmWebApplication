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

        engine.getMySupermarkets().updateOrder(order,storeSingleOrderItemsMap,superMarket,engine);

      engine.addNewOrderAlert(order); //add alerts to stores owners

        // send notification to store owners...
        // handle money transfer... done
        //throw alert -> update success...done

       getServletContext()
               .getRequestDispatcher("/showFeedbacksForm").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processes(req, resp);
    }
}
