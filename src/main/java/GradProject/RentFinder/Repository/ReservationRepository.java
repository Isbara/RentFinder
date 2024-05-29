package GradProject.RentFinder.Repository;

import GradProject.RentFinder.Models.Reservation;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    //This query needs the addition of decision value based on Boolean or boolean decision.
    @Query(nativeQuery = true, value = "SELECT * FROM RESERVATION_TABLE WHERE USER_ID = :userID AND PROPERTY_ID = :propertyID")
    List<Reservation> findByIDs(@Param("userID") Long userID, @Param("propertyID") Long propertyID);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE RESERVATION_TABLE SET STATUS = :status_decision WHERE ID = :reservationId")
    void makeDecisionForStatus(@Param("reservationId") Long reservationId, @Param("status_decision") Boolean status_decision);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE RESERVATION_TABLE SET APPROVAL = :approval_decision WHERE ID = :reservationId")
    void makeDecisionForApproval(@Param("reservationId") Long reservationId, @Param("approval_decision") Boolean approval_decision);


}
