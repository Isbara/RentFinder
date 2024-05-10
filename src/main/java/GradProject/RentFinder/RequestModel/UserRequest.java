package GradProject.RentFinder.RequestModel;

import lombok.*;

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
    private Date dateOfBirth;

    public UserRequest(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }
}
