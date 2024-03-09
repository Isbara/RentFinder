package GradProject.RentFinder.Service;

import GradProject.RentFinder.Exception.AllExceptions;
import GradProject.RentFinder.Exception.Exceptions;
import GradProject.RentFinder.Mapper.*;
import GradProject.RentFinder.Models.*;
import GradProject.RentFinder.Repository.*;
import GradProject.RentFinder.RequestModel.RespondRequest;
import GradProject.RentFinder.RequestModel.ReviewRequest;
import GradProject.RentFinder.SecurityConfig.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            Optional<Property> optionalProperty = propertyRepository.findById(id);
            if(optionalProperty.isPresent())
                return propertyMapper.ConvertOptional(optionalProperty).getReviews();
            else
                throw  new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
        }
        else
            throw  new Exceptions(AllExceptions.TOKEN_EXPIRED);
    }

//    public Review WriteReview(String token, Long id, ReviewRequest request) {
//        return null;
//    }

    public Respond WriteRespond(String token, Long id, RespondRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            Respond respond = respondMapper.ConvertToModel(request);
            respond.setDate(new Date(System.currentTimeMillis()));
            Optional<Review> optionalReview = reviewRepository.findById(id);
            Review review;
            if(optionalReview.isPresent())
                review = reviewMapper.ConvertOptional(optionalReview);
            else
                throw  new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            respond.setReview(review);
            return respondRepository.save(respond);
        }
        else
            throw  new Exceptions(AllExceptions.TOKEN_EXPIRED);
    }
}
