package com.backend.stayEasy.sevice;

import com.backend.stayEasy.convertor.PaymentConverter;
import com.backend.stayEasy.dto.BookingDTO;
import com.backend.stayEasy.dto.PaymentDTO;
import com.backend.stayEasy.dto.PayoutDTO;
import com.backend.stayEasy.dto.RefundDTO;
import com.backend.stayEasy.entity.Booking;
import com.backend.stayEasy.entity.Mail;
import com.backend.stayEasy.entity.PaymentBill;
import com.backend.stayEasy.repository.BookingRepository;
import com.backend.stayEasy.repository.PaymentRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paypal.api.payments.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PaymentBillService {
	@Autowired
	private PaymentRepository PaymentRepo;
	
	@Autowired
	private MailService mailService;
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private PaymentConverter paymentConverter;

	@Autowired BookingService bookingService;
	@Autowired PaypalService paypalService;


	public PaymentDTO savePayment(Payment paymentParams, UUID bookingId) {
		// Nhận json tư Payment Paypal sau đó set dữ liệu trong database
		Optional<Booking> booking;
		booking = bookingRepository.findById(bookingId);
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setMethod(paymentParams.getPayer().getPaymentMethod());
		paymentDTO.setStatus(paymentParams.getState());
		paymentDTO.setCreateTime(paymentParams.getCreateTime());
		paymentDTO.setAccountType(paymentParams.getPayer().getPayerInfo().getPayerId());
		paymentDTO.setAmount(Float.parseFloat(paymentParams.getTransactions().get(0).getAmount().getTotal()));
		// Assuming first transaction holds relevant amount
		paymentDTO.setPaymentId(paymentParams.getId());
		paymentDTO.setCapturesId(paymentParams.getTransactions().get(0).getRelatedResources().get(0).getSale().getId());
		paymentDTO.setRefundStatus("NO");
		paymentDTO.setBookingId(booking.get().getBookingId());
		PaymentBill paymentBill = paymentConverter.toEntity(paymentDTO);
		PaymentRepo.save(paymentBill);
		return paymentConverter.toDTO(paymentBill);
	}

	public List<PaymentDTO> findByPaymentId(String paymentId) {
		return PaymentRepo.findByPaymentId(paymentId).stream().map(paymentConverter::toDTO)
				.collect(Collectors.toList());
	}
	public void updateBillWhenRefund(UUID paymentId, String status) {
		Optional<PaymentBill> paymentBillOptional = PaymentRepo.findById(paymentId);
		if (paymentBillOptional.isEmpty()) {
			// Handle booking not found (e.g., throw an exception, log an error, etc.)
			throw new RuntimeException("Booking with ID " + paymentId + " not found.");
		}
		PaymentBill paymentBill = paymentBillOptional.get();
		if (!status.equals(paymentBill.getRefundStatus())) {
			paymentBill.setRefundStatus(status);
			PaymentRepo.save(paymentBill);
			// send Email
		}
	}

	// find bill for return
    public List<PaymentDTO> findWithBookingId(UUID invoiceId) {
		return PaymentRepo.findByBookingBookingId(invoiceId).stream().map(paymentConverter::toDTO)
				.collect(Collectors.toList());
    }
	public RefundDTO createBillToRefund(RefundDTO refundDTO){
		List<PaymentDTO> paymentDTOs = findWithBookingId(refundDTO.getInvoiceId());
		Optional<PaymentDTO> firstPaymentDTO = paymentDTOs.stream().findFirst();
		if(firstPaymentDTO.isPresent() ){
			PaymentDTO firstPayment = firstPaymentDTO.get();
			refundDTO.setCaptureId(firstPayment.getCapturesId());
			refundDTO.setRefundAmount(refundDTO.getRefundAmount());
			refundDTO.setCurrencyCode("USD");
			refundDTO.setNoteToPayer(refundDTO.getNoteToPayer());
			refundDTO.setPaypalRequestId(firstPayment.getPaymentBillId());
			refundDTO.setInvoiceId(refundDTO.getInvoiceId());
		}
		System.out.println(refundDTO);
		return refundDTO ;

	}

	public ResponseEntity<Map<String, String>> processRefund(RefundDTO refundRequest) {
		RefundDTO refundDTO = createBillToRefund(refundRequest); // Assume this method exists within this class or elsewhere in your code
		String authorization = "Bearer " + paypalService.getAccessToken();
		UUID bookingId = refundDTO.getInvoiceId();
		UUID paymentId = refundDTO.getPaypalRequestId();
		Map<String, String> responseMap = new HashMap<>();
		JsonObject jsonObject;

		try {
			URL url = new URL("https://api-m.sandbox.paypal.com/v2/payments/captures/" + refundDTO.getCaptureId() + "/refund");
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("POST");
			httpConn.setRequestProperty("Content-Type", "application/json");
			httpConn.setRequestProperty("Authorization", authorization);
			httpConn.setRequestProperty("PayPal-Request-Id", refundDTO.getPaypalRequestId().toString());
			httpConn.setRequestProperty("Prefer", "return=representation");
			httpConn.setDoOutput(true);

			String requestBody = "{"
					+ "\"amount\": { \"value\": \"" + refundDTO.getRefundAmount() + "\", \"currency_code\": \"" + refundDTO.getCurrencyCode() + "\" },"
					+ "\"invoice_id\": \"" + refundDTO.getInvoiceId() + "\","
					+ "\"note_to_payer\": \"" + refundDTO.getNoteToPayer() + "\""
					+ "}";
			try (OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream())) {
				writer.write(requestBody);
			}

			// Handle response
			InputStream responseStream = (httpConn.getResponseCode() / 100 == 2) ? httpConn.getInputStream() : httpConn.getErrorStream();
			try (Scanner scanner = new Scanner(responseStream).useDelimiter("\\A")) {
				String response = scanner.hasNext() ? scanner.next() : "";
				jsonObject = new Gson().fromJson(response, JsonObject.class);

				if (jsonObject.has("status") && "COMPLETED".equals(jsonObject.get("status").getAsString())) {
					bookingService.updateBookingCancel(refundDTO.getInvoiceId(), true, false);
					responseMap.put("message", "Hủy booking thành công và đã hoàn tiển.");
					sendEmailCancelBooking(bookingId);
				} else {
					responseMap.put("message", "Mã lỗi"+jsonObject.get("status").getAsString());
				}
				updateBillWhenRefund(paymentId,jsonObject.get("status").getAsString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("error", "Lỗi trong giao dịch hoàn tiến.");
		}
		return ResponseEntity.ok(responseMap);
	}

	public PayoutDTO createPerformPayout(UUID requestBookingId) {
		PayoutDTO payoutDTO = new PayoutDTO();
		List<PaymentDTO> paymentDTOs = findWithBookingId(requestBookingId);
		Optional<PaymentDTO> firstPaymentDTO = paymentDTOs.stream().findFirst();
		if(firstPaymentDTO.isPresent()) {
			PaymentDTO firstPayment = firstPaymentDTO.get();
			payoutDTO.setSender_batch_id(firstPayment.getPaymentId());
			payoutDTO.setEmail_message("Thanh toán booking " + firstPayment.getBookingDTO().getPropertyName() + "mã" + firstPayment.getBookingId());
			payoutDTO.setEmail_subject("Thanh toán booking");
			payoutDTO.setSender_item_id(String.valueOf(requestBookingId));
			payoutDTO.setReceiver("sb-norco29338421@personal.example.com");
			payoutDTO.setRecipient_wallet("PAYPAL");
			payoutDTO.setNote("Chúc chủ nhà một ngay tốt lành!");
			payoutDTO.setAmount(firstPayment.getBookingDTO().getTotal().floatValue());
		}
		System.out.println(payoutDTO);
		return payoutDTO;
	}
	public String performPayout(PayoutDTO payoutDTO) throws IOException {
		String authorization = "Bearer " + paypalService.getAccessToken(); // Implement this method to get the access token
		URL url = new URL("https://api-m.sandbox.paypal.com/v1/payments/payouts");
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestMethod("POST");
		httpConn.setRequestProperty("Content-Type", "application/json");
		httpConn.setRequestProperty("Authorization", authorization);
		httpConn.setDoOutput(true);

		try (OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream())) {
			writer.write("{ \"sender_batch_header\":" +
					" { \"sender_batch_id\": \"" + payoutDTO.getSender_batch_id() + "\"," +
					" \"email_subject\": \"" + payoutDTO.getEmail_subject() + "\", " +
					"\"email_message\": \"" + payoutDTO.getEmail_message() + "\" }, " +
					"\"items\": [ { \"recipient_type\": \"EMAIL\", \"amount\": { \"value\": \"" + payoutDTO.getAmount() + "\", \"currency\": \"USD\" }, " +
					"\"note\": \"Thanks for your patronage!\", \"sender_item_id\": \"" + payoutDTO.getSender_item_id() + "\"," +
					" \"receiver\": \"" + payoutDTO.getReceiver() + "\", " +
					"\"recipient_wallet\": \"PAYPAL\" } ] }");
			writer.flush();
		}

		int responseCode = httpConn.getResponseCode();
		if (responseCode >= 200 && responseCode < 300) {
			try (Scanner scanner = new Scanner(httpConn.getInputStream()).useDelimiter("\\A")) {
				return scanner.hasNext() ? scanner.next() : "";
			}
		} else {
			try (Scanner scanner = new Scanner(httpConn.getErrorStream()).useDelimiter("\\A")) {
				throw new IOException(scanner.hasNext() ? scanner.next() : "Unknown error");
			}
		}
	}

	private void sendEmailCancelBooking(UUID bookingId) {
		BookingDTO bookingDTO = bookingService.getBookingById(bookingId);
		String home = "http://localhost:3000/";
		String subject = "Hủy booking thành công " + bookingDTO.getPropertyName();
		String content = "Payment mail";
		// set new mail
		Mail mail = new Mail();
		mail.setRecipient(bookingDTO.getUserDTOS().getEmail());
		mail.setSubject(subject);
		mail.setContent(content);
		mailService.sendCancel(mail, bookingDTO, home);
	}
}
