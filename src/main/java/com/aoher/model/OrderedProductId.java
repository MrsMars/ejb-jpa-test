package com.aoher.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderedProductId implements Serializable {

    private static final long serialVersionUID = 1L;
    private int customerOrderId;
    private int productId;

    public OrderedProductId(int customerOrderId, int productId) {
        this.customerOrderId = customerOrderId;
        this.productId = productId;
    }

    public OrderedProductId() {
    }

    @Column(name = "customer_order_id", nullable = false)
    public int getCustomerOrderId() {
        return this.customerOrderId;
    }

    public void setCustomerOrderId(int customerOrderId) {
        this.customerOrderId = customerOrderId;
    }


    @Column(name = "product_id", nullable = false)
    public int getProductId() {
        return this.productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderedProductId that = (OrderedProductId) o;
        return customerOrderId == that.customerOrderId &&
                productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerOrderId, productId);
    }
}
