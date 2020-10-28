package webCode.servlets.storeOwnerOptions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class StoreOrdersServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            out.println("<div id='content' class='content'>");
            out.println("<div class=\"row\">");
            out.println("<div>");
            out.println("<h3>Stores you own</h3><p>Choose store:</p>");
            out.println("<ul id=\"storesList\" class=\"store-list\"></ul>");
            out.println("<form id=\"selectStore\" method=\"POST\" action=\"showStoreOrders\">");
            out.println("<input id=\"selected_store\" type='hidden'/>");
            out.println("</form>");
            out.println("</div>");
            out.println("<div class=\"col\">");
            //storeOrdersHistory:
            out.println("<div id=\"storeOrdersHistory\">");
            out.println("<h3>Orders history</h3><p>press on order to show the items from this store<p>");
            out.println("<table id=\"ordersHistoryTable\">");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>ID</th><th>Date</th><th>Customer name</th><th>Customer location</th>");
            out.println("<th>Total items amount</th><th>Total items cost</th><th>Delivery cost</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody></tbody>");
            out.println("</table>");
            out.println("</div>");
            //orderItems:
            //out.println("<div id=\"orderItems\" orderID='' style=\"display:none;\">");
            out.println("<div id=\"orderItems\" orderID=''>");
            out.println("<h3>Order Items</h3><p id='pInfo'>items purchase from selected order<p>");
            out.println("<table id=\"orderItemsTable\">");
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>ID</th><th>Name</th><th>Purchase method</th><th>Amount</th>");
            out.println("<th>Price</th><th>Total cost</th><th>Bought in discount</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody></tbody>");
            out.println("</table>");
            out.println("<form id=\"storeOrderItems\" method=\"POST\" action=\"\">");
            out.println("<input id=\"selected_order\" type='hidden' name=\"selectedOrder\"/>");
            out.println("</form>");
            out.println("</div>");
            out.println("</div>");//end 'col'
            out.println("</div>");//end 'row'
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
		<div class="row">
			<div id="storesList">
				<h3>Stores you own</h3>
				<p>Choose store:</p>
				<ul class="store-list">
					<li class="store-active" store="storeName1">store1</li>
					<li class="store" store="storeName2">store2</li>
					<li class="store" store="storeName3">store3</li>
					<li class="store" store="storeName4">Store 4</li>
				</ul>
				<form id="selectStore" method="POST" action="showStoreOrders">
					<input id="selected_store" type='hidden'/>
				</form>
			</div>
			<div class="col">
				<div id="storeOrdersHistory">
					<h3>Orders history</h3>
					<p>press on order to show the items from this store<p>
					<table id="ordersHistoryTable">
						<thead>
						<tr>
							<th>ID</th>
							<th>Date</th>
							<th>Customer name</th>
							<th>Customer location</th>
							<th>Total items amount</th>
							<th>Total items cost</th>
							<th>Delivery cost</th>
						</tr>
						</thead>
						<tbody>
						 </tbody>
					</table>
				</div>
				<div id="orderItems">
					<h3>Order Items</h3>
					<p>items purchase from selected store<p>
					<table id="orderItemsTable">
					<thead>
						<tr>
							<th>ID</th>
							<th>Name</th>
							<th>Purchase method</th>
							<th>Amount</th>
							<th>Price</th>
							<th>Total cost</th>
							<th>Bought in discount</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
             */