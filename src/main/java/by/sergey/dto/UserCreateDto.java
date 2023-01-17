package by.sergey.dto;

import by.sergey.entity.PersonalInfo;
import by.sergey.entity.Role;
import by.sergey.validation.UpdateCheck;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public record UserCreateDto(
                            @Valid
                            PersonalInfo personalInfo,
                            @NotNull
                            String username,
                            String info,
                            @NotNull(groups = UpdateCheck.class)
                            Role role,
                            Integer companyId) {
}
