package com.backend.stayEasy.api;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stayEasy.convertor.BookingConverter;
import com.backend.stayEasy.convertor.StatisticsConverter;
import com.backend.stayEasy.dto.BookingDTO;
import com.backend.stayEasy.dto.DailyRevenueDTO;
import com.backend.stayEasy.dto.PropertyDTO;
import com.backend.stayEasy.dto.StatisticsDTO;
import com.backend.stayEasy.dto.UserDTO;
import com.backend.stayEasy.entity.Holiday;
import com.backend.stayEasy.entity.Statistics;
import com.backend.stayEasy.repository.BookingRepository;
import com.backend.stayEasy.repository.StatisticsRepository;
import com.backend.stayEasy.sevice.StatisticSevice;
import com.backend.stayEasy.sevice.UserService;
import com.backend.stayEasy.sevice.impl.IPropertyService;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/stayeasy/admin")
public class AdminApi {
	@Autowired
	private StatisticSevice statisticSevice;
	@Autowired
	private StatisticsRepository statisticsRepository;
	
	@Autowired
	private StatisticsConverter statisticsConverter;
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private BookingConverter bookingConverter;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IPropertyService propertyService;
	
	private static Holiday[] holidays = {
		    new Holiday("Tết Dương lịch (Tết tây)", "2024-01-01", "2024-01-03", 10),
		    new Holiday("Ngày truyền thống học sinh, sinh viên Việt Nam", "2024-01-09", "2024-01-09", 2),
		    new Holiday("Ngày thành lập Đảng Cộng Sản Việt Nam", "2024-02-03", "2024-02-03", 3),
		    new Holiday("Lễ tình nhân (Valentine)", "2024-02-14", "2024-02-14", 0),
		    new Holiday("Ngày thầy thuốc Việt Nam", "2024-02-27", "2024-02-27", 0),
		    new Holiday("Ngày Quốc tế Phụ nữ", "2024-03-08", "2024-03-08", 0),
		    new Holiday("Ngày Quốc tế Hạnh phúc", "2024-03-20", "2024-03-20", 0),
		    new Holiday("Ngày thành lập Đoàn TNCS Hồ Chí Minh", "2024-03-26", "2024-03-26", 2),
		    new Holiday("Ngày Cá tháng Tư", "2024-04-01", "2024-04-01", 0),
		    new Holiday("Ngày giải phóng miền Nam", "2024-04-30", "2024-04-30", 0),
		    new Holiday("Ngày Quốc tế Lao động", "2024-05-01", "2024-05-01", 0),
		    new Holiday("Ngày chiến thắng Điện Biên Phủ", "2024-05-07", "2024-05-07", 1),
		    new Holiday("Ngày của mẹ", "2024-05-12", "2024-05-12", 0),
		    new Holiday("Ngày sinh Chủ tịch Hồ Chí Minh", "2024-05-19", "2024-05-19", 0),
		    new Holiday("Ngày Quốc tế thiếu nhi", "2024-06-01", "2024-06-01", 0),
		    new Holiday("Ngày của cha", "2024-06-18", "2024-06-18", 0),
		    new Holiday("Ngày báo chí Việt Nam", "2024-06-21", "2024-06-21", 6),
		    new Holiday("Ngày gia đình Việt Nam", "2024-06-28", "2024-06-28", 0),
		    new Holiday("Ngày dân số thế giới", "2024-07-11", "2024-07-11", 0),
		    new Holiday("Ngày Thương binh liệt sĩ", "2024-07-27", "2024-07-27", 0),
		    new Holiday("Ngày thành lập công đoàn Việt Nam", "2024-07-28", "2024-07-28", 0),
		    new Holiday("Ngày kỷ niệm Cách mạng Tháng 8 thành công", "2024-08-19", "2024-08-19", 0),
		    new Holiday("Ngày Quốc Khánh", "2024-09-02", "2024-09-02", 0),
		    new Holiday("Ngày thành lập Mặt trận Tổ quốc Việt Nam", "2024-09-10", "2024-09-10", 0),
		    new Holiday("Ngày quốc tế người cao tuổi", "2024-10-01", "2024-10-01", 0),
		    new Holiday("Ngày giải phóng thủ đô", "2024-10-10", "2024-10-10", 0),
		    new Holiday("Ngày doanh nhân Việt Nam", "2024-10-13", "2024-10-13", 0),
		    new Holiday("Ngày Phụ nữ Việt Nam", "2024-10-20", "2024-10-20", 0),
		    new Holiday("Ngày Halloween", "2024-10-31", "2024-10-31", 0),
		    new Holiday("Ngày pháp luật Việt Nam", "2024-11-09", "2024-11-09", 0),
		    new Holiday("Ngày Quốc tế Nam giới", "2024-11-19", "2024-11-19", 0),
		    new Holiday("Ngày Nhà giáo Việt Nam", "2024-11-20", "2024-11-20", 0),
		    new Holiday("Ngày thành lập Hội chữ thập đỏ Việt Nam", "2024-11-23", "2024-11-23", 0),
		    new Holiday("Ngày thế giới phòng chống AIDS", "2024-12-01", "2024-12-01", 0),
		    new Holiday("Ngày toàn quốc kháng chiến", "2024-12-19", "2024-12-19", 0),
		    new Holiday("Ngày lễ Giáng sinh", "2024-12-25", "2024-12-25", 0),
		    new Holiday("Ngày thành lập quân đội nhân dân Việt Nam", "2024-12-22", "2024-12-22", 0)
		};

	@GetMapping("/holiday-vn")
	public Holiday[] getHolidayVn() {
		return holidays;
	}
	
	@GetMapping("/revenue")
	public List<StatisticsDTO> getRevenueByMonth() {
		System.out.println("revenue here");
		List<StatisticsDTO> statisticsDTOs = new ArrayList<>();
		List<Statistics> statisticsList = statisticSevice.calculateRevenueForCurrentAndPreviousMonth();
		for (Statistics statisticsItem : statisticsList) {
			statisticsDTOs.add(statisticsConverter.toDTO(statisticsItem));
		}
		return statisticsDTOs;
	}
	
//	  trả về dữ liệu của tháng này và tháng trước theo từng property	
	@GetMapping("/statistics")
	public List<StatisticsDTO> getRevenueByMonthAndPropertyId(@RequestParam("propertyId") UUID propertyId) {
		System.out.println("get statistic id: " + propertyId);
		List<StatisticsDTO> statisticsDTOs = new ArrayList<>();
		List<Statistics> statisticsList = statisticSevice.calculateRevenueForCurrentAndPreviousMonthByPropertyId(propertyId);
		for (Statistics statisticsItem : statisticsList) {
			statisticsDTOs.add(statisticsConverter.toDTO(statisticsItem));
		}
		return statisticsDTOs;
	}
	

	 @GetMapping("/revenue/daily")
	    public Map<String, List<DailyRevenueDTO>> getDailyRevenues(@RequestParam("date") Date date) {
	        // Chuyển đổi Date SQL thành LocalDate
	        LocalDate localDate = date.toLocalDate();

	        // Lấy tháng và năm của ngày được cung cấp
	        int currentMonth = localDate.getMonthValue();
	        int currentYear = localDate.getYear();

	        // Truy vấn doanh thu hàng ngày của tháng hiện tại
	        List<DailyRevenueDTO> currentDailyRevenue = bookingRepository.findDailyRevenueByMonthAndYear(date);

	        // Truy vấn doanh thu hàng ngày của tháng trước
	        int previousMonth = currentMonth - 1;
	        int previousYear = currentYear;
	        if (previousMonth == 0) {
	            previousMonth = 12;
	            previousYear--;
	        }
	        Date previousMonthDate = Date.valueOf(LocalDate.of(previousYear, previousMonth, 1));
	        List<DailyRevenueDTO> previousDailyRevenue = bookingRepository.findDailyRevenueByMonthAndYear(previousMonthDate);

	        // Đặt dữ liệu vào một Map để trả về
	        Map<String, List<DailyRevenueDTO>> result = new HashMap<>();
	        result.put("currentMonthRevenue", currentDailyRevenue);
	        result.put("previousMonthRevenue", previousDailyRevenue);

	        return result;
	    }
	 
	 @GetMapping("/booking/daily")
	 public List<Object[]> getBookingDaily() {
	     LocalDate currentDate = LocalDate.now();
	     Date firstDayOfMonth = Date.valueOf(currentDate.withDayOfMonth(1));
	     Date lastDayOfMonth = Date.valueOf(currentDate.withDayOfMonth(currentDate.lengthOfMonth()));

	     return bookingRepository.countBookingAndCancelByDate(firstDayOfMonth, lastDayOfMonth);
	 }
	 
	 
	 @GetMapping("/statistics/monthly")
	 public List<StatisticsDTO> getStatisticsMonthly() {
		 LocalDate currentDate = LocalDate.now();
	     Date firstDayOfYear= Date.valueOf(currentDate.withDayOfYear(1));
	     Date todayDate = Date.valueOf(currentDate);

	     List<StatisticsDTO> statistics =  statisticsRepository
	    		 .sumRevenueFromStartOfYearToDate(firstDayOfYear, todayDate)
	    		 .stream()
				 .map(statisticsConverter::toDTO)
				 .collect(Collectors.toList());
	     return statistics;
	     
	 }
	 
	 
	 @GetMapping("/user/all")
	 public List<UserDTO> getAllUser() {
		 return userService.getAllUser();
	 }
	 
	 @GetMapping("/property/search")
	 public List<PropertyDTO> searchProperty(@RequestParam("keySearch") String keySearch){
		 return propertyService.findByPropertyNameOrAddressContainingIgnoreCase(keySearch);
	 }
	 
	 @GetMapping("/booking")
	 public List<BookingDTO> getBookingById(@RequestParam("propertyId") UUID propertyId){
	        // Lấy ngày hiện tại
	        LocalDate currentDate = LocalDate.now();
	        
	        // Lấy tháng hiện tại
	        int currentMonth = currentDate.getMonthValue();
	        
	        // Lấy ngày đầu của tháng hiện tại
	        LocalDate firstDayOfCurrentMonth = LocalDate.of(currentDate.getYear(), currentMonth, 1);
	        Date firstDateOfCurrentMonth = Date.valueOf(firstDayOfCurrentMonth);
		 return bookingRepository
				 .findAllByPropertyIdAndDateBookingAfterAndCancelIsNull(propertyId, firstDateOfCurrentMonth)
				 .stream()
				 .map(bookingConverter::toDTO)
				 .collect(Collectors.toList());
	 }

}