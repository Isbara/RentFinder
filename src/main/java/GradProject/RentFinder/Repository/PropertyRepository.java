package GradProject.RentFinder.Repository;

import GradProject.RentFinder.Models.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property,Long> {
}
