package by.sergey.mapper;

import by.sergey.dao.CompanyRepository;
import by.sergey.dto.UserCreateDto;
import by.sergey.entity.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserCreatMapper implements Mapper<UserCreateDto, User> {

    private final CompanyRepository companyRepository;

    @Override
    public User mapFrom(UserCreateDto object) {
        return User.builder()
                .personalInfo(object.personalInfo())
                .username(object.username())
                .info(object.info())
                .role(object.role())
                .company(companyRepository.findById(object.companyId()).orElseThrow(IllegalArgumentException::new))
                .build();
    }
}
