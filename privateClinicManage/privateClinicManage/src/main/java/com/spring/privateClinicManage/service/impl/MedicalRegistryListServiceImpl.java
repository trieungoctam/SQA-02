package com.spring.privateClinicManage.service.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.spring.privateClinicManage.QrCode.QRZXingGenerator;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.MedicalRegistryListRepository;
import com.spring.privateClinicManage.service.MedicalRegistryListService;

import jakarta.transaction.Transactional;

@Service
public class MedicalRegistryListServiceImpl implements MedicalRegistryListService {

	@Autowired
	private MedicalRegistryListRepository medicalRegistryListRepository;
	@Autowired
	private Cloudinary cloudinary;

	@Override
	@Transactional
	public void saveMedicalRegistryList(MedicalRegistryList medicalRegistryList) {
		medicalRegistryListRepository.save(medicalRegistryList);
	}

	@Override
	public MedicalRegistryList findMRLByUserAndSchedule(User user, Schedule schedule) {
		return medicalRegistryListRepository.findMRLByUserAndSchedule(user, schedule);
	}

	@Override
	public Integer countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved(User user,
			Schedule schedule,
			Boolean isCanceled, StatusIsApproved statusIsApproved) {
		return medicalRegistryListRepository
				.countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved(user, schedule,
						isCanceled, statusIsApproved);
	}

	@Override
	public List<MedicalRegistryList> findByUser(User user) {
		return medicalRegistryListRepository.findByUser(user);
	}

	@Override
	public MedicalRegistryList findById(Integer id) {
		Optional<MedicalRegistryList> optional = medicalRegistryListRepository.findById(id);
		if (optional.isEmpty())
			return null;
		return optional.get();
	}

	@Override
	public Page<MedicalRegistryList> findByScheduleAndStatusIsApprovedPaginated(Integer page,
			Integer size,
			List<MedicalRegistryList> mrls) {

		Pageable pageable = PageRequest.of(page - 1, size);

		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), mrls.size());
		List<MedicalRegistryList> paged = mrls.subList(start, end);

		return new PageImpl<>(paged, pageable, mrls.size());
	}

	@Override
	public List<MedicalRegistryList> findByScheduleAndStatusIsApproved(
			Integer year, Integer month, Integer day, StatusIsApproved status) {
		return medicalRegistryListRepository.findByScheduleAndStatusIsApproved(year, month, day,
				status);
	}

	@Override
	public List<MedicalRegistryList> findAllMrl() {
		return medicalRegistryListRepository.findAll();
	}

	@Override
	public Page<MedicalRegistryList> findByUserPaginated(Integer page, Integer size,
			List<MedicalRegistryList> mrls) {
		Pageable pageable = PageRequest.of(page - 1, size);

		mrls.sort(Comparator.comparing(MedicalRegistryList::getCreatedDate).reversed());

		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), mrls.size());
		List<MedicalRegistryList> pagedUsers = mrls.subList(start, end);

		return new PageImpl<>(pagedUsers, pageable, mrls.size());
	}

	@Override
	public Page<MedicalRegistryList> findMrlsPaginated(Integer page,
			Integer size,
			List<MedicalRegistryList> mrls) {

		mrls.sort(Comparator.comparing(MedicalRegistryList::getCreatedDate).reversed());
		Pageable pageable = PageRequest.of(page - 1, size);

		int start = (int) pageable.getOffset();
		int end = 0;
		List<MedicalRegistryList> mrlsPaginated;

		if (mrls.size() < start) {
			mrlsPaginated = Collections.emptyList();
		} else {
			end = Math.min((start + pageable.getPageSize()), mrls.size());
			mrlsPaginated = mrls.subList(start, end);
		}

		return new PageImpl<>(mrlsPaginated, pageable, mrls.size());
	}

	@Override
	public List<MedicalRegistryList> sortByStatusIsApproved(List<MedicalRegistryList> mrls,
			StatusIsApproved statusIsApproved) {
		return mrls.stream()
				.filter(mrl -> mrl.getStatusIsApproved().equals(statusIsApproved))
				.collect(Collectors.toList());
	}

	@Override
	public List<MedicalRegistryList> sortBy2StatusIsApproved(List<MedicalRegistryList> mrls,
			String s1, String s2) {
		mrls.sort(Comparator.comparing(MedicalRegistryList::getCreatedDate).reversed());
		return mrls.stream()
				.filter(mrl -> mrl.getStatusIsApproved().getStatus().equals(s1) ||
						mrl.getStatusIsApproved().getStatus().equals(s2))
				.collect(Collectors.toList());
	}

	@Override
	public List<MedicalRegistryList> findByAnyKey(String key) {
		return medicalRegistryListRepository.findByAnyKey(key);
	}

	@Override
	public List<MedicalRegistryList> sortBySchedule(List<MedicalRegistryList> mrls,
			Schedule schedule) {
		return mrls.stream()
				.filter(mrl -> mrl.getSchedule().equals(schedule))
				.collect(Collectors.toList());
	}

	@Override
	public List<MedicalRegistryList> sortByCreatedDate(List<MedicalRegistryList> mrls, Integer year,
			Integer month, Integer day) {
		return medicalRegistryListRepository.sortByCreatedDate(mrls, year, month, day);
	}

	@Override
	public List<MedicalRegistryList> findByScheduleAndStatusIsApproved2(Schedule schedule,
			StatusIsApproved status) {
		return medicalRegistryListRepository.findByScheduleAndStatusIsApproved2(schedule, status);
	}

	@Override
	public List<User> findUniqueUser(Schedule schedule, StatusIsApproved status) {

		return medicalRegistryListRepository.findUniqueUser(schedule, status);
	}

	@Override
	public void setCloudinaryField(MedicalRegistryList medicalRegistryList) {
		if (!medicalRegistryList.getFile().isEmpty()) {
			try {
				Map res = this.cloudinary.uploader().upload(
						medicalRegistryList.getFile().getBytes(),
						ObjectUtils.asMap("resource_type", "auto"));
				medicalRegistryList.setQrUrl(res.get("secure_url").toString());
				medicalRegistryList.setFile(null);
				this.medicalRegistryListRepository.save(medicalRegistryList);

			} catch (IOException ex) {
				Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	@Override
	public void createQRCodeAndUpLoadCloudinaryAndSetStatus(MedicalRegistryList medicalRegistryList,
			StatusIsApproved statusIsApproved)
			throws Exception {
		BufferedImage b = QRZXingGenerator
				.generateQRCodeImage(String.valueOf(medicalRegistryList.getId()));
		MultipartFile qrCodeFile = QRZXingGenerator.convertBufferedImageToMultipartFile(b);
		medicalRegistryList.setStatusIsApproved(statusIsApproved);
		medicalRegistryList.setFile(qrCodeFile);
		this.setCloudinaryField(medicalRegistryList);
	}

	@Override
	public Integer countMRLByScheduleAndStatuses(Schedule schedule,
			List<StatusIsApproved> statuses) {
		return medicalRegistryListRepository.countMRLByScheduleAndStatuses(schedule,
				statuses);
	}

	@Override
	public List<MedicalRegistryList> findAllMrlByUserAndName(User user, String nameRegister) {
		return medicalRegistryListRepository.findAllMrlByUserAndName(user, nameRegister);
	}

}
