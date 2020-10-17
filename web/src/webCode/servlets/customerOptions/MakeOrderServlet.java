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

public class MakeOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try (PrintWriter out = resp.getWriter()) {
            //out.println(req.getParameter("dateFromUser"));
            //checking data in engine.
            //returning data (converted to Json) to js.

//            String dateS = req.getParameter("dateFromUser");
//            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateS);
//            out.println(date);
            Engine engine = ServletUtils.getEngine(getServletContext());

            //saving the query parameters as strings .
            String dateAsString = req.getParameter("dateFromUser");
            String orderType = req.getParameter("typeofOrder");
            String xCord = req.getParameter("xCord");
            String yCord = req.getParameter("yCord");
            // getting the zone from session.
            String zoneName = SessionUtils.getAreaName(req);

            // check cord.->function for checking from engine
            if(engine.isCordValid(xCord,yCord,zoneName)){
                /*do somthing */
            }


            // convert date string to date. -> convert function
            //Date date = convertToDate(dateAsString);
            // static ? dynamic -> if static : ask store:  -> return form?
            /* how to do it????*/
            // create Json of selected items
            // present sells
            // present summery
            // save ? abort


            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
