package GradProject.RentFinder.Service;

import GradProject.RentFinder.Mapper.*;
import GradProject.RentFinder.Models.*;
import GradProject.RentFinder.Repository.*;
import GradProject.RentFinder.RequestModel.RespondRequest;
import GradProject.RentFinder.RequestModel.ReviewRequest;
import GradProject.RentFinder.SecurityConfig.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final ReservationMapper reservationMapper;
    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final RespondMapper respondMapper;
    private final RespondRepository respondRepository;


    public List<Review> GetPropertyReviews(String token, Long id) {
        String jwt = token.substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> optionalUser = userRepository.findByEmail(username);
        User user;
        if(optionalUser.isPresent()){
            user = userMapper.ConvertOptional(optionalUser);
        }
        else{
            return new ArrayList<Review>(); //değişecek.
        }
        boolean validity = jwtService.isTokenValid(jwt, user);
        if(validity){
            Optional<Property> optionalProperty = propertyRepository.findById(id);
            if(optionalProperty.isPresent())
                return propertyMapper.ConvertOptional(optionalProperty).getReviews();
            else
                return new ArrayList<Review>(); //değişecek.
        }
        else
            return new ArrayList<Review>(); //değişecek.
    }

//    public Review WriteReview(String token, Long id, ReviewRequest request) {
//        return null;
//    }

    public Respond WriteRespond(String token, Long id, RespondRequest request) {
        String jwt = token.substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> optionalUser = userRepository.findByEmail(username);
        User user;
        if(optionalUser.isPresent()){
            user = userMapper.ConvertOptional(optionalUser);
        }
        else{
            return new Respond(); //değişecek
        }
        boolean validity = jwtService.isTokenValid(jwt, user);
        if(validity){
            Respond respond = respondMapper.ConvertToModel(request);
            respond.setDate(new Date(System.currentTimeMillis()));
            Optional<Review> optionalReview = reviewRepository.findById(id);
            Review review;
            if(optionalReview.isPresent())
                review = reviewMapper.ConvertOptional(optionalReview);
            else
                return new Respond(); //değişecek
            respond.setReview(review);
            return respondRepository.save(respond);
        }
        else
            return new Respond(); //değişecek
    }
}
