import Api, { authAPI, endpoints } from "../config/Api";
import "./RegisterForm.css";
import {
  forwardRef,
  useImperativeHandle,
  useRef,
  useState,
  useEffect,
} from "react";
import { CircularProgress } from "@mui/material";
import { CustomerSnackbar } from "../Common/Common";
import dayjs from "dayjs";

const RegisterForm = forwardRef(function RegisterForm({ onClose }, ref) {
  const [loading, setLoading] = useState(false);
  const [formRegisterState, setFormRegisterState] = useState({
    name: "",
    email: "",
    phone: "",
    gender: "male",
    birthday: "",
    address: "",
    password: "",
    otp: "",
  });

  const [activeRegister, setActiveRegister] = useState(false);
  const [open, setOpen] = useState(false);
  const [data, setData] = useState({
    message: "Đăng kí thành công",
    severity: "success",
  });
  const [otpButtonDisabled, setOtpButtonDisabled] = useState(false);
  const [otpCountdown, setOtpCountdown] = useState(30);
  const dialog = useRef();

  const countdownIntervalRef = useRef(null);

  useImperativeHandle(ref, () => {
    return {
      open() {
        dialog.current.style.border = "none";
        dialog.current.style.background = "none";
        dialog.current.showModal();
      },

      close() {
        dialog.current.close();
      },
    };
  });

  const showSnackbar = (message, severity) => {
    setData({
      message: message,
      severity: severity,
    });

    setOpen(true);

    setTimeout(() => {
      setOpen(false);
    }, 2500);
  };

  function handleFormRegisterState(e) {
    const { name, value } = e.target;
    setFormRegisterState((prev) => ({
      ...prev,
      [name]: value,
    }));
  }

  useEffect(() => {
    const { otp, ...fieldsToCheck } = formRegisterState;
    const allFieldsFilled = Object.values(fieldsToCheck).every(
      (field) => field !== ""
    );

    if (allFieldsFilled) {
      setActiveRegister(true);
    } else {
      setActiveRegister(false);
    }
  }, [formRegisterState]);

  const register = async (event) => {
    event.preventDefault();
    setLoading(true);

    try {
      const response = await Api.post(
        endpoints["register"],
        {
          ...formRegisterState,
        },
        {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        }
      );

      if (response.status === 201) {
        showSnackbar("Đăng kí thành công", "success");
        setFormRegisterState({
          name: "",
          email: "",
          phone: "",
          gender: "male",
          birthday: "",
          address: "",
          password: "",
          otp: "",
        });
        dialog.current.close();
        countdownIntervalRef.current = setInterval(() => {
          setOtpCountdown((prevCountdown) => {
            if (prevCountdown === 1) {
              clearInterval(countdownIntervalRef.current);
              setOtpButtonDisabled(false);
              setOtpCountdown(30);
            }
            return prevCountdown - 1;
          });
        }, 1000);
      } else if (response.status !== 201) {
        showSnackbar(response.data, "error");
      }
    } catch {
      showSnackbar("Lỗi", "error");
    }
    setTimeout(() => {
      setLoading(false);
    }, 2400);
  };

  const sendOtp = async (event) => {
    event.preventDefault();
    setLoading(true);
    try {
      const response = await Api.post(
        endpoints["sendOtp"],
        {
          email: formRegisterState.email,
        },
        {
          validateStatus: function (status) {
            return status < 500; // Chỉ ném lỗi nếu status code >= 500
          },
        }
      );

      if (response.status === 200) {
        showSnackbar("Đã gửi OTP cho email !", "success");
        setOtpButtonDisabled(true);

        countdownIntervalRef.current = setInterval(() => {
          setOtpCountdown((prevCountdown) => {
            if (prevCountdown === 1) {
              clearInterval(countdownIntervalRef.current);
              setOtpButtonDisabled(false);
              setOtpCountdown(30);
            }
            return prevCountdown - 1;
          });
        }, 1000);
        setLoading(false);
      } else if (response.status !== 200) {
        showSnackbar(response.data, "error");
        setLoading(false);
      }
    } catch {
      showSnackbar("Lỗi", "error");
      setLoading(false);
    }

    setLoading(false);
  };

  return (
    <>
      <CustomerSnackbar
        open={open}
        message={data.message}
        severity={data.severity}
      />

      <dialog className="overflow-hidden" ref={dialog}>
        <div className="container d-flex justify-content-center align-items-center vh-100">
          <div className="register-form-container card p-5 ">
            <button
              className="btn-close position-absolute top-0 end-0 m-3"
              aria-label="Close"
              onClick={() => onClose()}
            ></button>
            <div className="text-center mb-4">
              <h2 className="text text-primary">ĐĂNG KÝ</h2>
            </div>
            <form onSubmit={register}>
              <div className="form-group mb-2 d-flex">
                <div className="flex-grow-1 me-5">
                  <label>Tên của bạn *</label>
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Tên của bạn"
                    name="name"
                    value={formRegisterState.name}
                    onChange={handleFormRegisterState}
                    required
                  />
                </div>
                <div>
                  <label>Giới tính *</label>
                  <div className="flex-grow-1 me-2">
                    <div className="form-check form-check-inline">
                      <input
                        checked
                        className="form-check-input"
                        type="radio"
                        name="gender"
                        id="male"
                        defaultValue={(formRegisterState.gender = "male")}
                        value={(formRegisterState.gender = "male")}
                        onChange={handleFormRegisterState}
                      />
                      <label className="form-check-label" htmlFor="male">
                        Nam
                      </label>
                    </div>
                    <div className="form-check form-check-inline">
                      <input
                        className="form-check-input"
                        type="radio"
                        name="gender"
                        id="female"
                        value={(formRegisterState.gender = "female")}
                        onChange={handleFormRegisterState}
                      />
                      <label className="form-check-label" htmlFor="female">
                        Nữ
                      </label>
                    </div>
                  </div>
                </div>
              </div>
              <div className="form-group mb-2 d-flex">
                <div className="flex-grow-1 me-2">
                  <label>Email *</label>
                  <input
                    type="email"
                    className="form-control"
                    placeholder="Email"
                    name="email"
                    value={formRegisterState.email}
                    onChange={handleFormRegisterState}
                    required
                  />
                </div>
                <div>
                  <label>Số điện thoại *</label>
                  <input
                    type="number"
                    className="form-control"
                    placeholder="Số điện thoại"
                    name="phone"
                    minLength={10}
                    maxLength={10}
                    min={0}
                    onChange={(e) => {
                      let phone = e.target.value;
                      if (phone.length > 10) phone.slice(0, 10);
                      else handleFormRegisterState(e);
                    }}
                    value={formRegisterState.phone}
                    required
                  />
                </div>
              </div>
              <div className="form-group mb-2">
                <label>Ngày sinh *</label>
                <input
                  type="date"
                  className="form-control"
                  name="birthday"
                  onChange={handleFormRegisterState}
                  value={formRegisterState.birthday}
                  required
                  max={dayjs(new Date()).format("YYYY-MM-DD")}
                />
              </div>
              <div className="form-group mb-2">
                <label>Địa chỉ *</label>
                <input
                  type="text"
                  className="form-control"
                  placeholder="Địa chỉ"
                  name="address"
                  onChange={handleFormRegisterState}
                  value={formRegisterState.address}
                  required
                />
              </div>
              <div className="form-group mb-2">
                <label>Mật khẩu *</label>
                <input
                  type="password"
                  className="form-control"
                  placeholder="Mật khẩu"
                  name="password"
                  onChange={handleFormRegisterState}
                  value={formRegisterState.password}
                  required
                />
              </div>
              <div className="form-group mb-2 d-flex">
                <div className="flex-grow-1 me-2">
                  <label>OTP*</label>
                  <input
                    required
                    type="number"
                    className={`form-control ${
                      activeRegister ? "" : "readonly"
                    }`}
                    placeholder="Nhập OTP"
                    maxLength="6"
                    name="otp"
                    onChange={handleFormRegisterState}
                    value={formRegisterState.otp}
                  />
                </div>
                {loading ? (
                  <div className="d-flex justify-content-center align-item-center">
                    <CircularProgress className="mt-3" />
                  </div>
                ) : (
                  <button
                    onClick={sendOtp}
                    type="button"
                    disabled={otpButtonDisabled}
                    className={`btn align-self-end ${
                      activeRegister ? "btn-danger" : "btn-secondary disabled"
                    }`}
                  >
                    {otpButtonDisabled ? otpCountdown : "Gửi OTP"}
                  </button>
                )}
              </div>
              {loading ? (
                <div className="d-flex justify-content-center align-item-center">
                  <CircularProgress className="mt-3" />
                </div>
              ) : (
                <button
                  type="submit"
                  className={`btn bt-register w-100 ${
                    activeRegister && formRegisterState.otp !== ""
                      ? ""
                      : "disabled"
                  }`}
                >
                  ĐĂNG KÝ
                </button>
              )}
            </form>
          </div>
        </div>
      </dialog>
    </>
  );
});

export default RegisterForm;
