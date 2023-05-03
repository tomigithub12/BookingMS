package ac.at.fhcampuswien.bookingms.mapper;


import ac.at.fhcampuswien.bookingms.dtos.RentalRequestDto;
import ac.at.fhcampuswien.bookingms.dtos.RentalResponseDto;
import ac.at.fhcampuswien.bookingms.dtos.RentalUpdateResponseDto;
import ac.at.fhcampuswien.bookingms.models.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public class RentalMapper {

    public Rental BookingRequestToRental(RentalRequestDto rentalBooking, String id){
        return Rental.builder()
                .customerId(id)
                .carId(rentalBooking.getCarId())
                .startDay(rentalBooking.getStartDay())
                .endDay(rentalBooking.getEndDay())
                .totalCost(rentalBooking.getTotalCost())
                .build();
    }

    public RentalResponseDto RentalToBookingResponse(Rental rental, String eMail){
        return RentalResponseDto.builder()
                .eMail(eMail)
                .carId(rental.getCarId())
                .startDay(rental.getStartDay())
                .endDay(rental.getEndDay())
                .totalCost(rental.getTotalCost())
                .build();
    }

    public RentalUpdateResponseDto RentalToUpdateResponse(Rental rentalUpdate) {
        return RentalUpdateResponseDto.builder()
                .id(rentalUpdate.getId())
                .carId(rentalUpdate.getCarId())
                .startDay(rentalUpdate.getStartDay())
                .endDay(rentalUpdate.getEndDay())
                .totalCost(rentalUpdate.getTotalCost())
                .build();
    }
}
