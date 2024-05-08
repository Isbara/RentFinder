package GradProject.RentFinder.Repository;

import GradProject.RentFinder.Models.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property,Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM PROPERTY_TABLE WHERE USER_ID = :ownerID")
    List<Property> findByOwnerID(@Param("ownerID") Long ownerID);
    @Modifying
    @Query(nativeQuery = true, value="SELECT * FROM PROPERTY_TABLE ORDER BY ID DESC")
    List<Property> findAll();
}
