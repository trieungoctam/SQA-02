package com.spring.privateClinicManage.service;

import com.spring.privateClinicManage.entity.*;
import com.spring.privateClinicManage.repository.MedicalRegistryListRepository;
import com.spring.privateClinicManage.service.impl.MedicalRegistryListServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class MedicalRegistryListServiceTest {
    @Mock
    private MedicalRegistryListRepository medicalRegistryListRepository;
    @InjectMocks
    private MedicalRegistryListServiceImpl medicalRegistryListService;

    private Schedule validSchedule;
    private StatusIsApproved validStatus;
    private User validUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Tạo dữ liệu mẫu hợp lệ
        validSchedule = new Schedule();
        validSchedule.setId(1);
        
        validStatus = new StatusIsApproved();
        validStatus.setId(1);
        validStatus.setStatus("APPROVED");
        
        validUser = new User();
        validUser.setId(1);
    }

    // ===================== findByScheduleAndStatusIsApproved =====================
    @Test
    @DisplayName("TC1: Đảm bảo trả về đúng danh sách phiếu khám theo lịch và trạng thái")
    void findByScheduleAndStatusIsApproved_HasData() {
        List<MedicalRegistryList> mockList = Arrays.asList(new MedicalRegistryList(), new MedicalRegistryList());
        when(medicalRegistryListRepository.findByScheduleAndStatusIsApproved(2024, 6, 1, validStatus))
            .thenReturn(mockList);

        List<MedicalRegistryList> result = medicalRegistryListService.findByScheduleAndStatusIsApproved(2024, 6, 1, validStatus);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("TC2: Đảm bảo trả về danh sách rỗng")
    void findByScheduleAndStatusIsApproved_NoData() {
        when(medicalRegistryListRepository.findByScheduleAndStatusIsApproved(1999, 1, 1, validStatus))
            .thenReturn(Collections.emptyList());

        List<MedicalRegistryList> result = medicalRegistryListService.findByScheduleAndStatusIsApproved(1999, 1, 1, validStatus);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("TC3: Đảm bảo xử lý khi truyền null")
    void findByScheduleAndStatusIsApproved_NullInput() {
        when(medicalRegistryListRepository.findByScheduleAndStatusIsApproved(null, 6, 1, validStatus))
            .thenReturn(Collections.emptyList());

        List<MedicalRegistryList> result = medicalRegistryListService.findByScheduleAndStatusIsApproved(null, 6, 1, validStatus);
        assertThat(result).isEmpty();
    }

    // ===================== findByScheduleAndStatusIsApprovedPaginated =====================
    @Test
    @DisplayName("TC4: Đảm bảo trả về đúng số lượng phần tử/trang")
    void findByScheduleAndStatusIsApprovedPaginated_HasData() {
        List<MedicalRegistryList> mockList = new ArrayList<>();
        for (int i = 0; i < 15; i++) mockList.add(new MedicalRegistryList());
        
        Page<MedicalRegistryList> page = new PageImpl<>(mockList.subList(0, 10), PageRequest.of(0, 10), 15);
        when(medicalRegistryListRepository.findAll(any(PageRequest.class)))
            .thenReturn(page);

        Page<MedicalRegistryList> result = medicalRegistryListService.findByScheduleAndStatusIsApprovedPaginated(1, 10, mockList);
        assertThat(result.getContent()).hasSize(10);
        assertThat(result.getTotalElements()).isEqualTo(15);
    }

    @Test
    @DisplayName("TC5: Đảm bảo trả về page rỗng")
    void findByScheduleAndStatusIsApprovedPaginated_NoData() {
        Page<MedicalRegistryList> emptyPage = new PageImpl<>(Collections.emptyList());
        when(medicalRegistryListRepository.findAll(any(PageRequest.class)))
            .thenReturn(emptyPage);

        Page<MedicalRegistryList> result = medicalRegistryListService.findByScheduleAndStatusIsApprovedPaginated(1, 10, Collections.emptyList());
        assertThat(result.getContent()).isEmpty();
    }

    // ===================== sortByStatusIsApproved =====================
    @Test
    @DisplayName("TC6: Đảm bảo chỉ trả về các phần tử có trạng thái đúng")
    void sortByStatusIsApproved_HasData() {
        MedicalRegistryList mrl1 = new MedicalRegistryList();
        mrl1.setStatusIsApproved(validStatus);
        
        MedicalRegistryList mrl2 = new MedicalRegistryList();
        mrl2.setStatusIsApproved(new StatusIsApproved());
        
        List<MedicalRegistryList> mockList = Arrays.asList(mrl1, mrl2);
        when(medicalRegistryListRepository.findByScheduleAndStatusIsApproved(any(), any(), any(), eq(validStatus)))
            .thenReturn(Collections.singletonList(mrl1));

        List<MedicalRegistryList> result = medicalRegistryListService.sortByStatusIsApproved(mockList, validStatus);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatusIsApproved()).isEqualTo(validStatus);
    }

    // ===================== findByScheduleAndStatusIsApproved2 =====================
    @Test
    @DisplayName("TC7: Đảm bảo trả về đúng danh sách phiếu khám theo lịch và trạng thái")
    void findByScheduleAndStatusIsApproved2_HasData() {
        List<MedicalRegistryList> mockList = Arrays.asList(new MedicalRegistryList(), new MedicalRegistryList());
        when(medicalRegistryListRepository.findByScheduleAndStatusIsApproved2(validSchedule, validStatus))
            .thenReturn(mockList);

        List<MedicalRegistryList> result = medicalRegistryListService.findByScheduleAndStatusIsApproved2(validSchedule, validStatus);
        assertThat(result).hasSize(2);
    }

    // ===================== findUniqueUser =====================
    @Test
    @DisplayName("TC8: Đảm bảo trả về danh sách user duy nhất")
    void findUniqueUser_HasData() {
        List<User> mockList = Arrays.asList(validUser, new User());
        when(medicalRegistryListRepository.findUniqueUser(validSchedule, validStatus))
            .thenReturn(mockList);

        List<User> result = medicalRegistryListService.findUniqueUser(validSchedule, validStatus);
        assertThat(result).hasSize(2);
    }

    // ===================== countMRLByScheduleAndStatuses =====================
    @Test
    @DisplayName("TC9: Đảm bảo trả về đúng số lượng phiếu khám")
    void countMRLByScheduleAndStatuses_HasData() {
        List<StatusIsApproved> statuses = Arrays.asList(validStatus, new StatusIsApproved());
        when(medicalRegistryListRepository.countMRLByScheduleAndStatuses(validSchedule, statuses))
            .thenReturn(5);

        Integer result = medicalRegistryListService.countMRLByScheduleAndStatuses(validSchedule, statuses);
        assertThat(result).isEqualTo(5);
    }

    // ===================== countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved =====================
    @Test
    @DisplayName("TC10: Đảm bảo trả về đúng số lượng phiếu khám theo điều kiện")
    void countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved_HasData() {
        when(medicalRegistryListRepository.countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved(
            validUser, validSchedule, false, validStatus))
            .thenReturn(3);

        Integer result = medicalRegistryListService.countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved(
            validUser, validSchedule, false, validStatus);
        assertThat(result).isEqualTo(3);
    }
} 