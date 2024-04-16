package GradProject.RentFinder.ServiceTest;

import GradProject.RentFinder.Exception.AllExceptions;
import GradProject.RentFinder.Helper.MockObjects;
import GradProject.RentFinder.Mapper.UserMapper;
import GradProject.RentFinder.Models.User;
import GradProject.RentFinder.RequestModel.UserRequest;
import GradProject.RentFinder.Repository.UserRepository;
import GradProject.RentFinder.SecurityConfig.JwtService;
import GradProject.RentFinder.Service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;

import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserServiceTest {



    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;


    private MockObjects mockObjects=new MockObjects();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this); //for initalizing mocks
    }

    @Test
    public void testCreateUser_Success() {

        UserRequest userRequest = mockObjects.userRequest_1();

        given(userRepository.findAll()).willReturn(List.of(mockObjects.user_1()));
        given(passwordEncoder.encode(userRequest.getPassword())).willReturn("encodedPassword");
        String result = userService.CreateUser(userRequest);
        assertNotNull(result);

    }
}
