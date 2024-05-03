package com.backend.stayEasy.api;

import com.backend.stayEasy.dto.BookingDTO;
import com.backend.stayEasy.dto.PayoutDTO;
import com.backend.stayEasy.sevice.BookingService;
import com.backend.stayEasy.sevice.PaymentBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/stayeasy/host-manager")
public class HostAPI {
    // CRUD Property
    @Autowired
    private BookingService bookingService;
    @Autowired
    private PaymentBillService paymentBillService;
    @Scheduled(cron = "0 */5 * * * *") // Chạy mỗi 5 phút
    public void scheduleBookingStatusUpdate() throws IOException {
        System.out.println("Chạy lịch schedule");
        try {
            // Lấy danh sách các booking cần cập nhật trạng thái
            List<UUID> listUpdate = bookingService.updateBookingStatusWithSchedule();
            // Duyệt qua danh sách và thực hiện việc cập nhật và thanh toán cho mỗi booking
            for (UUID id: listUpdate) {
                System.out.println("Updating booking: " + id);
                // Tạo thanh toán cho booking
                PayoutDTO payoutDTO = paymentBillService.createPerformPayout(id);
                // Thực hiện thanh toán cho chủ
                paymentBillService.performPayout(payoutDTO);
            }
        } catch (IOException e) {
            // Xử lý ngoại lệ IOException nếu có
            System.err.println("An error occurred while scheduling booking status update: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // Xử lý các ngoại lệ khác nếu có
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // get all booking
    @GetMapping(value = "/{id}&{filter}")
    public ResponseEntity<List<BookingDTO>> getAllBookingOfHost(@PathVariable("id") UUID id, @PathVariable("filter") String Filter ){
        return ResponseEntity.ok().body(bookingService.returnAllBookingOfHost(id, Filter));
    }

    // update status
   
    @PutMapping(value = "/update/{bookingId}&{status}")
    public ResponseEntity<String> confirmBooking(@PathVariable UUID bookingId, @PathVariable String status){
        bookingService.updateConfirmBooking(bookingId, status);
        // khi trạng thái thay đổi thì gửi thông báo or mail
        return ResponseEntity.ok("Confirmed " + status );
    }

}
