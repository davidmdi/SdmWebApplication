package webCode.servlets;

import SDM_CLASS.Location;
import SDM_CLASS.SuperDuperMarketDescriptor;
import logic.Logic.Engine;
import sun.misc.IOUtils;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
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

        String msg = engine.loadFileFromOwner(ownerName, filePart.getInputStream());

            /*
    try {
        Part filePart = request.getPart("xmlFile");
        String fileContent1 = readFromInputStream(filePart.getInputStream());
        engine.loadFileFromOwner(filePart.getInputStream())

        JAXBContext jc = JAXBContext.newInstance("SDM_CLASS");
        Unmarshaller u = jc.createUnmarshaller();
        sdmJAXB = (SuperDuperMarketDescriptor) u.unmarshal(filePart.getInputStream());
        System.out.println(sdmJAXB);
    }catch (JAXBException e){e.printStackTrace();}
             */



        //xmlFile

        /*
        // we could extract the 3rd member (not the file one) also as 'part' using the same 'key'
        // we used to upload it on the formData object in JS....
        Part name = request.getPart("name");
        String nameValue = readFromInputStream(name.getInputStream());
         */


        PrintWriter out = response.getWriter();

        Collection<Part> parts = request.getParts();

        System.out.println("Total parts : " + parts.size() + "\n");

        StringBuilder fileContent = new StringBuilder();

        for (Part part : parts) {
            //to write the content of the file to a string
            fileContent.append("New Part content:").append("\n");
            fileContent.append(readFromInputStream(part.getInputStream())).append("\n");
        }

        System.out.println(fileContent.toString());
        out.println(fileContent.toString());


    }


    private String readFromInputStream(InputStream inputStream) {
        return new Scanner(inputStream).useDelimiter("\\Z").next();
    }

}
