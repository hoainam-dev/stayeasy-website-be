package com.backend.stayEasy.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stayEasy.dto.CheckLoginResponseDTO;
import com.backend.stayEasy.dto.UserDTO;
import com.backend.stayEasy.sevice.JwtService;
import com.backend.stayEasy.sevice.impl.ITokenService;
import com.backend.stayEasy.sevice.impl.IUserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/user")
public class UserAPI {

	@Autowired
	private IUserService userService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private ITokenService tokenService;

	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<UserDTO>> getAllUser() {
		return ResponseEntity.ok(userService.getAllUser());
	}

	
	/**
	 * 
	 * @author NamHH
	 * @param request
	 * @return
	 */
	@GetMapping("/token")
	public ResponseEntity<?> getUserByAccessToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");//Lấy chuỗi token từ header
		if (token != null && token.startsWith("Bearer ")) {
			//tách ký tự "Bearer " ra khỏi chuỗi token vừa lấy được
			token = token.substring(7);
			
			//Tạo các var kiểm tra token
			boolean isValid = jwtService.isTokenValid(token); //kiểm tra token hợp lệ hay không
			boolean isExpired = jwtService.isTokenExpired(token);//Kiểm tra token còn hạn không => true -> hết hạn
			boolean isTokenExist = tokenService.getByToken(token).isPresent();//Kiểm tra tra token có tồn tại trong db không
			
			
			//tạo 1 obj để trả về người dùng
			CheckLoginResponseDTO result = new CheckLoginResponseDTO("", false, !isExpired, isTokenExist, isValid, null);
			
			if (isValid) {
				if (!isExpired && isTokenExist) {
					//token hợp lệ && còn hạn && có trong db -> lấy ra user
					result.setMessage("Thành công!");
					result.setLogin(true);
					result.setValid(isValid);
					result.setUser(userService.getUserByToken(token));
					return ResponseEntity.ok(result);
				}
				//token hợp lệ && (hết hạn || không tồn tại)
				result.setMessage("Token không hết hạn hoặc không tồn tại.");
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
			}
			//token không hợp lệ
			result.setMessage("Token không hợp lệ.");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tìm thấy token.");
	}

	
	/**
	 * 
	 * @author NamHH
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
		return ResponseEntity.ok(userService.getUserById(UUID.fromString(id)));
	}


	/**
	 * 
	 * @author NamHH
	 * @param userDTO
	 * @param request
	 * @return
	 */
	@PutMapping("/update")
	public ResponseEntity<?> update(@RequestBody UserDTO userDTO, HttpServletRequest request) {
		final String token = request.getHeader("Authorization").substring(7); // Lấy token từ Header

		final UUID currentUserId = userService.getUserByToken(token).getId();

		// Kiểm tra xem người dùng hiện tại có được phép thay đổi thông tin không?
		if (!currentUserId.equals(userDTO.getId())) {
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("message", "Bạn không có quyền thay đổi thông tin!");
			errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
			// Trả về thông báo lỗi
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
					.body(errorResponse);
		}
		return ResponseEntity.ok(userService.save(userDTO));
	}
	
	/**
	 * 
	 * @author NamHH
	 * @param userDTO
	 * @param request
	 * @return
	 */
	@PutMapping("/update-role")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> updateRole(@RequestBody UserDTO userDTO, HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();
		try {
			UserDTO user = userService.save(userDTO);// call method reset password từ service
			
			// Set các thông tin cần thiết để gửi về client
			response.put("message", "Thay đổi thành công!");
	        response.put("user", user);
	        response.put("status", HttpStatus.OK.value());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			// Thông báo lỗi về cho người dùng
						response.put("message", "Thay đổi thất bại!");
				        response.put("user", null);
				        response.put("status", HttpStatus.BAD_REQUEST.value());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

}
