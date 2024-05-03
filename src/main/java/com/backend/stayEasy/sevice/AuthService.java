package com.backend.stayEasy.sevice;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.stayEasy.convertor.UserConverter;
import com.backend.stayEasy.dto.SignInRequest;
import com.backend.stayEasy.dto.SignInResponse;
import com.backend.stayEasy.dto.SignUpRequest;
import com.backend.stayEasy.entity.Mail;
import com.backend.stayEasy.entity.Role;
import com.backend.stayEasy.entity.Token;
import com.backend.stayEasy.entity.User;
import com.backend.stayEasy.enums.TokenType;
import com.backend.stayEasy.repository.RoleRepository;
import com.backend.stayEasy.repository.TokenRepository;
import com.backend.stayEasy.repository.UserRepository;
import com.backend.stayEasy.sevice.impl.IMailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

	@Autowired
	private UserRepository userrepository;

	@Autowired
	private UserConverter userConverter;
	
	@Autowired
	private IMailService mailService;

	@Autowired
	private TokenRepository tokenRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;
	
	@Value("${token.expiration}")
	Long jwtExpirationMs;
	
	@Value("${refresh.token.expiration}")
	Long jwtRefreshExpirationMs;

	@Value("${phone.api.key}")
	String phoneKeyAPI;
	
	@Value("${phone.api.url}")
	String phoneUrlAPI;
	
	/**
	 * 
	 * @author NamHH
	 * @param request
	 * @return SignInResponse(accessToken, refreshToken, user)
	 */
	public ResponseEntity<?> register(SignUpRequest request) {
		// Kiểm tra xem email đã tồn tại trong hệ thống chưa
		if (userrepository.existsByEmail(request.getEmail())) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", "Email " + request.getEmail() + " đã đăng ký!");
			errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
			// Trả về thông báo lỗi khi email đã tồn tại
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
					.body(errorResponse);
		}
		
		List<Role> roles = new ArrayList<>();
		
		for(String roleName: request.getRole()) {
			roles.add(roleRepository.findRoleByName("ROLE_"+roleName));
		}
		
		//lấy và giá trị từ client gửi lên vào obj user mới
		var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
				.email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
				.roles(roles)
				.createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now()).build();
		
		var savedUser = userrepository.save(user); //lưu user vào db
		var jwtToken = jwtService.generateToken(user); //Tạo accessToken
		var refreshToken = jwtService.generateRefreshToken(user); //Tạo refreshToken
		saveUserToken(savedUser, jwtToken, refreshToken); //lưu token vào db
		return ResponseEntity.ok(SignInResponse.builder()
				.accessToken(jwtToken)
				.refreshToken(refreshToken)
				.user(userConverter.toDTO(user))
				.message("Đăng nhập thành công!")
				.build());
	}

	/**
	 * 
	 * @author NamHH
	 * @param request
	 * @return SignInResponse(accessToken, refreshToken, user)
	 */
	public SignInResponse authenticate(SignInRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
		var user = userrepository.findByEmail(request.getEmail()).orElseThrow();
		
		var jwtToken = jwtService.generateToken(user); //Tạo refreshToken
		var refreshToken = jwtService.generateRefreshToken(user); //lưu token vào db
		
		//Tìm và hủy tất cả các token đang hoạt động của user này
		//(đảm bảo chỉ duy nhất 1 token hoạt động)
		revokeAllUserTokens(user);
		
		saveUserToken(user, jwtToken, refreshToken); //Lưu token mới vào db
		return SignInResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).message("Đăng nhập thành công").user(userConverter.toDTO(user))
				.build();
	}

	/**
	 * 
	 * @author NamHH
	 * @param user
	 * @param jwtToken
	 * @param refreshToken
	 */
	private void saveUserToken(User user, String jwtToken, String refreshToken) {
		var token = Token.builder()
				.user(user)
				.tokenType(TokenType.BEARER)
				.expired(false)
				.revoked(false)
				.token(jwtToken)
				.refreshToken(refreshToken)
				.expirationTokenDate(LocalDateTime.now().plusSeconds(jwtExpirationMs/1000))//1day = 24*60*60
				.expirationRefTokenDate(LocalDateTime.now().plusSeconds(jwtRefreshExpirationMs/1000))//3day = 3*24*60*60
				.build();
		tokenRepository.save(token);
	}

	/**
	 * 
	 * @author NamHH
	 * @param user
	 * 
	 */
	private void revokeAllUserTokens(User user) {
		//Tìm tất cả các token bằng user_id
		var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		if (validUserTokens.isEmpty())
			return;
		validUserTokens.forEach(token -> {
			//Hủy các token tìm được
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);//lưu lại vào db
	}

	/**
	 * 
	 * @author NamHH
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//Lấy giá trị của header Authorization từ request.
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		final String refreshToken;
		final String userEmail;
		
		//Kiểm tra xem header Authorization có tồn tại và có bắt đầu bằng chuỗi "Bearer " không.
		//Nếu không tồn tại hoặc không bắt đầu bằng "Bearer ", phương thức trả về null
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return null;
		}
		//Bỏ "Bearer " trong chuỗi lấy được từ header
		refreshToken = authHeader.substring(7);
		
		Map<String, Object> errorResponse = new HashMap<>();
		
		//Get userName từ refreshToken 
		try {
			userEmail = jwtService.extractUsername(refreshToken);
			if (userEmail != null) {
				//Tìm kiếm người dùng trong cơ sở dữ liệu dựa trên email. Nếu không tìm thấy người dùng, phương thức sẽ ném một ngoại lệ.
				var user = this.userrepository.findByEmail(userEmail).orElseThrow();
				//Check refreshToken đó có hợp lệ không
				if (jwtService.isTokenValid(refreshToken, user)) {
					var newAccessToken = jwtService.generateToken(user);//Tạo token mới
					var newRefreshToken = jwtService.generateRefreshToken(user);//tạo refreshToken mới
					
					revokeAllUserTokens(user);//Tìm và hủy tất cả các token đang hoạt động của user này
					
					saveUserToken(user, newAccessToken, newRefreshToken);//lưu token vào db
					
					return ResponseEntity.ok(SignInResponse.builder()
							.accessToken(newAccessToken)
							.refreshToken(newRefreshToken)
							.user(userConverter.toDTO(user))
							.message("Làm mới token thành công!")
							.build());
				}
			}
		} catch (Exception e) {
			errorResponse.put("message", "Token Không hợp lệ!");
			errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.contentType(MediaType.APPLICATION_JSON)
					.body(errorResponse);
		}
		errorResponse.put("message", "Không tìm thấy token!");
		errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.body(errorResponse);
	}

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username).orElseThrow();
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}

	/**
	 * 
	 * @author namHH
	 * @param username
	 * @param newPassword
	 * @return
	 */
	public SignInResponse changePassword(String username, String newPassword) {
		//Tìm kiếm người dùng trong cơ sở dữ liệu dựa trên email.
		//Nếu không tìm thấy người dùng, ném một NoSuchElementException.
		User user = userRepository.findByEmail(username).orElseThrow();
		
		//Thiết lập mật khẩu mới cho người dùng, sau khi mã hóa mật khẩu mới.
		user.setPassword(bcryptEncoder.encode(newPassword));
		user.setUpdatedAt(LocalDateTime.now());
		userRepository.save(user);//lưu user vào db
		
		var jwtToken = jwtService.generateToken(user);//tạo accesstoken mới
		var refreshToken = jwtService.generateRefreshToken(user);//tạo refreshToken mới
		
		revokeAllUserTokens(user);//Tìm và hủy tất cả các token đang hoạt động của user này
		
		saveUserToken(user, jwtToken, refreshToken);//lưu token vào db
		return SignInResponse.builder()
				.accessToken(jwtToken)
				.refreshToken(refreshToken)
				.user(userConverter.toDTO(user))
				.message("Đổi mật khẩu thành công!")
				.build();
	}
	
	/**
	 * Method verify email to register account
	 * @param email
	 * @return
	 */
	public ResponseEntity<?> verifyEmailToRegisterAccount(String email){
		// Kiểm tra xem email đã tồn tại trong hệ thống chưa
		if (userRepository.existsByEmail(email)) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", "Email " + email + " đã đăng ký!");
			errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
			// Trả về thông báo lỗi khi email đã tồn tại
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
					.body(errorResponse);
		}
		
		//gửi code về email
		return sendVerifyCodeToEmail(email);
	}
	
	/**
	 * Method verify email to reset password
	 * @param email
	 * @return
	 */
	public ResponseEntity<?> verifyEmailToResetPassword(String email){
		// Kiểm tra xem email đã tồn tại trong hệ thống chưa
		if (!userRepository.existsByEmail(email)) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", "Email " + email + "  chưa đăng ký tài khoản nào!");
			errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
			// Trả về thông báo lỗi khi email đã tồn tại
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
					.body(errorResponse);
		}
		
		//gửi code về email
		return sendVerifyCodeToEmail(email);
	}
	
	/**
	 * Method send verify code to email
	 * @author NamHH
	 * @param email
	 * @return
	 */
	public ResponseEntity<?> sendVerifyCodeToEmail(String email){
		//Tạo 1 mã để xác thực
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
		
		String subject = "Xác minh Email";
		String code = result.toString();
		
        // set new mail
        Mail mail = new Mail();
        mail.setRecipient(email);// email lấy từ body request
        mail.setSubject(subject);
        mail.setContent(code);
        mailService.sendEmailVerify(mail, code);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Đã gửi mã tới email. Hãy kiểm tra email!");
        response.put("code", code);
        response.put("status", HttpStatus.OK.value());
        
        return ResponseEntity.ok(response);
	}
	
	
	/**
	 * Mehod send verify code to phone number
	 * @author NamHH
	 * @param phone
	 * @return
	 */
	public ResponseEntity<?> sendVerifyCodeToPhone(String phone){
		// Kiểm tra xem phone đã tồn tại trong hệ thống chưa
		if (userRepository.existsByPhone(phone)) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", "Số điện thoại đã sử dụng. Vui lòng sử dụng số khác!");
			errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
			// Trả về thông báo lỗi khi email đã tồn tại
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
					.body(errorResponse);
		}
		
		//Tạo 1 mã để xác thực
		int min = 100000; // Số nhỏ nhất có 6 chữ số
        int max = 999999; // Số lớn nhất có 6 chữ số
        Random random = new Random();
        String code = String.valueOf(random.nextInt((max - min) + 1) + min);
        
        Map<String, Object> responseAPI = new HashMap<>();
        
		try {
			// request to api send code to phone
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			
			okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
			
			@SuppressWarnings("deprecation")
			okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, 
					"{\"messages\":[{\"destinations\":["
					+ "{\"to\":\"84342531726\"},"
					+ "\"from\":\"StayEasy\","
					+ "\"text\":\"Mã xác thực của bạn là: "+ code +"\"}]}");
			
			Request request = new Request.Builder()
			    .url(phoneUrlAPI)
			    .method("POST", body)
			    .addHeader("Authorization", "App " + phoneKeyAPI)
			    .addHeader("Content-Type", "application/json")
			    .addHeader("Accept", "application/json")
			    .build();
			Thread.sleep(2000);
			Response response = client.newCall(request).execute();
	        
	        responseAPI.put("message", "Đã gửi mã. Hãy kiểm tra tin nhắn!");
	        responseAPI.put("code", code);
	        responseAPI.put("response", response);
	        responseAPI.put("status", HttpStatus.OK.value());
	        
	        return ResponseEntity.ok(responseAPI);
		} catch (Exception e) {
	        responseAPI.put("message", "Không gửi được mã!");
	        responseAPI.put("status", HttpStatus.BAD_REQUEST.value());
	        
	        return ResponseEntity.ok(responseAPI);
		}
	}
}
