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
    @Column(name = "IMAGE", columnDefinition = "LONGBLOB") // New column to store binary image data
    private byte[] image;
    @OneToMany(mappedBy = "property", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Review> reviews = new ArrayList<Review>();
    @OneToMany(mappedBy = "reserved", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Reservation> reservations = new ArrayList<Reservation>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    @JsonIgnore
    private User owner;
}
