package ac.at.fhcampuswien.bookingms.service;



import ac.at.fhcampuswien.bookingms.dtos.RentalUpdateRequestDto;
import ac.at.fhcampuswien.bookingms.dtos.RentalUpdateResponseDto;
import ac.at.fhcampuswien.bookingms.exceptions.BookingNotFoundException;
import ac.at.fhcampuswien.bookingms.models.Rental;
import ac.at.fhcampuswien.bookingms.repository.RentalRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RentalEntityService extends Throwable {

    @Autowired
    RentalRepository rentalRepository;

    @Autowired
    RentalMapper rentalMapper;

    @Autowired
    CustomerRepository customerRepository;

    public RentalResponseDto createBooking(RentalRequestDto rentalRequestDto, String eMail) {
        Long id = customerRepository.findIdByeMailIgnoreCase(eMail);
        Rental rentalBooking = rentalMapper.BookingRequestToRental(rentalRequestDto, id);
        Rental savedRental = rentalRepository.save(rentalBooking);

        return rentalMapper.RentalToBookingResponse(savedRental, eMail);
    }

    public List<Rental> getAllRentals(String eMail) {
        List<Rental> rentals;
        String id = customerRepository.findIdByeMailIgnoreCase(eMail);
        rentals = rentalRepository.findByCustomerId(id);
        return rentals;
    }

    public void deleteBooking(Long id) {
        rentalRepository.deleteById(id);
    }

    public RentalUpdateResponseDto updateBooking(RentalUpdateRequestDto rentalUpdateRequestDto) throws BookingNotFoundException {
            Rental rentalUpdate = rentalRepository.updateRental(rentalUpdateRequestDto.getCarId(), rentalUpdateRequestDto.getStartDay(),
                    rentalUpdateRequestDto.getEndDay(), rentalUpdateRequestDto.getTotalCost(), rentalUpdateRequestDto.getId());
            if(rentalUpdate == null) {
                throw new BookingNotFoundException("Booking with this ID does not exist!");
            }
            return rentalMapper.RentalToUpdateResponse(rentalUpdate);
    }

}
