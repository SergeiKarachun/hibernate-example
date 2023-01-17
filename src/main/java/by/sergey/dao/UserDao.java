package by.sergey.dao;


/*com.querydsl.apt.jpa.JPAAnnotationProcessor*/

import by.sergey.dto.CompanyDto;
import by.sergey.dto.PaymentFilter;
import by.sergey.entity.*;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.NoArgsConstructor;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static by.sergey.entity.QCompany.company;
import static by.sergey.entity.QPayment.payment;
import static by.sergey.entity.QUser.user;

@NoArgsConstructor
public class UserDao {
    private static final UserDao INSTANCE = new UserDao();

    public static UserDao getInstance() {
        return INSTANCE;
    }

    public List<User> findAll(Session session) {
        /*return session.createQuery("select u from User u", User.class).list();*/
        return new JPAQuery<User>(session)
                .select(user)
                .from(user).fetch();

        /*CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        criteria.select(user);

        return session.createQuery(criteria).list();*/
    }

    public List<User> findAllByFirstName(Session session, String firstname) {
        /*return session.createQuery("select u from User u where u.personalInfo.firstname = :firstname", User.class)
                .setParameter("firstname", firstname).list();*/


        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .where(user.personalInfo.firstname.eq(firstname))
                .fetch();
       /* CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);

        Root<User> user = criteria.from(User.class);
        //CriteriaQuery<User> select = criteria.select(user).where(cb.equal(user.get("personalInfo").get("firstname"), firstname));

        criteria.select(user).where(cb.equal(
                user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstname));

        return session.createQuery(criteria).list();*/
    }

    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
        /*return session.createQuery("select u from User u order by u.personalInfo.birthDate", User.class)
                .setMaxResults(limit)
                //.setFirstResult(offset)
                .list();*/

        return new JPAQuery<User>(session)
                .select(user)
                .from(user)
                .orderBy(user.personalInfo.birthDate.asc()).limit(limit)
                .fetch();
        /*CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);

        Root<User> userRoot = criteria.from(User.class);

        criteria.select(userRoot).orderBy(
                cb.asc(userRoot.get(User_.personalInfo).get(PersonalInfo_.birthDate)));


        return session.createQuery(criteria)
                .setMaxResults(limit)
                .list();*/
    }

    public List<User> findAllByCompanyName(Session session, String google) {
        /*return session.createQuery("select u from Company c " +
                "inner join c.users u " +
                "where c.name = :companyName",User.class)
                .setParameter("companyName", google)
                .list();*/
        return new JPAQuery<User>(session)
                .select(user)
                .from(company)
                .innerJoin(company.users, user)
                .where(company.name.eq(google))
                .fetch();
        /*CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);

        Root<Company> company = criteria.from(Company.class);
        MapJoin<Company, String, User> users = company.join(Company_.users);

        criteria.select(users).where(cb.equal(company.get(Company_.name), google));

        return session.createQuery(criteria).list();*/
    }

    public List<Payment> findAllPaymentsByCompanyName(Session session, String apple) {
        /*return session.createQuery("select p from Payment p " +
                        "join p.receiver u " +
                        "join u.company c " +
                        "where c.name = :companyName " +
                        "order by u.personalInfo.firstname, p.amount", Payment.class)
                .setParameter("companyName", apple)
                .list();*/


        return new JPAQuery<Payment>(session)
                .select(payment)
                .from(payment)
                .join(payment.receiver, user)
                .join(user.company, company)
                .where(company.name.eq(apple))
                .orderBy(user.personalInfo.firstname.asc(), payment.amount.asc())
                .fetch();

        /*CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Payment> criteria = cb.createQuery(Payment.class);
        Root<Payment> payment = criteria.from(Payment.class);
        Join<Payment, User> user = payment.join(Payment_.receiver);
        Join<User, Company> company = user.join(User_.company);

        criteria.select(payment).where(cb.equal(company.get(Company_.name), apple))
                .orderBy(
                        cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)),
                        cb.asc(payment.get(Payment_.AMOUNT)));

        return session.createQuery(criteria).list();*/
    }

    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, PaymentFilter paymentFilter) {
        /*return session.createQuery("select avg(p.amount) from Payment p " +
                        "join p.receiver u " +
                        "where u.personalInfo.firstname = :firstName " +
                        "and u.personalInfo.lastname = :lastName", Double.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName).uniqueResult();*/
        /*List<com.querydsl.core.types.Predicate> predicates = new ArrayList<>();
        if (paymentFilter.getFirstName() != null){
            predicates.add(user.personalInfo.firstname.eq(paymentFilter.getFirstName()));
        }
        if (paymentFilter.getFirstName() != null){
            predicates.add(user.personalInfo.lastname.eq(paymentFilter.getLastName()));
        }*/

        Predicate predicate = QPredicate.builder()
                .add(paymentFilter.getFirstName(), user.personalInfo.firstname::eq)
                .add(paymentFilter.getLastName(), user.personalInfo.lastname::eq)
                .buildAnd();


        return new JPAQuery<Double>(session)
                .select(payment.amount.avg())
                .from(payment)
                .join(payment.receiver, user)
                //.where(predicates.toArray(com.querydsl.core.types.Predicate[]::new))
                .where(predicate)
                .fetchOne();
        /*CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Double> criteria = cb.createQuery(Double.class);

        Root<Payment> payment = criteria.from(Payment.class);
        Join<Payment, User> user = payment.join(Payment_.receiver);

        List<Predicate> predicates = new ArrayList<>();

        if (firstName != null) {
            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName));
        }
        if (lastName != null) {
            predicates.add(cb.equal(user.get(User_.personalInfo).get(PersonalInfo_.lastname), lastName));
        }

        criteria.select(cb.avg(payment.get(Payment_.amount))).where(
                predicates.toArray(Predicate[]::new)
        );


        return session.createQuery(criteria).uniqueResult();*/
    }


    //Use List<CompanyDto> for Criteria
    public List<Tuple> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
        /*return session.createQuery("select c.name, avg(p.amount) from Company c " +
                "join c.users u " +
                "join u.payments p " +
                "group by c.name " +
                "order by c.name", Object[].class).list();*/
        return new JPAQuery<Tuple>(session)
                .select(company.name, payment.amount.avg())
                .from(company)
                .join(company.users, user)
                .join(user.payments, payment)
                .groupBy(company.name)
                .orderBy(company.name.asc())
                .fetch();
        /*CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<CompanyDto> criteria = cb.createQuery(CompanyDto.class);

        Root<Company> company = criteria.from(Company.class);
        MapJoin<Company, String, User> user = company.join(Company_.users, JoinType.INNER);
        ListJoin<User, Payment> payment = user.join(User_.payments);

        criteria.select(
                        cb.construct(CompanyDto.class,
                                company.get(Company_.name),
                                cb.avg(payment.get(Payment_.amount)))
                )
                .groupBy(company.get(Company_.name))
                .orderBy(cb.asc(company.get(Company_.name)));


        return session.createQuery(criteria).list();*/
    }

    public List<Tuple> isItPossible(Session session) {
        /*return session.createQuery("select u, avg(p.amount) from User u " +
                "join u.payments p  " +
                "group by u " +
                "having avg(p.amount) > (select avg(p.amount) from Payment p) " +
                "order by u.personalInfo.firstname", Object[].class).list();*/

        return new JPAQuery<Tuple>(session)
                .select(user, payment.amount.avg())
                .from(user)
                .join(user.payments, payment)
                .groupBy(user.id)
                .having(payment.amount.avg().gt(
                        new JPAQuery<Double>(session)
                                .select(payment.amount.avg())
                                .from(payment)
                ))
                .orderBy(user.personalInfo.firstname.asc())
                .fetch();


        /*CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);

        Root<User> user = criteria.from(User.class);
        ListJoin<User, Payment> payment = user.join(User_.payments);

        Subquery<Double> subquery = criteria.subquery(Double.class);
        Root<Payment> paymentSubquery = subquery.from(Payment.class);


        criteria.select(
                        cb.tuple(
                                user,
                                cb.avg(payment.get(Payment_.amount))
                        )
                )
                .groupBy(user.get(User_.id))
                .having(
                        cb.gt(cb.avg(payment.get(Payment_.amount)),
                                subquery.select(cb.avg(paymentSubquery.get(Payment_.amount)))
                        )
                )
                .orderBy(cb.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)));


        return session.createQuery(criteria).list();*/
    }
}
