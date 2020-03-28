package com.aoher.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customer", catalog = "shoppingcart")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String address;
    private String email;
    private String cityRegion;
    private String phone;
    private String ccNumber;
    private Set<CustomerOrder> customerOrderSet = new HashSet<>(0);

    public Customer(String name, String address, String email, String cityRegion, String phone, String ccNumber, Set<CustomerOrder> customerOrderSet) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.cityRegion = cityRegion;
        this.phone = phone;
        this.ccNumber = ccNumber;
        this.customerOrderSet = customerOrderSet;
    }

    public Customer(String name, String address, String email, String cityRegion, String phone, String ccNumber) {
        this(name, address, email, cityRegion, phone, ccNumber, new HashSet<>(0));
    }

    public Customer() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "address", nullable = false, length = 45)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name = "email", nullable = false, length = 45)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "city_region", nullable = false, length = 2)
    public String getCityRegion() {
        return cityRegion;
    }

    public void setCityRegion(String cityRegion) {
        this.cityRegion = cityRegion;
    }

    @Column(name = "phone", nullable = false, length = 45)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "cc_number", nullable = false, length = 19)
    public String getCcNumber() {
        return ccNumber;
    }

    public void setCcNumber(String ccNumber) {
        this.ccNumber = ccNumber;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "customer")
    public Set<CustomerOrder> getCustomerOrderSet() {
        return customerOrderSet;
    }

    public void setCustomerOrderSet(Set<CustomerOrder> customerOrderSet) {
        this.customerOrderSet = customerOrderSet;
    }
}
