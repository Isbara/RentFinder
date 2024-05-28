package GradProject.RentFinder.Mapper;

import GradProject.RentFinder.Models.Property;
import GradProject.RentFinder.RequestModel.PropertyRequest;
import org.springframework.stereotype.Component;

<<<<<<< Updated upstream
=======
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
>>>>>>> Stashed changes
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

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
<<<<<<< Updated upstream
        return property;
    }
    public PropertyRequest ConvertToRequest(Property property){
=======

        List<Image> images = new ArrayList<>();

        if(request.getImages() != null) {
            List<String> imageDataList = request.getImages();
            for (String imageData : imageDataList) {
                byte[] compressedImageDataBytes = compressImage(imageData);
                Image image = new Image();
                image.setData(compressedImageDataBytes);
                image.setProperty(property);
                images.add(image);
            }
            property.setImages(images);
        }
        return property;
    }
/*
    public PropertyRequest converttoResponse(Property property) {
>>>>>>> Stashed changes
        PropertyRequest request = new PropertyRequest();
        request.setPropertyType(property.getPropertyType());
        request.setFlatNo(property.getFlatNo());
        request.setAddress(property.getAddress());
        request.setDescription(property.getDescription());
        request.setPrice(property.getPrice());
        request.setPlaceOffers(property.getPlaceOffers());
<<<<<<< Updated upstream
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
    public static void UpdateConvertOptional(Property existingProperty, PropertyRequest updatedProperty){
=======

        List<String> imageDataList = new ArrayList<>();
        if (property.getImages() != null) {
            for (Image image : property.getImages()) {
                String decompressedImageData = decompressImage(image.getData());
                imageDataList.add(decompressedImageData);
            }
        }
        request.setImages(imageDataList);

        return request;
    }
*/
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
            property.setReviews(existingProperty.getReviews());
            property.setReservations(existingProperty.getReservations());
            property.setOwner(existingProperty.getOwner());
            List<Image> images = new ArrayList<>();
            for (Image existingImage : existingProperty.getImages()) {
                Image newImage = new Image();
                newImage.setData(existingImage.getData());
                newImage.setProperty(property);
                images.add(newImage);
            }
            property.setImages(images);
        }
        return property;
    }

    public static List<Property> Decompresser(List<Property> existingProperties) {
        List<Property> decompressedProperties = new ArrayList<>();
        for (Property existingProperty : existingProperties) {
            Property property = new Property();
            property.setPropertyID(existingProperty.getPropertyID());
            property.setPropertyType(existingProperty.getPropertyType());
            property.setFlatNo(existingProperty.getFlatNo());
            property.setAddress(existingProperty.getAddress());
            property.setDescription(existingProperty.getDescription());
            property.setPrice(existingProperty.getPrice());
            property.setPlaceOffers(existingProperty.getPlaceOffers());
            property.setReviews(existingProperty.getReviews());
            property.setReservations(existingProperty.getReservations());
            property.setOwner(existingProperty.getOwner());

            List<Image> images = new ArrayList<>();
            // Decompress images and add them to the property
            for (Image existingImage : existingProperty.getImages()) {
                byte[] decompressedData = decompressImage(existingImage.getData());
                Image decompressedImage = new Image();
                decompressedImage.setData(decompressedData);
                decompressedImage.setProperty(property);
                images.add(decompressedImage);
            }
            property.setImages(images);
            decompressedProperties.add(property);
        }
        return decompressedProperties;
    }


    public static void UpdateConvertOptional(Property existingProperty, PropertyRequest updatedProperty) {
>>>>>>> Stashed changes
        existingProperty.setPropertyType(updatedProperty.getPropertyType());
        existingProperty.setFlatNo(updatedProperty.getFlatNo());
        existingProperty.setAddress(updatedProperty.getAddress());
        existingProperty.setDescription(updatedProperty.getDescription());
        existingProperty.setPrice(updatedProperty.getPrice());
        existingProperty.setPlaceOffers(updatedProperty.getPlaceOffers());
<<<<<<< Updated upstream
    }
}
=======
        if (updatedProperty.getImages() != null) {
            existingProperty.getImages().clear();
            for (String imageData : updatedProperty.getImages()) {
                try {
                    byte[] imageDataBytes = Base64.getDecoder().decode(imageData);
                    Image image = new Image();
                    image.setData(imageDataBytes);
                    image.setProperty(existingProperty);
                    existingProperty.getImages().add(image);
                } catch (IllegalArgumentException e) {
                    // Handle invalid base64 data
                    // Log the error or throw an exception based on your application's error handling strategy
                }
            }
        }
    }

    private byte[] compressImage(String imageData) {
        byte[] imageDataBytes = Base64.getDecoder().decode(imageData);

        Deflater deflater = new Deflater();
        deflater.setInput(imageDataBytes);
        deflater.finish();

        byte[] compressedData = new byte[imageDataBytes.length];
        int compressedLength = deflater.deflate(compressedData);
        byte[] compressedImageDataBytes = new byte[compressedLength];
        System.arraycopy(compressedData, 0, compressedImageDataBytes, 0, compressedLength);

        return compressedImageDataBytes;
    }

    private static byte[] decompressImage(byte[] compressedData) {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            return outputStream.toByteArray();
        } catch (DataFormatException e) {
            e.printStackTrace();
            return null;
        } finally {
            inflater.end();
        }
    }

}
>>>>>>> Stashed changes
