package GradProject.RentFinder.Controller;

import GradProject.RentFinder.Mapper.PropertyMapper;
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
        List<Property> properties = propertyService.getAllProperties();
        List<Property> decompressed_properties= PropertyMapper.Decompresser(properties);
        return ResponseEntity.ok().body(decompressed_properties);
    }
    @PostMapping("/addProperty")
    public ResponseEntity<?> addProperty(@RequestHeader(value = "Authorization") String token, @RequestBody PropertyRequest request) {


        return ResponseEntity.ok().body(propertyService.addProperty(token, request));
    }
    @DeleteMapping("/deleteProperty/{propertyId}")
    public ResponseEntity<?> deleteProperty(@RequestHeader(value = "Authorization") String token,@PathVariable Long propertyId) {
        propertyService.deleteProperty(token,propertyId);
        return ResponseEntity.ok().body("Property deleted successfully");
    }

    @PutMapping("/updateProperty/{propertyId}")
    public ResponseEntity<?> updateProperty(@PathVariable Long propertyId, @Valid @RequestBody PropertyRequest propertyRequest) {

        return ResponseEntity.ok().body(propertyService.updateProperty(propertyId, propertyRequest));
    }

    @GetMapping("/getPropertyDetails/{propertyId}")
    public ResponseEntity<Property> GetPropertyDetails(@PathVariable Long propertyId){

        Property properties = propertyService.getPropertyDetails(propertyId);
        List<Property> decompressed_properties= PropertyMapper.Decompresser((List<Property>) properties);
        return ResponseEntity.ok().body((Property) decompressed_properties);
        
    }
    @GetMapping("/getUserProperties")
    public ResponseEntity<List<Property>> GetUserProperties(@RequestHeader(value = "Authorization") String token){
        List<Property> properties = propertyService.getUserProperties(token);
        List<Property> decompressed_properties= PropertyMapper.Decompresser(properties);
        return ResponseEntity.ok().body(decompressed_properties);
    }

}