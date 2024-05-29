package GradProject.RentFinder.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "RESPOND_TABLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Respond extends Comment{
    @ManyToOne()
    @JoinColumn(name = "REVIEW_ID")
    @ToString.Exclude
    @JsonIgnore
    private Review review;
    @ManyToOne()
    @JoinColumn(name = "USER_ID")
    @ToString.Exclude
    @JsonIgnore
    private User responder;
}
