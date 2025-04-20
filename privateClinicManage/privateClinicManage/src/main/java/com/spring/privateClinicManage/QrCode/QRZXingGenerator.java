package com.spring.privateClinicManage.QrCode;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRZXingGenerator {
	public static BufferedImage generateEAN13BarcodeImage(String barcodeText) throws Exception {
		EAN13Writer barcodeWriter = new EAN13Writer();
		BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.EAN_13, 300, 150);

		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}

	public static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
		QRCodeWriter barcodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);

		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}

	
	public static BufferedImage createQRwithText(String data, String topText, String bottomText)
			throws WriterException, IOException {
		QRCodeWriter barcodeWriter = new QRCodeWriter();
		BitMatrix matrix = barcodeWriter.encode(data, BarcodeFormat.QR_CODE, 200, 200);
		return modifiedQRCode(matrix, topText, bottomText);
	}

	private static BufferedImage modifiedQRCode(BitMatrix matrix, String topText,
			String bottomText) {
		int matrixWidth = matrix.getWidth();
		int matrixHeight = matrix.getHeight();

		BufferedImage image = new BufferedImage(matrixWidth, matrixHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, matrixWidth, matrixHeight);
		graphics.setColor(Color.BLACK);

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixHeight; j++) {
				if (matrix.get(i, j)) {
					graphics.fillRect(i, j, 1, 1);
				}
			}
		}

		FontMetrics fontMetrics = graphics.getFontMetrics();
		int topTextWidth = fontMetrics.stringWidth(topText);
		int bottomTextWidth = fontMetrics.stringWidth(bottomText);
		int finalWidth = Math.max(matrixWidth, Math.max(topTextWidth, bottomTextWidth)) + 1;
		int finalHeight = matrixHeight + fontMetrics.getHeight() + fontMetrics.getAscent() + 1;

		BufferedImage finalImage = new BufferedImage(finalWidth, finalHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D finalGraphics = finalImage.createGraphics();
		finalGraphics.setColor(Color.WHITE);
		finalGraphics.fillRect(0, 0, finalWidth, finalHeight);
		finalGraphics.setColor(Color.BLACK);

		finalGraphics.drawImage(image, (finalWidth - matrixWidth) / 2, fontMetrics.getAscent() + 2,
				null);
		finalGraphics.drawString(topText, (finalWidth - topTextWidth) / 2,
				fontMetrics.getAscent() + 2);
		finalGraphics.drawString(bottomText, (finalWidth - bottomTextWidth) / 2,
				finalHeight - fontMetrics.getDescent() - 5);

		return finalImage;
	}

	public static String readQRCode(String filePath, String charset, Map hintMap)
			throws FileNotFoundException, IOException, NotFoundException {
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
				new BufferedImageLuminanceSource(
						ImageIO.read(new FileInputStream(filePath)))));
		Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap,
				hintMap);
		return qrCodeResult.getText();
	}

	public static MultipartFile convertBufferedImageToMultipartFile(BufferedImage bufferedImage) {
		MultipartFile multipartFile = null;
		try {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, "png", baos);
			baos.flush();

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

			multipartFile = new MockMultipartFile("file", "qr.png", "image/png",
					bais);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return multipartFile;
	}

}
