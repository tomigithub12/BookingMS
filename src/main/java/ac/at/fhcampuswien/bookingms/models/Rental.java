package ac.at.fhcampuswien.bookingms.models;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;



@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document(collection = "Rental")
public class Rental {
    @Id
    private String id;
    private String customerId;
    private String carId;
    private LocalDate startDay;
    private LocalDate endDay;
    private float totalCost;
}



