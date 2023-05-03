package ac.at.fhcampuswien.bookingms.dtos;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Builder
@Setter
@Getter
public class RentalUpdateRequestDto {


    private String id;

    private String carId;

    private LocalDate startDay;

    private LocalDate endDay;

    private float totalCost;

    private String currentCurrency;



}
