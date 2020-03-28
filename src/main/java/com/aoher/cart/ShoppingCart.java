package com.aoher.cart;

import com.aoher.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    List<ShoppingCartItem> items;
    int numberOfItems;
    double total;

    public ShoppingCart() {
        items = new ArrayList<>();
        numberOfItems = 0;
        total = 0;
    }

    /**
     * Adds a <code>ShoppingCartItem</code> to the <code>ShoppingCart</code>'s <code>items</code>
     * list. If item of the specified <code>product</code> already exists in shopping cart list, the
     * quantity of that item is incremented.
     *
     * @param product the <code>Product</code> that defines the type of shopping cart item
     * @see ShoppingCartItem
     */
    public synchronized void addItem(Product product) {
        boolean newItem = true;

        for (ShoppingCartItem scItem : items) {
            if (scItem.getProduct().getId().equals(product.getId())) {
                newItem = false;
                scItem.incrementQuantity();
            }
        }

        if (newItem) {
            ShoppingCartItem scItem = new ShoppingCartItem(product);
            items.add(scItem);
        }
    }

    /**
     * Updates the <code>ShoppingCartItem</code> of the specified <code>product</code> to the
     * specified quantity. If '<code>0</code>' is the given quantity, the
     * <code>ShoppingCartItem</code> is removed from the <code>ShoppingCart</code>'s
     * <code>items</code> list.
     *
     * @param product the <code>Product</code> that defines the type of the shopping cart item
     * @param quantity the number which the <code>ShoppingCartItem</code> is updated to
     * @see ShoppingCartItem
     */
    public synchronized void update(Product product, String quantity) {
        short qty = -1;

        qty = Short.parseShort(quantity);

        if(qty >= 0) {
            ShoppingCartItem item = null;
            for (ShoppingCartItem scItem : items) {
                if (scItem.getProduct().getId().equals(product.getId())) {
                    if (qty != 0) {
                        scItem.setQuantity(qty);
                    } else {
                        item = scItem;
                        break;
                    }
                }
            }
            if (item != null) {
                items.remove(item);
            }
        }
    }

    /**
     * Returns the list of <code>ShoppingCartItems</code>.
     *
     * @return the <code>items</code> list
     * @see ShoppingCartItem
     */
    public synchronized List<ShoppingCartItem> getItems() {
        return items;
    }

    /**
     * Returns the sum of quantities for all items maintained in shopping cart <code>items</code>
     * list.
     *
     * @return the number of items in shopping cart
     * @see ShoppingCartItem
     */
    public synchronized int getNumberOfItems() {
        numberOfItems = 0;
        items.forEach(cart -> numberOfItems += cart.getQuantity());
        return numberOfItems;
    }

    /**
     * Returns the sum of the product price multiplied by the quantity for all items in shopping cart
     * list. This is the total cost excluding the surcharge.
     *
     * @return the cost of all items times their quantities
     * @see ShoppingCartItem
     */
    public synchronized double getSubtotal() {
        return items.stream()
                .mapToDouble(scItem -> (scItem.getQuantity() * scItem.getProduct().getPrice().doubleValue()))
                .sum();
    }

    /**
     * Calculates the total cost of the order. This method adds the subtotal to the designated
     * surcharge and sets the <code>total</code> instance variable with the result.
     *
     * @param surcharge the designated surcharge for all orders
     * @see ShoppingCartItem
     */
    public synchronized void calculateTotal(String surcharge) {
        double s = Double.parseDouble(surcharge);
        double amount = this.getSubtotal();
        amount += s;
        total = amount;
    }

    /**
     * Returns the total cost of the order for the given <code>ShoppingCart</code> instance.
     *
     * @return the cost of all items times their quantities plus surcharge
     */
    public synchronized double getTotal() {
        return total;
    }

    /**
     * Empties the shopping cart. All items are removed from the shopping cart <code>items</code>
     * list, <code>numberOfItems</code> and <code>total</code> are reset to '<code>0</code>'.
     *
     * @see ShoppingCartItem
     */
    public synchronized void clear() {
        items.clear();
        numberOfItems = 0;
        total = 0;
    }
}
