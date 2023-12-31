package az.coders.lawfirmmanagement.dto;

import az.coders.lawfirmmanagement.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Long id;

    String firstName;

    String secondName;

    String username;

    String password;

    Role role;

    String base64Photo;




}
