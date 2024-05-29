package GradProject.RentFinder.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PROPERTY_TABLE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Property {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private long propertyID;

    @Column(name = "TYPE")
    private char propertyType;

    @Column(name = "FLAT_NO")
    private int flatNo;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE")
    private int price;

    @Column(name = "OFFERS")
    private String placeOffers;

    // Updated to handle multiple images
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "reserved", cascade = CascadeType.REMOVE )
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    @JsonIgnore
    private User owner;

    public Property(char propertyType, int flatNo, String address, String description, int price, String placeOffers) {
        this.propertyType = propertyType;
        this.flatNo = flatNo;
        this.address = address;
        this.description = description;
        this.price = price;
        this.placeOffers = placeOffers;
    }
}
