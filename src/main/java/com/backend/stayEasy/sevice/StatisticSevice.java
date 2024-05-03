package com.backend.stayEasy.sevice;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.backend.stayEasy.entity.Statistics;
import com.backend.stayEasy.repository.BookingRepository;
import com.backend.stayEasy.repository.IPropertyRepository;
import com.backend.stayEasy.repository.LikeRepository;
import com.backend.stayEasy.repository.StatisticsRepository;
import com.backend.stayEasy.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class StatisticSevice {
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private LikeRepository likeRepository;
	
	@Autowired
	private IPropertyRepository propertyRepository;
	
	@Autowired
	private StatisticsRepository statisticsRepository;
	
	
	  @Transactional
//	  trả về dữ liệu của tháng này và tháng trước theo từng property
	  public List<Statistics> calculateRevenueForCurrentAndPreviousMonthByPropertyId(UUID propertyId) {
		    List<Statistics> result = new ArrayList<>();
		    
		    // Lấy ngày hiện tại
		    LocalDate currentDate = LocalDate.now();
		    
		    // Chuyển đổi ngày hiện tại sang kiểu java.sql.Date
		    Date todayDate = Date.valueOf(currentDate);
		    
		    // Lấy tháng hiện tại
		    int currentMonth = currentDate.getMonthValue();
		    
		    // Lấy tháng trước đó
		    int previousMonth = currentMonth - 1;
		    int previousYear = currentDate.getYear();
		    if (previousMonth == 0) {
		        // Nếu tháng hiện tại là tháng 1, tháng trước sẽ là tháng 12 của năm trước
		        previousMonth = 12;
		        previousYear -= 1;
		    }
		    
		    // Lấy ngày đầu của tháng hiện tại
		    LocalDate firstDayOfCurrentMonth = LocalDate.of(currentDate.getYear(), currentMonth, 1);
		    Date firstDateOfCurrentMonth = Date.valueOf(firstDayOfCurrentMonth);
		    
		    // Lấy ngày đầu của tháng trước đó
		    LocalDate firstDayOfPreviousMonth = LocalDate.of(previousYear, previousMonth, 1);
		    Date firstDateOfPreviousMonth = Date.valueOf(firstDayOfPreviousMonth);
		    
		    // Tính và lưu thống kê cho tháng hiện tại
		    Statistics currentMonthStatistics = calculateRevenueForMonthByPropertyId(firstDateOfCurrentMonth, todayDate, propertyId);
		    result.add(currentMonthStatistics);
		    
		    
		    // Lấy ngày cuối của tháng trước đó
		    Date lastDayOfLastMonthDate = Date.valueOf(firstDayOfCurrentMonth.minusDays(1));

		    // Tính và lưu thống kê cho tháng trước đó
		    Statistics previousMonthStatistics = calculateRevenueForMonthByPropertyId(firstDateOfPreviousMonth, lastDayOfLastMonthDate, propertyId);
		    result.add(previousMonthStatistics);
		    
		    return result;
		}
	  
	  
	
	  @Transactional
//	  trả về dữ liệu của tháng này và tháng trước
	  public List<Statistics> calculateRevenueForCurrentAndPreviousMonth() {
		    List<Statistics> result = new ArrayList<>();
		    
		    // Lấy ngày hiện tại
		    LocalDate currentDate = LocalDate.now();
		    
		    // Chuyển đổi ngày hiện tại sang kiểu java.sql.Date
		    Date todayDate = Date.valueOf(currentDate);
		    
		    // Lấy tháng hiện tại
		    int currentMonth = currentDate.getMonthValue();
		    
		    // Lấy tháng trước đó
		    int previousMonth = currentMonth - 1;
		    int previousYear = currentDate.getYear();
		    if (previousMonth == 0) {
		        // Nếu tháng hiện tại là tháng 1, tháng trước sẽ là tháng 12 của năm trước
		        previousMonth = 12;
		        previousYear -= 1;
		    }
		    
		    // Lấy ngày đầu của tháng hiện tại
		    LocalDate firstDayOfCurrentMonth = LocalDate.of(currentDate.getYear(), currentMonth, 1);
		    Date firstDateOfCurrentMonth = Date.valueOf(firstDayOfCurrentMonth);
		    
		    // Lấy ngày đầu của tháng trước đó
		    LocalDate firstDayOfPreviousMonth = LocalDate.of(previousYear, previousMonth, 1);
		    Date firstDateOfPreviousMonth = Date.valueOf(firstDayOfPreviousMonth);
		    
		    // Tính và lưu thống kê cho tháng hiện tại
		    System.out.println("cacule: " + firstDateOfCurrentMonth + " " + todayDate );
		    Statistics currentMonthStatistics = calculateRevenueForMonth(firstDateOfCurrentMonth, todayDate);
		    result.add(currentMonthStatistics);
		    
		    // Lấy ngày cuối của tháng trước đó
		    Date lastDayOfLastMonthDate = Date.valueOf(firstDayOfCurrentMonth.minusDays(1));

		    // Tính và lưu thống kê cho tháng trước đó
		    Statistics previousMonthStatistics = calculateRevenueForMonth(firstDateOfPreviousMonth, lastDayOfLastMonthDate);
		    result.add(previousMonthStatistics);
		    
		    return result;
		}

	  
		// Chạy vào ngày cuối cùng của tháng lúc 11:50:00 PM
		@Scheduled(cron = "0 50 23 L * ?")
		@Transactional
	    public void calculateAndSaveMonthlyStatistics() {
	        System.out.println("auto save");
	        // Lấy ngày hiện tại
	        LocalDate currentDate = LocalDate.now();
	        Date todayDate = Date.valueOf(currentDate);
	        
	        // Lấy tháng hiện tại
	        int currentMonth = currentDate.getMonthValue();
	        
	        // Lấy ngày đầu của tháng hiện tại
	        LocalDate firstDayOfCurrentMonth = LocalDate.of(currentDate.getYear(), currentMonth, 1);
	        Date firstDateOfCurrentMonth = Date.valueOf(firstDayOfCurrentMonth);
	        
	        // Tính và lưu thống kê cho tháng hiện tại
	        Statistics currentMonthStatistics = calculateRevenueForMonth(firstDateOfCurrentMonth, todayDate);
	        System.out.println(currentMonthStatistics);
	        statisticsRepository.save(currentMonthStatistics);
//	        return currentMonthStatistics;
	    }
	    

	  public Statistics calculateRevenueForMonth(Date startDate, Date endDate) {
		 System.out.println("detat: " + startDate  + " " + endDate);
	        Statistics statistics = new Statistics();
	        
	        // Thiết lập date cho đối tượng statistics
	        statistics.setDate(endDate);
	        
	        // Tính tổng doanh thu từ ngày đầu tháng đến ngày hiện tại
	        Double totalRevenue = bookingRepository.getTotalRevenueBetween(startDate, endDate);
	        statistics.setRevenue(totalRevenue != null ? totalRevenue : 0.0);
	        
	        // Đếm số lượng booking từ ngày đầu tháng cho đến ngày hiện tại
	        long totalBookings = bookingRepository.countBookingsBetween(startDate, endDate);
	        statistics.setTotalBookings(totalBookings);
	        
	        
	     // Chuyển đổi từ java.sql.Date sang java.time.LocalDateTime
	        LocalDateTime startDateTime = startDate.toLocalDate().atStartOfDay();
	        LocalDateTime endDateTime = endDate.toLocalDate().atTime(LocalTime.MAX);
	        // Đếm số lượng tài khoản được tạo từ đầu tháng cho đến ngày hiện tại
	        long totalAccounts = userRepository.countUsersCreatedBetween(startDateTime, endDateTime);
	        statistics.setTotalAccount(totalAccounts);
	        
	        // Đếm số lượng property từ ngày đầu tháng cho đến ngày hiện tại
	        long totalProperties = propertyRepository.countPropertiesBetween(startDate, endDate);
	        statistics.setTotalPost(totalProperties);
	        
	        // Đếm số lượng cancel booking từ ngày đầu tháng cho đến ngày hiện tại
	        long totalCancelPost = bookingRepository.countBookingWithCancelNotNull(startDate, endDate);
	        statistics.setTotalCancelBooking(totalCancelPost);
	        
	        return statistics;
	    }
	  
	  
//	  tính số liệu thống kê cho từng property cụ thể
	  public Statistics calculateRevenueForMonthByPropertyId(Date startDate, Date endDate, UUID propertyId) {

		        Statistics statistics = new Statistics();
		        
		        // Thiết lập date cho đối tượng statistics
		        statistics.setDate(endDate);
		        
		        // Tính tổng doanh thu từ ngày đầu tháng đến ngày hiện tại
		        Double totalRevenue = bookingRepository.getTotalRevenueBetweenAndByPropertyId(startDate, endDate, propertyId);
		        statistics.setRevenue(totalRevenue != null ? totalRevenue : 0.0);

		        
		        // Đếm số lượng booking từ ngày đầu tháng cho đến ngày hiện tại
		        long totalBookings = bookingRepository.countBookingsBetweenAndByPropertyId(startDate, endDate, propertyId);
		        statistics.setTotalBookings(totalBookings != -1 ? totalBookings : 0);
		        
		        // Đếm số lượng cancel booking từ ngày đầu tháng cho đến ngày hiện tại
		        long totalCancelPost = bookingRepository.countBookingWithCancelNotNullByPropertyId(startDate, endDate, propertyId);
		        statistics.setTotalCancelBooking(totalCancelPost != 0 ? totalCancelPost : 0);
		        
		        long totalLike = likeRepository.countByPropertyId(propertyId);
		        statistics.setTotalLike(totalLike != 0 ? totalLike : 0);
		        
		        return statistics;
		    } 
}