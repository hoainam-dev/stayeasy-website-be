package com.backend.stayEasy.sevice;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.stayEasy.convertor.BookingConverter;
import com.backend.stayEasy.dto.BookingDTO;
import com.backend.stayEasy.dto.PropertyDTO;
import com.backend.stayEasy.entity.Booking;
import com.backend.stayEasy.enums.Confirmation;
import com.backend.stayEasy.repository.BookingRepository;

@Service
public class BookingService {

	@Autowired
	PropertyService propertyService;

	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private BookingConverter bookingConverter;



	public BookingDTO getBookingById(UUID id) {
		Booking booking;
		booking = bookingRepository.findById(id).get();
		return bookingConverter.toDTO(booking);
	}

	public Booking findById(UUID id) {
		Booking booking;
		booking = bookingRepository.findById(id).get();
		return booking;
	}

	public List<BookingDTO> findAll() {
		return bookingRepository.findAll().stream().map(bookingConverter::toDTO).collect(Collectors.toList());
	}


	public List<UUID> updateBookingStatusWithSchedule() throws IOException {
		// Lấy danh sách các booking
		List<Booking> bookings = bookingRepository.findAll().stream()
				// loc danh sach
				.filter(Booking -> Booking.getStatus() && Booking.getConfirmation() != Confirmation.PENDING
						&& Booking.getConfirmation() != Confirmation.REJECTED)
				.toList();
		// Lặp qua từng booking để kiểm tra và cập nhật trạng thái
		LocalDate currentDate = LocalDate.now();
		System.out.println(currentDate);
		List<UUID> updateBooking = new ArrayList<>();
		for (Booking booking : bookings) {
			// neu trang thai khong phaoi la PENDING // REJECTED
			LocalDate checkInDate = booking.getCheckIn().toLocalDate(); // Chuyển đổi Date thành LocalDate
			LocalDate checkOutDate = booking.getCheckOut().toLocalDate(); // Chuyển đổi Date thành LocalDate
//            System.out.println(checkInDate);
//            // So sánh ngày check-in và ngày checkout với ngày hiện tại
//            System.out.println("confirm " +  booking.getConfirmation().equals(Confirmation.CONFIRMED));
//            System.out.println("checkin " +  checkInDate.equals(currentDate));
//            System.out.println("checkout " +  currentDate.isBefore(checkOutDate));
			if (booking.getConfirmation() == Confirmation.CONFIRMED && checkInDate.equals(currentDate)
					&& currentDate.isBefore(checkOutDate)) {
				// Nếu ngày hiện tại nằm trong khoảng từ ngày check-in đến ngày checkout
				// lấy danh sách CONFIRMED
				// Cập nhật trạng thái của booking thành "Đang diễn ra"
				booking.setConfirmation(Confirmation.IN_PROGRESS);
				System.out.println("Đã chạy");
			} else if (booking.getConfirmation() == Confirmation.IN_PROGRESS && checkOutDate.equals(currentDate)) {
				// Nếu ngày hiện tại sau ngày checkout
				// danh sach IN_PROGRESS
				// Cập nhật trạng thái của booking thành "Hoàn thành"
				booking.setConfirmation(Confirmation.COMPLETED);
				updateBooking.add(booking.getBookingId());
			}
			bookingRepository.save(booking);
		}
		return updateBooking;
	}

	public List<BookingDTO> returnMyBookings(UUID userId) {
		return bookingRepository.findAllByUser_IdOrderByDateBookingDesc(userId).stream().map(bookingConverter::toDTO)
				.collect(Collectors.toList());
	}

	public List<BookingDTO> returnListingBookings(UUID id) {
        return bookingRepository.findAllByPropertyPropertyIdOrderByDateBookingDesc(id).stream()
                .filter(Booking::getStatus)
                .map(bookingConverter::toDTO)
                .collect(Collectors.toList());
	}

	public void updateBookingStatus(UUID bookingId, boolean status) {
		Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
		if (!bookingOptional.isPresent()) {
			// Handle booking not found (e.g., throw an exception, log an error, etc.)
			throw new RuntimeException("Booking with ID " + bookingId + " not found.");
		}
		Booking booking = bookingOptional.get();
		if (booking.getStatus() != status) {
			booking.setStatus(status);
			booking.setConfirmation(Confirmation.valueOf(Confirmation.PENDING.name()));
			bookingRepository.save(booking);
			// Send notification (optional)
			// send Email
			// ...
		}
	}


	public void updateBookingCancel(UUID bookingId, boolean cancelBooking, boolean status) {
		Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
		if (bookingOptional.isEmpty()) {
			// Handle booking not found (e.g., throw an exception, log an error, etc.)
			throw new RuntimeException("Booking with ID " + bookingId + " not found.");
		}
		Booking booking = bookingOptional.get();
		booking.setCancel(cancelBooking); // true
		booking.setStatus(status); // false
		booking.setConfirmation(Confirmation.valueOf(Confirmation.REJECTED.name())); // Rejected
		bookingRepository.save(booking);
		// Send notification (optional)
		// send Email
		// ...
	}
	// create booking
	public BookingDTO createBooking(Booking booking) {
		booking = bookingRepository.save(booking);
		System.out.println("Booking added");
		return bookingConverter.toDTO(booking);
	}

	public BookingDTO newBooking(BookingDTO bookingDTO) {
		BookingDTO bookingDto = new BookingDTO();
		bookingDto.setPropertyId(bookingDTO.getPropertyId());
		bookingDto.setUserId(bookingDTO.getUserId());
		bookingDto.setTotal(bookingDTO.getPrice());
		bookingDto.setNumOfGuest(bookingDTO.getNumOfGuest());
		bookingDto.setDateBooking(Date.valueOf(java.time.LocalDate.now()));
		bookingDto.setCheckIn(bookingDTO.getCheckIn());
		bookingDto.setCheckOut(bookingDTO.getCheckOut());
		bookingDto.setNumberNight(bookingDTO.getNumberNight());
		bookingDto.setStatus(false);
		bookingDto.setConfirmation(Confirmation.RESERVE.name());
		System.out.println(Confirmation.RESERVE.name());
		return bookingDto;
	}

	public void deleteBooking(UUID userId, UUID propertyId) {
		// set du lieu lai thanh false khong xoa khoi database
		bookingRepository.deleteByUserIdAndPropertyIdAndStatusIsFalse(userId, propertyId);
	}

	public boolean isRoomAvailable(UUID id, Date checkInDate, Date checkOutDate) {
		// Logic to check room availability based on check-in and check-out dates
		List<Booking> conflictingBookings = bookingRepository.findConflictingBookings(id, checkInDate, checkOutDate);
		return conflictingBookings.isEmpty();
	}

	public void deleteBooking(UUID bookingId) {
		// set du lieu lai thanh false khong xoa khoi database
		Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
		if (bookingOptional.isPresent()) {
			Booking booking = bookingOptional.get();
			booking.setCancel(false);
			booking.setConfirmation(Confirmation.REJECTED);
		} else {
			throw new RuntimeException("Booking with ID " + bookingId + " not found.");
		}
	}

	// Service for host
	public List<BookingDTO> returnAllBookingOfHost(UUID id, String filter) {
		Confirmation confirmation = Enum.valueOf(Confirmation.class, filter);
		if (filter.isEmpty()) {
			confirmation = Confirmation.PENDING;
		}
		// Lấy danh sách các properties của host
		List<PropertyDTO> properties = propertyService.findAllPropertiesByHostId(id);
		// Lấy ra các propertyId của các properties
		List<UUID> propertyIds = properties.stream().map(PropertyDTO::getPropertyId).toList();

		// loc và trả về danh sách booking
		List<BookingDTO> bookings = new ArrayList<>();
		for (UUID propertyId : propertyIds) {
			List<Booking> propertyBookings = bookingRepository
					.findAllByPropertyPropertyIdAndConfirmationOrderByDateBookingDesc(propertyId, confirmation);
			List<BookingDTO> propertyBookingDTOs = propertyBookings.stream().map(bookingConverter::toDTO).toList();
			bookings.addAll(propertyBookingDTOs);
		}
		// result after filter
		return bookings;
	}

	public void updateConfirmBooking(UUID bookingId, String status) {
		Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
		if (bookingOptional.isEmpty()) {
			throw new RuntimeException("Booking with ID " + bookingId + " not found.");
		}
		try {
			Booking booking = bookingOptional.get();
			Confirmation confirmation = Enum.valueOf(Confirmation.class, status);
			if (!booking.getConfirmation().equals(confirmation)) {
				booking.setConfirmation(confirmation);
				bookingRepository.save(booking);
			}
		} catch (IllegalArgumentException e) {
			// Handle invalid status value (e.g., throw an exception, log an error, etc.)
			throw new IllegalArgumentException("Invalid status value: " + status);
		}
	}

}
