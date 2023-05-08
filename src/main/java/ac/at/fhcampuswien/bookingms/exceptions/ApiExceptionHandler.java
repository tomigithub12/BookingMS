package ac.at.fhcampuswien.bookingms.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    private final ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("Europe/Vienna"));

    @ExceptionHandler(CarNotAvailableException.class)
    public ResponseEntity<?> handleCarNotAvailablenError(CarNotAvailableException carNotAvailableException) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                carNotAvailableException.getMessage(),
                httpStatus,
                timestamp
        );
        return new ResponseEntity<>(apiException, httpStatus);
    }


    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<?> handleBookingNotFoundError(BookingNotFoundException bookingNotFoundException) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ApiException apiException = new ApiException(
                bookingNotFoundException.getMessage(),
                httpStatus,
                timestamp
        );
        return new ResponseEntity<>(apiException, httpStatus);
    }
}
