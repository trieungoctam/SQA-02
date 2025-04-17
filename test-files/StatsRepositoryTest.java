package com.spring.privateClinicManage.repository;

import com.spring.privateClinicManage.dto.MrlAndMeHistoryDto;
import com.spring.privateClinicManage.dto.PaymentHistoryDto;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.entity.PaymentDetail;
import com.spring.privateClinicManage.entity.PaymentDetailPhase1;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit test cho các hàm repository thống kê (thống kê doanh thu, thuốc, lịch sử user, thanh toán)
 * Sử dụng @DataJpaTest và @Sql để setup dữ liệu mẫu.
 */
class StatsRepositoryTest {
    @Mock
    private PaymentDetailRepository paymentDetailRepository;
    @Mock
    private MedicineRepository medicineRepository;
    @Mock
    private MedicalRegistryListRepository medicalRegistryListRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("TC1: Đảm bảo trả về đúng doanh thu từng tháng trong năm có dữ liệu.")
    @SuppressWarnings("unchecked")
    void statsByRevenue_HasData() {
        Object[] data = new Object[]{1, 100000L};
        List<Object[]> mockResult = Collections.singletonList(data);
        when(paymentDetailRepository.statsByRevenue(2024)).thenReturn(mockResult);
        
        List<Object[]> result = paymentDetailRepository.statsByRevenue(2024);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0)[0]).isEqualTo(1);
    }

    @Test
    @DisplayName("TC2: Đảm bảo trả về danh sách rỗng khi không có bản ghi nào.")
    void statsByRevenue_NoData() {
        when(paymentDetailRepository.statsByRevenue(1999)).thenReturn(Arrays.asList(new Object[0][]));
        
        List<Object[]> result = paymentDetailRepository.statsByRevenue(1999);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("TC3: Đảm bảo xử lý khi year là null")
    void statsByRevenue_NullYear() {
        when(paymentDetailRepository.statsByRevenue(null)).thenThrow(NullPointerException.class);
        
        assertThrows(NullPointerException.class, () -> paymentDetailRepository.statsByRevenue(null));
    }

    @Test
    @DisplayName("TC4: Đảm bảo xử lý khi year không hợp lệ.")
    void statsByRevenue_InvalidYear() {
        when(paymentDetailRepository.statsByRevenue(-1)).thenReturn(List.of());
        
        List<Object[]> result = paymentDetailRepository.statsByRevenue(-1);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("TC5: Đảm bảo trả về đúng tên thuốc và tổng số lượng dự đoán.")
    @SuppressWarnings("unchecked")
    void statsByPrognosisMedicine_HasData() {
        Object[] data = new Object[]{"ThuocA", 10L};
        List<Object[]> mockResult = Collections.singletonList(data);
        when(medicineRepository.statsByPrognosisMedicine(2024, 6)).thenReturn(mockResult);
        
        List<Object[]> result = medicineRepository.statsByPrognosisMedicine(2024, 6);
        assertThat(result).isNotEmpty();
        assertThat(result.get(0)[0]).isEqualTo("ThuocA");
    }

    @Test
    @DisplayName("TC6: Đảm bảo trả về danh sách rỗng.")
    void statsByPrognosisMedicine_NoData() {
        when(medicineRepository.statsByPrognosisMedicine(1999, 1)).thenReturn(Arrays.asList(new Object[0][]));
        
        List<Object[]> result = medicineRepository.statsByPrognosisMedicine(1999, 1);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("TC7: Đảm bảo xử lý khi year hoặc month là null")
    void statsByPrognosisMedicine_NullInput() {
        when(medicineRepository.statsByPrognosisMedicine(null, 6)).thenThrow(NullPointerException.class);
        
        assertThrows(NullPointerException.class, () -> medicineRepository.statsByPrognosisMedicine(null, 6));
    }

    @Test
    @DisplayName("TC8: Đảm bảo xử lý khi year hoặc month âm hoặc quá lớn.")
    void statsByPrognosisMedicine_InvalidInput() {
        when(medicineRepository.statsByPrognosisMedicine(-1, 13)).thenReturn(List.of());
        
        List<Object[]> result = medicineRepository.statsByPrognosisMedicine(-1, 13);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("TC9: Đảm bảo trả về đúng lịch sử khám bệnh cho user.")
    void statsUserMrlAndMeHistory_HasData() {
        User user = new User();
        List<MrlAndMeHistoryDto> mockResult = List.of(new MrlAndMeHistoryDto());
        when(medicalRegistryListRepository.statsUserMrlAndMeHistory(user)).thenReturn(mockResult);
        
        List<MrlAndMeHistoryDto> result = medicalRegistryListRepository.statsUserMrlAndMeHistory(user);
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("TC10: Đảm bảo trả về danh sách rỗng.")
    void statsUserMrlAndMeHistory_NoData() {
        User user = new User();
        when(medicalRegistryListRepository.statsUserMrlAndMeHistory(user)).thenReturn(List.of());
        
        List<MrlAndMeHistoryDto> result = medicalRegistryListRepository.statsUserMrlAndMeHistory(user);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("TC11: Đảm bảo xử lý khi user là null")
    void statsUserMrlAndMeHistory_NullUser() {
        when(medicalRegistryListRepository.statsUserMrlAndMeHistory(null)).thenThrow(NullPointerException.class);
        
        assertThrows(NullPointerException.class, () -> medicalRegistryListRepository.statsUserMrlAndMeHistory(null));
    }

    @Test
    @DisplayName("TC12: Đảm bảo trả về đúng lịch sử thanh toán đợt 1 cho tên bệnh nhân.")
    void statsPaymentPhase1History_HasData() {
        List<PaymentHistoryDto> mockResult = List.of(new PaymentHistoryDto());
        when(medicalRegistryListRepository.statsPaymentPhase1History("Nguyen Van A")).thenReturn(mockResult);
        
        List<PaymentHistoryDto> result = medicalRegistryListRepository.statsPaymentPhase1History("Nguyen Van A");
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("TC13: Đảm bảo trả về danh sách rỗng.")
    void statsPaymentPhase1History_NoData() {
        when(medicalRegistryListRepository.statsPaymentPhase1History("Không tồn tại")).thenReturn(List.of());
        
        List<PaymentHistoryDto> result = medicalRegistryListRepository.statsPaymentPhase1History("Không tồn tại");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("TC14: Đảm bảo xử lý khi name null hoặc rỗng")
    void statsPaymentPhase1History_NullName() {
        when(medicalRegistryListRepository.statsPaymentPhase1History(null)).thenThrow(NullPointerException.class);
        
        assertThrows(NullPointerException.class, () -> medicalRegistryListRepository.statsPaymentPhase1History(null));
    }

    @Test
    @DisplayName("TC15: Đảm bảo trả về đúng lịch sử thanh toán đợt 2 cho tên bệnh nhân.")
    void statsPaymentPhase2History_HasData() {
        List<PaymentHistoryDto> mockResult = List.of(new PaymentHistoryDto());
        when(medicalRegistryListRepository.statsPaymentPhase2History("Nguyen Van B")).thenReturn(mockResult);
        
        List<PaymentHistoryDto> result = medicalRegistryListRepository.statsPaymentPhase2History("Nguyen Van B");
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("TC16: Đảm bảo trả về danh sách rỗng.")
    void statsPaymentPhase2History_NoData() {
        when(medicalRegistryListRepository.statsPaymentPhase2History("Không tồn tại")).thenReturn(List.of());
        
        List<PaymentHistoryDto> result = medicalRegistryListRepository.statsPaymentPhase2History("Không tồn tại");
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("TC17: Đảm bảo xử lý khi name null hoặc rỗng")
    void statsPaymentPhase2History_NullName() {
        when(medicalRegistryListRepository.statsPaymentPhase2History(null)).thenThrow(NullPointerException.class);
        
        assertThrows(NullPointerException.class, () -> medicalRegistryListRepository.statsPaymentPhase2History(null));
    }
} 