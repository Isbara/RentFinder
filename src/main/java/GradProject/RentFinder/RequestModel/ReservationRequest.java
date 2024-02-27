package GradProject.RentFinder.RequestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationRequest {
    private int numberOfPeople;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
}
