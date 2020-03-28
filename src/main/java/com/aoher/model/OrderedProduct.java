package com.aoher.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ordered_product", catalog = "shoppingcart")
@NamedQueries({
        @NamedQuery(name = "OrderedProduct.findByCustomerOrderId", query = "SELECT o FROM OrderedProduct o WHERE o.id.customerOrderId = :customerOrderId")
})
public class OrderedProduct implements Serializable {

    private static final long serialVersionUID = 1L;
    private OrderedProductId id;
    private Product product;
    private CustomerOrder customerOrder;
    private int quantity;

    public OrderedProduct() {}

    public OrderedProduct(OrderedProductId id) {
        this.id = id;
    }

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "customerOrderId", column = @Column(name = "customer_order_id", nullable = false)),
            @AttributeOverride(name = "productId", column = @Column(name = "product_id", nullable = false))
    })
    public OrderedProductId getId() {
        return id;
    }

    public void setId(OrderedProductId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_order_id", nullable = false, insertable = false, updatable = false)
    public CustomerOrder getCustomerOrder() {
        return customerOrder;
    }

    public void setCustomerOrder(CustomerOrder customerOrder) {
        this.customerOrder = customerOrder;
    }

    @Column(name = "quantity", nullable = false)
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
