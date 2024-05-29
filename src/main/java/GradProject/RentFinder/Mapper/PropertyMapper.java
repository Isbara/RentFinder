package GradProject.RentFinder.Mapper;

import GradProject.RentFinder.Models.Property;
import GradProject.RentFinder.Models.Image;
import GradProject.RentFinder.RequestModel.PropertyRequest;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

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

        if (request.getImages() != null) {
            List<Image> images = new ArrayList<>();
            for (String imageData : request.getImages()) {
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
                byte[] decompressedData = decompressImage(existingImage.getData());
                Image newImage = new Image();
                newImage.setData(decompressedData);
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
        existingProperty.setPropertyType(updatedProperty.getPropertyType());
        existingProperty.setFlatNo(updatedProperty.getFlatNo());
        existingProperty.setAddress(updatedProperty.getAddress());
        existingProperty.setDescription(updatedProperty.getDescription());
        existingProperty.setPrice(updatedProperty.getPrice());
        existingProperty.setPlaceOffers(updatedProperty.getPlaceOffers());

        if (updatedProperty.getImages() != null) {
            existingProperty.getImages().clear();
            for (String imageData : updatedProperty.getImages()) {
                byte[] compressedImageDataBytes = compressImage(imageData);
                Image image = new Image();
                image.setData(compressedImageDataBytes);
                image.setProperty(existingProperty);
                existingProperty.getImages().add(image);
            }
        }
    }
    private static byte[] compressImage(String imageData) {
        byte[] imageDataBytes = Base64.getDecoder().decode(imageData);
        Deflater deflater = new Deflater();
        deflater.setInput(imageDataBytes);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(imageDataBytes.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        return outputStream.toByteArray();
    }

    private static byte[] decompressImage(byte[] compressedData) {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedData.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
        } catch (DataFormatException e) {
            e.printStackTrace();
        } finally {
            inflater.end();
        }

        return outputStream.toByteArray();
    }

}
