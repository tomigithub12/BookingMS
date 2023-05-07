package ac.at.fhcampuswien.bookingms;

import ac.at.fhcampuswien.bookingms.config.RabbitMQConfig;
import ac.at.fhcampuswien.bookingms.dtos.CarsResponseDto;
import ac.at.fhcampuswien.bookingms.dtos.RentalRequestDto;
import ac.at.fhcampuswien.bookingms.dtos.RentalResponseDtoWithCar;
import ac.at.fhcampuswien.bookingms.models.Car;
import ac.at.fhcampuswien.bookingms.models.Rental;
import ac.at.fhcampuswien.bookingms.repository.RentalRepository;
import ac.at.fhcampuswien.bookingms.service.RentalRestService;
import jakarta.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.EnableAsync;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@EnableAsync
@SpringBootApplication
public class BookingMsApplication {

    Logger logger = LoggerFactory.getLogger(BookingMsApplication.class);

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    RentalRestService rentalRestService;


    @Autowired
    RabbitTemplate rabbitTemplate;
    @PostConstruct
    public void initializeData() throws Exception {

        List<Rental> rentals = Stream.of(
                new Rental("1", "1", "1", LocalDate.of(2023, 01, 01), LocalDate.of(2023, 01, 10), 100),
                new Rental("2", "2", "2", LocalDate.of(2023, 01, 05), LocalDate.of(2023, 01, 15), 100),
                new Rental("3", "3", "3", LocalDate.of(2023, 02, 01), LocalDate.of(2023, 02, 10), 100),
                new Rental("4", "6456ef9d74e96531586bd01d", "4", LocalDate.of(2023, 06, 02), LocalDate.of(2023, 06, 10), 100)
        ).collect(Collectors.toList());
        rentalRepository.saveAll(rentals);
        logger.warn("Rentals Database Initialization successful!");

        RentalRequestDto rentalRequestDto = new RentalRequestDto("4", LocalDate.of(2023,01,01), LocalDate.of(2023,01,10), 5f, "EUR");
        RentalRequestDto rentalRequestDto1 = new RentalRequestDto("1", LocalDate.of(2023, 01, 01), LocalDate.of(2023, 01, 10), 2f, "TRY");
        RentalRequestDto rentalRequestDto2 = new RentalRequestDto("2", LocalDate.of(2023, 01, 05), LocalDate.of(2023, 01, 15), 1f, "USD");
        rentalRestService.createBooking(rentalRequestDto, "test@gmail.com");
        rentalRestService.createBooking(rentalRequestDto1, "test@gmail.com");
        rentalRestService.createBooking(rentalRequestDto2, "test@gmail.com");

        List<Rental> rentalList = rentalRestService.getAllBookings("test@gmail.com", "AUD");
        rentalRestService.removeBooking(rentalList.get(1).getId());
    }

    public static void main(String[] args) {
        SpringApplication.run(BookingMsApplication.class, args);
    }

}
