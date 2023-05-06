package ac.at.fhcampuswien.bookingms;

import ac.at.fhcampuswien.bookingms.models.Rental;
import ac.at.fhcampuswien.bookingms.repository.RentalRepository;
import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class BookingMsApplication {

    Logger logger = LoggerFactory.getLogger(BookingMsApplication.class);

    @Autowired
    private RentalRepository rentalRepository;
    @PostConstruct
    public void initializeData() throws Exception {

        List<Rental> rentals = Stream.of(
                new Rental("1", "1", "1", LocalDate.of(2023, 01, 01), LocalDate.of(2023, 01, 10), 100),
                new Rental("2", "2", "2", LocalDate.of(2023, 01, 05), LocalDate.of(2023, 01, 15), 100),
                new Rental("3", "3", "3", LocalDate.of(2023, 02, 01), LocalDate.of(2023, 02, 10), 100),
                new Rental("4", "3", "4", LocalDate.of(2023, 06, 02), LocalDate.of(2023, 06, 10), 100)
        ).collect(Collectors.toList());
        rentalRepository.saveAll(rentals);
        logger.warn("Rentals Database Inititalization succesful!");
    }

    public static void main(String[] args) {
        SpringApplication.run(BookingMsApplication.class, args);
    }

}
