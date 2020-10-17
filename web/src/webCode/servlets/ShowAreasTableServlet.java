package webCode.servlets;

import com.google.gson.Gson;
import logic.Logic.Engine;
import logic.Logic.My_CLASS.MySuperMarket;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class ShowAreasTableServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        //super.doGet(req, resp);
        //get all Areas
        //Convert areas to JSON object
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Engine engine = ServletUtils.getEngine(getServletContext());
            //Map<MyOwner,Set<MySuperMarket>> supermarkets = engine.getMySupermarkets().getSuperMarkets();

            Set<MySuperMarket> superMarketsForJson = getSuperMarketsJson(engine);
            String json = gson.toJson(superMarketsForJson);


            out.println(json);
            out.flush();
        }
    }

    private Set<MySuperMarket> getSuperMarketsJson(Engine engine) {
        Set<MySuperMarket> superMarketsForJson = new HashSet<>();

        for(Set<MySuperMarket> supermarket : engine.getMySupermarkets().getSuperMarkets().values()){
            superMarketsForJson.addAll(supermarket);
        }

        return superMarketsForJson;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
