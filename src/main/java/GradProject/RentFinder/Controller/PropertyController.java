package GradProject.RentFinder.Controller;

import GradProject.RentFinder.Models.Property;
import GradProject.RentFinder.Service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<?> addProperty(@Valid @RequestBody Property property, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        return ResponseEntity.ok().body(propertyService.addProperty(property));
    }
    @DeleteMapping("/deleteProperty/{propertyId}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long propertyId) {
        propertyService.deleteProperty(propertyId);
        return ResponseEntity.ok().body("Property deleted successfully");
    }

    @PutMapping("/updateProperty/{propertyId}")
    public ResponseEntity<?> updateProperty(@PathVariable Long propertyId, @Valid @RequestBody Property property, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }


        return ResponseEntity.ok().body(propertyService.updateProperty(propertyId, property));
    }


}