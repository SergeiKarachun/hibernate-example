package by.sergey;

import by.sergey.dao.CompanyRepository;
import by.sergey.dao.PaymentRepository;
import by.sergey.dao.UserRepository;
import by.sergey.dto.UserCreateDto;
import by.sergey.entity.*;
import by.sergey.interceptor.TransactionInterceptor;
import by.sergey.mapper.CompanyReadMapper;
import by.sergey.mapper.UserCreatMapper;
import by.sergey.mapper.UserReadMapper;
import by.sergey.service.UserService;
import by.sergey.util.HibernateUtil;
import by.sergey.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    //private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class);
    @Transactional
    public static void main(String[] args) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {


        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {

            Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(), new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
           // session.beginTransaction();

            CompanyRepository companyRepository = new CompanyRepository(session);

            CompanyReadMapper companyReadMapper = new CompanyReadMapper();
            UserReadMapper userReadMapper = new UserReadMapper(companyReadMapper);
            UserCreatMapper userCreatMapper = new UserCreatMapper(companyRepository);

            UserRepository userRepository = new UserRepository(session);
            PaymentRepository paymentRepository = new PaymentRepository(session);

            //UserService userService = new UserService(userRepository, userReadMapper, userCreatMapper);

            TransactionInterceptor transactionInterceptor = new TransactionInterceptor(sessionFactory);

            UserService userService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(UserService.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, UserCreatMapper.class)
                    .newInstance(userRepository, userReadMapper, userCreatMapper);

            userService.findById(1L).ifPresent(System.out::println);

            UserCreateDto userCreateDto = new UserCreateDto(
                    PersonalInfo.builder()
                            .firstname("Liza1")
                            .lastname("Stepanova1")
                            //.birthDate(LocalDate.now())
                            .build(),
                    "Liza3@gamil.com",
                    null,
                    Role.USER,
                    1
            );
            userService.create(userCreateDto);


            //session.getTransaction().commit();
        }


    }
}


