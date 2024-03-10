package GradProject.RentFinder.Controller;

import GradProject.RentFinder.Exception.AllExceptions;
import GradProject.RentFinder.Exception.Exceptions;
import GradProject.RentFinder.Models.Reservation;
import GradProject.RentFinder.RequestModel.ReservationRequest;
import GradProject.RentFinder.Service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/makeReservation/{id}")//User makes reservation for one specific property
    public ResponseEntity<Reservation> MakeReservation(@RequestHeader(value = "Authorization") String token, @PathVariable Long id, @RequestBody ReservationRequest request) {
        if(!reservationService.ValidateProperty(id))
        {
            throw new Exceptions(AllExceptions.PROPERTY_ID_NOT_FOUND);
        }
        return ResponseEntity.ok().body(reservationService.MakeReservation(token, id, request));
    }

    @GetMapping("/getReservation/{id}") //Returns user's reservations on the viewed property.
    public ResponseEntity<List<Reservation>> GetReservations(@RequestHeader(value = "Authorization") String token, @PathVariable Long id) {
        if(!reservationService.ValidateProperty(id))
        {
            throw new Exceptions(AllExceptions.PROPERTY_ID_NOT_FOUND);
        }
        return ResponseEntity.ok().body(reservationService.GetReservations(token, id));
    }

    @PostMapping("/decision/{reservationId}")
    public ResponseEntity<String> makeDecisionForReservation(@PathVariable Long reservationId, @RequestBody boolean decision) {

        if (!reservationService.ValidateReservation(reservationId)) {//Checks reservation id exists or not
            throw new Exceptions(AllExceptions.RESERVATION_ID_NOT_FOUND);
        }


        reservationService.MakeDecision(reservationId, decision); //Changes the decision according to the owners choice
        return ResponseEntity.ok().body("Decision successfully made");
    }

    @GetMapping("/getAllPropertyReservations")
    public ResponseEntity<List<Reservation>> GetAllPropertyReservations(@RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok().body(reservationService.GetAllPropertyReservations(token));
    }
}
