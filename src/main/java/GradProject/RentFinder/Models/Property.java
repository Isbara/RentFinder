package GradProject.RentFinder.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull
    private char propertyType;
    @Column(name = "FLAT_NO")
    @Min(1)
    private int flatNo;
    @Column(name = "ADDRESS")
    @NotBlank
    private String address;
    @NotBlank
    @Column(name = "DESCRIPTION")
    private String description;
    @Min(1)
    @Column(name = "PRICE")
    private int price;
    @NotBlank
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
