package GradProject.RentFinder.Controller;

import GradProject.RentFinder.RequestModel.UserRequest;
import GradProject.RentFinder.SecurityConfig.AuthResponse;
import GradProject.RentFinder.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> CreateUser(@RequestBody UserRequest user) {
        return ResponseEntity.ok().body(userService.CreateCompany(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> Login(@RequestBody UserRequest user) {
        return ResponseEntity.ok().body(userService.Login(user));
    }
}
