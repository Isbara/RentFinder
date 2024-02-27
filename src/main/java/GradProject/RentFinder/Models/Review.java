package GradProject.RentFinder.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "REVIEW_TABLE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review extends Comment{
    @Column(name = "USER_SCORE")
    private int userScore;
    @Column(name = "ALGO_RESULT")
    private boolean algoResult;
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Respond> respondList;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROPERTY_ID")
    @ToString.Exclude
    @JsonIgnore
    private Property property;
}