package webCode.servlets;

import logic.Logic.Engine;
import logic.Logic.My_CLASS.AccountAction;
import logic.Logic.My_CLASS.MyCustomer;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LoadMoneyOnActionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)  {
            process(req,resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) {
        resp.setContentType("text/html;charset=UTF-8");
        try(PrintWriter out = resp.getWriter()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(System.currentTimeMillis());
            Engine engine = ServletUtils.getEngine(getServletContext());
            String userName = SessionUtils.getUsername(req);

            MyCustomer customer = engine.getMyUsers().findCustomerByName(userName);
            if (customer != null) {
                BufferedReader reader = req.getReader();
                double moneyAmount = Double.parseDouble(reader.readLine());
                double before = customer.getUser().getAccount().getBalance();
                double after = before + moneyAmount;
                AccountAction action = new AccountAction("load", date, moneyAmount, before, after);
                customer.getUser().getAccount().addAction(action);
                out.println("Load money successfully");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }
}
/**
 *    try {
 *             SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
 *             Date date = new Date(System.currentTimeMillis());
 *             Engine engine = ServletUtils.getEngine(getServletContext());
 *             String userName = SessionUtils.getUsername(req);
 *             MyCustomer customer = engine.getMyUsers().findCustomerByName(userName);
 *             if (customer != null) {
 *                 double moneyAmount = Double.parseDouble(req.getParameter("moneyToLoad"));
 *                 double before = customer.getUser().getAccount().getBalance();
 *                 double after = before + moneyAmount;
 *                 AccountAction action = new AccountAction("load", date, moneyAmount, before, after);
 *                 customer.getUser().getAccount().getActionList().add(action);
 *                 try {
 *                     PrintWriter out = resp.getWriter();
 *                     out.println("Load money successfully");
 *                 } catch (IOException e) {
 *                     e.printStackTrace();
 *                 }
 *             }
 *         } catch (Exception e) {
 *             e.printStackTrace();
 *         }
 */




