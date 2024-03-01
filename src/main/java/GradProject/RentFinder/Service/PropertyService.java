package GradProject.RentFinder.Service;

import GradProject.RentFinder.Models.Property;
import GradProject.RentFinder.Repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public List<Property> getAllProperties(){
        return propertyRepository.findAll();
    }

    public Property addProperty(Property property){
        // Inputlarla ilgili daha çok kısıtlama buraya eklenebilir
        return propertyRepository.save(property);}

    public void deleteProperty(Long propertyId)
    {
        propertyRepository.deleteById(propertyId);

    }

    public String updateProperty(Long propertyId, Property updatedProperty){

        Optional<Property> existingPropertyOptional = propertyRepository.findById(propertyId);
        if (existingPropertyOptional.isPresent()) {
            Property existingProperty = existingPropertyOptional.get();
            existingProperty.setPropertyType(updatedProperty.getPropertyType());
            existingProperty.setFlatNo(updatedProperty.getFlatNo());
            existingProperty.setAddress(updatedProperty.getAddress());
            existingProperty.setDescription(updatedProperty.getDescription());
            existingProperty.setPrice(updatedProperty.getPrice());
            existingProperty.setPlaceOffers(updatedProperty.getPlaceOffers());
            propertyRepository.save(existingProperty);
            return("Property is updated");
        } else {
            return("Property id couldn't be found");
        }


    }



}
