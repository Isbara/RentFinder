package GradProject.RentFinder.Controller;

import GradProject.RentFinder.Models.Property;
import GradProject.RentFinder.Models.Reservation;
import GradProject.RentFinder.RequestModel.PropertyRequest;
import GradProject.RentFinder.RequestModel.UserRequest;
import GradProject.RentFinder.Service.PropertyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PropertyControllerTest {

    @Mock
    private PropertyService propertyService;

    @Autowired
    MockMvc mockMvc;
    Property property;
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

        PropertyRequest propertyRequest = new PropertyRequest('N', 12, "address", "description", 500, "offers");
        String requestPropertyBody = asJsonString(propertyRequest);
        ResultActions resultActionsProperty = this.mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/property/addProperty").contentType(MediaType.APPLICATION_JSON).header("Authorization", this.token).content(requestPropertyBody));
        MvcResult mvcResultProperty = resultActionsProperty.andDo(print()).andReturn();
        String contentAsStringProperty = mvcResultProperty.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        this.property = objectMapper.readValue(contentAsStringProperty, Property.class);
    }
    @Test
    public void testGetAllProperties() throws Exception {
        // Mock service response
        List<Property> properties = Collections.singletonList(new Property());
        when(propertyService.getAllProperties()).thenReturn(properties);

        // Perform GET request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/property/getProperties"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    public void testAddProperty() throws Exception {
        // Mock service response
        PropertyRequest request = new PropertyRequest();
        Property property = new Property();
        when(propertyService.addProperty(any(), any(PropertyRequest.class))).thenReturn(property);

        // Perform POST request and verify response
        mockMvc.perform(MockMvcRequestBuilders.post("/property/addProperty")
                        .header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.propertyID").value(8));
    }

    @Test
    public void testDeleteProperty() throws Exception {
        when(propertyService.deleteProperty(anyLong())).thenReturn("Property was deleted successfully");

        // Perform DELETE request and verify response
        mockMvc.perform(MockMvcRequestBuilders.delete("/property/deleteProperty/1").header("Authorization", this.token))
                .andExpect(status().isOk())
                .andExpect(content().string("Property was deleted successfully"));
    }

    @Test
    public void testUpdateProperty() throws Exception {
        when(propertyService.updateProperty(anyLong(), any(PropertyRequest.class))).thenReturn("Property is updated");

        // Perform PUT request and verify response
        mockMvc.perform(MockMvcRequestBuilders.put("/property/updateProperty/1").header("Authorization", this.token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new PropertyRequest())))
                .andExpect(status().isOk())
                .andExpect(content().string("Property is updated"));
    }

    @Test
    public void testGetPropertyDetails() throws Exception {
        // Mock service response
        Property property = new Property();
        when(propertyService.getPropertyDetails(anyLong())).thenReturn(property);

        // Perform GET request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/property/getPropertyDetails/1").header("Authorization", this.token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.propertyID").value(1));
    }

    @Test
    public void testGetUserProperties() throws Exception {
        // Mock service response
        List<Property> properties = Collections.singletonList(new Property());
        when(propertyService.getUserProperties(any())).thenReturn(properties);

        // Perform GET request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/property/getUserProperties").header("Authorization", this.token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    public void testGetPropertyReservations() throws Exception {
        // Mock service response
        List<Reservation> reservations = Collections.singletonList(new Reservation());
        when(propertyService.GetPropertyReservations(any(), anyLong())).thenReturn(reservations);

        // Perform GET request and verify response
        mockMvc.perform(MockMvcRequestBuilders.get("/property/getReservations/1").header("Authorization", this.token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").doesNotExist());
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
