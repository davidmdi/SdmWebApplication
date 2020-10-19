package webCode.servlets;

import SDM_CLASS.SuperDuperMarketDescriptor;
import logic.Logic.Engine;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       // response.sendRedirect("fileupload/form.html");


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        SuperDuperMarketDescriptor sdmJAXB;
        Engine engine = ServletUtils.getEngine(getServletContext());
        Part filePart = request.getPart("xmlFile");
        String ownerName = SessionUtils.getUsername(request);

        String msg = engine.createSDMSuperMarket(ownerName, filePart.getInputStream());

        PrintWriter out = response.getWriter();
        //out.println("<html><head></head><body><div>");
        out.println(msg);
        //out.println("</div></body></html>");
    }


    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

}
