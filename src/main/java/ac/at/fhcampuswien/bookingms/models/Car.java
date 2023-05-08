package ac.at.fhcampuswien.bookingms.models;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Car {


    private String id;
    private float dailyCost;
    private String brand;
    private String model;
    private String hp;
    private String buildDate;
    private String fuelType;
    private String imageLink;
}
