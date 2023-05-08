package ac.at.fhcampuswien.bookingms.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class CustomExchangeRateDto implements Serializable {

    private float value;
    private String currentCurrency;
    private String chosenCurrency;
}
