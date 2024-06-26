package GradProject.RentFinder.Mapper;

import GradProject.RentFinder.Models.Review;
import GradProject.RentFinder.RequestModel.ReviewRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class ReviewMapper {
    public Review ConvertToModel(ReviewRequest request){
        Review review = new Review();
        review.setDescription(request.getDescription());
        review.setDate(request.getDate());
        review.setUserScore(request.getUserScore());
        review.setFakeResult(request.isFakeResult());
        review.setSentimentResult(request.isSentimentResult());
        return review;
    }
    public ReviewRequest ConvertToRequest(Review review){
        ReviewRequest request = new ReviewRequest();
        request.setDescription(review.getDescription());
        request.setDate(review.getDate());
        request.setUserScore(review.getUserScore());
        request.setFakeResult(review.isFakeResult());
        request.setSentimentResult(review.isSentimentResult());
        return request;
    }
    public Review ConvertOptional(Optional<Review> model){
        Review review = new Review();
        review.setCommentID(model.get().getCommentID());
        review.setDescription(model.get().getDescription());
        review.setDate(model.get().getDate());
        review.setUserScore(model.get().getUserScore());
        review.setFakeResult(model.get().isFakeResult());
        review.setSentimentResult(model.get().isSentimentResult());
        review.setRespondList(model.get().getRespondList());
        review.setProperty(model.get().getProperty());
        return review;
    }
}
