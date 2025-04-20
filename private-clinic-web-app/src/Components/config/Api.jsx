import axios from "axios";

export const BASE_URL = "http://localhost:8888";

// export const BASE_URL = "https://9945-113-185-76-122.ngrok-free.app";

export let endpoints = {
  login: "/api/users/login/",
  currentUser: "/api/users/current-user/",
  register: "/api/users/register/",
  sendOtp: "/api/users/verify-email/",
  registerSchedule: "/api/benhnhan/register-schedule/",
  userRegisterScheduleList: "/api/benhnhan/user-register-schedule-list/",
  userCancelRegisterSchedule: (registerScheduleId) =>
    `/api/benhnhan/cancel-register-schedule/${registerScheduleId}/`,
  getAllRegisterScheduleList: "/api/yta/all-register-schedule/",
  getAllStatusIsApproved: "/api/users/getAllStatusIsApproved/",
  getAllUsers: "/api/yta/get-all-users/",
  getUsersByRegisterDateAndStatus: "/api/yta/get-users-schedule-status/",
  ytaAutoConfirmRegister: "/api/yta/auto-confirm-registers/",
  takeOrderFromQrCode: "/api/users/take-order-from-qrCode/",
  printOrderPdf: "/api/pdf/generate/",
  directRegister: "/api/yta/direct-register/",
  getAllProcessingUserToday: "/api/bacsi/get-all-processing-user-today/",
  getAllMedicineGroup: "/api/bacsi/get-all-medicine-group/",
  getAllMedicinesByGroup: (medicineGroupId) =>
    `/api/bacsi/get-all-medicine-by-group/${medicineGroupId}/`,
  getMedicineById: (medicineId) =>
    `/api/bacsi/get-medicine-by-id/${medicineId}/`,
  getAllMedicines: "/api/bacsi/get-all-medicines/",
  submitMedicalExamination: "/api/bacsi/submit-medical-examination/",
  getHistoryUserRegister: "/api/anyrole/get-history-user-register/",
  getPrescriptionItemsByMedicalExamId: (medicalExamId) =>
    `/api/anyrole/get-prescriptionItems-by-medicalExam-id/${medicalExamId}/`,
  benhnhanMOMOPayment: "/api/payment/momo/",
  benhnhanVNPAYPayment: "/api/payment/vnpay/",
  applyVoucherPayment: "/api/benhnhan/apply-voucher/",
  benhnhanGetMEByMrlId: (mrlId) =>
    `/api/anyrole/get-medical-exam-by-mrlId/${mrlId}/`,
  getAllBlogs: "/api/anyrole/blogs/",
  createNewBlog: "/api/anyrole/blogs/create/",
  getCommentBlogByBlogId: (blogId) =>
    `/api/anyrole/blogs/${blogId}/get-comment-blog/`,
  createNewCommentBlog: "/api/anyrole/blogs/create-comment-blog/",
  toggleLikeBlog: (blogId) => `/api/anyrole/blogs/${blogId}/likes/`,
  countLikeBlog: (blogId) => `/api/anyrole/blogs/${blogId}/count-likes/`,
  updateProfile: "/api/anyrole/update-profile/",
  changeAvatar: "/api/anyrole/change-avatar/",
  changePassword: "/api/anyrole/change-password/",
  logoutOnlineUser: "/api/anyrole/logout/",
  connentToConsultant: "/api/anyrole/connect-to-consultant/",
  getAllRecipientBySender: "/api/anyrole/get-all-recipient-by-sender/",
  getAllChatMessageBySenderAndRecipient:
    "/api/anyrole/get-all-chatMessage-by-sender-and-recipient/",
  isUserOnline: "/api/anyrole/is-user-online/",
  getLastChatMessage: "/api/anyrole/get-last-chat-message/",
  connentToNewRecipient: "/api/anyrole/connect-to-new-recipient/",
  getMrlAndMeUserHistory : "/api/benhnhan/get-mrl-and-me-user-history/",
  getPaymentHisotoryByName : "/api/benhnhan/get-payment-history-by-name/",
  cashPaymentMrl : `/api/yta/cash-payment/`,
  receiveVoucherGift : (mrlId) => `/api/benhnhan/receive-voucher/${mrlId}/`,
  handleCreateAdviseAnswer2 : '/api/v1/hugging-face/completion',
  getAttendanceExerciseToday : '/api/anyrole/attendance-exercise/get-today/',
  clockInAttendanceExercise : '/api/anyrole/attendance-exercise/clock-in/',
  clockOutAttendanceExercise : '/api/anyrole/attendance-exercise/clock-out/',
  totalPeriodAttendanceExercise : '/api/anyrole/attendance-exercise/total-period/',
  getCurrentUserWalletBalance : '/api/anyrole/wallet-balance/',
  withDrawAmountFromWallet : '/api/anyrole/wallet/withdraw/'
};

export const authAPI = () => {
  return axios.create({
    baseURL: `${BASE_URL}`,
    headers: {
      Authorization: localStorage.getItem("token"),
      "ngrok-skip-browser-warning": "69420",
      "bypass-tunnel-reminder": "69420",
    },
  });
};

export default axios.create({
  baseURL: `${BASE_URL}`,
  headers: {
    "ngrok-skip-browser-warning": "69420",
    "bypass-tunnel-reminder": "69420",
  },
});
