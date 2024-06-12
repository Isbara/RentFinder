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

import java.util.*;
import java.util.stream.Collectors;

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
            List<Reservation> overlappingReservations = reservationRepository.findOverlappingReservations(id, request.getStartDate(), request.getEndDate());
            if (!overlappingReservations.isEmpty())
                throw new Exceptions(AllExceptions.ALREADY_RESERVED);

            Property property = propertyMapper.ConvertOptional(propertyRepository.findById(id));
            if(property.getOwner().getUserID() == user.getUserID())
                throw new Exceptions(AllExceptions.SELF_RESERVATION);

            int bufferDays = 7;
            Calendar cal = Calendar.getInstance();

            cal.setTime(request.getStartDate());
            cal.add(Calendar.DATE, -bufferDays);
            Date bufferStartDate = cal.getTime();

            cal.setTime(request.getEndDate());
            cal.add(Calendar.DATE, bufferDays);
            Date bufferEndDate = cal.getTime();

            List<Reservation> userReservationsInBuffer = reservationRepository.findUserReservationsInBufferPeriod(user.getUserID(), bufferStartDate, bufferEndDate);
            if (!userReservationsInBuffer.isEmpty()) {
                throw new Exceptions(AllExceptions.MULTIPLE_CLOSE_RESERVATIONS);
            }

            Reservation reservation = reservationMapper.ConvertToModel(request);
            reservation.setReserver(user);
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

    public void MakeDecisionForApproval(Long reservationId, Boolean approval_decision, String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            reservationRepository.makeDecisionForApproval(reservationId, approval_decision);

            if (approval_decision) {
                Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
                if (!optionalReservation.isPresent()) {
                    throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
                }

                Reservation approvedReservation = optionalReservation.get();

                reservationRepository.deleteOverlappingUnapprovedReservations(approvedReservation.getReserved().getPropertyID(), approvedReservation.getStartDate(), approvedReservation.getEndDate());
            }
        } else {
            throw new Exceptions(AllExceptions.TOKEN_EXPIRED);
        }
    }

    public void MakeDecisionForStatus(Long reservationId,Boolean status_decision)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {

            try {
                reservationRepository.makeDecisionForStatus(reservationId, status_decision);
                if(status_decision){
                    Reservation reservation;
                    Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
                    if(optionalReservation.isPresent())
                        reservation = reservationMapper.ConvertOptional(optionalReservation);
                    else
                        throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
                    User user = reservation.getReserver();
                    user.setKarmaPoint(user.getKarmaPoint()+2);
                    userRepository.save(user);
                }
            } catch (Exception e) {
                throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            }
        }
    }
    public List<Reservation> GetAllUserReservations(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()){
            String jwt = token.substring(7);
            String username = jwtService.extractUsername(jwt);
            Optional<User> optionalUser = userRepository.findByEmail(username);
            if(optionalUser.isPresent()) {
                User user = optionalUser.get();
                List<Reservation> reservationList = new ArrayList<>();
                for (Property property : propertyRepository.findAll()) {
                    for (Reservation reservation : property.getReservations()) {
                        if (reservation.getReserver().equals(user)) {
                            reservationList.add(reservation);
                        }
                    }
                }
                return reservationList;
            } else {
                throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new Exceptions(AllExceptions.TOKEN_EXPIRED);
        }
    }


    public List<Reservation> getReservationsForOwnedProperties(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.isAuthenticated()) {
            String jwt = token.substring(7);
            String username = jwtService.extractUsername(jwt);
            Optional<User> optionalUser = userRepository.findByEmail(username);
            User user;
            if(optionalUser.isPresent()) {
                user = UserMapper.ConvertOptional(optionalUser);
            } else {
                throw new Exceptions(AllExceptions.INTERNAL_SERVER_ERROR);
            }
            return reservationRepository.findByPropertyUserID(user.getUserID());
        } else {
            throw new Exceptions(AllExceptions.TOKEN_EXPIRED);
        }
    }
}
