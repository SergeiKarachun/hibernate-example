package by.sergey.dao;

import by.sergey.entity.Payment;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;


public class PaymentRepository extends RepositoryBase<Long, Payment> {

    public PaymentRepository(EntityManager entityManager){
        super(Payment.class, entityManager);
    }


}
