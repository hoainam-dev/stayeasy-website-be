package com.backend.stayEasy.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stayEasy.dto.ChangePasswordRequest;
import com.backend.stayEasy.dto.SignInRequest;
import com.backend.stayEasy.dto.SignInResponse;
import com.backend.stayEasy.dto.SignUpRequest;
import com.backend.stayEasy.dto.TokenDTO;
import com.backend.stayEasy.dto.UserDTO;
import com.backend.stayEasy.sevice.AuthService;
import com.backend.stayEasy.sevice.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthAPI {

	@Autowired
	private AuthService authService;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	/**
	 * 
	 * @author NamHH
	 * @param request
	 * @return
	 */
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody SignUpRequest request) {

		return authService.register(request);
	}

	
	/**
	 * 
	 * @author NamHH
	 * @param request
	 * @return
	 */
	@PostMapping("/login")
	public ResponseEntity<SignInResponse> authenticate(@RequestBody SignInRequest request) {
		return ResponseEntity.ok(authService.authenticate(request));
	}

	
	/**
	 * @author NamHH
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/refresh-token")
	public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return authService.refreshToken(request, response);
	}

	
	/**
	 * @author NamHH
	 * @return
	 */
	@GetMapping("/token")
	public ResponseEntity<TokenDTO> getAllToken() {
		return ResponseEntity.ok(null);
	}

	
	/**
	 * 
	 * @author NamHH
	 * @param changePasswordRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/change-password")
	public ResponseEntity<SignInResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest,
			HttpServletRequest request) {
		final String token = request.getHeader("Authorization").substring(7); // Lấy token từ Header

		final String username = userService.getUserByToken(token).getEmail(); // Lấy tên người dùng từ token

		// Xác thực người dùng
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, changePasswordRequest.getOldPassword()));

		// Cập nhật mật khẩu
		return ResponseEntity.ok(authService.changePassword(username, changePasswordRequest.getNewPassword()));
	}
	
	
	/**
	 * Method reset password when forgot password
	 * @author NamHH
	 * @param changePasswordRequest
	 * @param request
	 * @return
	 */
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {

		String email = request.get("email");// lấy email từ body request
		
		String password = request.get("newPassword");// lấy newpassword từ body request

		Map<String, Object> response = new HashMap<>();// Tạo Map response để trả về thông tin cho client 
		
		try {
			UserDTO user = userService.resetPassword(email, password);// call method reset password từ service
			
			// Set các thông tin cần thiết để gửi về client
			response.put("message", "Đặt lại mật khẩu thành công!");
	        response.put("user", user);
	        response.put("status", HttpStatus.OK.value());
		} catch (Exception e) {
			// Thông báo lỗi về cho người dùng
			response.put("message", "Đặt lại mật khẩu thất bại!");
	        response.put("user", null);
	        response.put("status", HttpStatus.BAD_REQUEST.value());
		}
		
		// Cập nhật mật khẩu
		return ResponseEntity.ok(response);
	}

	
	/**
	 * Method send code to email for reset password
	 * @author NamHH
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	@PostMapping("/forgot-password")
	public ResponseEntity<?> verifyEmailToResetPassword(@RequestBody Map<String, String> request) {
		
		String email = request.get("email");// email lấy từ body request
		
		return authService.verifyEmailToResetPassword(email);
	}
	
	
	/**
	 * Method send code to email for register account
	 * @author NamHH
	 * @param request
	 * @return
	 */
	@PostMapping("/verify-email")
	public ResponseEntity<?> verifyEmailToRegisterAccout(@RequestBody Map<String, String> request) {
		
		String email = request.get("email");// email lấy từ body request
		
		return authService.verifyEmailToRegisterAccount(email);
	}
	
	
	/**
	 * Method send code to phone to update or add new phone number
	 * @author NamHH
	 * @param request
	 * @return 
	 * @throws IOException 
	 */
	@PostMapping("/verify-phone")
	public ResponseEntity<?> verifyPhone(@RequestBody Map<String, String> requestAPI) throws IOException {
		String phone = requestAPI.get("phone");// phone lấy từ body request
		return authService.sendVerifyCodeToPhone(phone);
	}
}