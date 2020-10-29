package webCode.servlets;

import com.google.gson.Gson;
import constants.Constants;
import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyItem;
import logic.Logic.My_CLASS.MyOwner;
import logic.Logic.My_CLASS.MySuperMarket;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AreaInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        //super.doGet(req, resp);
        //get all Areas
        //Convert areas to JSON object
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getEngine(getServletContext());
            String areaName =  SessionUtils.getAreaName(req);
            Set<MySuperMarket> areaSuperMarkets = engine.getMySupermarkets().getAreaSuperMarketsSet(areaName);

            String json = gson.toJson(areaSuperMarkets);

            out.println(json);
            out.flush();
        }
    }
}
