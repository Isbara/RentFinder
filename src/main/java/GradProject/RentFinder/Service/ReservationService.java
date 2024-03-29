package GradProject.RentFinder.Service;

import GradProject.RentFinder.Exception.AllExceptions;
import GradProject.RentFinder.Exception.Exceptions;
import GradProject.RentFinder.Mapper.PropertyMapper;
import GradProject.RentFinder.Mapper.ReservationMapper;
import GradProject.RentFinder.Mapper.UserMapper;
import GradProject.RentFinder.Models.Property;
import GradProject.RentFinder.Models.Reservation;
import GradProject.RentFinder.Models.User;
import GradProject.RentFinder.Repository.PropertyRepository;
import GradProject.RentFinder.Repository.ReservationRepository;
import GradProject.RentFinder.Repository.UserRepository;
import GradProject.RentFinder.RequestModel.ReservationRequest;
import GradProject.RentFinder.SecurityConfig.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final ReservationMapper reservationMapper;
    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    public Reservation MakeReservation(String token, Long id, ReservationRequest request) {
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
            Reservation reservation = reservationMapper.ConvertToModel(request);
            reservation.setReserver(user);
            Property property = propertyMapper.ConvertOptional(propertyRepository.findById(id));
            reservation.setReserved(property);
            return reservationRepository.save(reservation);
        }
        else
            throw new Exceptions(AllExceptions.TOKEN_EXPIRED);
    }

    public List<Reservation> GetReservations(String token, Long id) {
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
            return reservationRepository.findByIDs(user.getUserID(), id);
        }
        else
            throw new Exceptions(AllExceptions.TOKEN_EXPIRED);
    }
    public Boolean ValidateReservation(Long reservationId) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        return optionalReservation.isPresent();
    }
    public void MakeDecisionForStatus(Long reservationId,boolean status_decision)
    {
        try{
            reservationRepository.makeDecisionForStatus(reservationId,status_decision);
        }
        catch (Exception e)
        {
            throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
        }
    }

    public Boolean ValidateProperty(Long propertyId)
    {
        Optional<Property> optionalProperty=propertyRepository.findById(propertyId);
        return optionalProperty.isPresent();

    }

    public List<Reservation> GetAllPropertyReservations(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            String jwt = token.substring(7);
            String username = jwtService.extractUsername(jwt);
            Optional<User> optionalUser = userRepository.findByEmail(username);
            User user;
            ArrayList<Reservation> ReservationList = new ArrayList<Reservation>();
            if(optionalUser.isPresent())
                user = userMapper.ConvertOptional(optionalUser);
            else
                throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            for (Property property:user.getProperties()) {
                ReservationList.addAll(property.getReservations());//This loop and return can be changed based on front-end development stage!!!
            }
            return ReservationList;
        }
        else
            throw new Exceptions(AllExceptions.TOKEN_EXPIRED);
    }

    public void MakeDecisionForApproval(Long reservationId,boolean approval_decision)
    {
        try{
            reservationRepository.makeDecisionForApproval(reservationId,approval_decision);
        }
        catch (Exception e)
        {
            throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
        }
    }
}
