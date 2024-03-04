package GradProject.RentFinder.RequestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewRequest extends CommentRequest{
    private int userScore;
    private boolean algoResult;
}
