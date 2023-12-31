package az.coders.lawfirmmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@Entity
@Table(name = "company")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @NotBlank
    @Size(min = 2, max = 20)
    String name;


    @Email
    String email;

    @NotNull
    String website;

    @Pattern(regexp = "^[0-9]+$", message = "Field should contain only digits")
    String phoneNumber;

    @NotBlank
    String address;

    @NotBlank
    String city;

    @NotBlank
    String country;

    @NotBlank
    String description;

}
