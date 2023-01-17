package by.sergey.dto;

import by.sergey.entity.PersonalInfo;
import by.sergey.entity.Role;

public record UserReadDto(Long id,
                          PersonalInfo personalInfo,
                          String username,
                          String info,
                          Role role,
                          CompanyReadDto company) {
}
