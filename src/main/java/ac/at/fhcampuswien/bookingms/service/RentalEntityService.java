package ac.at.fhcampuswien.bookingms.service;



import ac.at.fhcampuswien.bookingms.dtos.RentalRequestDto;
import ac.at.fhcampuswien.bookingms.dtos.RentalResponseDto;
import ac.at.fhcampuswien.bookingms.dtos.RentalUpdateRequestDto;
import ac.at.fhcampuswien.bookingms.dtos.RentalUpdateResponseDto;
import ac.at.fhcampuswien.bookingms.exceptions.BookingNotFoundException;
import ac.at.fhcampuswien.bookingms.mapper.RentalMapper;
import ac.at.fhcampuswien.bookingms.models.Rental;
import ac.at.fhcampuswien.bookingms.rabbitMQ.RequestListener;
import ac.at.fhcampuswien.bookingms.repository.RentalRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ac.at.fhcampuswien.bookingms.config.RabbitMQConfig;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RentalEntityService extends Throwable {

    Logger logger = LoggerFactory.getLogger(RentalEntityService.class);

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RentalMapper rentalMapper;



    public RentalResponseDto createBooking(RentalRequestDto rentalRequestDto, String eMail) {
        String idResponse = (String) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.CUSTOMER_EXCHANGE, RabbitMQConfig.CUSTOMERID_MESSAGE_QUEUE, eMail);
        String extractedId = extractIdFromJsonObject(idResponse);
        Rental rentalBooking = rentalMapper.BookingRequestToRental(rentalRequestDto, extractedId);
        Rental savedRental = rentalRepository.save(rentalBooking);

        return rentalMapper.RentalToBookingResponse(savedRental, eMail);
    }

    public List<Rental> getAllRentals(String eMail) {
        List<Rental> rentals;
        String idResponse = (String) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.CUSTOMER_EXCHANGE, RabbitMQConfig.CUSTOMERID_MESSAGE_QUEUE,eMail);
        String extractedId = extractIdFromJsonObject(idResponse);
        rentals = rentalRepository.findByCustomerId(extractedId);
        return rentals;
    }

    public void deleteBooking(String id) {
        rentalRepository.deleteById(id);
        logger.warn("Rental with id " + id + " was deleted");
    }

 /*   public RentalUpdateResponseDto updateBooking(RentalUpdateRequestDto rentalUpdateRequestDto) throws BookingNotFoundException {
        Rental rentalUpdate = rentalRepository.updateRental(rentalUpdateRequestDto.getCarId(), rentalUpdateRequestDto.getStartDay(),
                rentalUpdateRequestDto.getEndDay(), rentalUpdateRequestDto.getTotalCost(), rentalUpdateRequestDto.getId());
        if(rentalUpdate == null) {
            throw new BookingNotFoundException("Booking with this ID does not exist!");
        }
        return rentalMapper.RentalToUpdateResponse(rentalUpdate);
    }*/

    public List<String> getBookedCarIds(LocalDate startDate, LocalDate endDate) {
        return rentalRepository.findAllAvailableCarsBetweenDates(startDate, endDate);
    }

    private String extractIdFromJsonObject(String jsonString){
        JSONObject jsonObject = new JSONObject(jsonString);

        String oidValue = jsonObject.getJSONObject("_id").getString("$oid");

        return oidValue;
    }
}
