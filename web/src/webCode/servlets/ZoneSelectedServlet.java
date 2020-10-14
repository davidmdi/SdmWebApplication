package webCode.servlets;

import constants.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ZoneSelectedServlet extends HttpServlet {
    private final String SINGLE_AREA_URL = "../../singleAreaPage/singleArea.html";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String zoneName = req.getParameter(Constants.SELECTED_ZONE);
        System.out.println(req.getRequestURL());
        resp.sendRedirect(SINGLE_AREA_URL);
    }
}
