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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public String CreateUser(UserRequest user){
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
        try{authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getEmail(),credentials.getPassword()));} //Throws exception if password is incorrect so added exception handling
        catch (AuthenticationException e)
        {
            throw new Exceptions(AllExceptions.WRONG_CREDENTIALS);
        }

        Optional<User> optionalUser =userRepository.findByEmail(credentials.getEmail());
        if(optionalUser.isPresent()){
            User user = userMapper.ConvertOptional(optionalUser);
            var jwtToken = jwtService.generateToken(user);
            return new AuthResponse(jwtToken);
        }
        else{
            throw new Exceptions(AllExceptions.WRONG_CREDENTIALS);
        }
    }

    public User UserDetails(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            String jwt = token.substring(7);
            String username = jwtService.extractUsername(jwt);
            Optional<User> optionalUser = userRepository.findByEmail(username);
            User user;
            if (optionalUser.isPresent())
                user = userMapper.ConvertOptional(optionalUser);
            else
                throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            return user;
        }
        else
            throw new Exceptions(AllExceptions.TOKEN_EXPIRED);
    }

    public String UpdateUser(String token, UserRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            String jwt = token.substring(7);
            String username = jwtService.extractUsername(jwt);
            Optional<User> optionalUser = userRepository.findByEmail(username);
            User user;
            if(optionalUser.isPresent())
                user = userMapper.ConvertOptional(optionalUser);
            else
                throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            if(request.getName()!=null)
                user.setName(request.getName());
            if(request.getSurname()!=null)
                user.setSurname(request.getSurname());
            if(request.getEmail()!=null)
                user.setEmail(request.getEmail());
            if(request.getPassword()!=null)
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            if(request.getPhoneNumber()!=null)
                user.setPhoneNumber(request.getPhoneNumber());
            if(request.getDateOfBirth()!=null)
                user.setDateOfBirth(request.getDateOfBirth());
            userRepository.save(user);
            return "User details were updated successfully";
        }
        else
            throw new Exceptions(AllExceptions.TOKEN_EXPIRED);
    }

    public String DeleteUser(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            String jwt = token.substring(7);
            String username = jwtService.extractUsername(jwt);
            Optional<User> optionalUser = userRepository.findByEmail(username);
            User user;
            if (optionalUser.isPresent())
                user = userMapper.ConvertOptional(optionalUser);
            else
                throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            userRepository.deleteById(user.getUserID());
            return "The profile was deleted successfully";
        } else
            throw new Exceptions(AllExceptions.TOKEN_EXPIRED);
    }
}
