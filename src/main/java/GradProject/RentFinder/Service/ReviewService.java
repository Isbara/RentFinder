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

    public Review WriteReview(String token, Long propertyID, Long reservationID, ReviewRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            String jwt = token.substring(7);
            String username = jwtService.extractUsername(jwt);
            Optional<User> optionalUser = userRepository.findByEmail(username);
            User user;
            if(optionalUser.isPresent())
                user = userMapper.ConvertOptional(optionalUser);
            else
                throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            Optional<Property> optionalProperty = propertyRepository.findById(propertyID);
            Property property;
            if(optionalProperty.isPresent())
                property = propertyMapper.ConvertOptional(optionalProperty);
            else
                throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            Optional<Reservation> optionalReservation = reservationRepository.findById(reservationID);
            Reservation reservation;
            if(optionalReservation.isPresent())
                reservation = reservationMapper.ConvertOptional(optionalReservation);
            else
                throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            Review review = reviewMapper.ConvertToModel(request);
            review.setDate(new Date(System.currentTimeMillis()));
            review.setProperty(property);
            review.setReservation(reservation);
            review.setReviewer(user);
            return reviewRepository.save(review);
        }
        else
            throw  new Exceptions(AllExceptions.TOKEN_EXPIRED);
    }

    public Respond WriteRespond(String token, Long id, RespondRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            String jwt = token.substring(7);
            String username = jwtService.extractUsername(jwt);
            Optional<User> optionalUser = userRepository.findByEmail(username);
            User user;
            if(optionalUser.isPresent())
                user = userMapper.ConvertOptional(optionalUser);
            else
                throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            Respond respond = respondMapper.ConvertToModel(request);
            respond.setDate(new Date(System.currentTimeMillis()));
            Optional<Review> optionalReview = reviewRepository.findById(id);
            Review review;
            if(optionalReview.isPresent())
                review = reviewMapper.ConvertOptional(optionalReview);
            else
                throw  new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            respond.setReview(review);
            respond.setResponder(user);
            System.out.println("HELP");
            return respondRepository.save(respond);
        }
        else
            throw  new Exceptions(AllExceptions.TOKEN_EXPIRED);
    }
}
