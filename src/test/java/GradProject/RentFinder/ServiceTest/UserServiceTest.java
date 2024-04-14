package GradProject.RentFinder.ServiceTest;

import GradProject.RentFinder.Exception.AllExceptions;
import GradProject.RentFinder.Mapper.UserMapper;
import GradProject.RentFinder.Models.User;
import GradProject.RentFinder.RequestModel.UserRequest;
import GradProject.RentFinder.Repository.UserRepository;
import GradProject.RentFinder.SecurityConfig.JwtService;
import GradProject.RentFinder.Service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserServiceTest {

    private UserService userService;
    private UserRepository userRepositoryMock;
    private UserMapper userMapperMock;
    private JwtService jwtServiceMock;
    private AuthenticationManager authenticationManagerMock;
    private PasswordEncoder passwordEncoderMock;

    @BeforeEach
    public void setup() {
        userRepositoryMock = Mockito.mock(UserRepository.class);
        userMapperMock = Mockito.mock(UserMapper.class);
        jwtServiceMock = Mockito.mock(JwtService.class);
        authenticationManagerMock = Mockito.mock(AuthenticationManager.class);
        passwordEncoderMock = Mockito.mock(PasswordEncoder.class);
        userService = new UserService(userRepositoryMock, userMapperMock, jwtServiceMock, authenticationManagerMock, passwordEncoderMock);
    }

    @Test
    void testCreateUser_Success() {
        // Arrange
        UserRequest userRequest = createUserRequest("John", "Doe", "john@example.com", "password", "1234567890", "1990-01-03");
        Mockito.when(userRepositoryMock.findAll()).thenReturn(new ArrayList<>()); // Mocking userRepository to return an empty list
        Mockito.when(passwordEncoderMock.encode(Mockito.anyString())).thenReturn("encodedPassword");
        Mockito.when(userMapperMock.ConvertOptional(Mockito.any())).thenReturn(new User()); // Mocking userMapper to return a mock User object

        // Act
        String result = userService.CreateUser(userRequest);

        // Assert
        Assertions.assertEquals("The new user is created successfully.", result);
    }

    private UserRequest createUserRequest(String name, String surname, String email, String password, String phoneNumber, String dateOfBirth) {
        Date dob;
        try {
            dob = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfBirth);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format for dateOfBirth");
        }
        return new UserRequest(name, surname, email, password, phoneNumber, dob);
    }
}
