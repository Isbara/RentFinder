package GradProject.RentFinder.Service;

import GradProject.RentFinder.Exception.AllExceptions;
import GradProject.RentFinder.Exception.Exceptions;
import GradProject.RentFinder.Mapper.UserMapper;
import GradProject.RentFinder.Models.Role;
import GradProject.RentFinder.Models.User;
import GradProject.RentFinder.Repository.UserRepository;
import GradProject.RentFinder.RequestModel.UserRequest;
import GradProject.RentFinder.SecurityConfig.AuthResponse;
import GradProject.RentFinder.SecurityConfig.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public String CreateCompany(UserRequest user){
        List<User> allUsers =userRepository.findAll();
        boolean emailExists = false;
        for(User cUser:allUsers){
            if (cUser.getUsername().equals(user.getEmail())) {
                emailExists = true;
                break;
            }
        }
        if(!emailExists){
            var newUser = User.builder()
                    .name(user.getName())
                    .surname(user.getSurname())
                    .email(user.getEmail())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .phoneNumber(user.getPhoneNumber())
                    .dateOfBirth(user.getDateOfBirth())
                    .karmaPoint(100)
                    .role(Role.USER)
                    .build();
            userRepository.save(newUser);
            return "The new user is created successfully.";
        }
        else{
             throw new Exceptions(AllExceptions.EMAIL_EXISTS);
        }
    }

    public AuthResponse Login(UserRequest credentials) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(),credentials.getPassword()));
        Optional<User> optionalUser =userRepository.findByEmail(credentials.getEmail());
        if(optionalUser.isPresent()){
            User user = userMapper.ConvertOptional(optionalUser);
            var jwtToken = jwtService.generateToken(user);
            return new AuthResponse(jwtToken);
        }
        else{
            return new AuthResponse("Invalid email or password");
        }
    }

    public User UserDetails(Long id)
    {
        Optional<User> optionalUser = userRepository.findById(id);
        User new_user;
        if(optionalUser.isPresent()){
            new_user = userMapper.ConvertOptional(optionalUser);
        }
        else
        {
            throw new Exceptions(AllExceptions.USER_ID_NOT_FOUND);
        }
        return new_user;


    }
}
