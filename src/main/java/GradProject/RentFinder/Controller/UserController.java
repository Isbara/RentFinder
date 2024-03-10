package GradProject.RentFinder.Controller;

import GradProject.RentFinder.Models.User;
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
        return ResponseEntity.ok().body(userService.CreateUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> Login(@RequestBody UserRequest user) {
        return ResponseEntity.ok().body(userService.Login(user));
    }
    @GetMapping("/getUserDetails")
    public ResponseEntity<User> getUserDetails(@RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok().body(userService.UserDetails(token));
    }
    @PutMapping("/update")
    public ResponseEntity<String> UpdateUser(@RequestHeader(value = "Authorization") String token, @RequestBody UserRequest request){
        return ResponseEntity.ok().body(userService.UpdateUser(token, request));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> DeleteUser(@RequestHeader(value = "Authorization") String token){
        return ResponseEntity.ok().body(userService.DeleteUser(token));
    }
}
