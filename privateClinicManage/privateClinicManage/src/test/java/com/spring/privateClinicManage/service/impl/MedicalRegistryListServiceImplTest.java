package com.spring.privateClinicManage.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.spring.privateClinicManage.QrCode.QRZXingGenerator;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.repository.MedicalRegistryListRepository;

@ExtendWith(MockitoExtension.class)
public class MedicalRegistryListServiceImplTest {

    @Mock
    private MedicalRegistryListRepository medicalRegistryListRepository;

    @Mock
    private Cloudinary cloudinary;

    @InjectMocks
    private MedicalRegistryListServiceImpl medicalRegistryListService;

    private MedicalRegistryList validMedicalRegistryList;
    private StatusIsApproved validStatusIsApproved;
    private BufferedImage mockQRCodeImage;
    private MultipartFile mockMultipartFile;
    private Map<String, String> mockCloudinaryResponse;
    private MockedStatic<QRZXingGenerator> mockedQRZXingGenerator;
    private Uploader mockUploader;

    @BeforeEach
    void setUp() throws Exception {
        // Tạo dữ liệu hợp lệ cho các test case
        validMedicalRegistryList = new MedicalRegistryList();
        validMedicalRegistryList.setId(1);
        validMedicalRegistryList.setName("Test Patient");

        validStatusIsApproved = new StatusIsApproved();
        validStatusIsApproved.setId(1);
        validStatusIsApproved.setStatus("APPROVED");

        mockQRCodeImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

        mockMultipartFile = new MockMultipartFile(
                "file",
                "test.png",
                "image/png",
                "test".getBytes()
        );

        mockCloudinaryResponse = new HashMap<>();
        mockCloudinaryResponse.put("secure_url", "https://cloudinary.com/qr.png");

        // Mock uploader của Cloudinary
        mockUploader = mock(Uploader.class);
    }

    @AfterEach
    void tearDown() {
        if (mockedQRZXingGenerator != null) {
            mockedQRZXingGenerator.close();
        }
    }

    /**
     * TC01: Kiểm tra tạo QR code và upload thành công
     */
    @Test
    void createQRCodeAndUpLoadCloudinaryAndSetStatus_Success() throws Exception {
        // Mock static methods của QRZXingGenerator
        mockedQRZXingGenerator = Mockito.mockStatic(QRZXingGenerator.class);
        mockedQRZXingGenerator.when(() -> QRZXingGenerator.generateQRCodeImage(any(String.class)))
                .thenReturn(mockQRCodeImage);
        mockedQRZXingGenerator.when(() -> QRZXingGenerator.convertBufferedImageToMultipartFile(any(BufferedImage.class)))
                .thenReturn(mockMultipartFile);

        // Mock Cloudinary uploader
        when(cloudinary.uploader()).thenReturn(mockUploader);
        when(mockUploader.upload(any(byte[].class), anyMap())).thenReturn(mockCloudinaryResponse);

        // Gọi hàm cần test
        medicalRegistryListService.createQRCodeAndUpLoadCloudinaryAndSetStatus(
                validMedicalRegistryList, validStatusIsApproved);

        // Kiểm tra trạng thái và url đã được set
        assertThat(validMedicalRegistryList.getStatusIsApproved()).isEqualTo(validStatusIsApproved);
        assertThat(validMedicalRegistryList.getQrUrl()).isEqualTo("https://cloudinary.com/qr.png");

        // Kiểm tra đã lưu vào repository
        verify(medicalRegistryListRepository).save(validMedicalRegistryList);
    }

    /**
     * TC02: Kiểm tra xử lý với medicalRegistryList null
     */
    @Test
    void createQRCodeAndUpLoadCloudinaryAndSetStatus_MedicalRegistryListNull_ShouldThrowException() {
        // Gọi hàm với medicalRegistryList = null, mong đợi lỗi NullPointerException
        assertThatThrownBy(() -> medicalRegistryListService.createQRCodeAndUpLoadCloudinaryAndSetStatus(
                null, validStatusIsApproved))
                .isInstanceOf(NullPointerException.class);
    }

    /**
     * TC03: Kiểm tra xử lý với statusIsApproved null
     */
    @Test
    void createQRCodeAndUpLoadCloudinaryAndSetStatus_StatusIsApprovedNull_ShouldThrowException() {
        // Gọi hàm với statusIsApproved = null, mong đợi lỗi NullPointerException
        assertThatThrownBy(() -> medicalRegistryListService.createQRCodeAndUpLoadCloudinaryAndSetStatus(
                validMedicalRegistryList, null))
                .isInstanceOf(NullPointerException.class);
    }

    /**
     * TC04: Kiểm tra xử lý với medicalRegistryList không có ID
     */
    @Test
    void createQRCodeAndUpLoadCloudinaryAndSetStatus_MedicalRegistryListWithoutId_ShouldThrowException() {
        // Tạo đối tượng không có ID
        MedicalRegistryList registryWithoutId = new MedicalRegistryList();
        registryWithoutId.setName("Test Patient");

        // Gọi hàm, mong đợi lỗi (có thể NullPointerException hoặc IllegalArgumentException tùy code thực tế)
        assertThatThrownBy(() -> medicalRegistryListService.createQRCodeAndUpLoadCloudinaryAndSetStatus(
                registryWithoutId, validStatusIsApproved))
                .isInstanceOf(Exception.class);
    }

    /**
     * TC05: Kiểm tra xử lý khi upload thất bại
     */
    @Test
    void createQRCodeAndUpLoadCloudinaryAndSetStatus_CloudinaryUploadFails_ShouldThrowException() throws Exception {
        // Mock static methods của QRZXingGenerator
        mockedQRZXingGenerator = Mockito.mockStatic(QRZXingGenerator.class);
        mockedQRZXingGenerator.when(() -> QRZXingGenerator.generateQRCodeImage(any(String.class)))
                .thenReturn(mockQRCodeImage);
        mockedQRZXingGenerator.when(() -> QRZXingGenerator.convertBufferedImageToMultipartFile(any(BufferedImage.class)))
                .thenReturn(mockMultipartFile);

        // Mock Cloudinary uploader ném lỗi
        when(cloudinary.uploader()).thenReturn(mockUploader);
        when(mockUploader.upload(any(byte[].class), anyMap())).thenThrow(new RuntimeException("Cloudinary upload failed"));

        // Gọi hàm và kiểm tra exception
        assertThatThrownBy(() -> medicalRegistryListService.createQRCodeAndUpLoadCloudinaryAndSetStatus(
                validMedicalRegistryList, validStatusIsApproved))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Cloudinary upload failed");

        // Đảm bảo không lưu vào repository khi upload lỗi
        verify(medicalRegistryListRepository, never()).save(any(MedicalRegistryList.class));
    }
}
