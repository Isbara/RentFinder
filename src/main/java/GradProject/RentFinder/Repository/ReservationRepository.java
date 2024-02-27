package GradProject.RentFinder.Repository;

import GradProject.RentFinder.Models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
