package ac.at.fhcampuswien.bookingms.service;

import ac.at.fhcampuswien.bookingms.config.RabbitMQConfig;
import ac.at.fhcampuswien.bookingms.dtos.*;
import ac.at.fhcampuswien.bookingms.exceptions.BookingNotFoundException;
import ac.at.fhcampuswien.bookingms.exceptions.CarNotAvailableException;
import ac.at.fhcampuswien.bookingms.models.Car;
import ac.at.fhcampuswien.bookingms.models.Rental;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalRestService {

    //TODO

/*    @NonNull
    CustomerEntityService customerEntityService;
    @NonNull
    CarEntityService carEntityService;
    @NonNull
    JwtService jwtService;
    @NonNull
    CurrencySOAPService currencySOAPService;
    */

    @Autowired
    RentalEntityService rentalEntityService;

    @Autowired
    RabbitTemplate rabbitTemplate;



    private static final DecimalFormat df = new DecimalFormat("#.##");

    public List<Rental> getAllBookings(String userEmail, String currentCurrency) {
        ConversionRequestDto conversionRequestDto = new ConversionRequestDto("USD", currentCurrency);
        String eMail = userEmail;
        List<Rental> allRentals = new ArrayList<>();
        List<Rental> allRentalsFromDB = rentalEntityService.getAllRentals(eMail);

        double exchangeRate = (double) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.CARS_EXCHANGE, RabbitMQConfig.EXCHANGERATE_MESSAGE_QUEUE, conversionRequestDto);
        float convertedExchangeRate = (float) exchangeRate;

        for (Rental r : allRentalsFromDB) {
            float totalCostConverted =  r.getTotalCost() * convertedExchangeRate;
            float totalCostFormatted = formatCosts(totalCostConverted);
            r.setTotalCost(totalCostFormatted);
            allRentals.add(r);
        }
        return allRentals;
    }

    public RentalResponseDto createBooking(RentalRequestDto rentalBooking, String userEmail) throws CarNotAvailableException {

        String eMail = userEmail;

        if (isCarAvailable(rentalBooking)) {
            String currentCurrency = rentalBooking.getCurrentCurrency();
            if (!currentCurrency.equals("USD")) {
                CustomExchangeRateDto customExchangeRateDto = new CustomExchangeRateDto(rentalBooking.getTotalCost(), currentCurrency, "USD");
                double totalCostInUSDResponse = (double) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.CARS_EXCHANGE, RabbitMQConfig.CUSTOM_EXCHANGERATE_MESSAGE_QUEUE, customExchangeRateDto);
                float totalCostInUSD = (float) totalCostInUSDResponse;
                float totalCostFormatted = formatCosts(totalCostInUSD);
                rentalBooking.setTotalCost(totalCostFormatted);
            }
            RentalResponseDto responseDto = rentalEntityService.createBooking(rentalBooking, eMail);
            CustomExchangeRateDto customExchangeRateResponseDto = new CustomExchangeRateDto(responseDto.getTotalCost(), "USD", currentCurrency);
            double totalCostConvertedResponse = (double) rabbitTemplate.convertSendAndReceive(RabbitMQConfig.CARS_EXCHANGE, RabbitMQConfig.CUSTOM_EXCHANGERATE_MESSAGE_QUEUE, customExchangeRateResponseDto);
            float totalCostConverted = (float) totalCostConvertedResponse;
            float totalCostFormatted = formatCosts(totalCostConverted);
            responseDto.setTotalCost(totalCostFormatted);
            return responseDto;
        } else {
            throw new CarNotAvailableException("This Car is not available in this time period!");
        }
    }
/*
    public RentalUpdateResponseDto updateBooking(RentalUpdateRequestDto rentalUpdateRequestDto) throws CurrencyServiceNotAvailableException, BookingNotFoundException {

        String currentCurrency = rentalUpdateRequestDto.getCurrentCurrency();
        if (!currentCurrency.equals("USD")) {
            GetConvertedValue getConvertedValue = new GetConvertedValue(rentalUpdateRequestDto.getTotalCost(), currentCurrency, "USD");
            float totalCostInUSD = currencySOAPService.getConvertedValue(getConvertedValue).floatValue();
            float totalCostFormatted = formatCosts(totalCostInUSD);
            rentalUpdateRequestDto.setTotalCost(totalCostFormatted);
        }
        RentalUpdateResponseDto responseDto = rentalEntityService.updateBooking(rentalUpdateRequestDto);
        GetConvertedValue getConvertedValue = new GetConvertedValue(responseDto.getTotalCost(), "USD", currentCurrency);
        float totalCostConverted = currencySOAPService.getConvertedValue(getConvertedValue).floatValue();
        float totalCostFormatted = formatCosts(totalCostConverted);
        responseDto.setTotalCost(totalCostFormatted);
        return responseDto;

    }*/

    public void removeBooking(String id) {
        rentalEntityService.deleteBooking(id);
    }

    private float formatCosts(float costs) {
        String formatTotal = df.format(costs);
        String replacedTotal = formatTotal.replace(",", ".");
        return Float.parseFloat(replacedTotal);
    }

    public boolean isCarAvailable(RentalRequestDto rentalBooking) {
        AvailableCarsRequestDto availableCarsRequestDto = new AvailableCarsRequestDto(rentalBooking.getStartDay(), rentalBooking.getEndDay());
        CarsResponseDto carsResponseDto = rabbitTemplate.convertSendAndReceiveAsType(RabbitMQConfig.CARS_EXCHANGE, RabbitMQConfig.GET_FREE_CARS_MESSAGE_QUEUE, availableCarsRequestDto, new ParameterizedTypeReference<>() {});
        //carEntityService.getFreeCarsBetweenDates(rentalBooking.getStartDay(), rentalBooking.getEndDay());
        List<Car> bookedCars = carsResponseDto.getCars();

        for (Car c : bookedCars) {
            if (c.getId().equals(rentalBooking.getCarId())) {
                return true;
            }
        }
        return false;
    }

    /*
    public boolean isCarAlreadyBookedForUpdate(RentalUpdateRequestDto rentalUpdateRequestDto) throws CarNotAvailableException {
        List<Car> bookedCars = carEntityService.getFreeCarsBetweenDates(rentalUpdateRequestDto.getStartDay(), rentalUpdateRequestDto.getEndDay());
        for (Car c : bookedCars) {
            if (c.getId().equals(rentalUpdateRequestDto.getCarId())) {
                return true;
            }
        }
        return false;
    }*/
}
