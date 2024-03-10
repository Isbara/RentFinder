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

    @PostMapping("/makeReservation/{propertyId}")//User makes reservation for one specific property
    public ResponseEntity<Reservation> MakeReservation(@RequestHeader(value = "Authorization") String token, @PathVariable Long propertyId, @RequestBody ReservationRequest request) {
        if(!reservationService.ValidateProperty(propertyId))
        {
            throw new Exceptions(AllExceptions.PROPERTY_ID_NOT_FOUND);
        }
        return ResponseEntity.ok().body(reservationService.MakeReservation(token, propertyId, request));
    }

    @GetMapping("/getReservation/{propertyId}") //Returns user's reservations on the viewed property.
    public ResponseEntity<List<Reservation>> GetReservations(@RequestHeader(value = "Authorization") String token, @PathVariable Long propertyId) {
        if(!reservationService.ValidateProperty(propertyId))
        {
            throw new Exceptions(AllExceptions.PROPERTY_ID_NOT_FOUND);
        }
        return ResponseEntity.ok().body(reservationService.GetReservations(token, propertyId));
    }

    @PostMapping("/status/{reservationId}") // Property owner decides whether the user stayed in the property or not, true=stayed false=not
    public ResponseEntity<String> makeStatusDecisionForReservation(@PathVariable Long reservationId, @RequestBody boolean status_decision) {

        if (!reservationService.ValidateReservation(reservationId)) {//Checks reservation id exists or not
            throw new Exceptions(AllExceptions.RESERVATION_ID_NOT_FOUND);
        }


        reservationService.MakeDecisionForStatus(reservationId, status_decision); //Changes the decision according to the owners choice
        return ResponseEntity.ok().body("Decision successfully made");
    }

    @GetMapping("/getAllPropertyReservations") // Returns owners all property reservations
    public ResponseEntity<List<Reservation>> GetAllPropertyReservations(@RequestHeader(value = "Authorization") String token) {
        return ResponseEntity.ok().body(reservationService.GetAllPropertyReservations(token));
    }

    @PostMapping("/approval/{reservationId}") // Property owner decides whether the user stayed in the property or not, true=stayed false=not
    public ResponseEntity<String> makeApprovalDecisionForReservation(@PathVariable Long reservationId, @RequestBody boolean approval_decision) {

        if (!reservationService.ValidateReservation(reservationId)) {//Checks reservation id exists or not
            throw new Exceptions(AllExceptions.RESERVATION_ID_NOT_FOUND);
        }


        reservationService.MakeDecisionForApproval(reservationId, approval_decision); //Changes the decision according to the owners choice
        return ResponseEntity.ok().body("Decision successfully made");
    }






}
