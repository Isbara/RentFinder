package GradProject.RentFinder.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
    private Date startDate;
    @Column(name = "END_DATE")
    private Date endDate;
    @Column(name="STATUS")
    private boolean status;
    @Column(name="APPROVAL")
    private boolean approval;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    @JsonIgnore
    private User reserver;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROPERTY_ID")
    @ToString.Exclude
    private Property reserved;
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Review review;
    @JsonIgnore
    public Property getReserved() {
        return this.reserved;
    }
    @JsonIgnore
    public void setReserved(Property reserved){
        this.reserved = reserved;
    }

    public Long getPropertyID(){//sa
        return reserved.getPropertyID();
    }
}
