package GradProject.RentFinder.Controller;

import GradProject.RentFinder.Models.Property;
import GradProject.RentFinder.RequestModel.PropertyRequest;
import GradProject.RentFinder.Service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/property")
public class PropertyController{
    private final PropertyService propertyService;


    @GetMapping("/getProperties")
    public ResponseEntity<List<Property>> getAllProperties() {
        return ResponseEntity.ok().body(propertyService.getAllProperties());
    }
    @PostMapping("/addProperty")
    public ResponseEntity<Property> addProperty(@RequestHeader(value = "Authorization") String token, @RequestBody PropertyRequest request) {
        return ResponseEntity.ok().body(propertyService.addProperty(token, request));
    }
    @DeleteMapping("/deleteProperty/{propertyId}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok().body(propertyService.deleteProperty(propertyId));
    }

    @PutMapping("/updateProperty/{propertyId}")
    public ResponseEntity<String> updateProperty(@PathVariable Long propertyId, @Valid @RequestBody PropertyRequest propertyRequest) {
        return ResponseEntity.ok().body(propertyService.updateProperty(propertyId, propertyRequest));
    }

    @GetMapping("/getPropertyDetails/{propertyId}")
    public ResponseEntity<Property> GetPropertyDetails(@PathVariable Long propertyId){
        return ResponseEntity.ok().body(propertyService.getPropertyDetails(propertyId));
    }
    @GetMapping("/getUserProperties")
    public ResponseEntity<List<Property>> GetUserProperties(@RequestHeader(value = "Authorization") String token){
        return ResponseEntity.ok().body(propertyService.getUserProperties(token));
    }

}