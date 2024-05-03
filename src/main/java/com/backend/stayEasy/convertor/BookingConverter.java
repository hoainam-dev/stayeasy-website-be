package com.backend.stayEasy.convertor;

import com.backend.stayEasy.dto.BookingDTO;

import com.backend.stayEasy.entity.Booking;
import com.backend.stayEasy.enums.Confirmation;
import com.backend.stayEasy.repository.IPropertyRepository;
import com.backend.stayEasy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookingConverter {
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private PropertyConverter propertyConverter;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IPropertyRepository propertyRepository;

    public BookingDTO toDTO(Booking booking) {
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setBookingId(booking.getBookingId());
        bookingDTO.setPropertyId(booking.getProperty().getPropertyId());
        bookingDTO.setUserId(booking.getUser().getId());
        bookingDTO.setDateBooking(booking.getDateBooking());
        bookingDTO.setCheckIn(booking.getCheckIn());
        bookingDTO.setCheckOut(booking.getCheckOut());
        bookingDTO.setUserDTOS(userConverter.toDTO(booking.getUser()));
        bookingDTO.setPropertyDTOS(propertyConverter.toDTO(booking.getProperty()));
        bookingDTO.setPropertyName(booking.getProperty().getPropertyName());
        bookingDTO.setNumberNight(booking.getNumNight());
        bookingDTO.setNumOfGuest(booking.getNumGuest());
        bookingDTO.setTotal(booking.getTotalPrice());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setConfirmation(booking.getConfirmation().name());
//        bookingDTO.setPropertyName(listingServiceStatic.findById(booking.getProperty().getId()).getPropertyName());
        return  bookingDTO;
    }
    public Booking toEntity(BookingDTO bookingDto){
        Booking booking = new Booking();
        booking.setBookingId(bookingDto.getBookingId());
        booking.setDateBooking(bookingDto.getDateBooking());
        booking.setNumNight(bookingDto.getNumberNight());
        booking.setCheckIn(bookingDto.getCheckIn());
        booking.setCheckOut(bookingDto.getCheckOut());
        booking.setTotalPrice(bookingDto.getTotal());
        booking.setNumGuest(bookingDto.getNumOfGuest());
        booking.setProperty(propertyRepository.findById(bookingDto.getPropertyId()).get());
        booking.setUser(userRepository.findById(bookingDto.getUserId()).get());
        booking.setStatus(bookingDto.getStatus());
        booking.setConfirmation(Confirmation.valueOf(bookingDto.getConfirmation()));
        return  booking;
    }
}



