package GradProject.RentFinder.Service;

import GradProject.RentFinder.Exception.AllExceptions;
import GradProject.RentFinder.Exception.Exceptions;
import GradProject.RentFinder.Mapper.PropertyMapper;
import GradProject.RentFinder.Mapper.UserMapper;
import GradProject.RentFinder.Models.Property;
import GradProject.RentFinder.Models.User;
import GradProject.RentFinder.Repository.PropertyRepository;
import GradProject.RentFinder.Repository.UserRepository;
import GradProject.RentFinder.RequestModel.PropertyRequest;
import GradProject.RentFinder.SecurityConfig.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public Property addProperty(String token, PropertyRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            String jwt = token.substring(7);
            String username = jwtService.extractUsername(jwt);
            Optional<User> optionalUser = userRepository.findByEmail(username);
            User user;
            if(optionalUser.isPresent())
                user = userMapper.ConvertOptional(optionalUser);
            else
                throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            Property property = propertyMapper.ConvertToModel(request);
            property.setOwner(user);
            return propertyRepository.save(property);
        }
        else
            throw new Exceptions(AllExceptions.TOKEN_EXPIRED);
    }

    public void deleteProperty(Long propertyId){
        propertyRepository.deleteById(propertyId);

    }

    public String updateProperty(Long propertyId, PropertyRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            Optional<Property> existingPropertyOptional = propertyRepository.findById(propertyId);
            Property existingProperty;
            if (existingPropertyOptional.isPresent())
                existingProperty = existingPropertyOptional.get();
            else
                throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            PropertyMapper.UpdateConvertOptional(existingProperty, request);
            propertyRepository.save(existingProperty);
            return ("Property is updated");
        }
        else
            throw new Exceptions(AllExceptions.TOKEN_EXPIRED);
    }


    public Property getPropertyDetails(Long id) {
        Optional<Property> optionalProperty = propertyRepository.findById(id);
        return propertyMapper.ConvertOptional(optionalProperty);
    }

    public List<Property> getUserProperties(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            String jwt = token.substring(7);
            String username = jwtService.extractUsername(jwt);
            Optional<User> optionalUser = userRepository.findByEmail(username);
            User user;
            if(optionalUser.isPresent())
                user = userMapper.ConvertOptional(optionalUser);
            else
                throw  new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            return propertyRepository.findByOwnerID(user.getUserID());
        }
        else{
            throw new Exceptions(AllExceptions.TOKEN_EXPIRED);
        }
    }
}
