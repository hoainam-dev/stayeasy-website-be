package com.backend.stayEasy.repository;

import com.backend.stayEasy.dto.DailyRevenueDTO;
import com.backend.stayEasy.entity.Booking;
import com.backend.stayEasy.enums.Confirmation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findAllByUser_IdOrderByDateBookingDesc(UUID id);
    List<Booking> findAllByPropertyPropertyIdOrderByDateBookingDesc(UUID property_propertyId);
    @Query("SELECT b FROM Booking b " +
            "WHERE b.property.propertyId = :propertyID AND b.status = true AND (b.checkIn<= :checkOutDate) AND (b.checkOut >= :checkInDate)")
    List<Booking> findConflictingBookings(@Param("propertyID") UUID propertyID, @Param("checkInDate") Date checkInDate,
                                          @Param("checkOutDate") Date checkOutDate);
    
    
    @Query("SELECT SUM(b.totalPrice) FROM Booking b WHERE b.dateBooking BETWEEN :startDate AND :endDate AND b.cancel IS NULL")
    Double getTotalRevenueBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    
    @Query("SELECT SUM(b.totalPrice) FROM Booking b "
    		+ "WHERE b.property.propertyId = :propertyId "
    		+ "AND b.dateBooking BETWEEN :startDate "
    		+ "AND :endDate "
    		+ "AND b.cancel IS NULL")
    Double getTotalRevenueBetweenAndByPropertyId(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("propertyId") UUID propertyId);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.dateBooking BETWEEN :startDate AND :endDate")
    long countBookingsBetween(Date startDate, Date endDate);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE "
    		+ "b.property.propertyId = :propertyId AND "
    		+ " b.dateBooking BETWEEN :startDate AND :endDate "
    		+ "AND b.cancel IS NULL ")
    long countBookingsBetweenAndByPropertyId(Date startDate, Date endDate, UUID propertyId);
    
    @Query("SELECT NEW com.backend.stayEasy.dto.DailyRevenueDTO(b.dateBooking, COALESCE(SUM(b.totalPrice), 0.0)) " +
    	       "FROM Booking b " +
    	       "WHERE FUNCTION('MONTH', b.dateBooking) = FUNCTION('MONTH', :date) AND FUNCTION('YEAR', b.dateBooking) = FUNCTION('YEAR', :date) AND b.cancel IS NULL " +
    	       "GROUP BY b.dateBooking " +
    	       "ORDER BY b.dateBooking")
    List<DailyRevenueDTO> findDailyRevenueByMonthAndYear(@Param("date") Date date);

    
    @Query("SELECT b.dateBooking, COUNT(b), SUM(CASE WHEN b.cancel = true THEN 1 ELSE 0 END) FROM Booking b WHERE b.dateBooking BETWEEN ?1 AND ?2 GROUP BY b.dateBooking")
    List<Object[]> countBookingAndCancelByDate(Date startDate, Date endDate);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.cancel IS NOT NULL AND b.dateBooking BETWEEN :startDate AND :endDate")
    long countBookingWithCancelNotNull(Date startDate, Date endDate);

    @Query("SELECT COUNT(b) FROM Booking b WHERE"
    		+ " b.property.propertyId = :propertyId "
    		+ "AND b.cancel IS NOT NULL "
    		+ "AND b.dateBooking BETWEEN :startDate AND :endDate")
    long countBookingWithCancelNotNullByPropertyId(Date startDate, Date endDate, UUID propertyId);

    
    @Query("SELECT b FROM Booking b WHERE b.property.propertyId = :propertyId AND b.dateBooking >= :firstDateOfCurrentMonth AND b.cancel IS NULL")
    List<Booking> findAllByPropertyIdAndDateBookingAfterAndCancelIsNull(@Param("propertyId") UUID propertyId, @Param("firstDateOfCurrentMonth") Date firstDateOfCurrentMonth);
    
    List<Booking> findAllByPropertyPropertyIdAndConfirmationOrderByDateBookingDesc(UUID property_propertyId, Confirmation confirmation);

    @Transactional
    @Modifying
    @Query("DELETE FROM Booking b WHERE b.user.id = :userId AND b.property.propertyId = :propertyId AND b.status = false")
    void deleteByUserIdAndPropertyIdAndStatusIsFalse(UUID userId, UUID propertyId);
}
