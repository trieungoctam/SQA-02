package com.spring.privateClinicManage.QrCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test cho QRZXingGenerator.createQRwithText
 */
class QRZXingGeneratorTest {

    /**
     * TC01: Kiểm tra tạo QR code với đầy đủ text
     */
    @Test
    @DisplayName("TC01: Tạo QR code với đầy đủ text trên/dưới")
    void createQRwithText_FullText_Success() throws Exception {
        String data = "123456";
        String topText = "Phòng khám ABC";
        String bottomText = "Số thứ tự";
        BufferedImage qrImage = QRZXingGenerator.createQRwithText(data, topText, bottomText);
        assertThat(qrImage).isNotNull();
        assertThat(qrImage.getWidth()).isGreaterThan(0);
        assertThat(qrImage.getHeight()).isGreaterThan(0);
    }

    /**
     * TC02: Kiểm tra xử lý với text trên/dưới rỗng
     */
    @Test
    @DisplayName("TC02: Tạo QR code với text trên/dưới rỗng")
    void createQRwithText_EmptyTopBottomText_Success() throws Exception {
        String data = "123456";
        String topText = "";
        String bottomText = "";
        BufferedImage qrImage = QRZXingGenerator.createQRwithText(data, topText, bottomText);
        assertThat(qrImage).isNotNull();
        assertThat(qrImage.getWidth()).isGreaterThan(0);
        assertThat(qrImage.getHeight()).isGreaterThan(0);
    }

    /**
     * TC03: Kiểm tra xử lý với data null
     */
    @Test
    @DisplayName("TC03: Xử lý khi data null")
    void createQRwithText_DataNull_ThrowsException() {
        String data = null;
        String topText = "Phòng khám ABC";
        String bottomText = "Số thứ tự";
        assertThrows(Exception.class, () -> QRZXingGenerator.createQRwithText(data, topText, bottomText));
    }

    /**
     * TC04: Kiểm tra xử lý với text quá dài
     */
    @Test
    @DisplayName("TC04: Tạo QR code với text trên/dưới rất dài")
    void createQRwithText_LongText_Success() throws Exception {
        String data = "123456";
        String topText = "a".repeat(100);
        String bottomText = "b".repeat(100);
        BufferedImage qrImage = QRZXingGenerator.createQRwithText(data, topText, bottomText);
        assertThat(qrImage).isNotNull();
        assertThat(qrImage.getWidth()).isGreaterThan(0);
        assertThat(qrImage.getHeight()).isGreaterThan(0);
    }
} 