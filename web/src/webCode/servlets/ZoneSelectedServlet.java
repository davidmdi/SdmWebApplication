package webCode.servlets;

import constants.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ZoneSelectedServlet extends HttpServlet {
    private final String SINGLE_AREA_URL = "pages/singleAreaPage/singleArea.html";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String zoneName = req.getParameter(Constants.SELECTED_ZONE);
        req.getSession(true).setAttribute(Constants.SELECTED_ZONE, zoneName);
        resp.sendRedirect(SINGLE_AREA_URL);

    }//http://localhost:8080/webSdm/zoneSelected
}
