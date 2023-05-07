package ac.at.fhcampuswien.bookingms.rabbitMQ;

import ac.at.fhcampuswien.bookingms.BookingMsApplication;
import ac.at.fhcampuswien.bookingms.config.RabbitMQConfig;
import ac.at.fhcampuswien.bookingms.dtos.AvailableCarsRequestDto;
import ac.at.fhcampuswien.bookingms.repository.RentalRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class RequestListener {

    Logger logger = LoggerFactory.getLogger(RequestListener.class);
    @Autowired
    RentalRepository rentalRepository;

    /*@Async
    @RabbitListener(queues = RabbitMQConfig.BOOKED_CARS_MESSAGE_QUEUE)
    public List<String> onBookedCarsRequest(AvailableCarsRequestDto availableCarsRequestDto) {
        logger.warn("Retrieved request from CarInventoryMS");
        return rentalRepository.findAllAvailableCarsBetweenDates(availableCarsRequestDto.getFrom(), availableCarsRequestDto.getTo());
    }*/

    @Async
    @RabbitListener(queues = RabbitMQConfig.BOOKED_CARS_MESSAGE_QUEUE)
    public CompletableFuture onBookedCarsRequest(AvailableCarsRequestDto availableCarsRequestDto) {
        logger.warn("Retrieved request from CarInventoryMS");
        return CompletableFuture.completedFuture(rentalRepository.findAllAvailableCarsBetweenDates(availableCarsRequestDto.getFrom(), availableCarsRequestDto.getTo()));
    }
}
