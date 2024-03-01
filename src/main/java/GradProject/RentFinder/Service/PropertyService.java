package GradProject.RentFinder.Service;

import GradProject.RentFinder.Models.Property;
import GradProject.RentFinder.Repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

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




}
