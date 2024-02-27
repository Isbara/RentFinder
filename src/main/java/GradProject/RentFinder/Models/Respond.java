package GradProject.RentFinder.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RESPOND_TABLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Respond extends Comment{
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REVIEW_ID")
    @ToString.Exclude
    @JsonIgnore
    private Review review;
}
