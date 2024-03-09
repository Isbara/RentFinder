package GradProject.RentFinder.Mapper;

import GradProject.RentFinder.Models.Reservation;
import GradProject.RentFinder.RequestModel.ReservationRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReservationMapper {
    public Reservation ConvertToModel(ReservationRequest request){
        Reservation reservation = new Reservation();
        reservation.setNumberOfPeople(request.getNumberOfPeople());
        reservation.setStartDate(request.getStartDate());
        reservation.setEndDate(request.getEndDate());
        return reservation;
    }
    public ReservationRequest ConvertToRequest(Reservation reservation){
        ReservationRequest request = new ReservationRequest();
        request.setNumberOfPeople(reservation.getNumberOfPeople());
        request.setStartDate(reservation.getStartDate());
        request.setEndDate(reservation.getEndDate());
        return request;
    }
    public Reservation ConvertOptional(Optional<Reservation> model){
        Reservation reservation = new Reservation();
        reservation.setReservationID(model.get().getReservationID());
        reservation.setNumberOfPeople(model.get().getNumberOfPeople());
        reservation.setStartDate(model.get().getStartDate());
        reservation.setEndDate(model.get().getEndDate());
        reservation.setReserver(model.get().getReserver());
        reservation.setReserved(model.get().getReserved());
        reservation.setStatus(model.get().isStatus());//boolean and Boolean are different. Needs change based on decisions.
        return reservation;
    }
}
