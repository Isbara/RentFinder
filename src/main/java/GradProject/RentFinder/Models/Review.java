package GradProject.RentFinder.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "REVIEW_TABLE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review extends Comment{
    @Column(name = "USER_SCORE")
    private int userScore;
    @Column(name = "FAKE_RESULT")
    private boolean fakeResult;
    @Column(name = "SENTIMENT_RESULT")
    private boolean sentimentResult;
    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Respond> respondList = new ArrayList<Respond>();
    @ManyToOne()
    @JoinColumn(name = "PROPERTY_ID")
    @ToString.Exclude
    private Property property;
    @OneToOne()
    @JoinColumn(name = "RESERVATION_ID")
    private Reservation reservation;
    @ManyToOne()
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    private User reviewer;

    @JsonIgnore
    public Property getProperty() {
        return this.property;
    }
    @JsonIgnore
    public void setProperty(Property property){
        this.property = property;
    }

    public Long getPropertyID(){
        return property.getPropertyID();
    }

    @JsonIgnore
    public Reservation getReservation() {
        return this.reservation;
    }
    @JsonIgnore
    public void setReservation(Reservation reservation){
        this.reservation = reservation;
    }

    public Long getReservationID(){
        return reservation.getReservationID();
    }

    @JsonIgnore
    public User getReviewer() {
        return this.reviewer;
    }
    @JsonIgnore
    public void setReviewer(User reviewer){
        this.reviewer = reviewer;
    }

    public Long getReviewerID(){
        return reviewer.getUserID();
    }
    public String getReviewerName(){
        return(reviewer.getName() + " " + reviewer.getSurname());
    }
    public int getReviewerKarma(){
        return reviewer.getKarmaPoint();
    }
}
