package GradProject.RentFinder.Mapper;

import GradProject.RentFinder.Models.Property;
import GradProject.RentFinder.RequestModel.PropertyRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PropertyMapper {
    public Property ConvertToModel(PropertyRequest request){
        Property property = new Property();
        property.setPropertyType(request.getPropertyType());
        property.setFlatNo(request.getFlatNo());
        property.setAddress(request.getAddress());
        property.setDescription(request.getDescription());
        property.setPrice(request.getPrice());
        property.setPlaceOffers(request.getPlaceOffers());
        return property;
    }
    public PropertyRequest ConvertToRequest(Property property){
        PropertyRequest request = new PropertyRequest();
        request.setPropertyType(property.getPropertyType());
        request.setFlatNo(property.getFlatNo());
        request.setAddress(property.getAddress());
        request.setDescription(property.getDescription());
        request.setPrice(property.getPrice());
        request.setPlaceOffers(property.getPlaceOffers());
        return request;
    }
    public Property ConvertOptional(Optional<Property> model){
        Property property = new Property();
        property.setPropertyID(model.get().getPropertyID());
        property.setPropertyType(model.get().getPropertyType());
        property.setFlatNo(model.get().getFlatNo());
        property.setAddress(model.get().getAddress());
        property.setDescription(model.get().getDescription());
        property.setPrice(model.get().getPrice());
        property.setPlaceOffers(model.get().getPlaceOffers());
        property.setReviews(model.get().getReviews());
        property.setReservations(model.get().getReservations());
        property.setOwner(model.get().getOwner());
        return property;
    }
    public static void UpdateConvertOptional(Property existingProperty, PropertyRequest updatedProperty)
    {
        existingProperty.setPropertyType(updatedProperty.getPropertyType());
        existingProperty.setFlatNo(updatedProperty.getFlatNo());
        existingProperty.setAddress(updatedProperty.getAddress());
        existingProperty.setDescription(updatedProperty.getDescription());
        existingProperty.setPrice(updatedProperty.getPrice());
        existingProperty.setPlaceOffers(updatedProperty.getPlaceOffers());

    }
}
