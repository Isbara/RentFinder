package GradProject.RentFinder.Controller;

import GradProject.RentFinder.Models.Ticket;
import GradProject.RentFinder.RequestModel.TicketRequest;
import GradProject.RentFinder.Service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;
    @PostMapping()
    public ResponseEntity<Ticket> CreateTicket(@RequestHeader(value = "Authorization") String token, @RequestBody TicketRequest request){
        return ResponseEntity.ok().body(ticketService.CreateTicket(token, request));
    }
}
