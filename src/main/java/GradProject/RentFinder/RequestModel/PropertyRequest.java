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
}
