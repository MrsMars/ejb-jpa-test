package com.aoher.controller;

import com.aoher.model.Customer;
import com.aoher.model.CustomerOrder;
import com.aoher.session.CustomerFacade;
import com.aoher.session.CustomerOrderFacade;
import com.aoher.session.OrderManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminServlet",
        urlPatterns = {"/admin/",
                "/admin/viewOrders",
                "/admin/viewCustomers",
                "/admin/customerRecord",
                "/admin/orderRecord",
                "/admin/logout"})
public class AdminServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(AdminServlet.class);

    private static final long serialVersionUID = 1L;

    @EJB
    private OrderManager orderManager;

    @EJB
    private CustomerFacade customerFacade;

    @EJB
    private CustomerOrderFacade customerOrderFacade;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(true);
        String userPath = request.getServletPath();

        switch (userPath) {
            case "/admin/viewCustomers":
                List<Customer> customerList = customerFacade.findAll();
                request.setAttribute("customerList", customerList);
                break;
            case "/admin/viewOrders":
                List<CustomerOrder> orderList = customerOrderFacade.findAll();
                request.setAttribute("orderList", orderList);
                break;
            case "/admin/customerRecord":
                String customerId = request.getQueryString();

                Customer customer = customerFacade.find(Integer.parseInt(customerId));
                request.setAttribute("customerRecord", customer);

                CustomerOrder order = customerOrderFacade.findByCustomer(customer);
                request.setAttribute("order", order);
                break;
            case "/admin/orderRecord":
                String orderId = request.getQueryString();

                Map orderMap = orderManager.getOrderDetails(Integer.parseInt(orderId));
                request.setAttribute("customer", orderMap.get("customer"));
                request.setAttribute("products", orderMap.get("products"));
                request.setAttribute("orderRecord", orderMap.get("orderRecord"));
                request.setAttribute("orderedProducts", orderMap.get("orderedProducts"));
                break;
            case "/admin/logout":
                session = request.getSession();
                session.invalidate();   // terminate session

                response.sendRedirect("/azimbo/admin/");
                return;
        }

        userPath = "/admin/index.jsp";
        try {
            request.getRequestDispatcher(userPath).forward(request, response);
        } catch (ServletException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processRequest(request, response);
    }
}
