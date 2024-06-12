package GradProject.RentFinder.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Boolean status;
    @Column(name="APPROVAL")
    private Boolean approval;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    @JsonIgnore
    private User reserver;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROPERTY_ID")
    @ToString.Exclude
    private Property reserved;
    @OneToOne(mappedBy = "reservation", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private Review review;
    @Column(name="DAYS")
    private long days;

    @JsonIgnore
    public Property getReserved() {
        return this.reserved;
    }
    @JsonIgnore
    public void setReserved(Property reserved){
        this.reserved = reserved;
    }

    public String getAdress() {return reserved.getAddress();}
    public Long getPropertyID(){
        return reserved.getPropertyID();
    }
    public String getPhoneNumber(){
        return reserver.getPhoneNumber();
    }

    public String getUserName() {return reserver.getName();}
    public String getUserSurname() {return reserver.getSurname();}

    public int getKarmaPoint() { return reserver.getKarmaPoint();}
    public int getReserverKarma(){
        return reserver.getKarmaPoint();
    }
}
