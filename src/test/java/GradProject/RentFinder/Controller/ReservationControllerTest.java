package GradProject.RentFinder.Controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import GradProject.RentFinder.Models.Property;
import GradProject.RentFinder.RequestModel.PropertyRequest;
import GradProject.RentFinder.RequestModel.UserRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
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

import GradProject.RentFinder.Models.Reservation;
import GradProject.RentFinder.RequestModel.ReservationRequest;
import GradProject.RentFinder.Service.ReservationService;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @Autowired
    MockMvc mockMvc;

    String token;

    Property property;
    Reservation reservation;

    @BeforeAll
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

        PropertyRequest propertyRequest = new PropertyRequest('N', 12, "address", "description", 500, "offers");
        String requestPropertyBody = asJsonString(propertyRequest);
        ResultActions resultActionsProperty = this.mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/property/addProperty").contentType(MediaType.APPLICATION_JSON).header("Authorization", this.token).content(requestPropertyBody));
        MvcResult mvcResultProperty = resultActionsProperty.andDo(print()).andReturn();
        String contentAsStringProperty = mvcResultProperty.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        this.property = objectMapper.readValue(contentAsStringProperty, Property.class);

        ReservationRequest reservationRequest = new ReservationRequest(5,new Date(2024,12,20), new Date(2024,12,31));
        String requestReservationBody = asJsonString(reservationRequest);
        ResultActions resultActionsReservation = this.mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/reservation/makeReservation/1").contentType(MediaType.APPLICATION_JSON).header("Authorization", this.token).content(requestReservationBody));
        MvcResult mvcResultReservation = resultActionsReservation.andDo(print()).andReturn();
        String contentAsStringReservation = mvcResultReservation.getResponse().getContentAsString();
        JSONObject reservationJson = new JSONObject(contentAsStringReservation);
        reservationJson.remove("propertyID");
        reservationJson.remove("phoneNumber");
        reservationJson.remove("reserverKarma");
        contentAsStringReservation = reservationJson.toString();
        this.reservation = objectMapper.readValue(contentAsStringReservation, Reservation.class);
    }

    @Test
    public void testMakeReservation() throws Exception {
        // Mock service response
        when(reservationService.MakeReservation(anyString(), anyLong(), any(ReservationRequest.class))).thenReturn(reservation);

        // Perform POST request and verify response
        mockMvc.perform(MockMvcRequestBuilders.post("/reservation/makeReservation/1")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new ReservationRequest())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reservationID").value(2));
    }

    @Test
    public void testGetReservations() throws Exception {
        // Mock service response
        List<Reservation> reservations = Collections.singletonList(reservation);
        when(reservationService.GetReservations(anyString(), anyLong())).thenReturn(reservations);

        // Perform GET request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/reservation/getReservation/1").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    public void testMakeStatusDecisionForReservation() throws Exception {
        // Mock service response
        doNothing().when(reservationService).MakeDecisionForStatus(eq(this.reservation.getReservationID()), eq(true));

        // Perform PUT request and verify response
        mockMvc.perform(MockMvcRequestBuilders.put("/reservation/status/1").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", token)
                        .content(asJsonString(true)))
                .andExpect(status().isOk());
    }

    @Test
    public void testMakeApprovalDecisionForReservation() throws Exception {
        // Mock service response
        doNothing().when(reservationService).MakeDecisionForApproval(eq(this.reservation.getReservationID()), eq(true), eq(this.token));

        // Perform PUT request and verify response
        mockMvc.perform(MockMvcRequestBuilders.put("/reservation/approval/1").header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("true")) // Send appropriate boolean value as content
                .andExpect(status().isOk());
    }


    @Test
    public void testGetAllPropertyReservations() throws Exception {
        // Mock service response
        List<Reservation> reservations = Collections.singletonList(reservation);
        when(reservationService.GetAllPropertyReservations(anyString())).thenReturn(reservations);

        // Perform GET request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/reservation/getAllPropertyReservations").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    public void testGetAllUserReservations() throws Exception {
        // Mock service response
        List<Reservation> reservations = Collections.singletonList(reservation);
        when(reservationService.GetAllUserReservations(anyString())).thenReturn(reservations);

        // Perform GET request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/reservation/getAllUserReservations").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
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
