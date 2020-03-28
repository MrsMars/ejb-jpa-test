package com.aoher.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "product", catalog = "shoppingcart")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Category category;
    private String name;
    private BigDecimal price;
    private String description;
    private Date dateCreated;
    private Set<OrderedProduct> orderedProducts = new HashSet<>(0);

    public Product(Category category, String name, BigDecimal price, String description, Date dateCreated, Set<OrderedProduct> orderedProducts) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.description = description;
        this.dateCreated = dateCreated;
        this.orderedProducts = orderedProducts;
    }

    public Product(Category category, String name, BigDecimal price, String description, Date dateCreated) {
        this(category, name, price, description, dateCreated, new HashSet<>(0));
    }

    public Product() {
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "price", nullable = false, precision = 5)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "description", nullable = false, length = 145)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", nullable = false, length = 19)
    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    public Set<OrderedProduct> getOrderedProducts() {
        return orderedProducts;
    }

    public void setOrderedProducts(Set<OrderedProduct> orderedProducts) {
        this.orderedProducts = orderedProducts;
    }
}
