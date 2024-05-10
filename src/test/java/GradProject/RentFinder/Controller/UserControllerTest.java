package GradProject.RentFinder.Controller;

import GradProject.RentFinder.Models.User;
import GradProject.RentFinder.RequestModel.UserRequest;
import GradProject.RentFinder.SecurityConfig.AuthResponse;
import GradProject.RentFinder.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testRegisterUser() throws Exception {
        // Test registration endpoint
        UserRequest userRequest = new UserRequest("John", "Doe", "john.doe@example.com", "password", "123456789", new Date());
        when(userService.CreateUser(any(UserRequest.class))).thenReturn("The new user is created successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("The new user is created successfully"));
    }

    @Test
    public void testLoginUser() throws Exception {
        // Create a user request with valid credentials
        UserRequest userRequest = new UserRequest("john.doe@example.com", "password");

        // Mock the login response from userService
        AuthResponse authResponse = new AuthResponse("JWT_TOKEN");
        when(userService.Login(any(UserRequest.class))).thenReturn(authResponse);

        // Perform the login request
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse();

        // Extract the JWT token from the response JSON
        String jsonResponse = response.getContentAsString();
        String token = JsonPath.read(jsonResponse, "$.token");

        // Assert that the token is not null or empty
        assertNotNull(token);

        // Assert that the token matches the one from the AuthResponse
        assertEquals("JWT_TOKEN", token);
    }

    @Test
    public void testGetUserDetails() throws Exception {
        // Test getUserDetails endpoint
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        UserRequest userRequest = new UserRequest("john.doe@example.com", "password");
        AuthResponse token = new AuthResponse("JWT_TOKEN");

        when(userService.Login(userRequest)).thenReturn(token);
        when(userService.UserDetails(token.getToken())).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getUserDetails")
                        .header("Authorization", "Bearer " + token.getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Create a user request with updated details
        UserRequest userRequest = new UserRequest("John", "Doe", "john.doe@example.com", "password", "123456789", new Date());

        // Mock the JWT token
        AuthResponse authResponse = new AuthResponse("JWT_TOKEN");

        // Mock the behavior of UserService.UpdateUser method
        when(userService.UpdateUser(eq(authResponse.getToken()), any(UserRequest.class))).thenReturn("User details were updated successfully");

        // Perform the PUT request to update user details
        mockMvc.perform(MockMvcRequestBuilders.put("/user/update")
                        .header("Authorization", "Bearer " + authResponse.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk()); // Check if the response status is OK
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Test delete user endpoint
        String token = "JWT_TOKEN";
        when(userService.DeleteUser(token)).thenReturn("The profile was deleted successfully");

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // Utility method to convert object to JSON string
    private String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
