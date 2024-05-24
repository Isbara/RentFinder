package GradProject.RentFinder.Controller;

import GradProject.RentFinder.Models.Property;
import GradProject.RentFinder.Models.Reservation;
import GradProject.RentFinder.RequestModel.PropertyRequest;
import GradProject.RentFinder.RequestModel.ReservationRequest;
import GradProject.RentFinder.RequestModel.UserRequest;
import GradProject.RentFinder.Service.PropertyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PropertyControllerTest {

    @Mock
    PropertyService propertyService;

    @Autowired
    MockMvc mockMvc;
    Property property;
    String token;
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
    @Order(1)
    public void testAddProperty() throws Exception {
        PropertyRequest request = new PropertyRequest();
        Property property = new Property();
        when(propertyService.addProperty(any(), any(PropertyRequest.class))).thenReturn(property);

        mockMvc.perform(MockMvcRequestBuilders.post("/property/addProperty")
                        .header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.propertyID").value(2));
    }
    @Test
    @Order(5)
    public void testGetAllProperties() throws Exception {
        List<Property> properties = Collections.singletonList(this.property);
        when(propertyService.getAllProperties()).thenReturn(properties);

        mockMvc.perform(MockMvcRequestBuilders.get("/property/getProperties"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @Order(7)
    public void testDeleteProperty() throws Exception {
        when(propertyService.deleteProperty(anyLong())).thenReturn("Property was deleted successfully");

        mockMvc.perform(MockMvcRequestBuilders.delete("/property/deleteProperty/1").header("Authorization", this.token))
                .andExpect(status().isOk())
                .andExpect(content().string("Property was deleted successfully"));
    }

    @Test
    @Order(3)
    public void testGetPropertyDetails() throws Exception {
        Property property = new Property();
        when(propertyService.getPropertyDetails(anyLong())).thenReturn(property);

        mockMvc.perform(MockMvcRequestBuilders.get("/property/getPropertyDetails/1").header("Authorization", this.token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.propertyID").value(1));
    }

    @Test
    @Order(6)
    public void testGetUserProperties() throws Exception {
        List<Property> properties = Collections.singletonList(new Property());
        when(propertyService.getUserProperties(any())).thenReturn(properties);

        mockMvc.perform(MockMvcRequestBuilders.get("/property/getUserProperties").header("Authorization", this.token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @Order(4)
    public void testGetPropertyReservations() throws Exception {
        List<Reservation> reservations = Collections.singletonList(this.reservation);
        when(propertyService.GetPropertyReservations(any(), anyLong())).thenReturn(reservations);

        mockMvc.perform(MockMvcRequestBuilders.get("/property/getReservations/1").header("Authorization", this.token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].reservationID").value(this.reservation.getReservationID()));
    }

    @Test
    @Order(2)
    public void testUpdateProperty() throws Exception {
        when(propertyService.updateProperty(anyLong(), any(PropertyRequest.class))).thenReturn("Property is updated");

        mockMvc.perform(MockMvcRequestBuilders.put("/property/updateProperty/1").header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new PropertyRequest())))
                .andExpect(status().isOk())
                .andExpect(content().string("Property is updated"));
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
