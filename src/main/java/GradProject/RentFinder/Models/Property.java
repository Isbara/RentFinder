package GradProject.RentFinder.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

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
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Review> reviews;
    @OneToMany(mappedBy = "reserved", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Reservation> reservations;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    @JsonIgnore
    private User owner;
}
