package GradProject.RentFinder.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "TICKET_TABLE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {
    @Id
    @Column(name = "ID")
    @GeneratedValue
    private long ticketID;
    @Column(name = "TOPIC")
    private String topic;
    @Column(name = "LOCATION")
    private String location;
    @Column(name = "DETAILS")
    private String details;
    @ManyToOne()
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    @JsonIgnore
    private User submitter;
}
