package com.spring.privateClinicManage.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.spring.privateClinicManage.entity.PaymentDetailPhase1;

/**
 * Unit tests for PaymentDetailRepository
 * This class tests the payment detail repository with database operations
 *
 * Các test case kiểm tra chức năng truy vấn và thống kê dữ liệu thanh toán:
 * - Lưu và truy xuất thông tin thanh toán
 * - Thống kê doanh thu theo năm
 * - Thống kê doanh thu với năm không có dữ liệu
 * - Xóa thông tin thanh toán
 *
 * Tất cả các test case đều sử dụng @Rollback(true) để đảm bảo dữ liệu test không ảnh hưởng đến database
 */
/**
 * @DataJpaTest - Sử dụng để test các repository với database
 * @AutoConfigureTestDatabase - Cấu hình để sử dụng database thật thay vì database in-memory
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PaymentDetailRepositoryTest {

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private PaymentDetailPhase1Repository paymentDetailPhase1Repository;

    // Test data
    private PaymentDetailPhase1 payment1;
    private PaymentDetailPhase1 payment2;
    private PaymentDetailPhase1 payment3;
    private int currentYear;

    /**
     * Setup test data before each test
     * Khởi tạo dữ liệu test cho các test case
     * Dữ liệu bao gồm các đối tượng PaymentDetailPhase1 với các thông tin khác nhau
     */
    @BeforeEach
    void setUp() {
        // Get current year for testing
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);

        // Create test payments for current year
        payment1 = new PaymentDetailPhase1();
        payment1.setOrderId("TEST123456");
        payment1.setAmount(100000L);
        payment1.setDescription("Test payment 1");
        payment1.setResultCode("0");
        payment1.setPartnerCode("VNPAY");

        // Set date to January of current year
        calendar.set(currentYear, Calendar.JANUARY, 15);
        payment1.setCreatedDate(calendar.getTime());

        payment2 = new PaymentDetailPhase1();
        payment2.setOrderId("TEST234567");
        payment2.setAmount(200000L);
        payment2.setDescription("Test payment 2");
        payment2.setResultCode("0");
        payment2.setPartnerCode("MOMO");

        // Set date to February of current year
        calendar.set(currentYear, Calendar.FEBRUARY, 20);
        payment2.setCreatedDate(calendar.getTime());

        payment3 = new PaymentDetailPhase1();
        payment3.setOrderId("TEST345678");
        payment3.setAmount(150000L);
        payment3.setDescription("Test payment 3");
        payment3.setResultCode("0");
        payment3.setPartnerCode("CASH");

        // Set date to January of current year (same month as payment1)
        calendar.set(currentYear, Calendar.JANUARY, 25);
        payment3.setCreatedDate(calendar.getTime());
    }

    /**
     * Test case: TC_REPO_01
     * Test saving and retrieving payment details
     * Input: PaymentDetailPhase1 object
     * Expected output: Saved payment can be retrieved with correct data
     *
     * Mục tiêu: Kiểm tra việc lưu và truy xuất thông tin thanh toán
     * Đầu vào: Đối tượng PaymentDetailPhase1 hợp lệ
     * Đầu ra mong đợi: Có thể truy xuất được đối tượng đã lưu với dữ liệu chính xác
     *
     * @Rollback(true) - Đảm bảo dữ liệu test sẽ được rollback sau khi test hoàn thành
     */
    @Test
    @Rollback(true)
    @DisplayName("TC_REPO_01: Save and retrieve payment details")
    void testSaveAndRetrievePayment() {
        // Save payment
        PaymentDetailPhase1 savedPayment = paymentDetailPhase1Repository.save(payment1);

        // Retrieve payment
        PaymentDetailPhase1 retrievedPayment = paymentDetailPhase1Repository.findById(savedPayment.getId()).orElse(null);

        // Assert
        assertNotNull(retrievedPayment);
        assertEquals(payment1.getOrderId(), retrievedPayment.getOrderId());
        assertEquals(payment1.getAmount(), retrievedPayment.getAmount());
        assertEquals(payment1.getDescription(), retrievedPayment.getDescription());
        assertEquals(payment1.getResultCode(), retrievedPayment.getResultCode());
        assertEquals(payment1.getPartnerCode(), retrievedPayment.getPartnerCode());
    }

    /**
     * Test case: TC_REPO_02
     * Test statistics by revenue for a specific year
     * Input: Current year with multiple payments in different months
     * Expected output: Correct monthly revenue statistics
     *
     * Mục tiêu: Kiểm tra thống kê doanh thu theo năm
     * Đầu vào: Năm hiện tại với nhiều thanh toán ở các tháng khác nhau
     * Đầu ra mong đợi: Thống kê doanh thu chính xác theo từng tháng
     *
     * @Rollback(true) - Đảm bảo dữ liệu test sẽ được rollback sau khi test hoàn thành
     * CheckDB: Kiểm tra kết quả thống kê từ database có đúng với dữ liệu đã tạo không
     */
    @Test
    @Rollback(true)
    @DisplayName("TC_REPO_02: Statistics by revenue for a specific year")
    void testStatsByRevenue() {
        // Save test payments
        paymentDetailPhase1Repository.save(payment1);
        paymentDetailPhase1Repository.save(payment2);
        paymentDetailPhase1Repository.save(payment3);

        // Get statistics
        List<Object[]> stats = paymentDetailRepository.statsByRevenue(currentYear);

        // Assert
        assertNotNull(stats);
        assertEquals(2, stats.size()); // Should have 2 months (January and February)

        // Check January stats (month 1)
        Object[] januaryStats = stats.stream()
                .filter(s -> ((Number)s[0]).intValue() == 1)
                .findFirst()
                .orElse(null);
        assertNotNull(januaryStats);
        assertEquals(250000L, ((Number)januaryStats[1]).longValue()); // 100000 + 150000

        // Check February stats (month 2)
        Object[] februaryStats = stats.stream()
                .filter(s -> ((Number)s[0]).intValue() == 2)
                .findFirst()
                .orElse(null);
        assertNotNull(februaryStats);
        assertEquals(200000L, ((Number)februaryStats[1]).longValue());
    }

    /**
     * Test case: TC_REPO_03
     * Test statistics by revenue for a year with no payments
     * Input: Year with no payments
     * Expected output: Empty statistics list
     *
     * Mục tiêu: Kiểm tra thống kê doanh thu với năm không có dữ liệu
     * Đầu vào: Năm không có thanh toán nào
     * Đầu ra mong đợi: Danh sách thống kê rỗng
     *
     * @Rollback(true) - Đảm bảo dữ liệu test sẽ được rollback sau khi test hoàn thành
     * CheckDB: Kiểm tra kết quả thống kê với năm không có dữ liệu
     */
    @Test
    @Rollback(true)
    @DisplayName("TC_REPO_03: Statistics by revenue for a year with no payments")
    void testStatsByRevenue_NoPayments() {
        // Get statistics for a different year
        List<Object[]> stats = paymentDetailRepository.statsByRevenue(currentYear - 1);

        // Assert
        assertNotNull(stats);
        assertTrue(stats.isEmpty());
    }

    /**
     * Test case: TC_REPO_04
     * Test deleting payment details
     * Input: PaymentDetailPhase1 object to delete
     * Expected output: Payment is successfully deleted
     *
     * Mục tiêu: Kiểm tra xóa thông tin thanh toán
     * Đầu vào: Đối tượng PaymentDetailPhase1 để xóa
     * Đầu ra mong đợi: Thanh toán được xóa thành công
     *
     * @Rollback(true) - Đảm bảo dữ liệu test sẽ được rollback sau khi test hoàn thành
     * CheckDB: Kiểm tra xác nhận dữ liệu đã bị xóa khỏi database
     */
    @Test
    @Rollback(true)
    @DisplayName("TC_REPO_04: Delete payment details")
    void testDeletePayment() {
        // Save payment
        PaymentDetailPhase1 savedPayment = paymentDetailPhase1Repository.save(payment1);

        // Delete payment
        paymentDetailPhase1Repository.deleteById(savedPayment.getId());

        // Try to retrieve deleted payment
        boolean exists = paymentDetailPhase1Repository.existsById(savedPayment.getId());

        // Assert
        assertFalse(exists);
    }
}
