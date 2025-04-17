package com.spring.privateClinicManage.service;

import com.spring.privateClinicManage.dto.MrlAndMeHistoryDto;
import com.spring.privateClinicManage.dto.PaymentHistoryDto;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.MedicalRegistryListRepository;
import com.spring.privateClinicManage.repository.MedicineRepository;
import com.spring.privateClinicManage.repository.PaymentDetailRepository;
import com.spring.privateClinicManage.service.impl.StatsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class StatsServiceTest {
    @Mock
    private MedicineRepository medicineRepository;
    @Mock
    private PaymentDetailRepository paymentDetailRepository;
    @Mock
    private MedicalRegistryListRepository medicalRegistryListRepository;
    @InjectMocks
    private StatsServiceImpl statsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ===================== statsByPrognosisMedicine =====================
    @Test
    @DisplayName("TC1: Đảm bảo trả về danh sách thống kê đúng với năm và tháng có dữ liệu.")
    void statsByPrognosisMedicine_HasData() {
        List<Object[]> mockResult = Arrays.asList(new Object[]{"ThuocA", 10L}, new Object[]{"ThuocB", 5L});
        when(medicineRepository.statsByPrognosisMedicine(2024, 6)).thenReturn(mockResult);
        List<Object[]> result = statsService.statsByPrognosisMedicine(2024, 6);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0)[0]).isEqualTo("ThuocA");
    }

    @Test
    @DisplayName("TC2: Đảm bảo trả về danh sách rỗng khi không có dữ liệu.")
    void statsByPrognosisMedicine_NoData() {
        when(medicineRepository.statsByPrognosisMedicine(1999, 1)).thenReturn(Collections.emptyList());
        List<Object[]> result = statsService.statsByPrognosisMedicine(1999, 1);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("TC3: Đảm bảo xử lý khi year hoặc month là null (bỏ qua nếu code gốc không xử lý null)")
    void statsByPrognosisMedicine_NullInput() {
        // Nếu code gốc không xử lý null, nên bỏ qua testcase này hoặc mong đợi NullPointerException
        when(medicineRepository.statsByPrognosisMedicine(null, 6)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> statsService.statsByPrognosisMedicine(null, 6));
    }

    @Test
    @DisplayName("TC4: Đảm bảo xử lý khi year hoặc month không hợp lệ.")
    void statsByPrognosisMedicine_InvalidInput() {
        when(medicineRepository.statsByPrognosisMedicine(-1, 13)).thenReturn(Collections.emptyList());
        List<Object[]> result = statsService.statsByPrognosisMedicine(-1, 13);
        assertThat(result).isEmpty();
    }

    // ===================== statsByRevenue =====================
    @Test
    @DisplayName("TC5: Đảm bảo trả về doanh thu từng tháng trong năm có dữ liệu.")
    void statsByRevenue_HasData() {
        List<Object[]> mockResult = Arrays.asList(new Object[]{1, 100000L}, new Object[]{2, 200000L});
        when(paymentDetailRepository.statsByRevenue(2024)).thenReturn(mockResult);
        List<Object[]> result = statsService.statsByRevenue(2024);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0)[0]).isEqualTo(1);
    }

    @Test
    @DisplayName("TC6: Đảm bảo trả về danh sách rỗng khi không có dữ liệu.")
    void statsByRevenue_NoData() {
        when(paymentDetailRepository.statsByRevenue(1999)).thenReturn(Collections.emptyList());
        List<Object[]> result = statsService.statsByRevenue(1999);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("TC7: Đảm bảo xử lý khi year là null (bỏ qua nếu code gốc không xử lý null)")
    void statsByRevenue_NullInput() {
        when(paymentDetailRepository.statsByRevenue(null)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> statsService.statsByRevenue(null));
    }

    @Test
    @DisplayName("TC8: Đảm bảo xử lý khi year âm hoặc quá lớn.")
    void statsByRevenue_InvalidInput() {
        when(paymentDetailRepository.statsByRevenue(-1)).thenReturn(Collections.emptyList());
        List<Object[]> result = statsService.statsByRevenue(-1);
        assertThat(result).isEmpty();
    }

    // ===================== paginatedStatsUserMrlAndMeHistory =====================
    @Test
    @DisplayName("TC9: Đảm bảo trả về đúng số lượng phần tử/trang.")
    void paginatedStatsUserMrlAndMeHistory_HasData() {
        User user = new User();
        List<MrlAndMeHistoryDto> mockList = new ArrayList<>();
        for (int i = 0; i < 15; i++) mockList.add(new MrlAndMeHistoryDto());
        when(medicalRegistryListRepository.statsUserMrlAndMeHistory(user)).thenReturn(mockList);
        Page<MrlAndMeHistoryDto> page = statsService.paginatedStatsUserMrlAndMeHistory(1, 10, user);
        assertThat(page.getContent()).hasSize(10);
        assertThat(page.getTotalElements()).isEqualTo(15);
    }

    @Test
    @DisplayName("TC10: Đảm bảo trả về page rỗng khi user không có lịch sử.")
    void paginatedStatsUserMrlAndMeHistory_NoData() {
        User user = new User();
        when(medicalRegistryListRepository.statsUserMrlAndMeHistory(user)).thenReturn(Collections.emptyList());
        Page<MrlAndMeHistoryDto> page = statsService.paginatedStatsUserMrlAndMeHistory(1, 10, user);
        assertThat(page.getContent()).isEmpty();
    }

    @Test
    @DisplayName("TC11: Đảm bảo xử lý khi user là null (bỏ qua nếu code gốc không xử lý null)")
    void paginatedStatsUserMrlAndMeHistory_NullUser() {
        when(medicalRegistryListRepository.statsUserMrlAndMeHistory(null)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> statsService.paginatedStatsUserMrlAndMeHistory(1, 10, null));
    }

    @Test
    @DisplayName("TC12: Đảm bảo xử lý khi page hoặc size <= 0.")
    void paginatedStatsUserMrlAndMeHistory_InvalidPageSize() {
        User user = new User();
        List<MrlAndMeHistoryDto> mockList = new ArrayList<>();
        for (int i = 0; i < 5; i++) mockList.add(new MrlAndMeHistoryDto());
        when(medicalRegistryListRepository.statsUserMrlAndMeHistory(user)).thenReturn(mockList);
        // page = 0
        Page<MrlAndMeHistoryDto> page = statsService.paginatedStatsUserMrlAndMeHistory(0, 10, user);
        assertThat(page.getContent()).isEmpty();
        // size = 0
        page = statsService.paginatedStatsUserMrlAndMeHistory(1, 0, user);
        assertThat(page.getContent()).isEmpty();
    }

    // ===================== statsPaymentPhase1History =====================
    @Test
    @DisplayName("TC13: Đảm bảo trả về đúng lịch sử thanh toán cho tên bệnh nhân.")
    void statsPaymentPhase1History_HasData() {
        List<PaymentHistoryDto> mockList = Arrays.asList(new PaymentHistoryDto(), new PaymentHistoryDto());
        when(medicalRegistryListRepository.statsPaymentPhase1History("Nguyen Van A")).thenReturn(mockList);
        List<PaymentHistoryDto> result = statsService.statsPaymentPhase1History("Nguyen Van A");
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("TC14: Đảm bảo trả về danh sách rỗng.")
    void statsPaymentPhase1History_NoData() {
        when(medicalRegistryListRepository.statsPaymentPhase1History("Không tồn tại")).thenReturn(Collections.emptyList());
        List<PaymentHistoryDto> result = statsService.statsPaymentPhase1History("Không tồn tại");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("TC15: Đảm bảo xử lý khi name null hoặc rỗng (bỏ qua nếu code gốc không xử lý null)")
    void statsPaymentPhase1History_NullName() {
        when(medicalRegistryListRepository.statsPaymentPhase1History(null)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> statsService.statsPaymentPhase1History(null));
    }

    // ===================== statsPaymentPhase2History =====================
    @Test
    @DisplayName("TC16: Đảm bảo trả về đúng lịch sử thanh toán cho tên bệnh nhân.")
    void statsPaymentPhase2History_HasData() {
        List<PaymentHistoryDto> mockList = Arrays.asList(new PaymentHistoryDto(), new PaymentHistoryDto());
        when(medicalRegistryListRepository.statsPaymentPhase2History("Nguyen Van B")).thenReturn(mockList);
        List<PaymentHistoryDto> result = statsService.statsPaymentPhase2History("Nguyen Van B");
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("TC17: Đảm bảo trả về danh sách rỗng.")
    void statsPaymentPhase2History_NoData() {
        when(medicalRegistryListRepository.statsPaymentPhase2History("Không tồn tại")).thenReturn(Collections.emptyList());
        List<PaymentHistoryDto> result = statsService.statsPaymentPhase2History("Không tồn tại");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("TC18: Đảm bảo xử lý khi name null hoặc rỗng (bỏ qua nếu code gốc không xử lý null)")
    void statsPaymentPhase2History_NullName() {
        when(medicalRegistryListRepository.statsPaymentPhase2History(null)).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> statsService.statsPaymentPhase2History(null));
    }

    // ===================== sortByCreatedDate =====================
    @Test
    @DisplayName("TC19: Đảm bảo danh sách được sắp xếp đúng theo ngày tạo giảm dần.")
    void sortByCreatedDate_Sorted() {
        PaymentHistoryDto dto1 = new PaymentHistoryDto();
        PaymentHistoryDto dto2 = new PaymentHistoryDto();
        PaymentHistoryDto dto3 = new PaymentHistoryDto();
        Date now = new Date();
        dto1.setCreatedDate(new Date(now.getTime() - 10000));
        dto2.setCreatedDate(now);
        dto3.setCreatedDate(new Date(now.getTime() - 20000));
        List<PaymentHistoryDto> input = Arrays.asList(dto1, dto2, dto3);
        List<PaymentHistoryDto> result = statsService.sortByCreatedDate(input);
        assertThat(result.get(0).getCreatedDate()).isEqualTo(dto2.getCreatedDate());
    }

    @Test
    @DisplayName("TC20: Đảm bảo trả về danh sách rỗng.")
    void sortByCreatedDate_EmptyList() {
        List<PaymentHistoryDto> result = statsService.sortByCreatedDate(Collections.emptyList());
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("TC21: Đảm bảo không thay đổi thứ tự với 1 phần tử.")
    void sortByCreatedDate_OneElement() {
        PaymentHistoryDto dto = new PaymentHistoryDto();
        dto.setCreatedDate(new Date());
        List<PaymentHistoryDto> result = statsService.sortByCreatedDate(Collections.singletonList(dto));
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(dto);
    }

    @Test
    @DisplayName("TC22: Đảm bảo xử lý khi phDto là null (bỏ qua nếu code gốc không xử lý null)")
    void sortByCreatedDate_NullList() {
        // Nếu code gốc không xử lý null, mong đợi NullPointerException
        assertThrows(NullPointerException.class, () -> statsService.sortByCreatedDate(null));
    }
} 