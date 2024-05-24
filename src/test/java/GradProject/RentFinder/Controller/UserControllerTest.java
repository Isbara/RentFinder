package GradProject.RentFinder.Controller;

import GradProject.RentFinder.Models.User;
import GradProject.RentFinder.RequestModel.UserRequest;
import GradProject.RentFinder.SecurityConfig.AuthResponse;
import GradProject.RentFinder.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Mock
    private UserService userService;
    @Autowired
    MockMvc mockMvc;
    String token;

    @BeforeEach
    public void setup() throws Exception {
        UserRequest userRequest = new UserRequest("John", "Doe", "john.doe@example.com", "123", "1234567890", new Date());
        String requestBody = asJsonString(userRequest);
        this.mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/user/register").contentType(MediaType.APPLICATION_JSON).content(requestBody));
        UserRequest credentials = new UserRequest("john.doe@example.com", "123");
        String requestBodyCredentials = asJsonString(credentials);
        ResultActions resultActions = this.mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/user/login").contentType(MediaType.APPLICATION_JSON).content(requestBodyCredentials));
        MvcResult mvcResult = resultActions.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(contentAsString);
        this.token = "Bearer " + jsonObject.getString("token");
        System.out.println("The returned JWT TOKEN IS: " + this.token);
    }

    @Test
    public void testRegisterUser() throws Exception {
        UserRequest userRequest = new UserRequest("Diao", "Chan", "Diao.Chan@example.com", "123", "1234567890", new Date());
        when(userService.CreateUser(any(UserRequest.class))).thenReturn("The new user is created successfully");

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("The new user is created successfully."));
    }

    @Test
    public void testLoginUser() throws Exception {
        UserRequest userRequest = new UserRequest("john.doe@example.com", "123");
        AuthResponse authResponse = new AuthResponse(this.token.substring(7));
        when(userService.Login(any(UserRequest.class))).thenReturn(authResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value(authResponse.getToken()));
    }

    @Test
    public void testGetUserDetails() throws Exception {
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setPassword("123");
        when(userService.UserDetails(token)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/getUserDetails")
                        .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    public void testUpdateUser() throws Exception {
        UserRequest userRequest = new UserRequest("John", "Doe", "john.doe@example.com", "123", "1234567890", new Date());
        when(userService.UpdateUser(eq(token), any(UserRequest.class))).thenReturn("User details were updated successfully");

        mockMvc.perform(MockMvcRequestBuilders.put("/user/update")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User details were updated successfully"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(userService.DeleteUser(token)).thenReturn("The profile was deleted successfully");

        mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete")
                        .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("The profile was deleted successfully"));
    }
    private String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}