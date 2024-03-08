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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
        String jwt = token.substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> optionalUser = userRepository.findByEmail(username);
        User user;
        if(optionalUser.isPresent()){ //Checking user
            user = userMapper.ConvertOptional(optionalUser);
        }
        else{
            return new Reservation(); //değişecek.
        }
        boolean validity =jwtService.isTokenValid(jwt, user);
        if(validity){ //Checking token
            Reservation reservation = reservationMapper.ConvertToModel(request);
            reservation.setReserver(user);
            Property property = propertyMapper.ConvertOptional(propertyRepository.findById(id));
            reservation.setReserved(property);
            return reservationRepository.save(reservation);
        }
        else{
            return new Reservation(); // değişecek.
        }
    }

    public List<Reservation> GetReservations(String token, Long id) {
        String jwt = token.substring(7);
        String username = jwtService.extractUsername(jwt);
        Optional<User> optionalUser = userRepository.findByEmail(username);
        User user;
        if(optionalUser.isPresent()){
            user = userMapper.ConvertOptional(optionalUser);
        }
        else{
            return new ArrayList<Reservation>(); //değişecek.
        }
        boolean validity =jwtService.isTokenValid(jwt, user);
        if(validity){
            return reservationRepository.findByIDs(user.getUserID(), id);
        }
        else{
            return new ArrayList<Reservation>(); //değişecek.
        }
    }
    public Boolean ValidateReservation(Long reservationId) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        return optionalReservation.isPresent();
    }
    public void MakeDecision(Long reservationId,Boolean decision)
    {
        try{
            reservationRepository.makeDecision(reservationId,decision);
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
}
