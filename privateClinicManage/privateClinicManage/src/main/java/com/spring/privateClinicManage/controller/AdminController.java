package com.spring.privateClinicManage.controller;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.spring.privateClinicManage.entity.Medicine;
import com.spring.privateClinicManage.entity.MedicineGroup;
import com.spring.privateClinicManage.entity.Role;
import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.entity.UnitMedicineType;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.entity.Voucher;
import com.spring.privateClinicManage.entity.VoucherCondition;
import com.spring.privateClinicManage.service.MedicineGroupService;
import com.spring.privateClinicManage.service.MedicineService;
import com.spring.privateClinicManage.service.RoleService;
import com.spring.privateClinicManage.service.ScheduleService;
import com.spring.privateClinicManage.service.StatsService;
import com.spring.privateClinicManage.service.UnitMedicineTypeService;
import com.spring.privateClinicManage.service.UserService;
import com.spring.privateClinicManage.service.VoucherConditionService;
import com.spring.privateClinicManage.service.VoucherService;
import com.spring.privateClinicManage.utilities.CalendarFormat;
import com.spring.privateClinicManage.utilities.CalendarFormatUtil;

import jakarta.validation.Valid;

@Controller
public class AdminController {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PasswordEncoder encoder;
	@Autowired
	private UnitMedicineTypeService unitMedicineTypeService;
	@Autowired
	private MedicineGroupService medicineGroupService;
	@Autowired
	private MedicineService medicineService;
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private VoucherConditionService voucherConditionService;
	@Autowired
	private StatsService statsService;
	@Autowired
	private ScheduleService scheduleService;

	@ModelAttribute
	public void addAttributes(Model model) {
		User user = userService.getCurrentLoginUser();
		model.addAttribute("currentUser", user);

		List<Role> roles = roleService.findAllRoles();
		model.addAttribute("roles", roles);

	}

	@GetMapping("/login")
	public String showSignInForm() {
		return "admin/authenticate/login";
	}

	@GetMapping("/admin")
	public String index() {
		return "admin/index";
	}

	@GetMapping("/admin/usersList")
	public String getUsersList(Model model, @RequestParam Map<String, String> params) {

		Integer page = Integer.parseInt(params.getOrDefault("page", "1"));
		Integer size = Integer.parseInt(params.getOrDefault("size", "5"));
		String sortIsActived = params.getOrDefault("sortIsActived", "none");
		String sortRole = params.getOrDefault("sortRole", "none");
		String anyKey = params.getOrDefault("sortAnyText", "");

		List<User> users = userService.findAllUsers();

		if (!anyKey.isBlank())
			users = userService.findByAnyText(anyKey);

		if (!sortIsActived.isBlank() && !sortIsActived.equals("none")) {
			users = userService.sortByActive(users, sortIsActived);
		}

		if (!sortRole.isBlank() && !sortRole.equals("none")) {
			Role role = roleService.findByName(sortRole);
			if (role != null)
				users = userService.sortByRole(users, role);

		}

		Page<User> userListsPaginated;

		page = page > 0 ? page : 1;
		size = size > 0 ? size : 5;

		if (!anyKey.isBlank() || !sortIsActived.isBlank() && !sortIsActived.equals("none")
				|| !sortRole.isBlank() && !sortRole.equals("none")) {

			userListsPaginated = userService.findSortedPaginateUser(size, page, users);

			model.addAttribute("sortAnyText", anyKey);
			model.addAttribute("sortIsActived", sortIsActived);
			model.addAttribute("sortRole", sortRole);

		} else {
			userListsPaginated = userService
					.findAllUserPaginated(
							PageRequest.of(page - 1, size));
		}

		Integer totalPages = userListsPaginated.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("userListsPaginated", userListsPaginated);
		model.addAttribute("page", page);
		model.addAttribute("size", size);

		return "admin/user/usersList";
	}

	@GetMapping("/admin/addNewUser")
	public String getFormAddNewUser(Model model) {
		model.addAttribute("user", new User());
		return "admin/user/addOrUpdateUser";
	}

	@GetMapping("/admin/updateUser/{userId}")
	public String getFormUpdateUser(Model model, @PathVariable("userId") Integer userId) {
		User user = userService.findUserById(userId);
		model.addAttribute("user", user);
		return "admin/user/addOrUpdateUser";
	}

	@PostMapping("/admin/addOrUpdateUser")
	public String addOrUpdateUser(Model model, @ModelAttribute("user") User user,
			@RequestParam Map<String, String> params,
			@RequestPart("avatarFile") MultipartFile avatarFile) throws ParseException {

		if (user.getId() != null) {
			User existUser = userService.findUserById(user.getId());
			user.setPassword(existUser.getPassword());
		} else {
			user.setPassword(encoder.encode(user.getPassword()));
		}

		if (avatarFile.getOriginalFilename() != ""
				|| !avatarFile.getOriginalFilename().isEmpty()) {
			user.setFile(avatarFile);
			userService.setCloudinaryField(user);
		}

		userService.saveUser(user);

		return "redirect:/admin/usersList";
	}

	@GetMapping("/admin/unit-medicine-type-list")
	public String getUmtList(Model model, @RequestParam Map<String, String> params) {

		Integer page = Integer.parseInt(params.getOrDefault("page", "1"));
		Integer size = Integer.parseInt(params.getOrDefault("size", "5"));
		String name = params.getOrDefault("name", "");

		List<UnitMedicineType> umts = new ArrayList<>();

		UnitMedicineType umt;
		if (!name.isBlank()) {
			umt = unitMedicineTypeService.findUtmByUnitName(name);
			if (umt != null)
				umts.add(umt);
		} else {
			umts = unitMedicineTypeService.findAllUmt();
		}

		page = page > 0 ? page : 1;
		size = size > 0 ? size : 5;

		Page<UnitMedicineType> umtsPaginated = unitMedicineTypeService.paginateUmtList(size, page,
				umts);

		Integer totalPages = umtsPaginated.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("umtsPaginated", umtsPaginated);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("name", name);

		return "admin/medicine/medicineUnitList";
	}

	@GetMapping("/admin/addNewUmt")
	public String getFormAddUmt(Model model) {
		model.addAttribute("umt", new UnitMedicineType());
		return "admin/medicine/addOrUpdateUmt";
	}

	@GetMapping("/admin/updateUmt/{umtId}")
	public String getFormUpdateUmt(Model model, @PathVariable("umtId") Integer umtId) {
		UnitMedicineType unitMedicineType = unitMedicineTypeService.findUtmById(umtId);
		model.addAttribute("umt", unitMedicineType);
		return "admin/medicine/addOrUpdateUmt";
	}

	@PostMapping("/admin/addOrUpdateUmt")
	public String addOrUpdateUmt(Model model, @Valid @ModelAttribute("umt") UnitMedicineType umt,
			BindingResult bindingResult, @RequestParam Map<String, String> params)
			throws ParseException {

		UnitMedicineType existUtm = unitMedicineTypeService
				.findUtmByUnitName(umt.getUnitName());

		if (umt.getId() == null && existUtm != null)
			bindingResult.rejectValue("unitName", null,
					"Đã tồn tại tên đơn vị này !");

		if (bindingResult.hasErrors()) {
			model.addAttribute("umt", umt);
			return "admin/medicine/addOrUpdateUmt";
		}

		unitMedicineTypeService.saveUnitMedicineType(umt);

		return "redirect:/admin/unit-medicine-type-list";

	}

	@GetMapping("/admin/medicine-group-list")
	public String getMedicineGroupList(Model model, @RequestParam Map<String, String> params) {

		Integer page = Integer.parseInt(params.getOrDefault("page", "1"));
		Integer size = Integer.parseInt(params.getOrDefault("size", "5"));
		String name = params.getOrDefault("name", "");

		List<MedicineGroup> medicineGroups = new ArrayList<>();
		MedicineGroup medicineGroup;
		if (!name.isBlank()) {
			medicineGroup = medicineGroupService.findMedicineByGroupByName(name);
			if (medicineGroup != null)
				medicineGroups.add(medicineGroup);
		} else {
			medicineGroups = medicineGroupService.findAllMedicineGroup();
		}

		page = page > 0 ? page : 1;
		size = size > 0 ? size : 5;

		Page<MedicineGroup> medicineGroupsPaginated = medicineGroupService
				.paginateMedicineGroupList(size, page,
						medicineGroups);

		Integer totalPages = medicineGroupsPaginated.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("medicineGroupsPaginated", medicineGroupsPaginated);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("name", name);

		return "admin/medicine/medicineGroupList";
	}

	@GetMapping("/admin/addNewMedicineGroup")
	public String getFormAddMedicineGroup(Model model) {
		model.addAttribute("medicineGroup", new MedicineGroup());
		return "admin/medicine/addOrUpdateMedicineGroup";
	}

	@GetMapping("/admin/updateMedicineGroup/{medicineGroupId}")
	public String getFormUpdateMedicineGroup(Model model,
			@PathVariable("medicineGroupId") Integer medicineGroupId) {
		MedicineGroup medicineGroup = medicineGroupService.findMedicineGroupById(medicineGroupId);
		model.addAttribute("medicineGroup", medicineGroup);
		return "admin/medicine/addOrUpdateMedicineGroup";
	}

	@PostMapping("/admin/addOrUpdateMedicineGroup")
	public String addOrUpdateUmt(Model model,
			@Valid @ModelAttribute("medicineGroup") MedicineGroup medicineGroup,
			BindingResult bindingResult, @RequestParam Map<String, String> params)
			throws ParseException {

		MedicineGroup medicineGroupExist = medicineGroupService
				.findMedicineByGroupByName(medicineGroup.getGroupName());

		if (medicineGroup.getId() == null && medicineGroupExist != null)
			bindingResult.rejectValue("groupName", null,
					"Đã tồn tại tên nhóm thuốc này !");

		if (bindingResult.hasErrors()) {
			model.addAttribute("medicineGroup", medicineGroup);
			return "admin/medicine/addOrUpdateMedicineGroup";
		}

		medicineGroupService.saveMedicineGroup(medicineGroup);

		return "redirect:/admin/medicine-group-list";

	}

	@GetMapping("/admin/medicinesList")
	public String getMedicinesList(Model model, @RequestParam Map<String, String> params) {

		Integer page = Integer.parseInt(params.getOrDefault("page", "1"));
		Integer size = Integer.parseInt(params.getOrDefault("size", "5"));
		String name = params.getOrDefault("name", "");
		String sortByUmt = params.getOrDefault("sortByUmt", "none");
		String sortByGroup = params.getOrDefault("sortByGroup", "none");

		List<Medicine> medicines = new ArrayList<>();

		if (!name.isBlank()) {
			medicines = medicineService.findByName(name);

		} else
			medicines = medicineService.findAllMedicines();

		UnitMedicineType unitMedicineType = unitMedicineTypeService
				.findUtmByUnitName(sortByUmt);
		if (!sortByUmt.isBlank() && unitMedicineType != null) {
			medicines = medicineService.sortByUtm(medicines, unitMedicineType);
		}

		MedicineGroup medicineGroup = medicineGroupService
				.findMedicineByGroupByName(sortByGroup);
		if (!sortByGroup.isBlank() && medicineGroup != null) {
			medicines = medicineService.sortByGroup(medicines, medicineGroup);
		}

		Page<Medicine> medicinesPaginated = medicineService.medicinesPaginated(page, size,
				medicines);

		page = page > 0 ? page : 1;
		size = size > 0 ? size : 5;

		Integer totalPages = medicinesPaginated.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("medicinesPaginated", medicinesPaginated);
		model.addAttribute("sortByUmt", sortByUmt);
		model.addAttribute("sortByGroup", sortByGroup);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("name", name);

		List<UnitMedicineType> umtsInit = unitMedicineTypeService.findAllUmt();
		List<MedicineGroup> medicineGroupsInit = medicineGroupService.findAllMedicineGroup();

		model.addAttribute("umts", umtsInit);
		model.addAttribute("medicineGroups", medicineGroupsInit);

		return "admin/medicine/medicinesList";
	}

	@GetMapping("/admin/addNewMedicine")
	public String getFormAddNewMedicine(Model model) {
		Medicine medicine = new Medicine();
		model.addAttribute("medicine", medicine);

		List<UnitMedicineType> umts = unitMedicineTypeService.findAllUmt();
		List<MedicineGroup> medicineGroups = medicineGroupService.findAllMedicineGroup();

		model.addAttribute("umts", umts);
		model.addAttribute("medicineGroups", medicineGroups);

		return "admin/medicine/addOrUpdateMedicine";
	}

	@GetMapping("/admin/updateMedicine/{medicineId}")
	public String getFormUpdateMedicine(Model model,
			@PathVariable("medicineId") Integer medicineId) {
		Medicine medicine = medicineService.findById(medicineId);
		model.addAttribute("medicine", medicine);

		List<UnitMedicineType> umts = unitMedicineTypeService.findAllUmt();
		List<MedicineGroup> medicineGroups = medicineGroupService.findAllMedicineGroup();

		model.addAttribute("umts", umts);
		model.addAttribute("medicineGroups", medicineGroups);

		return "admin/medicine/addOrUpdateMedicine";
	}

	@PostMapping("/admin/addOrUpdateMedicine")
	public String addOrUpdateUmt(Model model,
			@Valid @ModelAttribute("medicine") Medicine medicine,
			BindingResult bindingResult, @RequestParam Map<String, String> params)
			throws ParseException {

		List<Medicine> medicineExist = medicineService
				.findByName(medicine.getName());

		if (medicine.getId() == null && medicineExist.size() > 0)
			bindingResult.rejectValue("name", null,
					"Đã tồn tại tên thuốc này !");

		if (bindingResult.hasErrors()) {
			model.addAttribute("medicine", medicine);
			return "admin/medicine/addOrUpdateMedicine";
		}

		medicineService.saveMedicine(medicine);

		return "redirect:/admin/medicinesList";

	}

	@GetMapping("/admin/vouchers-list")
	public String getVoucherList(Model model, @RequestParam Map<String, String> params) {

		Integer page = Integer.parseInt(params.getOrDefault("page", "1"));
		Integer size = Integer.parseInt(params.getOrDefault("size", "5"));
		String name = params.getOrDefault("name", "");

		List<Voucher> vouchers = new ArrayList<>();

		if (!name.isBlank()) {
			vouchers = voucherService.findAllVouchersByCodeContaining(name);

		} else
			vouchers = voucherService.findAllVouchers();

		Page<Voucher> vouchersPaginated = voucherService.vouchersPaginated(page, size,
				vouchers);

		page = page > 0 ? page : 1;
		size = size > 0 ? size : 5;

		Integer totalPages = vouchersPaginated.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("vouchersPaginated", vouchersPaginated);
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("name", name);

		return "admin/voucher/vouchersList";
	}

	@GetMapping("/admin/addNewVoucher")
	public String getFormAddNewVoucher(Model model) {
		Voucher voucher = new Voucher();
		model.addAttribute("voucher", voucher);

		return "admin/voucher/addOrUpdateVoucher";
	}

	@GetMapping("/admin/updateVoucher/{voucherId}")
	public String getFormUpdateVoucher(Model model,
			@PathVariable("voucherId") Integer voucherId) {
		Voucher voucher = voucherService.findVoucherById(voucherId);
		model.addAttribute("voucher", voucher);

		return "admin/voucher/addOrUpdateVoucher";
	}

	@PostMapping("/admin/addOrUpdateVoucher")
	public String addOrUpdateVoucher(Model model,
			@Valid @ModelAttribute("voucher") Voucher voucher,
			BindingResult bindingResult,
			@RequestParam Map<String, Object> params)
			throws ParseException {

		Voucher existVoucher = voucherService.findVoucherByCode(voucher.getCode());

		if (voucher.getId() == null && existVoucher != null)
			bindingResult.rejectValue("code", null,
					"Đã tồn tại mã giảm giá này !");

		if (bindingResult.hasErrors()) {
			model.addAttribute("voucher", voucher);
			return "admin/voucher/addOrUpdateVoucher";
		}
		/*
		 * voucher khi packing bean thì nó chứa địa chỉ của voucher , ae packing bean
		 * phần VoucherCondition nó ko có địa chỉ , thành ra sẽ tự create 1 object mới
		 * bất chấp
		 */
		if (voucher.getId() != null) {

			VoucherCondition voucherCondition = existVoucher.getVoucherCondition();

			voucherCondition.setExpiredDate(voucher.getVoucherCondition().getExpiredDate());
			voucherCondition.setPercentSale(voucher.getVoucherCondition().getPercentSale());
			voucher.setVoucherCondition(voucherCondition);

		}

		voucherService.saveVoucher(voucher);

		return "redirect:/admin/vouchers-list";

	}

	@GetMapping("/admin/statsByPrognosisMedicine")
	public String statsByPrognosisMedicine(Model model, @RequestParam Map<String, String> params) {

		String yearlyPrognosis = params.getOrDefault("yearlyPrognosis",
				String.valueOf(LocalDate.now().getYear()));

		yearlyPrognosis = yearlyPrognosis.isEmpty() || yearlyPrognosis.equals("")
				|| Integer.parseInt(yearlyPrognosis) < 2000
				|| Integer.parseInt(yearlyPrognosis) > LocalDateTime.now().getYear()
						? String.valueOf(LocalDateTime.now().getYear())
						: yearlyPrognosis;

		String monthlyPrognosis = params.getOrDefault("monthlyPrognosis",
				String.valueOf(LocalDate.now().getMonthValue()));

		monthlyPrognosis = monthlyPrognosis.isEmpty() || monthlyPrognosis.equals("")
				? String.valueOf(LocalDateTime.now().getMonthValue())
				: monthlyPrognosis;

		List<Object[]> stats1 = statsService
				.statsByPrognosisMedicine(Integer.parseInt(yearlyPrognosis),
						Integer.parseInt(monthlyPrognosis));

		List<Map<String, Object>> formattedStats1 = new ArrayList<>();

		for (Object[] s : stats1) {
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("medicineName", s[0]);
			resultMap.put("prognosis", s[1]);
			formattedStats1.add(resultMap);

		}

		model.addAttribute("prognosisMedicineStats", formattedStats1);
		model.addAttribute("monthlyPrognosis", monthlyPrognosis);
		model.addAttribute("yearlyPrognosis", yearlyPrognosis);

		return "admin/stats/statsByPrognosis";
	}

	@GetMapping("/admin/statsByRevenue")
	public String statsByRevenue(Model model, @RequestParam Map<String, String> params) {

		String yearlyRevenue = params.getOrDefault("yearlyRevenue",
				String.valueOf(LocalDate.now().getYear()));

		yearlyRevenue = yearlyRevenue.isEmpty() || yearlyRevenue.equals("")
				|| Integer.parseInt(yearlyRevenue) < 2000
				|| Integer.parseInt(yearlyRevenue) > LocalDateTime.now().getYear()
						? String.valueOf(LocalDateTime.now().getYear())
						: yearlyRevenue;

		List<Object[]> stats2 = statsService
				.statsByRevenue(Integer.parseInt(yearlyRevenue));

		List<Map<Object, Object>> formattedStats2 = new ArrayList<>();

		for (Object[] s : stats2) {
			Map<Object, Object> resultMap = new HashMap<>();
			resultMap.put("month", s[0]);
			resultMap.put("revenue", s[1]);
			formattedStats2.add(resultMap);

		}

		model.addAttribute("revenueStats", formattedStats2);
		model.addAttribute("yearlyRevenue", yearlyRevenue);

		return "admin/stats/statsByRevenue";
	}

	@GetMapping("/admin/schedule-list")
	public String getScheduleList(Model model, @RequestParam Map<String, String> params) {

		String date = params.getOrDefault("date", null);

		Integer page = Integer.parseInt(params.getOrDefault("page", "1"));
		Integer size = Integer.parseInt(params.getOrDefault("size", "5"));

		List<Schedule> schedules = new ArrayList<>();

		if (date != null && !date.isBlank()) {
			CalendarFormat cd = CalendarFormatUtil.parseStringToCalendarFormat(date);
			Schedule exSchedule = scheduleService.findByDayMonthYear(cd.getYear(), cd.getMonth(),
					cd.getDay());
			if (exSchedule != null)
				schedules.add(exSchedule);

		} else {
			schedules = scheduleService.findAllSchedule();
		}

		Page<Schedule> schedulesPaginated = scheduleService.schedulePaginated(page, size,
				schedules);

		page = page > 0 ? page : 1;
		size = size > 0 ? size : 5;

		Integer totalPages = schedulesPaginated.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
					.boxed()
					.collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("schedulesPaginated", schedulesPaginated);
		model.addAttribute("page", page);
		model.addAttribute("size", size);

		return "admin/schedule/schedulesList";
	}

	@GetMapping("/admin/addNewSchedule")
	public String getFormAddNewSchedule(Model model) {
		Schedule schedule = new Schedule();
		model.addAttribute("schedule", schedule);

		return "admin/schedule/addOrUpdateSchedule";
	}

	@GetMapping("/admin/updateSchedule/{scheduleId}")
	public String getFormUpdateSchedule(Model model,
			@PathVariable("scheduleId") Integer scheduleId) {

		Schedule schedule = scheduleService.findById(scheduleId);
		model.addAttribute("schedule", schedule);

		return "admin/schedule/addOrUpdateSchedule";
	}

	@PostMapping("/admin/addOrUpdateSchedule")
	public String addOrUpdateVoucher(Model model,
			@Valid @ModelAttribute("schedule") Schedule schedule,
			BindingResult bindingResult,
			@RequestParam Map<String, Object> params)
			throws ParseException {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(schedule.getDate());

//		Integer month = calendar.get(Calendar.MONTH) == 12 ? 1 : calendar.get(Calendar.MONTH) + 1;
		Integer month = calendar.get(Calendar.MONTH) + 1;

		Schedule existSchedule = scheduleService.findByDayMonthYear(calendar.get(Calendar.YEAR),
				month,
				calendar.get(Calendar.DAY_OF_MONTH));

		if (schedule.getId() == null && existSchedule != null)
			bindingResult.rejectValue("date", null, "Đã tồn tại lịch ngày làm này !");

		if (bindingResult.hasErrors()) {
			model.addAttribute("schedule", schedule);
			return "admin/schedule/addOrUpdateSchedule";
		}

		scheduleService.saveSchedule(schedule);

		return "redirect:/admin/schedule-list";

	}

}
