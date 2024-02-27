package GradProject.RentFinder.Repository;

import GradProject.RentFinder.Models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
}
