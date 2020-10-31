package webCode.servlets.customerOptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ShowMakeOrderForm extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            out.println("<div id='content' class='content'>");
                out.println("<div class='row'>");
                    out.println("<div class ='col'>");
                        out.println("<h3>Make Order</h3>");
                        out.println("<form id='initDataForOrder' action='' method='GET'>");

                            out.println("<div class='row'>");
                                out.println("<div class='col-25'>");
                                    out.println("<label>Date:</label>");
                                out.println("</div>");
                                out.println("<div class='col-75'>");
                                    out.println("<input type='date' id='dateId' name='dateFromUser' required>");
                                out.println("</div>");
                            out.println("</div>");

                            out.println("<div class='row'>");
                                out.println("<div class='col-25'>");
                                    out.println("<label>Type:</label>");
                                out.println("</div>");
                                out.println("<div class='col-75'>");
                                    out.println("<label for='static'>");
                                        out.println("<input type='radio' id='static' name='typeofOrder' value='static' checked = true>Static");
                                    out.println("</label>");
                                    out.println("<label for='dynamic'>");
                                        out.println("<input type='radio' id='dynamic' name='typeofOrder' value='dynamic'>Dynamic");
                                    out.println("</label>");
                                out.println("</div>");
                            out.println("</div>");

                            out.println("<div class='row'>");
                                out.println("<div class='col-25'>");
                                    out.println("<label>Location:</label>");
                                out.println("</div>");
                                out.println("<div class='col-75'>");
                                    out.println("<input type='number' class='text-cord' id='xCord' name='xCord' placeholder='X..' min='1' max='50' required/>");
                                    out.println("<input type='number' class='text-cord' id='yCord' name='yCord' placeholder='Y..' min='1' max='50' required/>");
                                out.println("</div>");
                            out.println("</div>");

                            out.println("<div class='row'>");
                                out.println("<input type='submit' value='start Shopping' class='login-button'/>");
                            out.println("</div>");
                        out.println("</form>");
                        out.println("<div id='staticOrDynamicOrder'><h5 id='errorMsg' style='color:red;'></h5></div>");
                    out.println("</div>");
                out.println("</div>");
            out.println("</div>");

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
}
/*
	<div id="content">
		<div class='row'>
			<div class='col'>
				<form id='initDataForOrder' action= " + MAKE_ORDER_PAGE_URL + "method='GET' >
					<div class='row'>
						<h2>Make Order</h2>
					</div>
					<div class='row'>
						<div class='col-25'>
							<label>Date:</label>
						</div>
						<div class='col-75'>
							<input type='date' id='dateId' name='dateFromUser' required>
						</div>
					</div>
					<div class='row'>
						<div class='col-25'>
							<label>Type:</label>
						</div>
						<div class='col-75'>
							<label for='static'>
								<input type='radio' id='static' name='typeofOrder' value='static' checked = true>
								Static
							</label>
							<label for='dynamic'>
								<input type='radio' id='dynamic' name='typeofOrder' value='dynamic'>
								Dynamic
							</label>
						</div>
					</div>
					<div class='row'>
						<div class='col-25'>
							<label>Location:</label>
						</div>
						<div class='col-75'>
							<input type='number' class='text-cord' id='xCord' name='xCord' placeholder='X..' min='1' max='50' required/>
							<input type='number' class='text-cord' id='yCord' name='yCord' placeholder='Y..' min='1' max='50' required/>
						</div>
					</div>
					<div class='row'>
						<input type='submit' value='start Shopping' class='login-button'/>
					</div>
				</form>
			</div>
		</div>
	</div>
 */