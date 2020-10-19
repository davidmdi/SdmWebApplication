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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class MakeOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException , IOException {
        PrintWriter out = resp.getWriter();
        try  {
            //resp.setContentType("text/html;charset=UTF-8");

            Engine engine = ServletUtils.getEngine(getServletContext());
            //saving the query parameters as strings // need to save it on the session?
            String dateAsString = req.getParameter("dateFromUser");
            String orderType = req.getParameter("typeofOrder");
            int xCord = Integer.parseInt(req.getParameter("xCord"));
            int yCord = Integer.parseInt(req.getParameter("yCord")) ;
            // getting the zone from session.
            String zoneName = SessionUtils.getAreaName(req);
            if(engine.isCoordinateAreValid(xCord,yCord,zoneName)){
                if(orderType.equalsIgnoreCase("static")){
                    // return comboBox of store selection should be type of form witch
                    // send it to other servlet :
                    // if so need to save the query parameters on the session?
                    // create a Jason of chosen store items.

                }
                else{
                    // return Items Json
                }
            }
            else{
                out.println("illegal coordinates:cord out of range/duplicate with store" +
                        " , please type integer in rang of 1-50");
            }


            /*
            * check coords .
            * create json.
            * return form->for static dynamic
            *
             */
            out.flush();
        }catch (NumberFormatException e){
            out.println("illegal coordinates , please type integer in rang of 1-50");
            // need write it in html format.
        }
    }

    private Date convertStringDate(String dateAsString) throws ParseException {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateAsString);
           return date;
    }
}
