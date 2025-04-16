package com.spring.privateClinicManage.service;

import com.itextpdf.io.exceptions.IOException;
import com.spring.privateClinicManage.entity.MedicalRegistryList;

public interface DownloadPDFService {
	public byte[] generateOrderPdf(MedicalRegistryList mrl)
			throws IOException, java.io.IOException;

	public void generateAndSaveLocation(MedicalRegistryList mrl)
			throws java.io.IOException;
}
