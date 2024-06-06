package GradProject.RentFinder.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @Column(name = "POSITIVE")
    private int positiveReviews;
    @Column(name = "NEGATIVE")
    private int negativeReviews;

    // Updated to handle multiple images
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "reserved", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Reservation> reservations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    @JsonIgnore
    private User owner;



    public Property(char propertyType, int flatNo, String address, String description, int price, String placeOffers, int positiveReviews, int negativeReviews) {
        this.propertyType = propertyType;
        this.flatNo = flatNo;
        this.address = address;
        this.description = description;
        this.price = price;
        this.placeOffers = placeOffers;
        this.positiveReviews = positiveReviews;
        this.negativeReviews = negativeReviews;
    }

}
