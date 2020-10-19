package webCode.servlets.storeOwnerOptions;

import com.google.gson.Gson;
import logic.Logic.Engine;
import logic.Logic.My_CLASS.MyItem;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;

import static constants.Constants.USERNAME;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class AddNewStoreServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String storeName = request.getParameter("storeName");
        String storeLocationX = request.getParameter("storeLocationX");
        String storeLocationY = request.getParameter("storeLocationY");
        String ppk = request.getParameter("ppk");
        String items = request.getParameter("items");//[[object],[object]]

        Gson g = new Gson();
        Item p = g.fromJson(items, Item.class);



        Part itemsPart = request.getPart("items");//[{itemID:"", itemPrice:""}]
        //List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName())).collect(Collectors.toList());
        try (
            PrintWriter out = response.getWriter()) {

            out.println("Store added");
            out.flush();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private class Item{
        private int id;
        private double price;

        public  Item(){}

        public Item(int id, double price){
            this.id = id;
            this.price = price;
        }
    }
}
