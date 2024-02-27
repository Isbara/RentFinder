package GradProject.RentFinder.Repository;

import GradProject.RentFinder.Models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
}
