package by.sergey.dao;

import by.sergey.entity.Company;
import by.sergey.entity.User;

import javax.persistence.EntityManager;


public class UserRepository extends RepositoryBase<Long, User> {

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);


    }




}
