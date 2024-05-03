package com.backend.stayEasy.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stayEasy.dto.PayoutDTO;
import com.backend.stayEasy.dto.RefundDTO;
import com.backend.stayEasy.enums.Confirmation;
import com.backend.stayEasy.sevice.BookingService;
import com.backend.stayEasy.sevice.PaymentBillService;
import com.backend.stayEasy.sevice.PaypalService;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/stayeasy/payment")
public class PaymentAPI {
    @Autowired
    private PaymentBillService paymentService;
    
    @Autowired
    private BookingService bookingService;

    @Autowired 
    private PaypalService paypalService;

    // Method payment host when traveler checkout ( khi user checkout thi AUTO PAYMENT  cho host theo stk đã dky trong user account )
    @GetMapping("/captures/{capture_id}")
    public ResponseEntity<Object> lookupCaptures(@PathVariable("capture_id") String captureId) {
        // Ideally, fetch your token from a secure place
        String response;
        String Authorization = "Bearer " + paypalService.getAccessToken();
        HttpURLConnection httpConn = null;
        try {
            URL url = new URL("https://api-m.sandbox.paypal.com/v2/payments/captures/" + captureId);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("Authorization", Authorization);
            System.out.println(url);
            try (InputStream responseStream = httpConn.getResponseCode() / 100 == 2 ? httpConn.getInputStream() : httpConn.getErrorStream();
                 Scanner scanner = new Scanner(responseStream).useDelimiter("\\A")) {
                System.out.println(scanner.next());
                response = scanner.hasNext() ? scanner.next() : "";
            }

        } catch (IOException e) {
            System.out.println("Lỗi rồi");
            return ResponseEntity.internalServerError().body("An error occurred while making the request: " + e.getMessage());
        } finally {
            if (httpConn != null) {
                System.out.println("End");
                httpConn.disconnect();
            }
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/refund")
    public ResponseEntity<Map<String, String>> refundTransaction(@RequestBody RefundDTO refundRequest) throws IOException {
        return paymentService.processRefund(refundRequest);

    }
    @PostMapping("/performPayout/{idBooking}")
    public ResponseEntity<Object> performPayout( @PathVariable String idBooking) {
        UUID requestBookingId = UUID.fromString(idBooking);
        PayoutDTO createPayout = paymentService.createPerformPayout(requestBookingId);
        try {
            String response = paymentService.performPayout(createPayout);
            bookingService.updateConfirmBooking(requestBookingId, String.valueOf(Confirmation.COMPLETED));

            return ResponseEntity.ok().body(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("An error occurred while performing the payout: " + e.getMessage());
        }
    }



}
