package GradProject.RentFinder.Controller;

import GradProject.RentFinder.Models.Respond;
import GradProject.RentFinder.Models.Review;
import GradProject.RentFinder.RequestModel.RespondRequest;
import GradProject.RentFinder.RequestModel.ReviewRequest;
import GradProject.RentFinder.Service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{id}") //Get reviews of a property
    public ResponseEntity<List<Review>> GetPropertyReviews(@PathVariable Long id){
        return ResponseEntity.ok().body(reviewService.GetPropertyReviews(id));
    }

    @PostMapping("/{propertyID}/{reservationID}")
    public ResponseEntity<Review> WriteReview(@RequestHeader(value = "Authorization") String token, @PathVariable Long propertyID, @PathVariable Long reservationID, @RequestBody ReviewRequest request){
        return ResponseEntity.ok().body(reviewService.WriteReview(token, propertyID, reservationID, request));
    }

    @PostMapping("/response/{id}")
    public ResponseEntity<Respond> WriteRespond(@RequestHeader(value = "Authorization") String token, @PathVariable Long id, @RequestBody RespondRequest request){
        return ResponseEntity.ok().body(reviewService.WriteRespond(token, id, request));
    }
}
