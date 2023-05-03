package ac.at.fhcampuswien.bookingms.dtos;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RentalUpdateResponseDto {

    private String id;

    private String carId;

    private LocalDate startDay;

    private LocalDate endDay;

    private float totalCost;

}
