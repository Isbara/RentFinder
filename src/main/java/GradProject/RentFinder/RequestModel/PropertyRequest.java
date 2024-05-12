package GradProject.RentFinder.RequestModel;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PropertyRequest {
    private char propertyType;
    private int flatNo;
    private String address;
    private String description;
    private int price;
    private String placeOffers;
    private byte[] image;

    public PropertyRequest(char propertyType, int flatNo, String address, String description, int price, String placeOffers) {
        this.propertyType = propertyType;
        this.flatNo = flatNo;
        this.address = address;
        this.description = description;
        this.price = price;
        this.placeOffers = placeOffers;
    }
}
