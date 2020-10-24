package webCode.servlets.customerOptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateOrderInSystemServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processes(req, resp);
    }

    private void processes(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // dont forget casting session attribute to MyOrder & storeSingleOrderItemsMap....
        //from the request i get the order
        // call to a function witch updates the data base (including money management + throw alert and fidback option)
        // deleteOrderFromSession.

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processes(req, resp);
    }
}
