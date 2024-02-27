package GradProject.RentFinder.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RESERVATION_TABLE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private long reservationID;
    @Column(name = "PEOPLE_COUNT")
    private int numberOfPeople;
    @Column(name = "START_DATE")
    private java.time.LocalDate startDate;
    @Column(name = "END_DATE")
    private java.time.LocalDate endDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    @JsonIgnore
    private User reserver;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROPERTY_ID")
    @ToString.Exclude
    @JsonIgnore
    private Property reserved;
}
