package GradProject.RentFinder.RequestModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequest {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy")
    private Date dateOfBirth;

    public UserRequest(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }
}
