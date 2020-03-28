package com.aoher.controller;

import com.aoher.cart.ShoppingCart;
import com.aoher.model.Category;
import com.aoher.model.Product;
import com.aoher.session.CategoryFacade;
import com.aoher.session.OrderManager;
import com.aoher.session.ProductFacade;

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

@WebServlet(name = "ControllerServlet", loadOnStartup = 1,
        urlPatterns = {"/category",
                "/addToCart",
                "/viewCart",
                "/updateCart",
                "/chooseLanguage",
                "/purchase",
                "/checkout"})
public class ControllerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private String surcharge;

    @EJB
    private CategoryFacade categoryFacade;

    @EJB
    private ProductFacade productFacade;

    @EJB
    private OrderManager orderManager;

    public ControllerServlet() {
    }

    @Override
    public void init() {
        getServletContext().setAttribute("categories", categoryFacade.findAll());
        surcharge = getServletContext().getInitParameter("deliverySurcharge");
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String userPath = request.getServletPath();

        Category selectedCategory;
        List<Product> categoryProducts;

        switch (userPath) {
            case "/category":
                // get categoryID from request
                String categoryID = request.getQueryString();

                if (categoryID != null) {

                    selectedCategory = categoryFacade.find(Integer.parseInt(categoryID));
                    session.setAttribute("selectedCategory", selectedCategory);

                    categoryProducts = selectedCategory.getProducts();
                    session.setAttribute("categoryProducts", categoryProducts);
                }
                break;

            // if view cart is requested
            case "/viewCart":

                String clear = request.getParameter("clear");

                if ((clear != null && clear.equals("true"))) {
                    ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
                    cart.clear();
                }

                userPath = "/cart";
                break;

            // if checkout page is requested
            case "/checkout":
                ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");

                // calculate total
                cart.calculateTotal(surcharge);

                // forward to checkout page and switch to a secure channel

                break;
        }

        String url = "WEB-INF/view" + userPath + ".jsp";
        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userPath = request.getServletPath();
        HttpSession session = request.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");

        // if user adds to cart
        switch (userPath) {
            case "/addToCart": {

                // if user is adding item to cart for first time
                // create cart object and attach it to user session
                if (cart == null) {

                    cart = new ShoppingCart();
                    session.setAttribute("cart", cart);
                }

                // get user input from request
                String productId = request.getParameter("productId");

                if (!productId.isEmpty()) {

                    Product product = productFacade.find(Integer.parseInt(productId));
                    cart.addItem(product);
                }

                userPath = "/category";
                break;
            }

            // if update cart action is called
            case "/updateCart": {
                // get input from request
                String productId = request.getParameter("productId");
                String quantity = request.getParameter("quantity");

                Product product = productFacade.find(Integer.parseInt(productId));
                cart.update(product, quantity);

                userPath = "/cart";
                break;
            }

            // if purchase action is called
            case "/purchase":

                if (cart != null) {

                    // extract user data from request
                    String name = request.getParameter("name");
                    String email = request.getParameter("email");
                    String phone = request.getParameter("phone");
                    String address = request.getParameter("address");
                    String cityRegion = request.getParameter("cityRegion");
                    String ccNumber = request.getParameter("creditcard");

                    boolean validationErrorFlag = false;

                    if (validationErrorFlag == true) {
                        request.setAttribute("validationErrorFlag", validationErrorFlag);
                        userPath = "/checkout";

                    } else {

                        int orderId =
                                orderManager.placeOrder(name, email, phone, address, cityRegion, ccNumber, cart);

                        if (orderId != 0) {

                            session.invalidate();

                            @SuppressWarnings("rawtypes")
                            Map orderMap = orderManager.getOrderDetails(orderId);

                            request.setAttribute("customer", orderMap.get("customer"));
                            request.setAttribute("products", orderMap.get("products"));
                            request.setAttribute("orderRecord", orderMap.get("orderRecord"));
                            request.setAttribute("orderedProducts", orderMap.get("orderedProducts"));

                            userPath = "/confirmation";

                        } else {
                            userPath = "/checkout";
                            request.setAttribute("orderFailureFlag", true);
                        }
                    }
                }
                break;
        }
        String url = "/WEB-INF/view" + userPath + ".jsp";

        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}