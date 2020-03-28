package com.aoher.session;

import com.aoher.model.CustomerOrder;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CustomerOrderFacade extends AbstractFacade<CustomerOrder> {

    @PersistenceContext(unitName = "affablebean")
    private EntityManager em;

    public CustomerOrderFacade() {
        super(CustomerOrder.class);
    }

    @RolesAllowed("azimboAdmin")
    public CustomerOrder findByCustomer(Object customer) {
        return (CustomerOrder) em.createNamedQuery("CustomerOrder.findByCustomer")
                .setParameter("customer", customer)
                .getSingleResult();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
