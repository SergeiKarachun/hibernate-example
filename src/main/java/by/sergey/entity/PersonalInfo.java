package by.sergey.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PersonalInfo {

    @Serial
    private static final long serialVersionUID = 1L;

    private String firstname;
    private String lastname;

    //    @Convert(converter = BirthdayConverter.class)

    @NotNull
    private LocalDate birthDate;
}
