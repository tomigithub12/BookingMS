package ac.at.fhcampuswien.bookingms.controller;

import ac.at.fhcampuswien.bookingms.dtos.*;
import ac.at.fhcampuswien.bookingms.exceptions.BookingNotFoundException;
import ac.at.fhcampuswien.bookingms.exceptions.CarNotAvailableException;
import ac.at.fhcampuswien.bookingms.models.Rental;
import ac.at.fhcampuswien.bookingms.service.RentalRestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "Bookings", description = "Endpoints for managing bookings.")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RentalController {

    @Autowired
    @NotNull
    RentalRestService rentalRestService;

    //TODO
/*    @Autowired
    CarRestService carRestService;*/

    @GetMapping("/allBookings")
    @Operation(
            summary = "Lists all bookings.",
            tags = {"Bookings"},
            responses = {
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Rental.class))),
                    @ApiResponse(description = "Currency Service is not available!", responseCode = "500", content = @Content)
            })
    public ResponseEntity<List<RentalResponseDtoWithCar>> getBookings(@Valid @RequestHeader(value = "Auth") String token,
                                                                      @RequestParam String currentCurrency,
                                                                      HttpServletRequest request) {
        List<Rental> rentals = rentalRestService.getAllBookings(request, currentCurrency);
        List<Long> carIds = rentals.stream().map(Rental::getCarId).toList();
        List<Car> cars = carRestService.getCarsByIds(carIds);
        List<RentalResponseDtoWithCar> rentalResponseDtosWithCar = rentals.stream().map(rental -> {
            Car car = cars.stream().filter(filteredCar -> {
                return Objects.equals(filteredCar.getId(), rental.getCarId());
            })
                    .findFirst().orElseThrow();

            return RentalResponseDtoWithCar
                    .builder()
                    .id(rental.getId())
                    .customerId(rental.getCustomerId())
                    .car(car)
                    .startDay(rental.getStartDay())
                    .endDay(rental.getEndDay())
                    .totalCost(rental.getTotalCost())
                    .build();
        })
                .toList();

        return new ResponseEntity<>(rentalResponseDtosWithCar, HttpStatus.OK);
    }

    @PostMapping("/booking")
    @Operation(
            summary = "Creates a booking in the database.",
            tags = {"Bookings"},
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RentalResponseDto.class))),
                    @ApiResponse(description = "This Car is not available in this time period!", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Currency Service is not available!", responseCode = "500", content = @Content)
            })
    public ResponseEntity<RentalResponseDto> createBooking(@Valid @RequestHeader(value = "Auth") String token,
                                                           @RequestBody RentalRequestDto rentalBooking,
                                                           HttpServletRequest request) throws CarNotAvailableException {
            RentalResponseDto rentalResponseDto = rentalRestService.createBooking(rentalBooking, request);
            return new ResponseEntity<>(rentalResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/booking")
    @Operation(
            summary = "Update a booking in the database.",
            tags = {"Bookings"},
            responses = {
                    @ApiResponse(description = "Created", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RentalResponseDto.class))),
                    @ApiResponse(description = "Booking with this ID does not exist!", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Currency Service is not available!", responseCode = "500", content = @Content)
            })
    public ResponseEntity<RentalUpdateResponseDto> updateBooking(@Valid @RequestHeader(value = "Auth") String token,
                                                                 RentalUpdateRequestDto rentalUpdateRequestDto) throws BookingNotFoundException {
        RentalUpdateResponseDto rentalUpdateResponseDto = rentalRestService.updateBooking(rentalUpdateRequestDto);
        return new ResponseEntity<>(rentalUpdateResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/booking/{bookingId}")
    @Operation(summary = "Delete booking entry from database.", tags = {"Bookings"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeBooking(@Valid @RequestHeader(value = "Auth") String token, @Valid @PathVariable Long bookingId) {
        rentalRestService.removeBooking(bookingId);
    }
}
