import { components } from "react-select";
import AdviseSection from "../AdviseSection/AdviseSection";
import AppointmentForm from "../AppointmentForm/AppointmentForm";
import CencorRegister from "../CensorRegister/CensorRegister";
import Chatting from "../Chatting/Chatting";
import ChattingToAI from "../ChattingToAI/ChattingToAI";
import DirectRegister from "../DirectRegister/DirectRegister";
import ExaminationForm from "../ExaminationForm/ExaminationForm";
import Home from "../Home/Home";
import QRScanner from "../QRScan/QRScanner";
import UserHistory from "../UserHistory/UserHistory";
import UserProcessingList from "../UserProcessingList/UserProcessingList";
import UserProfile from "../UserProfile/UserProfile";
import UserRegisterScheduleList from "../UserRegisterScheduleList/UserRegisterScheduleList";
import ManageExerciseTime from "../ManageExerciseTime/ManageExerciseTime";

const publicRoutes = [
  { path: "/", component: Home, role: "ROLE_ALL" },
  {
    path: "/register-schedule",
    component: AppointmentForm,
    role: "ROLE_BENHNHAN",
  },
  {
    path: "/user-register-schedule-list",
    component: UserRegisterScheduleList,
    role: "ROLE_BENHNHAN",
  },
  { path: "/censor-register", component: CencorRegister, role: "ROLE_YTA" },
  { path: "/qr-scan-take-order", component: QRScanner, role: "ROLE_YTA" },
  {
    path: "/directly-register-schedule",
    component: DirectRegister,
    role: "ROLE_YTA",
  },
  {
    path: "/prepare-examination-form",
    component: UserProcessingList,
    role: "ROLE_BACSI",
  },
  { path: "/examination-form", component: ExaminationForm, role: "ROLE_BACSI" },
  { path: "/advise-section", component: AdviseSection, role: "ROLE_ALL" },
  { path: "/user-profile", component: UserProfile, role: "ROLE_ANY" },
  { path: "/history", component: UserHistory, role: "ROLE_BENHNHAN" },
  { path: "/chatting", component: Chatting, role: "ROLE_ANY" },
  { path: "/chatting-to-AI", component: ChattingToAI, role: "ROLE_ANY" },
  { path: "/manage-time-exercise", component : ManageExerciseTime, role : "ROLE_ANY"}

];

const privateRoutes = [];

export { publicRoutes, privateRoutes };
