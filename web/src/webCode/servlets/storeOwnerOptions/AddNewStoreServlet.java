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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.List;

import static constants.Constants.USERNAME;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class AddNewStoreServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
/*
        String storeName = request.getParameter("storeName");
        String storeLocationX = request.getParameter("storeLocationX");
        String storeLocationY = request.getParameter("storeLocationY");
        String ppk = request.getParameter("ppk");
        String items = request.getParameter("items");//[[object],[object]]
        Part itemsPart = request.getPart("items");//[{itemID:"", itemPrice:""}]
*/
        //JSONArray arr = jsonObject.getJSONArray("arrayParamName");
/*
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(request.getPart("items").getInputStream());//request.getReader();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } finally {
            reader.close();
        }
        System.out.println(sb.toString());
*/

        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        Store store = gson.fromJson(reader, Store.class);
        System.out.println(store.toString());
        String name = store.name;
        System.out.println(name);
        System.out.println(store.items.size());
        System.out.println(store.items.get(0));


        //List<Part> fileParts = request.getParts().stream().filter(part -> "file".equals(part.getName())).collect(Collectors.toList());
        try (
            PrintWriter out = response.getWriter()) {

            out.println(store.toString());
            out.println("\nStore added");
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


    private class Store{
        private String name;
        private int x;
        private int y;
        private double ppk;
        private List<Item> items;

        public Store(String name, int x, int y, double ppk, List<Item> items){
            this.name = name;
            this.x = x;
            this.y = y;
            this.ppk = ppk;
            this.items = items;
        }

        @Override
        public String toString() {
            return "Store{" +
                    "name='" + name + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    ", ppk=" + ppk +
                    ", items=" + items +
                    '}';
        }
    }

    private class Item{
        private int id;
        private double price;

        public  Item(){}

        public Item(int id, double price){
            this.id = id;
            this.price = price;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "id=" + id +
                    ", price=" + price +
                    '}';
        }
    }
}
