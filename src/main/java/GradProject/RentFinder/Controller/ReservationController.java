package GradProject.RentFinder.Controller;

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

    @PostMapping("/makeReservation/{id}")
    public ResponseEntity<Reservation> MakeReservation(@RequestHeader(value = "Authorization") String token, @PathVariable Long id, @RequestBody ReservationRequest request){
        return ResponseEntity.ok().body(reservationService.MakeReservation(token, id, request));
    }
    @GetMapping("/getReservation/{id}")
    public ResponseEntity<List<Reservation>> GetReservations(@RequestHeader(value = "Authorization") String token, @PathVariable Long id){
        return ResponseEntity.ok().body(reservationService.GetReservations(token, id));
    }
}
