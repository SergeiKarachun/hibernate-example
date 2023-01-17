package by.sergey.service;

import by.sergey.dao.UserRepository;
import by.sergey.dto.UserCreateDto;
import by.sergey.dto.UserReadDto;
import by.sergey.entity.User;
import by.sergey.mapper.Mapper;
import by.sergey.mapper.UserCreatMapper;
import by.sergey.mapper.UserReadMapper;
import by.sergey.validation.UpdateCheck;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import javax.transaction.Transactional;
import javax.validation.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreatMapper userCreatMapper;

    @Transactional
    public Long create(UserCreateDto userDto){
        //validate
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserCreateDto>> validationResult = validator.validate(userDto, UpdateCheck.class);
        if (!validationResult.isEmpty()){
            throw new ConstraintViolationException(validationResult);
        }
        //to do
        //map
        User userEntity = userCreatMapper.mapFrom(userDto);
        return userRepository.save(userEntity).getId();
    }

    @Transactional
    public <T> Optional findById(Long id, Mapper<User, T> mapper){
        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJpaHintName(), userRepository.getEntityManager().getEntityGraph("WithCompany")
        );
        return userRepository.findById(id, properties)

                .map(mapper::mapFrom);
    }

    @Transactional
    public Optional<UserReadDto> findById(Long id){
        return findById(id, userReadMapper);
    }

    @Transactional
    public boolean delete(Long id){
        Optional<User> maybyUser = userRepository.findById(id);
        maybyUser.ifPresent(user -> userRepository.delete(user.getId()));
        return maybyUser.isPresent();
    }
}
