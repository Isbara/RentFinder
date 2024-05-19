package GradProject.RentFinder.Controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import GradProject.RentFinder.Mapper.UserMapper;
import GradProject.RentFinder.Models.*;
import GradProject.RentFinder.Repository.UserRepository;
import GradProject.RentFinder.RequestModel.*;
import GradProject.RentFinder.SecurityConfig.JwtService;
import GradProject.RentFinder.Service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {

    @Mock
    private ReviewService reviewService;

    @Autowired
    MockMvc mockMvc;

    String token;
    Property property;
    Reservation reservation;
    Review review;
    Respond respond;
    User user;
    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;
    static Integer x = 0;

    @BeforeEach
    public void setup() throws Exception {
        x+=1;
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
        Optional<User> optionalUser = userRepository.findByEmail("john.doe@example.com");
        this.user = userMapper.ConvertOptional(optionalUser);

        PropertyRequest propertyRequest = new PropertyRequest('N', 12, "address", "description", 500, "offers");
        String requestPropertyBody = asJsonString(propertyRequest);
        ResultActions resultActionsProperty = this.mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/property/addProperty").contentType(MediaType.APPLICATION_JSON).header("Authorization", this.token).content(requestPropertyBody));
        MvcResult mvcResultProperty = resultActionsProperty.andDo(print()).andReturn();
        String contentAsStringProperty = mvcResultProperty.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        this.property = objectMapper.readValue(contentAsStringProperty, Property.class);

        ReservationRequest reservationRequest = new ReservationRequest(5,new Date(2024,12,20), new Date(2024,12,31));
        String requestReservationBody = asJsonString(reservationRequest);
        ResultActions resultActionsReservation = this.mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/reservation/makeReservation/" + x.toString()).contentType(MediaType.APPLICATION_JSON).header("Authorization", this.token).content(requestReservationBody));
        MvcResult mvcResultReservation = resultActionsReservation.andDo(print()).andReturn();
        String contentAsStringReservation = mvcResultReservation.getResponse().getContentAsString();
        JSONObject reservationJson = new JSONObject(contentAsStringReservation);
        reservationJson.remove("propertyID");
        reservationJson.remove("phoneNumber");
        contentAsStringReservation = reservationJson.toString();
        this.reservation = objectMapper.readValue(contentAsStringReservation, Reservation.class);

        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setDescription("Great place");
        reviewRequest.setDate(new Date());
        reviewRequest.setUserScore(5);
        reviewRequest.setAlgoResult(true);
        String requestReviewBody = asJsonString(reviewRequest);
        ResultActions resultActionsReview = this.mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/review/" + x.toString() + "/" + x.toString()).contentType(MediaType.APPLICATION_JSON).header("Authorization", this.token).content(requestReviewBody));
        MvcResult mvcResultReview = resultActionsReview.andDo(print()).andReturn();
        String contentAsStringReview = mvcResultReview.getResponse().getContentAsString();
        JSONObject reviewJson = new JSONObject(contentAsStringReview);
        reviewJson.remove("propertyID");
        reviewJson.remove("property");
        reviewJson.remove("reservation");
        reviewJson.remove("reservationID");
        reviewJson.remove("reviewer");
        reviewJson.remove("reviewerID");
        contentAsStringReview = reviewJson.toString();
        this.review = objectMapper.readValue(contentAsStringReview, Review.class);

    }

    @Test
    public void testGetPropertyReviews() throws Exception {
        // Mock service response
        List<Review> reviews = Collections.singletonList(review);
        when(reviewService.GetPropertyReviews(anyLong())).thenReturn(reviews);

        // Perform GET request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/review/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].commentID").value(1));
    }

    @Test
    public void testWriteReview() throws Exception {
        // Mock service response
        when(reviewService.WriteReview(anyString(), anyLong(), anyLong(), any(ReviewRequest.class))).thenReturn(review);

        // Perform POST request and verify response
        mockMvc.perform(MockMvcRequestBuilders.post("/review/3/3")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new ReviewRequest())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.commentID").value(3));
    }

    @Test
    public void testWriteRespond() throws Exception {
        // Mock service response
        when(reviewService.WriteRespond(anyString(), anyLong(), any(RespondRequest.class))).thenReturn(respond);

        // Perform POST request and verify response
        mockMvc.perform(MockMvcRequestBuilders.post("/review/response/2")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new RespondRequest())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.commentID").value(1));
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
