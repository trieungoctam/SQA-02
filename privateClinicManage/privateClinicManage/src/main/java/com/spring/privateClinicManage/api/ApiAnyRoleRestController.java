package com.spring.privateClinicManage.api;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.spring.privateClinicManage.dto.*;
import com.spring.privateClinicManage.entity.*;
import com.spring.privateClinicManage.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.spring.privateClinicManage.component.OnlinerUsers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/anyrole")
public class ApiAnyRoleRestController {

	@Autowired
	private UserService userService;
	@Autowired
	private BlogService blogService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private CommentBlogService commentBlogService;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	@Autowired
	private LikeBlogService likeBlogService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private OnlinerUsers onlineUsers;
	@Autowired
	private ChatRoomService chatRoomService;
	@Autowired
	private ChatMessageService chatMessageService;
	@Autowired
	private MedicalRegistryListService medicalRegistryListService;
	@Autowired
	private MedicalExaminationService medicalExaminationService;
	@Autowired
	private PrescriptionItemsService prescriptionItemsService;
	@Autowired
	private AttendanceExerciseRecordService attendanceExerciseRecordService;
	@Autowired
	private WallerService wallerService;
	@Autowired
	private WalletHistoryService walletHistoryService;

	@PostMapping("/logout/")
	@CrossOrigin
	public ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response) {
		
		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		onlineUsers.findAndRemoveSessionIdByKey(currentUser.getRole().getName(), "",
				currentUser.getId());

		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, response,
				SecurityContextHolder.getContext().getAuthentication());

		return new ResponseEntity<Object>("Đăng xuất thành công !", HttpStatus.OK);
	}

	@GetMapping(path = "/get-all-online-users/")
	@CrossOrigin
	public ResponseEntity<Object> getAllOnlineUsers() {
		return new ResponseEntity<Object>(onlineUsers.getOnlineUsers(), HttpStatus.OK);
	}

	@PatchMapping(path = "/update-profile/")
	@CrossOrigin
	public ResponseEntity<Object> updateProfile(@RequestBody UpdateProfileDto updateProfileDto) {
		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		if (!updateProfileDto.getName().isBlank()
				&& !currentUser.getName().equals(updateProfileDto.getName())) {
			currentUser.setName(updateProfileDto.getName());
		}
		if (!updateProfileDto.getAddress().isBlank()
				&& !currentUser.getAddress().equals(updateProfileDto.getAddress())) {
			currentUser.setAddress(updateProfileDto.getAddress());
		}
		if (updateProfileDto.getBirthday() != null)
			currentUser.setBirthday(updateProfileDto.getBirthday());

		userService.saveUser(currentUser);

		return new ResponseEntity<Object>(HttpStatus.OK);

	}

	@PatchMapping(path = "/change-avatar/", consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	@CrossOrigin
	public ResponseEntity<Object> changeAvatar(
			@RequestPart("avatar") MultipartFile files) {
		User user = userService.getCurrentLoginUser();

		if (files == null || files.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		if (user != null) {
			user.setFile(files);
			userService.setCloudinaryField(user);

			return new ResponseEntity<>("Cập nhật ảnh đại diện thành công !", HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PatchMapping(path = "/change-password/")
	@CrossOrigin
	public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {

		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		Boolean isPasswordMatched = userService.authUser(currentUser.getEmail(),
				changePasswordDto.getOldPassword());
		if (isPasswordMatched == false)
			return new ResponseEntity<>("Sai mật khẩu cũ !", HttpStatus.UNAUTHORIZED);

		currentUser.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
		userService.saveUser(currentUser);

		return new ResponseEntity<>("Thay đổi mật khẩu thành công !", HttpStatus.OK);
	}

	@GetMapping(path = "/blogs/")
	@CrossOrigin
	public ResponseEntity<Object> getAllBlogs(@RequestParam Map<String, String> params) {

		User currentUser = userService.getCurrentLoginUser();

		Integer page = Integer.parseInt(params.getOrDefault("page", "1"));
		Integer size = Integer.parseInt(params.getOrDefault("size", "5"));

		List<Blog> blogs;

		String key = params.getOrDefault("key", "");

		if (!key.isBlank()) {

			blogs = blogService.findByAnyKey(key);
		} else
			blogs = blogService.findAllBlogs();

		blogs.stream().forEach(b -> {
			List<CommentBlog> cb = commentBlogService.findByBlog(b);
			Boolean isCommented = cb == null || cb.size() < 1 ? false : true;
			b.setIsCommented(isCommented);

			if (currentUser != null) {
				LikeBlog likeBlog = likeBlogService.findLikeBlogByUserAndBlog(currentUser, b);
				Boolean hasLiked = likeBlog == null ? false : likeBlog.getHasLiked();
				b.setHasLiked(hasLiked);
			} else {
				b.setHasLiked(false);
			}

		});

		Page<Blog> allBlogsPaginated = blogService
				.allBlogsPaginated(page, size, blogs);

		return new ResponseEntity<Object>(allBlogsPaginated, HttpStatus.OK);
	}

	@PostMapping(path = "/blogs/create/")
	@CrossOrigin
	public ResponseEntity<Object> createBlog(@RequestBody BlogDto blogDto) {

		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		Blog blog = new Blog();
		blog.setTitle(blogDto.getTitle());
		blog.setContent(blogDto.getContent());
		blog.setUser(currentUser);
		blog.setCreatedDate(new Date());
		blogService.saveBlog(blog);

		return new ResponseEntity<>(blog, HttpStatus.CREATED);
	}

	@GetMapping(path = "/blogs/{blogId}/get-comment-blog/")
	@CrossOrigin
	public ResponseEntity<Object> getCommentBlogByBlog(@PathVariable("blogId") Integer blogId) {
		Blog blog = blogService.findById(blogId);
		if (blog == null)
			return new ResponseEntity<>("Bài viết này không tồn tại !", HttpStatus.NOT_FOUND);

		List<CommentBlog> cb = commentBlogService.findByBlog(blog);

		if (cb == null || cb.size() < 1)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<>(cb.get(0).getComment(), HttpStatus.OK);
	}

	@PostMapping(path = "/blogs/create-comment-blog/")
	@CrossOrigin
	public ResponseEntity<Object> createCommentBlog(@RequestBody CommentDto commentDto) {

		Blog blog = blogService.findById(commentDto.getBlogId());
		if (blog == null)
			return new ResponseEntity<>("Bài viết này không tồn tại !", HttpStatus.NOT_FOUND);

		User currentUser = userService.getCurrentLoginUser();

		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại !", HttpStatus.NOT_FOUND);

		List<CommentBlog> cb = commentBlogService.findByBlog(blog);

		if (cb.size() > 0)
			return new ResponseEntity<>("Bài viết này đã được trả lời !", HttpStatus.UNAUTHORIZED);

		Comment comment = new Comment();
		comment.setCreatedDate(new Date());
		comment.setUser(currentUser);
		comment.setContent(commentDto.getContent());

		commentService.saveComment(comment);

		CommentBlog commentBlog = new CommentBlog();
		commentBlog.setBlog(blog);
		commentBlog.setComment(comment);
		commentBlogService.saveCommentBlog(commentBlog);

		blog.setIsCommented(true);

		messagingTemplate.convertAndSend("/notify/recievedNewComment/" + blog.getUser().getId(),
				commentBlog);

		return new ResponseEntity<>(commentBlog, HttpStatus.CREATED);
	}

	@GetMapping(path = "/blogs/{blogId}/count-likes/")
	@CrossOrigin
	public ResponseEntity<Object> countLikeBlog(@PathVariable("blogId") Integer blogId) {

		Blog blog = blogService.findById(blogId);
		if (blog == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		Integer countLikesBlog = likeBlogService.countLikeBlogByBlog(blog);

		return new ResponseEntity<>(new CountDto(countLikesBlog), HttpStatus.OK);
	}

	@PostMapping(path = "/blogs/{blogId}/likes/")
	@CrossOrigin
	public ResponseEntity<Object> toggleLikeBlog(@PathVariable("blogId") Integer blogId) {

		User currentUser = userService.getCurrentLoginUser();
		Blog blog = blogService.findById(blogId);

		if (blog == null || currentUser == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

		LikeBlog likeBlog = likeBlogService.findLikeBlogByUserAndBlog(currentUser, blog);
		if (likeBlog != null) {

			Boolean hasLiked = likeBlog.getHasLiked();

			likeBlog.setHasLiked(!hasLiked);
			blog.setHasLiked(!hasLiked);
			likeBlog.setBlog(blog);

			likeBlogService.saveLikeBlog(likeBlog);

			return new ResponseEntity<>(likeBlog, HttpStatus.OK);
		}

		likeBlog = new LikeBlog();

		likeBlog.setUser(currentUser);
		likeBlog.setHasLiked(true);
		blog.setHasLiked(true);
		likeBlog.setBlog(blog);
		likeBlogService.saveLikeBlog(likeBlog);

		Integer countLikesBlog = likeBlogService.countLikeBlogByBlog(blog);
		blog.setTotalLikes(countLikesBlog);

		messagingTemplate.convertAndSend("/notify/recievedLikeBlog/" + blog.getUser().getId(),
				likeBlog);

		return new ResponseEntity<>(likeBlog, HttpStatus.OK);
	}

	@GetMapping("/connect-to-consultant/")
	@CrossOrigin
	public ResponseEntity<Object> connectToConsultant() {

		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		User tempConsultant = onlineUsers.findFirstROLE_TUVAN();

		if (tempConsultant == null)
			return new ResponseEntity<Object>("Hiện tại không có tư vấn viên nào đang hoạt động",
					HttpStatus.NO_CONTENT);
		User consultant = userService.findUserById(tempConsultant.getId());

		chatRoomService.getChatRoomId(currentUser, consultant, true);

		return new ResponseEntity<>(consultant, HttpStatus.OK);
	}

	@GetMapping("/get-all-recipient-by-sender/")
	@CrossOrigin
	public ResponseEntity<Object> getAllRecipientBySender() {

		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		List<ChatMessage> chatMessages = chatMessageService
				.findLatestMessagesBySenderAndSortChatRoomByLatestMessage(currentUser);

		List<RecipientChatRoomDto> rcrDto = new ArrayList<>();

		chatMessages.forEach((cm) -> {
			ChatRoom chatRoom;
			chatRoom = chatRoomService.findChatRoomByChatRoomIdAndSender(cm.getChatRoomId(),
					currentUser);

			rcrDto.add(new RecipientChatRoomDto(chatRoom, cm));
		});

		return new ResponseEntity<>(rcrDto, HttpStatus.OK);

//		List<ChatRoom> chatRooms = chatRoomService.findBySender(currentUser);
//
//		return new ResponseEntity<>(chatRooms, HttpStatus.OK);
	}

	@PostMapping("/get-all-chatMessage-by-sender-and-recipient/")
	@CrossOrigin
	public ResponseEntity<Object> getAllChatMessageBySenderAndRecipient(
			@RequestBody GetChatMessageDto getChatMessageDto) {

		if (getChatMessageDto.getSenderId() == null)
			return new ResponseEntity<>("Người gửi không tồn tại", HttpStatus.NOT_FOUND);
		if (getChatMessageDto.getRecipientId() == null)
			return new ResponseEntity<>("Người nhận không tồn tại", HttpStatus.NOT_FOUND);

		User sender = userService.findUserById(getChatMessageDto.getSenderId());
		User recipient = userService.findUserById(getChatMessageDto.getRecipientId());

		List<ChatMessage> chatMessages = chatMessageService.findBySenderAndRecipient(sender,
				recipient);

		return new ResponseEntity<>(chatMessages, HttpStatus.OK);
	}

	@PostMapping("/is-user-online/")
	@CrossOrigin
	public ResponseEntity<Object> isUserOnline(@RequestBody OnlineUserDto onlineUserDto) {

		if (onlineUserDto.getUserId() == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		User user = userService.findUserById(onlineUserDto.getUserId());

		if (user == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		Boolean isOnline = onlineUsers.isUserOnline(user);

		return new ResponseEntity<>(isOnline, HttpStatus.OK);
	}

	@PostMapping("/get-last-chat-message/")
	@CrossOrigin
	public ResponseEntity<Object> getLastChatMessage(@RequestBody RecipientDto recipientDto) {

		if (recipientDto.getRecipientId() == null)
			return new ResponseEntity<>("Người nhận không tồn tại", HttpStatus.NOT_FOUND);

		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);
		User recipient = userService.findUserById(recipientDto.getRecipientId());

		if (recipient == null)
			return new ResponseEntity<>("Người nhận không tồn tại", HttpStatus.NOT_FOUND);

		List<ChatMessage> lastChatMessages = chatMessageService
				.findTopByOrderByCreatedDateDesc(currentUser, recipient);
		ChatMessage lastChatMessage = null;

		if (lastChatMessages.size() > 0)
			lastChatMessage = lastChatMessages.get(0);

		if (lastChatMessage == null)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);

		return new ResponseEntity<>(lastChatMessage, HttpStatus.OK);

	}

	@PostMapping("/connect-to-new-recipient/")
	@CrossOrigin
	public ResponseEntity<Object> connectToNewRecipient(@RequestBody RecipientDto recipientDto) {

		if (recipientDto.getRecipientId() == null)
			return new ResponseEntity<>("Người nhận không tồn tại", HttpStatus.NOT_FOUND);

		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		User recipient = userService.findUserById(recipientDto.getRecipientId());
		if (recipient == null)
			return new ResponseEntity<>("Người nhận không tồn tại", HttpStatus.NOT_FOUND);

		String chatRoomId = chatRoomService.getChatRoomId(currentUser, recipient, false);

		if (chatRoomId != null)
			return new ResponseEntity<>(recipient, HttpStatus.OK);

		chatRoomService.getChatRoomId(currentUser, recipient, true);

		return new ResponseEntity<>(recipient, HttpStatus.OK);
	}

	@PostMapping("/get-history-user-register/")
	@CrossOrigin
	public ResponseEntity<Object> getHistoryUserRegister(
			@RequestBody HisotryUserMedicalRegisterDto hisotryUserMedicalRegisterDto) {

		User currentUser = userService.getCurrentLoginUser();
		User patient = userService.findByEmail(hisotryUserMedicalRegisterDto.getEmail());
		if (currentUser == null || patient == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		List<MedicalRegistryList> mrls = medicalRegistryListService.findAllMrlByUserAndName(patient,
				hisotryUserMedicalRegisterDto.getNameRegister());

		mrls = medicalRegistryListService.sortBy2StatusIsApproved(mrls, "FOLLOWUP", "FINISHED");

		List<MedicalExamination> mas = new ArrayList<>();

		mrls.forEach(mrl -> {
			mas.add(mrl.getMedicalExamination());
		});

		return new ResponseEntity<>(mas, HttpStatus.OK);
	}

	@GetMapping("/get-prescriptionItems-by-medicalExam-id/{medicalExamId}/")
	@CrossOrigin
	public ResponseEntity<Object> getPrescriptionItemsByMedicalExamId(
			@PathVariable("medicalExamId") Integer medicalExamId) {

		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		MedicalExamination medicalExamination = medicalExaminationService.findById(medicalExamId);

		if (medicalExamination == null)
			return new ResponseEntity<>("Phiếu khám không tồn tại", HttpStatus.NOT_FOUND);

		List<PrescriptionItems> pis = prescriptionItemsService
				.findByMedicalExamination(medicalExamination);

		return new ResponseEntity<>(pis, HttpStatus.OK);
	}

	@GetMapping(value = "/get-medical-exam-by-mrlId/{mrlId}/")
	@CrossOrigin
	public ResponseEntity<Object> getMedicalExamByMrlId(@PathVariable("mrlId") Integer mrlId) {
		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		MedicalRegistryList mrl = medicalRegistryListService.findById(mrlId);
		if (mrl == null || mrl.getIsCanceled())
			return new ResponseEntity<>("Phiếu đăng kí này không tồn tại hoặc đã được hủy !",
					HttpStatus.NOT_FOUND);
		MedicalExamination me = mrl.getMedicalExamination();

		if (me == null)
			return new ResponseEntity<>("Phiếu đăng kí này chưa có phiếu khám bệnh !",
					HttpStatus.NOT_FOUND);

		List<PrescriptionItems> pis = prescriptionItemsService
				.findByMedicalExamination(me);

		PaymentPhase2OutputDto pp2 = new PaymentPhase2OutputDto();
		pp2.setMe(me);
		pp2.setPis(pis);

		return new ResponseEntity<>(pp2, HttpStatus.OK);
	}

	@GetMapping(value = "/attendance-exercise/get-today/")
	@CrossOrigin
	public ResponseEntity<Object> getTodayAttendanceExercise() {
		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);
		AttendanceExerciseRecord attendanceExerciseRecord = attendanceExerciseRecordService
				.findAttendanceExerciseRecordByClockIn(LocalDateTime.now().getYear(),
						LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth(), currentUser);
		if (attendanceExerciseRecord == null)
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<>(attendanceExerciseRecord, HttpStatus.OK);

	}

	@GetMapping(value = "/attendance-exercise/clock-in/")
	@CrossOrigin
	public ResponseEntity<Object> attendanceExerciseClockIn() {
		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);


		AttendanceExerciseRecord attendanceExerciseRecord = new AttendanceExerciseRecord();
		attendanceExerciseRecord.setCreatedTime(LocalDateTime.now());
		attendanceExerciseRecord.setUpdatedTime(LocalDateTime.now());
		attendanceExerciseRecord.setClockIn(LocalDateTime.now());
		attendanceExerciseRecord.setUser(currentUser);

		attendanceExerciseRecordService.saveAttendanceExerciseRecord(attendanceExerciseRecord);

		return new ResponseEntity<>(attendanceExerciseRecord, HttpStatus.OK);
	}

	@GetMapping(value = "/attendance-exercise/clock-out/")
	@CrossOrigin
	public ResponseEntity<Object> attendanceExerciseClockOut() {
		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		AttendanceExerciseRecord attendanceExerciseRecord = attendanceExerciseRecordService
				.findAttendanceExerciseRecordByClockIn(LocalDateTime.now().getYear(),
						LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth(), currentUser);

		if (attendanceExerciseRecord == null)
			return new ResponseEntity<>("Không tìm thấy record clock in !",HttpStatus.NOT_FOUND);

		attendanceExerciseRecord.setClockOut(LocalDateTime.now());
		attendanceExerciseRecord.setUpdatedTime(LocalDateTime.now());

		Duration duration = Duration.between(attendanceExerciseRecord.getClockIn(), LocalDateTime.now());
		int minutes = Integer.parseInt(String.valueOf(duration.toMinutes()));
		attendanceExerciseRecord.setPeriod(minutes);

		attendanceExerciseRecordService.saveAttendanceExerciseRecord(attendanceExerciseRecord);

		Wallet wallet = wallerService.findByUser(currentUser);
		if (wallet == null)
			return new ResponseEntity<>("Không tìm thấy ví người dùng", HttpStatus.NOT_FOUND);

		Long newBalance = wallet.getBalance() + duration.toMinutes() * 500 ;
		wallet.setBalance(newBalance);
		wallerService.saveWallet(wallet);

		return new ResponseEntity<>(attendanceExerciseRecord, HttpStatus.OK);
	}

	@GetMapping("/attendance-exercise/total-period/")
	@CrossOrigin
	public ResponseEntity<Object> attendanceExerciseTotalPeriod() {
		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		Integer totalPeriod = attendanceExerciseRecordService
				.totalPeriodAttendanceExerciseRecordByUser(currentUser);

		totalPeriod = totalPeriod == null ? 0 : totalPeriod;

		return new ResponseEntity<>(totalPeriod, HttpStatus.OK);
	}

	@GetMapping("/wallet-balance/")
	@CrossOrigin
	public Object getWallet() {
		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		Wallet wallet = wallerService.findByUser(currentUser);

		if (wallet == null) {
			wallet = new Wallet();
			wallet.setUser(currentUser);
			wallet.setCreatedTime(LocalDateTime.now());
			wallet.setBalance(0L);
			wallerService.saveWallet(wallet);
		}

		return new ResponseEntity<>(wallet.getBalance(), HttpStatus.OK);
	}

	@PostMapping("/wallet/withdraw/")
	@CrossOrigin
	public Object walletWithdraw(@RequestBody WalletHistoryDto walletHistoryDto) {
		User currentUser = userService.getCurrentLoginUser();
		if (currentUser == null)
			return new ResponseEntity<>("Người dùng không tồn tại", HttpStatus.NOT_FOUND);

		Wallet wallet = wallerService.findByUser(currentUser);

		if (wallet == null)
			return new ResponseEntity<>("Không tìm thấy ví thanh toán của người dùng", HttpStatus.NOT_FOUND);

		if(wallet.getBalance() < walletHistoryDto.getWithDraw())
			return new ResponseEntity<>("Số dư ví không đủ !", HttpStatus.BAD_REQUEST);

		wallet.setBalance(wallet.getBalance() - walletHistoryDto.getWithDraw());
		wallerService.saveWallet(wallet);

		WalletHistory walletHistory = new WalletHistory();
		walletHistory.setWallet(wallet);
		walletHistory.setWithdrawal(walletHistoryDto.getWithDraw());
		walletHistory.setCreatedDate(LocalDateTime.now());
		walletHistory.setNote(walletHistoryDto.getNote());

		walletHistoryService.saveWalletHistory(walletHistory);

		return new ResponseEntity<>(walletHistory, HttpStatus.OK);
	}

}
