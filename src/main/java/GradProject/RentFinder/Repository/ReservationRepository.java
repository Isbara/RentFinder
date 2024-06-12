package GradProject.RentFinder.Repository;

import GradProject.RentFinder.Models.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    //This query needs the addition of decision value based on Boolean or boolean decision.
    @Query(nativeQuery = true, value = "SELECT * FROM RESERVATION_TABLE WHERE USER_ID = :userID AND PROPERTY_ID = :propertyID")
    List<Reservation> findByIDs(@Param("userID") Long userID, @Param("propertyID") Long propertyID);
    @Query(nativeQuery = true, value = "SELECT * FROM RESERVATION_TABLE WHERE PROPERTY_ID IN (SELECT ID FROM PROPERTY_TABLE WHERE USER_ID = :userID)")
    List<Reservation> findByPropertyUserID(@Param("userID") Long userID);
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE RESERVATION_TABLE SET STATUS = :status_decision WHERE ID = :reservationId")
    void makeDecisionForStatus(@Param("reservationId") Long reservationId, @Param("status_decision") Boolean status_decision);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE RESERVATION_TABLE SET APPROVAL = :approval_decision WHERE ID = :reservationId")
    void makeDecisionForApproval(@Param("reservationId") Long reservationId, @Param("approval_decision") Boolean approval_decision);

    @Query(nativeQuery = true, value = "SELECT * FROM RESERVATION_TABLE WHERE PROPERTY_ID = :propertyID AND APPROVAL = TRUE AND (START_DATE <= :endDate AND END_DATE >= :startDate)")
    List<Reservation> findOverlappingReservations(@Param("propertyID") Long propertyID, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
    @Query(nativeQuery = true, value = "SELECT * FROM RESERVATION_TABLE WHERE USER_ID = :userID AND STATUS = TRUE AND (START_DATE <= :bufferEndDate AND END_DATE >= :bufferStartDate)")
    List<Reservation> findUserReservationsInBufferPeriod(@Param("userID") Long userID, @Param("bufferStartDate") Date bufferStartDate, @Param("bufferEndDate") Date bufferEndDate);
    @Query(nativeQuery = true, value = "SELECT * FROM RESERVATION_TABLE WHERE PROPERTY_ID = :propertyID AND STATUS IS NULL AND (START_DATE <= :endDate AND END_DATE >= :startDate)")
    List<Reservation> findOverlappingUnapprovedReservations(@Param("propertyID") Long propertyID, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "DELETE FROM RESERVATION_TABLE WHERE ID IN :reservationIds AND APPROVAL = FALSE")
    void deleteReservationsByIds(@Param("reservationIds") List<Long> reservationIds);
}
