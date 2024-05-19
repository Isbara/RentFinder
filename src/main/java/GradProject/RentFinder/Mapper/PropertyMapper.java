package GradProject.RentFinder.Mapper;

import GradProject.RentFinder.Models.Image;
import GradProject.RentFinder.Models.Property;
import GradProject.RentFinder.RequestModel.PropertyRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Component
public class PropertyMapper {
    public Property ConvertToModel(PropertyRequest request) {
        Property property = new Property();
        property.setPropertyType(request.getPropertyType());
        property.setFlatNo(request.getFlatNo());
        property.setAddress(request.getAddress());
        property.setDescription(request.getDescription());
        property.setPrice(request.getPrice());
        property.setPlaceOffers(request.getPlaceOffers());

        List<Image> images = new ArrayList<>();

        // Iterate over the images using a traditional for loop
        List<String> imageDataList = request.getImages();
        for (String imageData : imageDataList) {
                byte[] imageDataBytes = Base64.getDecoder().decode(imageData);
                Image image = new Image();
                image.setData(imageDataBytes);
                image.setProperty(property);
                images.add(image);
        }

        property.setImages(images);
        return property;
    }

    public PropertyRequest convertToRequest(Property property) {
        PropertyRequest request = new PropertyRequest();
        request.setPropertyType(property.getPropertyType());
        request.setFlatNo(property.getFlatNo());
        request.setAddress(property.getAddress());
        request.setDescription(property.getDescription());
        request.setPrice(property.getPrice());
        request.setPlaceOffers(property.getPlaceOffers());

        // Convert each image data byte array to a Base64 string
        List<String> imageDataList = new ArrayList<>();
        for (Image image : property.getImages()) {
            byte[] imageDataBytes = image.getData();
            String imageDataString = Base64.getEncoder().encodeToString(imageDataBytes);
            imageDataList.add(imageDataString);
        }
        request.setImages(imageDataList);

        return request;
    }

    public Property ConvertOptional(Optional<Property> model) {
        Property property = new Property();
        if (model.isPresent()) {
            Property existingProperty = model.get();
            property.setPropertyID(existingProperty.getPropertyID());
            property.setPropertyType(existingProperty.getPropertyType());
            property.setFlatNo(existingProperty.getFlatNo());
            property.setAddress(existingProperty.getAddress());
            property.setDescription(existingProperty.getDescription());
            property.setPrice(existingProperty.getPrice());
            property.setPlaceOffers(existingProperty.getPlaceOffers());

            // Copy reviews, reservations, and owner directly
            property.setReviews(existingProperty.getReviews());
            property.setReservations(existingProperty.getReservations());
            property.setOwner(existingProperty.getOwner());

            // Convert each image to a new Image object and add it to the property's images list
            List<Image> images = new ArrayList<>();
            for (Image existingImage : existingProperty.getImages()) {
                Image newImage = new Image();
                newImage.setData(existingImage.getData());
                newImage.setProperty(property); // Set the property reference for each image
                images.add(newImage);
            }
            property.setImages(images);
        }
        return property;
    }

    public static void UpdateConvertOptional(Property existingProperty, PropertyRequest updatedProperty) {
        // Update existing property fields
        existingProperty.setPropertyType(updatedProperty.getPropertyType());
        existingProperty.setFlatNo(updatedProperty.getFlatNo());
        existingProperty.setAddress(updatedProperty.getAddress());
        existingProperty.setDescription(updatedProperty.getDescription());
        existingProperty.setPrice(updatedProperty.getPrice());
        existingProperty.setPlaceOffers(updatedProperty.getPlaceOffers());

        // Clear existing images
        existingProperty.getImages().clear();

        // Convert each image data string to a byte array and update the existing property's images
        for (String imageData : updatedProperty.getImages()) {
            try {
                byte[] imageDataBytes = Base64.getDecoder().decode(imageData);
                Image image = new Image();
                image.setData(imageDataBytes);
                image.setProperty(existingProperty); // Set the property reference for each image
                existingProperty.getImages().add(image);
            } catch (IllegalArgumentException e) {
                // Handle invalid base64 data
                // Log the error or throw an exception based on your application's error handling strategy
            }
        }
    }

}
