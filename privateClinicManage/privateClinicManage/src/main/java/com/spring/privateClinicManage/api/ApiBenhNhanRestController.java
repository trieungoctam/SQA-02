package com.spring.privateClinicManage.api;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.privateClinicManage.dto.ApplyVoucherDto;
import com.spring.privateClinicManage.dto.MrlAndMeHistoryDto;
import com.spring.privateClinicManage.dto.NameDto;
import com.spring.privateClinicManage.dto.PaymentHistoryDto;
import com.spring.privateClinicManage.dto.RegisterScheduleDto;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.MrlVoucher;
import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.entity.UserVoucher;
import com.spring.privateClinicManage.entity.Voucher;
import com.spring.privateClinicManage.entity.VoucherCondition;
import com.spring.privateClinicManage.service.MedicalRegistryListService;
import com.spring.privateClinicManage.service.MrlVoucherService;
import com.spring.privateClinicManage.service.PrescriptionItemsService;
import com.spring.privateClinicManage.service.ScheduleService;
import com.spring.privateClinicManage.service.StatsService;
import com.spring.privateClinicManage.service.StatusIsApprovedService;
import com.spring.privateClinicManage.service.UserService;
import com.spring.privateClinicManage.service.UserVoucherService;
import com.spring.privateClinicManage.service.VoucherService;

@RestController
@RequestMapping("/api/benhnhan/")
public class ApiBenhNhanRestController {

	private UserService userService;
	private Environment environment;
	private ScheduleService scheduleService;
	private MedicalRegistryListService medicalRegistryListService;
	private StatusIsApprovedService statusIsApprovedService;
	private SimpMessagingTemplate messagingTemplate;
	private VoucherService voucherService;
	private UserVoucherService userVoucherService;
	private PrescriptionItemsService prescriptionItemsService;
	private StatsService statsService;
	private MrlVoucherService mrlVoucherService;

	@Autowired
	public ApiBenhNhanRestController(UserService userService, Environment environment,
			ScheduleService scheduleService, MedicalRegistryListService medicalRegistryListService,
			StatusIsApprovedService statusIsApprovedService,
			SimpMessagingTemplate messagingTemplate, VoucherService voucherService,
			UserVoucherService userVoucherService,
			PrescriptionItemsService prescriptionItemsService, StatsService statsService,
			MrlVoucherService mrlVoucherService) {
		super();
		this.userService = userService;
		this.environment = environment;
		this.scheduleService = scheduleService;
		this.medicalRegistryListService = medicalRegistryListService;
		this.statusIsApprovedService = statusIsApprovedService;
		this.messagingTemplate = messagingTemplate;
		this.voucherService = voucherService;
		this.userVoucherService = userVoucherService;
		this.prescriptionItemsService = prescriptionItemsService;
		this.statsService = statsService;
		this.mrlVoucherService = mrlVoucherService;
	}

	// ROLE_BENHNHAN

	@PostMapping(value = "/register-schedule/")
	@CrossOrigin
	public ResponseEntity<Object> registerSchedule(
			@RequestBody RegisterScheduleDto registerScheduleDto) {
		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(registerScheduleDto.getDate());

//		Schedule schedule = scheduleService.findByDate(registerScheduleDto.getDate());
		Schedule schedule = scheduleService.findByDayMonthYear(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));

		if (schedule == null) {
			schedule = new Schedule();
			schedule.setDate(registerScheduleDto.getDate());
			schedule.setIsDayOff(false);
			scheduleService.saveSchedule(schedule);
		}

		if (schedule.getIsDayOff())
			return new ResponseEntity<>(
					"Phòng khám không có lịch làm việc ngày này, xin lối quý khách",
					HttpStatus.UNAUTHORIZED);

		StatusIsApproved statusIsApproved = statusIsApprovedService.findByStatus("CHECKING");

		Integer countMedicalRegistryList = medicalRegistryListService
				.countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved(currentUser, schedule,
						false, statusIsApproved);

		if (countMedicalRegistryList >= Integer
				.parseInt(environment.getProperty("register_schedule_per_day_max")))
			return new ResponseEntity<>(
					"Tài khoản này đã đăng kí đủ 4 phiếu đang xét duyệt trong ngày hôm nay !",
					HttpStatus.UNAUTHORIZED);

		MedicalRegistryList medicalRegistryList = new MedicalRegistryList();
		medicalRegistryList.setCreatedDate(new Date());
		medicalRegistryList.setStatusIsApproved(statusIsApproved);
		medicalRegistryList.setIsCanceled(false);
		medicalRegistryList.setUser(currentUser);
		medicalRegistryList.setName(registerScheduleDto.getName());
		medicalRegistryList.setFavor(registerScheduleDto.getFavor());
		medicalRegistryList.setSchedule(schedule);

		medicalRegistryListService.saveMedicalRegistryList(medicalRegistryList);

		messagingTemplate.convertAndSend("/notify/registerContainer/",
				medicalRegistryList);

		return new ResponseEntity<>(medicalRegistryList, HttpStatus.CREATED);

	}

	@GetMapping(value = "/user-register-schedule-list/")
	@CrossOrigin
	public ResponseEntity<Object> getCurrentUserRegisterScheduleList(
			@RequestParam Map<String, String> params) {
		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		Integer page = Integer.parseInt(params.getOrDefault("page", "1"));
		Integer size = Integer.parseInt(params.getOrDefault("size",
				environment.getProperty("user_register_list_pagesize")));

		List<MedicalRegistryList> mrls = medicalRegistryListService.findByUser(currentUser);

		Page<MedicalRegistryList> registryListsPaginated = medicalRegistryListService
				.findByUserPaginated(page, size, mrls);

		return new ResponseEntity<>(registryListsPaginated, HttpStatus.OK);

	}

	@PatchMapping(value = "/cancel-register-schedule/{registerScheduleId}/")
	@CrossOrigin
	public ResponseEntity<Object> cancelRegisterSchedule(
			@PathVariable("registerScheduleId") Integer registerScheduleId) {

		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		MedicalRegistryList medicalRegistryList = medicalRegistryListService
				.findById(registerScheduleId);

		if (medicalRegistryList == null || medicalRegistryList.getIsCanceled())
			return new ResponseEntity<>("Phiếu đăng kí này không tồn tại hoặc đã được hủy !",
					HttpStatus.NOT_FOUND);

		if (!medicalRegistryList.getStatusIsApproved().getStatus().equals("CHECKING"))
			return new ResponseEntity<>("Đã thanh toán , không thể hủy !",
					HttpStatus.NOT_FOUND);

		StatusIsApproved statusIsApproved = statusIsApprovedService.findByStatus("CANCELED");

		medicalRegistryList.setIsCanceled(true);
		medicalRegistryList.setStatusIsApproved(statusIsApproved);
		medicalRegistryListService.saveMedicalRegistryList(medicalRegistryList);

		return new ResponseEntity<>("Đã hủy lịch thành công !", HttpStatus.OK);

	}

	@PostMapping(value = "/apply-voucher/")
	@CrossOrigin
	public ResponseEntity<Object> applyVoucher(@RequestBody ApplyVoucherDto applyVoucherDto) {
		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		String code = applyVoucherDto.getCode();

		Voucher voucher = voucherService.findVoucherByCode(code);

		if (voucher == null)
			return new ResponseEntity<>("Mã giảm giá này không tồn tại !", HttpStatus.NOT_FOUND);

		if (voucher.getIsActived() == false)
			return new ResponseEntity<>("Mã giảm giá này không có hiệu lực !",
					HttpStatus.UNAUTHORIZED);

		Date expiredDate = voucher.getVoucherCondition().getExpiredDate();
		if (expiredDate.compareTo(new Date()) < 0)
			return new ResponseEntity<>("Mã giảm giá này đã hết hạn sử dụng !",
					HttpStatus.UNAUTHORIZED);

		UserVoucher userVoucher = userVoucherService.findByUserAndVoucher(currentUser,
				voucher);
		if (userVoucher != null)
			if (userVoucher.getIsUsed())
				return new ResponseEntity<>("Bạn đã sử dụng mã giảm giá này !",
						HttpStatus.UNAUTHORIZED);

		return new ResponseEntity<>(voucher, HttpStatus.OK);

	}

	@GetMapping("/get-mrl-and-me-user-history/")
	@CrossOrigin
	public ResponseEntity<Object> getMrlAndMeUserHistory(@RequestParam Map<String, String> params) {
		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		Integer page = Integer.parseInt(params.getOrDefault("page", "1"));
		Integer size = Integer.parseInt(params.getOrDefault("size", "3"));

		Page<MrlAndMeHistoryDto> userMrAndMeHistoryPaginated = statsService
				.paginatedStatsUserMrlAndMeHistory(page, size, currentUser);

		return new ResponseEntity<>(userMrAndMeHistoryPaginated, HttpStatus.OK);
	}

	@PostMapping("/get-payment-history-by-name/")
	@CrossOrigin
	public ResponseEntity<Object> getPaymentHistoryByName(@RequestBody NameDto nameDto) {
		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		if (nameDto.getName() == null)
			return new ResponseEntity<>("Tên không được rỗng !", HttpStatus.NOT_FOUND);

		List<PaymentHistoryDto> php1Dtos = statsService
				.statsPaymentPhase1History(nameDto.getName());

		List<PaymentHistoryDto> php2Dtos = statsService
				.statsPaymentPhase2History(nameDto.getName());

		List<PaymentHistoryDto> total2Phase = new ArrayList<>();

		total2Phase.addAll(php1Dtos);
		total2Phase.addAll(php2Dtos);

		total2Phase = statsService.sortByCreatedDate(total2Phase);

		return new ResponseEntity<>(total2Phase, HttpStatus.OK);

	}

	@GetMapping("/receive-voucher/{urlId}/")
	@CrossOrigin
	public ResponseEntity<Object> receiveVoucherAsGift(@PathVariable("urlId") Integer urlId) {

		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		MedicalRegistryList mrl = medicalRegistryListService.findById(urlId);

		if (mrl == null)
			return new ResponseEntity<>("Phiếu đăng ký khám bệnh không tồn tại",
					HttpStatus.NOT_FOUND);

		String status = mrl.getStatusIsApproved().getStatus();

		if (!status.equals("FINISHED") && !status.equals("FOLLOWUP"))
			return new ResponseEntity<>("Trạng thái không hợp lệ",
					HttpStatus.UNAUTHORIZED);
		
		Voucher voucher = null;

		if (mrl.getIsVoucherTaken() == false) {

			Random random = new Random();
			Integer percentSale = random.nextInt(16) + 10;

			voucher = new Voucher();
			voucher.setCode(UUID.randomUUID().toString().replace("-", ""));
			voucher.setIsActived(true);
			voucher.setDescription(
					"Giảm giá " + percentSale + "% cho tổng giá trị hóa đơn thanh toán");

			VoucherCondition voucherCondition = new VoucherCondition();
			voucherCondition.setPercentSale(percentSale);

			Date currentDate = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(currentDate);
			calendar.add(Calendar.MONTH, 3);
			Date expiredDate = calendar.getTime();

			voucherCondition.setExpiredDate(expiredDate);

			voucher.setVoucherCondition(voucherCondition);

			voucherService.saveVoucher(voucher);

			mrl.setIsVoucherTaken(true);
			medicalRegistryListService.saveMedicalRegistryList(mrl);

			MrlVoucher mrlVoucher = new MrlVoucher();
			mrlVoucher.setMrl(mrl);
			mrlVoucher.setVoucher(voucher);

			mrlVoucherService.saveMrlVoucher(mrlVoucher);

			UserVoucher userVoucher = new UserVoucher();
			userVoucher.setIsOwned(true);
			userVoucher.setIsUsed(false);
			userVoucher.setUser(currentUser);
			userVoucher.setVoucher(voucher);

			userVoucherService.saveUserVoucher(userVoucher);
		} else {
			MrlVoucher mrlVoucher = mrlVoucherService.findByMrl(mrl);
			if (mrlVoucher == null)
				return new ResponseEntity<>("Mã giảm giá này không tồn tại không tồn tại",
						HttpStatus.NOT_FOUND);
			voucher = mrlVoucher.getVoucher();
			if (voucher == null)
				return new ResponseEntity<>("Mã giảm giá này không tồn tại không tồn tại",
						HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(voucher, HttpStatus.OK);
	}

}
