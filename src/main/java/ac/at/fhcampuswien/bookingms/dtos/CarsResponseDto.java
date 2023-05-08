package ac.at.fhcampuswien.bookingms.dtos;

import ac.at.fhcampuswien.bookingms.models.Car;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarsResponseDto implements Serializable {

    private List<Car> cars;

}
