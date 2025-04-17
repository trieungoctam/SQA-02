package com.spring.privateClinicManage.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.service.DownloadPDFService;

@Service
public class DownloadPDFOrderServiceImpl implements DownloadPDFService {

	@Autowired
	private Environment env;


	@Override
	public byte[] generateOrderPdf(MedicalRegistryList mrl)
			throws IOException, java.io.IOException {
		File fontvuArialFile = new File(env.getProperty("spring.fonts.fontvuArialFile"));
		File fontvuArialBoldFile = new File(
				env.getProperty("spring.fonts.fontvuArialBoldFile"));

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PdfWriter writer = new PdfWriter(byteArrayOutputStream);
		PdfDocument pdfDoc = new PdfDocument(writer);
		Rectangle r = new Rectangle(300, 300);
		PageSize p = new PageSize(r);
		Document document = new Document(pdfDoc, p);
		document.setMargins(0, 0, 0, 0);

		PdfFont vuArialfont = PdfFontFactory.createFont(
				fontvuArialFile.getAbsolutePath(),
				PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);

		PdfFont vuArialBoldfont = PdfFontFactory.createFont(
				fontvuArialBoldFile.getAbsolutePath(),
				PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);

		Paragraph header = new Paragraph("PHÒNG KHÁM TƯ NHÂN")
				.setFontSize(20)
				.setBold()
				.setTextAlignment(TextAlignment.CENTER).setFont(vuArialBoldfont);
		document.add(header);

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = formatter.format(mrl.getSchedule().getDate());

		Paragraph date = new Paragraph(formattedDate)
				.setFontSize(14)
				.setTextAlignment(TextAlignment.CENTER);
		document.add(date);

		Paragraph doctorInfo = new Paragraph(mrl.getName() + "  -  " + mrl.getUser().getPhone())
				.setFontSize(14)
				.setTextAlignment(TextAlignment.CENTER);
		document.add(doctorInfo);

		Paragraph ticketNumber = new Paragraph(String.valueOf(mrl.getOrder()))
				.setFontSize(70)
				.setTextAlignment(TextAlignment.CENTER).setFont(vuArialBoldfont);
		document.add(ticketNumber);

		Paragraph footer = new Paragraph("Quý Khách vui lòng chờ tới số thứ tự.\nXIN CẢM ƠN!")
				.setFontSize(16)
				.setTextAlignment(TextAlignment.CENTER).setFont(vuArialfont);
		document.add(footer);

		document.setFont(vuArialfont);
		document.close();

		return byteArrayOutputStream.toByteArray();
	}

	@Override
	public void generateAndSaveLocation(MedicalRegistryList medicalRegistryList)
			throws java.io.IOException {

		byte[] pdfBytes = this.generateOrderPdf(medicalRegistryList);

		String pathLocation = env.getProperty("spring.pdf.location");
		String fileName = "OrderMrlId_" + medicalRegistryList.getId() + ".pdf";
		Path path = Paths.get(pathLocation, fileName);
		Files.write(path, pdfBytes);
	}

}
