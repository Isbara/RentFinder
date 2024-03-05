package GradProject.RentFinder.Service;

import GradProject.RentFinder.Exception.AllExceptions;
import GradProject.RentFinder.Exception.Exceptions;
import GradProject.RentFinder.Mapper.PropertyMapper;
import GradProject.RentFinder.Mapper.UserMapper;
import GradProject.RentFinder.Models.Property;
import GradProject.RentFinder.Models.User;
import GradProject.RentFinder.Repository.PropertyRepository;
import GradProject.RentFinder.Repository.UserRepository;
import GradProject.RentFinder.SecurityConfig.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
            if(updatedProperty.getAddress().length()<5) // Just for example with input values
                throw new Exceptions(AllExceptions.ADDRESS_LENGTH);
            propertyRepository.save(existingProperty);
            return("Property is updated");
        } else {
            throw new Exceptions(AllExceptions.PROPERTY_ID_NOT_FOUND);
        }


    }


    public Property getPropertyDetails(String token, Long id) {
        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //if(authentication.isAuthenticated()) {
        // ilk  validity if versiyonu. Şükrü hocaya sorulacak.
        String jwt = token.substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> optionalUser = userRepository.findByEmail(username);
        User user;
        if(optionalUser.isPresent()){
            user = userMapper.ConvertOptional(optionalUser);
        }
        else{
            return new Property(); //değişecek.
        }
        boolean validity =jwtService.isTokenValid(jwt, user);
        if(validity){
            Optional<Property> optionalProperty = propertyRepository.findById(id);
            return propertyMapper.ConvertOptional(optionalProperty);
        }
        else{
            return new Property(); //değişecek.
        }
    }

    public List<Property> getUserProperties(String token) {
        String jwt = token.substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> optionalUser = userRepository.findByEmail(username);
        User user;
        if(optionalUser.isPresent()){
            user = userMapper.ConvertOptional(optionalUser);
        }
        else{
            return new ArrayList<Property>(); //değişecek.
        }
        boolean validity =jwtService.isTokenValid(jwt, user);
        if(validity){
            return propertyRepository.findByOwnerID(user.getUserID());
        }
        else{
            return new ArrayList<Property>(); //değişecek.
        }
    }
}
