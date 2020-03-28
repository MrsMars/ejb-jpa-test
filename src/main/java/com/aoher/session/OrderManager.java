package com.aoher.session;

import com.aoher.cart.ShoppingCart;
import com.aoher.cart.ShoppingCartItem;
import com.aoher.model.*;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.math.BigDecimal;
import java.util.*;

@Stateless
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
public class OrderManager {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager em;

    @Resource
    private SessionContext context;

    @EJB
    private ProductFacade productFacade;

    @EJB
    private CustomerOrderFacade customerOrderFacade;

    @EJB
    private OrderedProductFacade orderedProductFacade;

    public int placeOrder(String name, String email, String phone, String address, String cityRegion,
                          String ccNumber, ShoppingCart cart) {
        try {
            Customer customer = addCustomer(name, email, phone, address, cityRegion, ccNumber);
            CustomerOrder order = addOrder(customer, cart);
            addOrderedItems(order, cart);
            return order.getId();
        } catch (Exception e) {
            context.setRollbackOnly();
            return 0;
        }
    }

    private Customer addCustomer(String name, String email, String phone, String address,
                                 String cityRegion, String ccNumber) {

        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setCityRegion(cityRegion);
        customer.setCcNumber(ccNumber);

        em.persist(customer);
        return customer;
    }

    private CustomerOrder addOrder(Customer customer, ShoppingCart cart) {

        // set up customer order
        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        order.setAmount(BigDecimal.valueOf(cart.getTotal()));

        // create confirmation number
        Random number = new Random();
        int i = number.nextInt(999999999);
        order.setConfirmationNumber(i);

        em.persist(order);
        return order;
    }

    private void addOrderedItems(CustomerOrder order, ShoppingCart cart) {

        em.flush();

        List<ShoppingCartItem> items = cart.getItems();

        // iterate through shopping cart and create OrderedProducts
        // set up primary key object
        // create ordered item using PK object
        // set quantity
        items.forEach(scItem -> {
            int productId = scItem.getProduct().getId();
            OrderedProductId orderedProductId = new OrderedProductId();
            orderedProductId.setCustomerOrderId(order.getId());
            orderedProductId.setProductId(productId);
            OrderedProduct orderedItem = new OrderedProduct(orderedProductId);
            orderedItem.setQuantity(scItem.getQuantity());
            em.persist(orderedItem);
        });
    }

    public Map getOrderDetails(int orderId) {

        Map orderMap = new HashMap();

        // get order
        CustomerOrder order = customerOrderFacade.find(orderId);

        // get customer
        Customer customer = order.getCustomer();

        // get all ordered products
        List<OrderedProduct> orderedProducts = orderedProductFacade.findByOrderId(orderId);

        // get product details for ordered items
        List<Product> products = new ArrayList<>();

        for (OrderedProduct op : orderedProducts) {

            Product p = (Product) productFacade.find(op.getId().getProductId());
            products.add(p);
        }

        // add each item to orderMap
        orderMap.put("orderRecord", order);
        orderMap.put("customer", customer);
        orderMap.put("orderedProducts", orderedProducts);
        orderMap.put("products", products);

        return orderMap;
    }
}
