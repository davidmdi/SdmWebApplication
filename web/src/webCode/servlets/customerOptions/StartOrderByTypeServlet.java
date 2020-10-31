package webCode.servlets.customerOptions;

import logic.Logic.Engine;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class StartOrderByTypeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            Engine engine = ServletUtils.getEngine(getServletContext());
            String dateAsString = req.getParameter("dateFromUser");
            String orderType = req.getParameter("typeofOrder");
            int xCord = Integer.parseInt(req.getParameter("xCord"));
            int yCord = Integer.parseInt(req.getParameter("yCord"));
            // getting the zone from session.
            String zoneName = SessionUtils.getAreaName(req);
            if (!engine.isStoreLocationValid(xCord, yCord)) {
                out.println("<h5 id='errorMsg' style='color:red;'>Error! there is a store in that location</h5>");
                //out.println("<h6 id=\"errMsg\" style=\"color:red;\">error:hitting store location</h6>");
            } else {
                out.println("<h5 id='errorMsg' style='color:red;'></h5>");
            }
        }
    }
}