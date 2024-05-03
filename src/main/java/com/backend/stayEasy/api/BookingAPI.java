package com.backend.stayEasy.api;

import com.backend.stayEasy.convertor.BookingConverter;
import com.backend.stayEasy.dto.BookingDTO;
import com.backend.stayEasy.dto.PaymentDTO;
import com.backend.stayEasy.entity.Mail;
import com.backend.stayEasy.sevice.BookingService;
import com.backend.stayEasy.sevice.PaymentBillService;
import com.backend.stayEasy.sevice.PaypalService;
import com.backend.stayEasy.sevice.impl.IMailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/stayeasy/booking")
public class BookingAPI {
    public static final String SUCCESS_URL = "/paypal/success";
    public static final String CANCEL_URL = "/paypal/cancel";
    @Autowired
    PaypalService service;
    @Autowired
    PaymentBillService paymentService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private IMailService mailService;
    @Autowired
    private BookingConverter bookingConverter;
    @Autowired PaypalService paypalService;
    private UUID bookingId;

    // chi admin moi xem duoc
    @GetMapping(value = "")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BookingDTO>> returnMyActiveBookings() {
        return ResponseEntity.ok().body(bookingService.findAll());
    }

    // get danh sach booking user
    @GetMapping("/traveler/{id}")
    public ResponseEntity<List<BookingDTO>> getBookingById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok().body(bookingService.returnMyBookings(id));
    }
    @GetMapping("/listing/{id}")	
    public ResponseEntity<List<BookingDTO>> returnListingBookings(@PathVariable("id") UUID id) {
        return ResponseEntity.ok().body(bookingService.returnListingBookings(id));
    }

    // huy booking (check ngay truoc checkin 1 ngay  , return payment ,them vao bang paymnet bill , cap nhat trang thai vot  booking)
    // refund payment (lay stk cua user da thanh toan va refund)
    @DeleteMapping("/traveler-reserve/{userId}&{propertyId}")
    public ResponseEntity<String> cancelBooking(@PathVariable("userId") UUID userId, @PathVariable("propertyId") UUID propertyId) {
        bookingService.deleteBooking(userId, propertyId);
        Map<String, String> response = new HashMap<>();
        response.put("message" , "booking đã bị hủy");
        return ResponseEntity.ok((response.toString()));
    }

    // create payment and update booking (check id  user isn't host )
    @PostMapping("/create")
    public ResponseEntity<String> createBooking(@RequestBody BookingDTO bookingParam) throws JsonProcessingException {
        if (bookingService.isRoomAvailable(bookingParam.getPropertyId(), bookingParam.getCheckIn(), bookingParam.getCheckOut())) {
            BookingDTO bookingDTO = bookingService.newBooking(bookingParam);
            // Lưu booking vào cơ sở dữ liệu
            BookingDTO savedBooking = bookingService.createBooking(bookingConverter.toEntity(bookingDTO)); //  phương thức saveBooking
            if (savedBooking != null) {
                bookingId = savedBooking.getBookingId();
                // Chuyển hướng đến PayPal để thanh town
                String approvalUrl = payment(bookingParam);
                System.out.println(approvalUrl);
                Map<String, String> response = new HashMap<>();
                response.put("approvalUrl", approvalUrl);
                return ResponseEntity.ok(new ObjectMapper().writeValueAsString(response));
            } else {
                // Nếu không thể lưu booking vào cơ sở dữ liệu
                return new ResponseEntity<>("Không thể lưu booking vào cơ sở dữ liệu", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("Phòng chưa thể đặt trong thời gian này", HttpStatus.BAD_REQUEST);
        }
    }

    // payment with paypal
    public String payment(BookingDTO bookingDTO) {
        try {
            Payment payment = service.createPayment(bookingDTO.getTotal(), bookingDTO.getCurrency(), bookingDTO.getIntent(), bookingDTO.getMethod(), bookingDTO.getDescription(), "http://localhost:3000/payment" + CANCEL_URL, "http://localhost:3000/payment" + SUCCESS_URL);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    System.out.println("link" + link.getRel());
                    return link.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "";
    }

    // return success payment
    @GetMapping(value = SUCCESS_URL)
    public ResponseEntity<List<PaymentDTO>> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId)  {
        boolean emailSent = false;
        // Kiểm tra trạng thái gửi email từ cache
        try {
            Payment payment = service.executePayment(paymentId, payerId);
            String data = payment.toJSON();
            if ("approved".equals(payment.getState())) {
                // tranh null exception
                bookingService.updateBookingStatus(bookingId, true);
                paymentService.savePayment(payment, bookingId);
                System.out.println(data);
                // Lưu thông tin payment'
                if (!emailSent) {
                    sendEmailBookingSuccess(bookingId);
                    // Cập nhật trạng thái gửi email vào cache
                    emailSent = true;
                }
                return ResponseEntity.ok().body(paymentService.findByPaymentId(paymentId));
            }

        } catch (PayPalRESTException e) {
            if(!emailSent){
                sendEmailPayBooking(bookingId); // Send email notification about the cancellation
                emailSent = true;
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // return when cancel payment
    @GetMapping(value = CANCEL_URL)
    public ResponseEntity<Map<String, Object>> cancelPay()  {
         boolean emailSent = false;
        Map<String, Object> response = new HashMap<>();
        if (!emailSent) {
            // Gửi email nhắc thanh toán chỉ 1 lần
            sendEmailPayBooking(bookingId);
            String message = "Đã hủy thanh toán và gửi email nhắc thanh toán thành công.";
            BookingDTO bookingDTO = bookingService.getBookingById(bookingId);
            String paymentLinkTemplate = "http://localhost:3000/booking/%s?checkin=%s&checkout=%s&numGuest=%d";
            String paymentLink = String.format(paymentLinkTemplate, bookingDTO.getPropertyDTOS().getPropertyId(), bookingDTO.getCheckIn(), bookingDTO.getCheckOut(), bookingDTO.getNumOfGuest());
            response.put("message", message);
            response.put("bookingDTO", bookingDTO);
            response.put("paymentLink", paymentLink);
            emailSent = true;
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // can code in booking service
    private void sendEmailPayBooking(UUID bookingId) {
        BookingDTO bookingDTO = bookingService.getBookingById(bookingId);
        String paymentLinkTemplate = "http://localhost:3000/booking/%s?checkin=%s&checkout=%s&numGuest=%d";
        String paymentLink = String.format(paymentLinkTemplate, bookingDTO.getPropertyDTOS().getPropertyId(), bookingDTO.getCheckIn(), bookingDTO.getCheckOut(), bookingDTO.getNumOfGuest());
        String subject = "Nhắc nhở thanh toán đặt phòng của bạn tại " + bookingDTO.getPropertyName();
        String content = "Payment mail";
        // set new mail
        Mail mail = new Mail();
        mail.setRecipient(bookingDTO.getUserDTOS().getEmail());
        mail.setSubject(subject);
        mail.setContent(content);
        mailService.sendEmailPayment(mail, bookingDTO, paymentLink);
    }
    // can code in booking service
    private void sendEmailBookingSuccess(UUID bookingId) {
        BookingDTO bookingDTO = bookingService.getBookingById(bookingId);
        String subject = "Thanh toán booking thành công cho " + bookingDTO.getPropertyName();
        String content = "Payment mail";
        // set new mail
        Mail mail = new Mail();
        mail.setRecipient(bookingDTO.getUserDTOS().getEmail());
        mail.setSubject(subject);
        mail.setContent(content);
        mailService.sendEmailSuccess(mail, bookingDTO);
    }

    // get transaction detail
    @GetMapping("/lookup-transaction")
    public ResponseEntity<List<PaymentDTO>> LookupTrans(@RequestParam("paymentId") String paymentId) {
        System.out.println("running");
        return ResponseEntity.ok().body(paymentService.findByPaymentId(paymentId));
    }
}






