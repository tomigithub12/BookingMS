package ac.at.fhcampuswien.bookingms.repository;

import ac.at.fhcampuswien.bookingms.models.Rental;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface RentalRepository extends MongoRepository<Rental, String> {


    @Query(value = "{ $or: [ " +
            "{ startDay: { $gte: ?0 }, endDay: { $lte: ?1 } }, " +
            "{ startDay: { $lte: ?0 }, endDay: { $gte: ?1 } }, " +
            "{ startDay: { $lte: ?1 }, endDay: { $gte: ?1 } }, " +
            "{ startDay: { $lte: ?0 }, endDay: { $gte: ?0 } } " +
            "], carId: { $exists: true } }", fields = "{ carId : 1 }")
    List<String> findAllAvailableCarsBetweenDates(LocalDate startDate, LocalDate endDate);
}
