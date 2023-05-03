package ac.at.fhcampuswien.bookingms.dtos;

import lombok.*;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RentalResponseDto {

    private String eMail;
    private String carId;
    private LocalDate startDay;
    private LocalDate endDay;
    private float totalCost;

}
